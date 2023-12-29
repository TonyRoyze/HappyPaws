package com.example.happypaws.Classes;

public class Post {
    private int id;
    private int ownerId;
    private int caretakerId;
    private int petId;
    private String phone;
    private String location;
    private String services;
    private String status;
    private String startDate;
    private String endDate;

    public Post(int id, int ownerId, int caretakerId, int petId, String phone, String location, String services, String status, String startDate, String endDate) {
        this.id = id;
        this.ownerId = ownerId;
        this.caretakerId = caretakerId;
        this.petId = petId;
        this.phone = phone;
        this.location = location;
        this.services = services;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getCaretakerId() {
        return caretakerId;
    }

    public void setCaretakerId(int caretakerId) {
        this.caretakerId = caretakerId;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
