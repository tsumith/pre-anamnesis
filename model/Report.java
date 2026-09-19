package com.sumith.jfs.PaAnaBot.model;
public class Report {
    private Long id;
    private String doctorCode;
    private String patientEmail;
    private String reportText;
    private String date;
    public Report() {
    }
    public Report(String doctorCode, String patientEmail, String reportText, String date) {
        this.doctorCode = doctorCode;
        this.patientEmail = patientEmail;
        this.reportText = reportText;
        this.date = date;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDoctorCode() {
        return doctorCode;
    }
    public void setDoctorCode(String doctorCode) {
        this.doctorCode = doctorCode;
    }
    public String getPatientEmail() {
        return patientEmail;
    }
    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }
    public String getReportText() {
        return reportText;
    }
    public void setReportText(String reportText) {
        this.reportText = reportText;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}
