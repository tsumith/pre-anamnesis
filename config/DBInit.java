package com.sumith.jfs.PaAnaBot.config;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.sumith.jfs.PaAnaBot.repositoryjdbc.DoctorRepositoryJDBC;
import com.sumith.jfs.PaAnaBot.repositoryjdbc.PatientRepositoryJDBC;
import com.sumith.jfs.PaAnaBot.repositoryjdbc.ReportRepositoryJDBC;
import com.sumith.jfs.PaAnaBot.repositoryjdbc.ChatRepositoryJDBC;
@Component
public class DBInit implements CommandLineRunner {
    private final DoctorRepositoryJDBC doctorRepo;
    private final PatientRepositoryJDBC patientRepo;
    private final ReportRepositoryJDBC reportRepo;
    private final ChatRepositoryJDBC chatRepo;
    public DBInit(DoctorRepositoryJDBC doctorRepo,
            PatientRepositoryJDBC patientRepo,
            ReportRepositoryJDBC reportRepo,
            ChatRepositoryJDBC chatRepo) {
        this.doctorRepo = doctorRepo;
        this.patientRepo = patientRepo;
        this.reportRepo = reportRepo;
        this.chatRepo = chatRepo;
    }
    @Override
    public void run(String... args) throws Exception {
        doctorRepo.createTableIfNotExists();
        patientRepo.createTableIfNotExists();
        reportRepo.createTableIfNotExists();
        chatRepo.createTableIfNotExists();
        System.out.println("✅ All SQLite tables are ready!");
    }
}
