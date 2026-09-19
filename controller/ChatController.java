package com.sumith.jfs.PaAnaBot.controller;
import com.sumith.jfs.PaAnaBot.model.ChatMessage;
import com.sumith.jfs.PaAnaBot.service.ChatService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.sql.SQLException;
import java.util.*;
@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }
    @org.springframework.beans.factory.annotation.Autowired
    private com.sumith.jfs.PaAnaBot.service.SymptomMiningService symptomService;
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody ChatMessage message) {
        try {
            chatService.saveMessage(message);
            return ResponseEntity.ok("Message sent successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Database error");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/history/{doctorCode}/{patientEmail}")
    public ResponseEntity<?> getChatHistory(
            @PathVariable String doctorCode,
            @PathVariable String patientEmail) {
        System.out.println("Fetching chat history for Doctor: " + doctorCode + ", Patient: " + patientEmail);
        try {
            return ResponseEntity.ok(chatService.getChatHistory(doctorCode, patientEmail));
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body("Failed to fetch chat history");
        }
    }
    @GetMapping("/doctor/{doctorCode}")
    public ResponseEntity<?> getDoctorChats(@PathVariable String doctorCode) {
        List<Map<String, Object>> chats = new ArrayList<>();
        try {
            List<String> patientIds = chatService.getPatientsByDoctor(doctorCode);
            for (String pid : patientIds) {
                chatService.getLastMessage(doctorCode, pid).ifPresent(msg -> {
                    Map<String, Object> chatSummary = new HashMap<>();
                    chatSummary.put("patientId", pid);
                    chatSummary.put("message", msg.getMessage());
                    chatSummary.put("timestamp", msg.getTimestamp());
                    chats.add(chatSummary);
                });
            }
            return ResponseEntity.ok(chats);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to load chats");
        }
    }
    @GetMapping("/patient/{patientEmail}")
    public ResponseEntity<?> getPatientChats(@PathVariable String patientEmail) {
        List<Map<String, Object>> chats = new ArrayList<>();
        try {
            List<String> doctorIds = chatService.getDoctorsByPatient(patientEmail);
            for (String did : doctorIds) {
                chatService.getLastMessage(did, patientEmail).ifPresent(msg -> {
                    Map<String, Object> chatSummary = new HashMap<>();
                    chatSummary.put("doctorId", did);
                    chatSummary.put("message", msg.getMessage());
                    chatSummary.put("timestamp", msg.getTimestamp());
                    chats.add(chatSummary);
                });
            }
            return ResponseEntity.ok(chats);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to load patient chats");
        }
    }
    @GetMapping("/analyze/{doctorCode}/{patientEmail}")
    public ResponseEntity<?> analyzeChat(@PathVariable String doctorCode, @PathVariable String patientEmail) {
        try {
            List<ChatMessage> history = chatService.getChatHistory(doctorCode, patientEmail);
            List<String> textMessages = history.stream()
                    .map(ChatMessage::getMessage)
                    .toList();
            String dominantSymptoms = symptomService.extractDominantSymptoms(textMessages);
            return ResponseEntity.ok(Collections.singletonMap("analysis", dominantSymptoms));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Analysis failed");
        }
    }
}
