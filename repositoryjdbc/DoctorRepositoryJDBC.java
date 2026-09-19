package com.sumith.jfs.PaAnaBot.repositoryjdbc;
import com.sumith.jfs.PaAnaBot.model.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@Repository
public class DoctorRepositoryJDBC {
    @Autowired
    private DataSource dataSource;
    public void createTableIfNotExists() throws SQLException {
        String sql = """
                    CREATE TABLE IF NOT EXISTS doctor (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT,
                        email TEXT UNIQUE,
                        password TEXT,
                        specialization TEXT,
                        doctor_code TEXT UNIQUE
                    )
                """;
        try (Connection conn = dataSource.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM doctor WHERE email = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }
    public void save(Doctor doctor) throws SQLException {
        String sql = "INSERT INTO doctor(name, email, password, specialization, doctor_code) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, doctor.getName());
            stmt.setString(2, doctor.getEmail());
            stmt.setString(3, doctor.getPassword());
            stmt.setString(4, doctor.getSpecialization());
            stmt.setString(5, doctor.getDoctorCode());
            stmt.executeUpdate();
        }
        System.out.println("new doctor has been registerd: " + doctor.toString());
    }
    public Doctor findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM doctor WHERE email = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setId(rs.getLong("id"));
                doctor.setName(rs.getString("name"));
                doctor.setEmail(rs.getString("email"));
                doctor.setPassword(rs.getString("password"));
                doctor.setSpecialization(rs.getString("specialization"));
                doctor.setDoctorCode(rs.getString("doctor_code"));
                return doctor;
            }
        }
        return null;
    }
    public List<Doctor> findAll() throws SQLException {
        String sql = "SELECT * FROM doctor";
        List<Doctor> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setId(rs.getLong("id"));
                doctor.setName(rs.getString("name"));
                doctor.setEmail(rs.getString("email"));
                doctor.setSpecialization(rs.getString("specialization"));
                doctor.setDoctorCode(rs.getString("doctor_code"));
                list.add(doctor);
            }
        }
        return list;
    }
}
