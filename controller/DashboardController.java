package com.sumith.jfs.PaAnaBot.controller;
import com.sumith.jfs.PaAnaBot.service.DashboardService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Map;
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }
    @GetMapping("/stats/{role}/{id}")
    public ResponseEntity<?> getStats(@PathVariable String role, @PathVariable String id) {
        try {
            Map<String, Object> stats = dashboardService.getDashboardStats(role, id);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to fetch stats");
        }
    }
}
