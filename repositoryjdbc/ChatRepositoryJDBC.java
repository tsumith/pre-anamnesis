package com.sumith.jfs.PaAnaBot.repositoryjdbc;
import com.sumith.jfs.PaAnaBot.model.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Repository
public class ChatRepositoryJDBC {
    @Autowired
    private DataSource dataSource;
    @PostConstruct
    public void init() {
        try {
            createTableIfNotExists();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void createTableIfNotExists() throws SQLException {
        String sql = """
                    CREATE TABLE IF NOT EXISTS chat_message (
                        id SERIAL PRIMARY KEY,
                        doctor_code VARCHAR(100),
                        patientEmail VARCHAR(100),
                        sender VARCHAR(100),
                        message TEXT,
                        timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                    )
                """;
        try (Connection conn = dataSource.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            stmt.execute(
                    "CREATE INDEX IF NOT EXISTS idx_chat_doctor_patient ON chat_message (doctor_code, patientEmail)");
        }
    }
    public void save(ChatMessage msg) throws SQLException {
        String sql = """
                    INSERT INTO chat_message(doctor_code, patientEmail, sender, message, timestamp)
                    VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, msg.getDoctorCode());
            stmt.setString(2, msg.getPatientEmail());
            stmt.setString(3, msg.getSender());
            stmt.setString(4, msg.getMessage());
            stmt.setTimestamp(5,
                    Timestamp.valueOf(msg.getTimestamp() != null ? msg.getTimestamp() : LocalDateTime.now()));
            stmt.executeUpdate();
        }
    }
    public List<ChatMessage> findByDoctorCodeAndPatientEmail(String doctorCode, String patientEmail)
            throws SQLException {
        String sql = """
                    SELECT * FROM chat_message
                    WHERE doctor_code = ? AND patientEmail = ?
                    ORDER BY timestamp ASC
                """;
        List<ChatMessage> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, doctorCode);
            stmt.setString(2, patientEmail);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToChatMessage(rs));
                }
            }
        }
        return list;
    }
    public List<ChatMessage> findByDoctorCode(String doctorCode) throws SQLException {
        String sql = """
                    SELECT * FROM chat_message
                    WHERE doctor_code = ?
                    ORDER BY timestamp DESC
                """;
        List<ChatMessage> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, doctorCode);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToChatMessage(rs));
                }
            }
        }
        return list;
    }
    public List<ChatMessage> findByPatientId(String patientId) throws SQLException {
        String sql = """
                    SELECT * FROM chat_message
                    WHERE patientEmail = ?
                    ORDER BY timestamp DESC
                """;
        List<ChatMessage> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, patientId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToChatMessage(rs));
                }
            }
        }
        return list;
    }
    private ChatMessage mapRowToChatMessage(ResultSet rs) throws SQLException {
        ChatMessage msg = new ChatMessage();
        msg.setId(rs.getLong("id"));
        msg.setDoctorCode(rs.getString("doctor_code"));
        msg.setPatientEmail(rs.getString("patientEmail"));
        msg.setSender(rs.getString("sender"));
        msg.setMessage(rs.getString("message"));
        Timestamp ts = rs.getTimestamp("timestamp");
        msg.setTimestamp(ts != null ? ts.toLocalDateTime() : LocalDateTime.now());
        return msg;
    }
}
