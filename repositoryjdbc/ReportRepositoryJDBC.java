package com.sumith.jfs.PaAnaBot.repositoryjdbc;
import com.sumith.jfs.PaAnaBot.model.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@Repository
public class ReportRepositoryJDBC {
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
                    CREATE TABLE IF NOT EXISTS report (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        doctor_code TEXT,
                        patient_email TEXT,
                        report_text TEXT,
                        date TEXT
                    )
                """;
        try (Connection conn = dataSource.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
    public void save(Report report) throws SQLException {
        String sql = "INSERT INTO report (doctor_code, patient_email, report_text, date) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, report.getDoctorCode());
            stmt.setString(2, report.getPatientEmail());
            stmt.setString(3, report.getReportText());
            stmt.setString(4, report.getDate());
            stmt.executeUpdate();
        }
    }
    public List<Report> findByDoctorCode(String doctorCode) throws SQLException {
        String sql = "SELECT * FROM report WHERE doctor_code = ? ORDER BY id DESC";
        List<Report> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, doctorCode);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Report r = new Report();
                    r.setId(rs.getLong("id"));
                    r.setDoctorCode(rs.getString("doctor_code"));
                    r.setPatientEmail(rs.getString("patient_email"));
                    r.setReportText(rs.getString("report_text"));
                    r.setDate(rs.getString("date"));
                    list.add(r);
                }
            }
        }
        return list;
    }
    public List<Report> findByPatientEmail(String email) throws SQLException {
        String sql = "SELECT * FROM report WHERE patient_email = ? ORDER BY id DESC";
        List<Report> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Report r = new Report();
                    r.setId(rs.getLong("id"));
                    r.setDoctorCode(rs.getString("doctor_code"));
                    r.setPatientEmail(rs.getString("patient_email"));
                    r.setReportText(rs.getString("report_text"));
                    r.setDate(rs.getString("date"));
                    list.add(r);
                }
            }
        }
        return list;
    }
}
