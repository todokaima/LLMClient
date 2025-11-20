package teo.chat.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import teo.chat.dto.PostMessageDTO;
import teo.chat.entity.Post;
import teo.chat.service.PostService;
import tools.jackson.databind.ObjectMapper;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final PostService postService;
    private final RestTemplate restTemplate;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ApiController(PostService postService) {
        this.postService = postService;
        this.restTemplate = new RestTemplate();
    }

    // Fetch all posts (for AJAX history)
    @GetMapping("/posts")
    public List<PostMessageDTO> getAllPosts() {
        return postService.findAll().stream()
                .map(post -> new PostMessageDTO(
                        post.getField1(),
                        post.getField2(),
                        post.getField3(),
                        post.getCreatedAt().format(FORMATTER)
                ))
                .collect(Collectors.toList());
    }

    // Save a new post (postage page)
    @PostMapping("/postage")
    public String savePost(@ModelAttribute Post post) {
        postService.save(post);
        return "Post saved successfully!";
    }

    // Ollama AI question endpoint
    @PostMapping("/ask")
    public String askQuestion(@RequestBody Map<String, String> payload) {
        String question = payload.get("question");

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

            // NDJSON parsing
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
