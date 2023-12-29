package com.example.happypaws.Classes;

public class Pet {
    private int id;
    private String name;
    private int age;
    private String type;
    private String breed;
    private String sex;
    private String color;
    private String note;
    private int userid;

    public Pet(int id, String name, int age, String type, String breed, String sex, String color, String note, int userid) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.type = type;
        this.breed = breed;
        this.sex = sex;
        this.color = color;
        this.note = note;
        this.userid = userid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}
