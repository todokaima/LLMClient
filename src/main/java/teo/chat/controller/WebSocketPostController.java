package teo.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import teo.chat.dto.PostDTO;
import teo.chat.dto.PostMessageDTO;
import teo.chat.entity.Post;
import teo.chat.service.PostService;

import java.time.format.DateTimeFormatter;

@Controller
public class WebSocketPostController {

    private final PostService postService;
    private final ObjectMapper objectMapper;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public WebSocketPostController(PostService postService) {
        this.postService = postService;
        this.objectMapper = new ObjectMapper();
    }

    @MessageMapping("/newPost")
    @SendTo("/topic/posts")
    public PostMessageDTO handleNewPost(@Payload PostDTO dto) {
        Post saved = postService.save(new Post(dto.getField1(), dto.getField2(), dto.getField3()));

        return new PostMessageDTO(
                saved.getField1(),
                saved.getField2(),
                saved.getField3(),
                saved.getCreatedAt().format(FORMATTER)
        );
    }
}
