package com.sumith.jfs.PaAnaBot.controller;
import com.sumith.jfs.PaAnaBot.config.JwtUtil;
import com.sumith.jfs.PaAnaBot.model.Doctor;
import com.sumith.jfs.PaAnaBot.model.Patient;
import com.sumith.jfs.PaAnaBot.service.AuthService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @GetMapping("/test")
    public String test() {
        return "Backend working";
    }
    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/register/doctor")
    public ResponseEntity<?> registerDoctor(@RequestBody Doctor doctor) {
        try {
            String result = authService.registerDoctor(doctor);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering doctor");
        }
    }
    @PostMapping("/register/patient")
    public ResponseEntity<?> registerPatient(@RequestBody Patient patient) {
        try {
            String result = authService.registerPatient(patient);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering patient");
        }
    }
    @PostMapping("/login/doctor")
    public ResponseEntity<?> loginDoctor(@RequestBody Doctor request) {
        try {
            Object user = authService.login(request.getEmail(), request.getPassword());
            if (user instanceof Doctor doctor) {
                String token = JwtUtil.generateToken(doctor.getEmail(), "doctor");
                Map<String, Object> resp = new HashMap<>();
                resp.put("status", "success");
                resp.put("user", doctor);
                resp.put("role", "doctor");
                resp.put("token", token);
                return ResponseEntity.ok(resp);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid doctor credentials");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Login error");
        }
    }
    @PostMapping("/login/patient")
    public ResponseEntity<?> loginPatient(@RequestBody Patient request) {
        try {
            Object user = authService.login(request.getEmail(), request.getPassword());
            if (user instanceof Patient patient) {
                String token = JwtUtil.generateToken(patient.getEmail(), "patient");
                Map<String, Object> resp = new HashMap<>();
                resp.put("status", "success");
                resp.put("user", patient);
                resp.put("role", "patient");
                resp.put("token", token);
                return ResponseEntity.ok(resp);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid patient credentials");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Login error");
        }
    }
    public record LoginResponse(String status, Object user, String role) {
    }
}
