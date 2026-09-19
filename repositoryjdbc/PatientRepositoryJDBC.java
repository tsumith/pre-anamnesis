package com.sumith.jfs.PaAnaBot.repositoryjdbc;
import com.sumith.jfs.PaAnaBot.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@Repository
public class PatientRepositoryJDBC {
    @Autowired
    private DataSource dataSource;
    public void createTableIfNotExists() throws SQLException {
        String sql = """
                    CREATE TABLE IF NOT EXISTS patient (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT,
                        email TEXT UNIQUE,
                        password TEXT,
                        age INTEGER,
                        gender TEXT,
                        doctor_code TEXT
                    )
                """;
        try (Connection conn = dataSource.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM patient WHERE email = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }
    public void save(Patient patient) throws SQLException {
        String sql = "INSERT INTO patient(name, email, password, age, gender, doctor_code) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, patient.getName());
            stmt.setString(2, patient.getEmail());
            stmt.setString(3, patient.getPassword());
            stmt.setInt(4, patient.getAge());
            stmt.setString(5, patient.getGender());
            stmt.setString(6, patient.getDoctorCode());
            stmt.executeUpdate();
        }
        System.out.println("new patient created: " + patient.toString());
    }
    public Patient findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM patient WHERE email = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Patient patient = new Patient();
                patient.setId(rs.getLong("id"));
                patient.setName(rs.getString("name"));
                patient.setEmail(rs.getString("email"));
                patient.setPassword(rs.getString("password"));
                patient.setAge(rs.getInt("age"));
                patient.setGender(rs.getString("gender"));
                patient.setDoctorCode(rs.getString("doctor_code"));
                return patient;
            }
        }
        return null;
    }
    public List<Patient> findAll() throws SQLException {
        String sql = "SELECT * FROM patient";
        List<Patient> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Patient patient = new Patient();
                patient.setId(rs.getLong("id"));
                patient.setName(rs.getString("name"));
                patient.setEmail(rs.getString("email"));
                patient.setAge(rs.getInt("age"));
                patient.setGender(rs.getString("gender"));
                patient.setDoctorCode(rs.getString("doctor_code"));
                list.add(patient);
            }
        }
        return list;
    }
    public List<Patient> findByDoctorCode(String doctorCode) throws SQLException {
        String sql = "SELECT * FROM patient WHERE doctor_code = ?";
        List<Patient> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, doctorCode);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Patient patient = new Patient();
                patient.setId(rs.getLong("id"));
                patient.setName(rs.getString("name"));
                patient.setEmail(rs.getString("email"));
                patient.setAge(rs.getInt("age"));
                patient.setGender(rs.getString("gender"));
                patient.setDoctorCode(rs.getString("doctor_code"));
                list.add(patient);
            }
        }
        return list;
    }
    public int countByDoctorCode(String doctorCode) throws SQLException {
        String sql = "SELECT COUNT(*) FROM patient WHERE doctor_code = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, doctorCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return rs.getInt(1);
        }
        return 0;
    }
}
