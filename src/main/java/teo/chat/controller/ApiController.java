package teo.chat.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import teo.chat.entity.Post;
import teo.chat.service.PostService;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final PostService postService;
    private final RestTemplate restTemplate;

    public ApiController(PostService postService) {
        this.postService = postService;
        this.restTemplate = new RestTemplate();
    }

    @PostMapping("/postage")
    public String savePost(@ModelAttribute Post post) {
        postService.save(post);
        return "Post saved successfully!";
    }

    @PostMapping("/ask")
    public String askQuestion(@RequestBody Map<String, String> payload) {
        String question = payload.get("question");

        // Prepare request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "dolphin-llama3:8b");
        requestBody.put("prompt", question);
        Map<String, Object> options = new HashMap<>();
        options.put("num_ctx", 8000);
        requestBody.put("options", options);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        String responseText = "";
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "http://localhost:11434/api/generate",
                    request,
                    String.class
            );

            // NDJSON comes as multiple lines, each is a JSON object
            String[] lines = response.getBody().split("\n");
            StringBuilder sb = new StringBuilder();
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    Map<String, Object> obj = new ObjectMapper().readValue(line, Map.class);
                    if (obj.containsKey("response")) {
                        sb.append(obj.get("response"));
                    }
                }
            }
            responseText = sb.toString();
        } catch (Exception e) {
            responseText = "Error calling Ollama API: " + e.getMessage();
        }

        return responseText;
    }
}