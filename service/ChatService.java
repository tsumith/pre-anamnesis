package com.sumith.jfs.PaAnaBot.service;
import org.springframework.stereotype.Service;
import com.sumith.jfs.PaAnaBot.model.ChatMessage;
import com.sumith.jfs.PaAnaBot.repositoryjdbc.ChatRepositoryJDBC;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class ChatService {
    private final ChatRepositoryJDBC chatRepository;
    public ChatService(ChatRepositoryJDBC chatRepository) {
        this.chatRepository = chatRepository;
    }
    public void saveMessage(ChatMessage message) throws SQLException {
        if (message.getDoctorCode() == null || message.getPatientEmail() == null || message.getMessage() == null) {
            throw new IllegalArgumentException("Doctor Code, Patient Email, and message cannot be null");
        }
        chatRepository.save(message);
    }
    public List<ChatMessage> getChatHistory(String doctorCode, String patientEmail) throws SQLException {
        return chatRepository.findByDoctorCodeAndPatientEmail(doctorCode, patientEmail);
    }
    public List<String> getPatientsByDoctor(String doctorCode) {
        try {
            List<ChatMessage> allMessages = chatRepository.findByDoctorCode(doctorCode);
            return allMessages.stream()
                    .map(ChatMessage::getPatientEmail)
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    public List<String> getDoctorsByPatient(String patientId) {
        try {
            List<ChatMessage> allMessages = chatRepository.findByPatientId(patientId);
            return allMessages.stream()
                    .map(ChatMessage::getDoctorCode)
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    public Optional<ChatMessage> getLastMessage(String doctorCode, String patientEmail) {
        try {
            List<ChatMessage> messages = chatRepository.findByDoctorCodeAndPatientEmail(doctorCode, patientEmail);
            return messages.stream()
                    .max(Comparator.comparing(ChatMessage::getTimestamp)); 
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
    public List<ChatMessage> getRecentChats(String userId, String role) {
        try {
            if ("doctor".equalsIgnoreCase(role)) {
                return chatRepository.findByDoctorCode(userId)
                        .stream()
                        .sorted(Comparator.comparing(ChatMessage::getTimestamp).reversed())
                        .limit(10)
                        .collect(Collectors.toList());
            } else if ("patient".equalsIgnoreCase(role)) {
                return chatRepository.findByPatientId(userId)
                        .stream()
                        .sorted(Comparator.comparing(ChatMessage::getTimestamp).reversed())
                        .limit(10)
                        .collect(Collectors.toList());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
