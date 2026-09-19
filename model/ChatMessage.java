package com.sumith.jfs.PaAnaBot.model;
import java.time.LocalDateTime;
public class ChatMessage {
    private Long id;
    private String doctorCode;
    private String patientEmail;
    private String sender;
    private String message;
    private LocalDateTime timestamp = LocalDateTime.now();
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDoctorCode() {
        return doctorCode;
    }
    public void setDoctorCode(String doctorCode) {
        this.doctorCode = doctorCode;
    }
    public String getPatientEmail() {
        return patientEmail;
    }
    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
