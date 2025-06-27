package org.example.chatbot.controller;

import org.example.chatbot.model.ChatMessage;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @PostMapping("/receive")
    public String receiveMessage(@RequestBody ChatMessage chatMessage) {
        try {
            Firestore db = FirestoreClient.getFirestore();

            // 1. Store user message
            Map<String, Object> userMsg = new HashMap<>();
            userMsg.put("from", chatMessage.getFrom());
            userMsg.put("message", chatMessage.getMessage());
            userMsg.put("timestamp", System.currentTimeMillis());
            userMsg.put("sender", "user");

            db.collection("messages").add(userMsg);

            // 2. Generate bot reply
            String reply = generateAutoReply(chatMessage.getMessage());

            // 3. Store bot reply
            Map<String, Object> botMsg = new HashMap<>();
            botMsg.put("from", "bot");
            botMsg.put("message", reply);
            botMsg.put("timestamp", System.currentTimeMillis());
            botMsg.put("sender", "bot");

            db.collection("messages").add(botMsg);

            return "✅ Message received and bot replied";

        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Failed to process message";
        }
    }

    // Simple rule-based chatbot logic
    private String generateAutoReply(String message) {
        String msg = message.toLowerCase().trim();
        return switch (msg) {
            case "hi", "hello" -> "👋 Hello! How can I assist you today?";
            case "help" -> "🛠 You can ask me about this chatbot, say hi, or type 'bye' to exit.";
            case "info" -> "🤖 I’m a demo chatbot built with Spring Boot and Firebase.";
            case "bye" -> "👋 Bye! Have a great day.";
            default -> "❓ Sorry, I didn't understand that. Try saying 'help'.";
        };
    }
}
