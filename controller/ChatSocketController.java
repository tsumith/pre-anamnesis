package com.sumith.jfs.PaAnaBot.controller;
import com.sumith.jfs.PaAnaBot.model.ChatMessage;
import com.sumith.jfs.PaAnaBot.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.sql.SQLException;
@Controller
public class ChatSocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    public ChatSocketController(SimpMessagingTemplate messagingTemplate, ChatService chatService) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
    }
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        try {
            chatService.saveMessage(chatMessage);
            String destination = "/topic/" + chatMessage.getDoctorCode() + "/" + chatMessage.getPatientEmail();
            messagingTemplate.convertAndSend(destination, chatMessage);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
