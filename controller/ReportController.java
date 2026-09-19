package com.sumith.jfs.PaAnaBot.controller;
import com.sumith.jfs.PaAnaBot.model.Report;
import com.sumith.jfs.PaAnaBot.service.ReportService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.sql.SQLException;
import java.util.List;
@RestController
@RequestMapping("/api/report")
public class ReportController {
    private final ReportService reportService;
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }
    @PostMapping("/generate")
    public ResponseEntity<?> generateReport(@RequestBody Report report) {
        try {
            reportService.generateReport(report);
            return ResponseEntity.ok("Report saved successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to generate report");
        }
    }
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getReportsByPatient(@PathVariable String patientId) {
        try {
            List<Report> reports = reportService.getReportsByPatient(patientId);
            return ResponseEntity.ok(reports);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to fetch patient reports");
        }
    }
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<?> getReportsByDoctor(@PathVariable String doctorId) {
        try {
            List<Report> reports = reportService.getReportsByDoctor(doctorId);
            return ResponseEntity.ok(reports);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to fetch doctor reports");
        }
    }
}
