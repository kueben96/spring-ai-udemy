package bmi.springframework.springaiintro;

import org.springframework.ai.chat.client.ChatClient;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatModel chatModel;

    public ChatController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/dad-jokes")
    public String generate(@RequestParam(value="message", defaultValue = "Tell me a dad joke") String message) {
        String resp = chatModel.call(message);
        return resp;
    }

}
