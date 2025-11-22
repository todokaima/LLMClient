package teo.chat.controller;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class KafkaController {

    private final KafkaTemplate<String, String> kafkaTemplate;

    // Constructor injection ensures kafkaTemplate is initialized
    public KafkaController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/send")
    public String sendToKafka(@RequestParam String message) {
        kafkaTemplate.send("topic1", message);
        return "Sent: " + message;
    }
}
