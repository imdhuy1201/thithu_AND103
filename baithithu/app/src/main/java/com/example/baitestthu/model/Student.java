package com.example.baitestthu.model;

import com.google.gson.annotations.SerializedName;

public class Student {
    @SerializedName("_id")
    private String id;
    private String hoten,quequan;
    private double diem;
    private String hinhanh;

    public Student() {
    }

    public Student(String id, String hoten, String quequan, double diem, String hinhanh) {
        this.id = id;
        this.hoten = hoten;
        this.quequan = quequan;
        this.diem = diem;
        this.hinhanh = hinhanh;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public String getQuequan() {
        return quequan;
    }

    public void setQuequan(String quequan) {
        this.quequan = quequan;
    }

    public double getDiem() {
        return diem;
    }

    public void setDiem(double diem) {
        this.diem = diem;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }
}
