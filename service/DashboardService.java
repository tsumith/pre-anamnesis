package com.sumith.jfs.PaAnaBot.service;
import com.sumith.jfs.PaAnaBot.model.Doctor;
import com.sumith.jfs.PaAnaBot.model.Patient;
import com.sumith.jfs.PaAnaBot.repositoryjdbc.DoctorRepositoryJDBC;
import com.sumith.jfs.PaAnaBot.repositoryjdbc.PatientRepositoryJDBC;
import com.sumith.jfs.PaAnaBot.repositoryjdbc.ReportRepositoryJDBC;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.*;
@Service
public class DashboardService {
    private final DoctorRepositoryJDBC doctorRepo;
    private final PatientRepositoryJDBC patientRepo;
    private final ReportRepositoryJDBC reportRepo;
    public DashboardService(DoctorRepositoryJDBC doctorRepo, PatientRepositoryJDBC patientRepo,
            ReportRepositoryJDBC reportRepo) {
        this.doctorRepo = doctorRepo;
        this.patientRepo = patientRepo;
        this.reportRepo = reportRepo;
    }
    public List<Patient> getAllPatients() {
        try {
            return patientRepo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    public List<Doctor> getAllDoctors() {
        try {
            return doctorRepo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    public Map<String, Object> getDashboardStats(String role, String id) throws SQLException {
        Map<String, Object> result = new HashMap<>();
        if (role.equalsIgnoreCase("doctor")) {
            Doctor doctor = doctorRepo.findByEmail(id); 
            if (doctor == null)
                throw new RuntimeException("Doctor not found");
            int patientCount = patientRepo.countByDoctorCode(doctor.getDoctorCode());
            List<Patient> patientList = patientRepo.findByDoctorCode(doctor.getDoctorCode());
            result.put("doctorName", doctor.getName());
            result.put("specialization", doctor.getSpecialization());
            result.put("totalPatients", patientCount);
            result.put("patients", patientList);
        }
        return result;
    }
}
