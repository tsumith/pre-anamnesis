package com.sumith.jfs.PaAnaBot.service;
import com.sumith.jfs.PaAnaBot.model.Report;
import com.sumith.jfs.PaAnaBot.repositoryjdbc.ReportRepositoryJDBC;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;
@Service
public class ReportService {
    private final ReportRepositoryJDBC repository;
    public ReportService(ReportRepositoryJDBC repository) {
        this.repository = repository;
    }
    public void generateReport(Report report) throws SQLException {
        repository.save(report);
    }
    public List<Report> getReportsByDoctor(String doctorCode) throws SQLException {
        return repository.findByDoctorCode(doctorCode);
    }
    public List<Report> getReportsByPatient(String patientEmail) throws SQLException {
        return repository.findByPatientEmail(patientEmail);
    }
}
