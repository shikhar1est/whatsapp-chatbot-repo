package org.example.chatbot.controller;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
public class FirebaseTestController {

    @GetMapping("/firestore-test")
    public String testWriteToFirestore() {
        Firestore db = FirestoreClient.getFirestore();

        Map<String, Object> data = new HashMap<>();
        data.put("message", "Hello from Spring Boot");
        data.put("timestamp", System.currentTimeMillis());

        try {
            db.collection("testMessages").add(data);
            return "✅ Successfully wrote to Firestore!";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Firestore write failed: " + e.getMessage();
        }
    }
}
