package teo.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import teo.chat.entity.Post;
import teo.chat.service.PostService;

import java.util.List;

@Controller
public class PageController {

    private final PostService postService;

    public PageController(PostService postService) {
        this.postService = postService;
    }

    // Home page
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // Post creation page
    @GetMapping("/postage")
    public String postagePage() {
        return "postage";
    }

    // Greeting page
    @GetMapping("/greeting")
    public String greeting(@RequestParam(name = "name", defaultValue = "World") String name,
                           Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    // History page
    @GetMapping("/history")
    public String history() {
        // No need to pass posts in model: we fetch old posts via AJAX
        return "history";
    }
}
