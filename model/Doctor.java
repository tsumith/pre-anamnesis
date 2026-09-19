package com.sumith.jfs.PaAnaBot.model;
public class Doctor {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String specialization;
    private String doctorCode;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getSpecialization() {
        return specialization;
    }
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    public void setDoctorCode(String doctorCode) {
        this.doctorCode = doctorCode;
    }
    public String getDoctorCode() {
        return doctorCode;
    }
    public String toString() {
        return "doctor id:" + this.id + "name:" + this.name + "doctor id: " + doctorCode;
    }
}
