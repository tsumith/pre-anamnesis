package com.sumith.jfs.PaAnaBot.service;
import org.springframework.stereotype.Service;
import com.sumith.jfs.PaAnaBot.model.Doctor;
import com.sumith.jfs.PaAnaBot.model.Patient;
import com.sumith.jfs.PaAnaBot.repositoryjdbc.DoctorRepositoryJDBC;
import com.sumith.jfs.PaAnaBot.repositoryjdbc.PatientRepositoryJDBC;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@Service
public class AuthService {
    private final DoctorRepositoryJDBC doctorRepository;
    private final PatientRepositoryJDBC patientRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public AuthService(DoctorRepositoryJDBC doctorRepository, PatientRepositoryJDBC patientRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }
    public String registerDoctor(Doctor doctor) throws SQLException {
        if (doctorRepository.findByEmail(doctor.getEmail()) != null) {
            return "Doctor already exists!";
        }
        String uniqueCode = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        doctor.setDoctorCode(uniqueCode);
        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        doctorRepository.save(doctor);
        return "Doctor registered successfully!";
    }
    public String registerPatient(Patient patient) throws SQLException {
        if (patientRepository.findByEmail(patient.getEmail()) != null) {
            return "Patient already exists!";
        }
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        patientRepository.save(patient);
        return "Patient registered successfully!";
    }
    public Object login(String email, String password) throws SQLException {
        Doctor doctor = doctorRepository.findByEmail(email);
        if (doctor != null && passwordEncoder.matches(password, doctor.getPassword()))
            return doctor;
        Patient patient = patientRepository.findByEmail(email);
        if (patient != null && passwordEncoder.matches(password, patient.getPassword()))
            return patient;
        return null;
    }
}
