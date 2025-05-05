package com.example.it_proger.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import javax.xml.crypto.Data;
import java.time.LocalDate;

@Entity
public class Guests {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long guestId;

    private String fName;
    private String lName;
    private String pName;
    private LocalDate dataBorn;
    private String phone;
    private String email;
    private String address;
    private String passportNumber;


    public Guests(){}


    public Guests(Long guestId, String fName, String lName, String pName, LocalDate dataBorn, String phone, String email, String address, String passportNumber) {
        this.guestId = guestId;
        this.fName = fName;
        this.lName = lName;
        this.pName = pName;
        this.dataBorn = dataBorn;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.passportNumber = passportNumber;
    }


    public Long getGuestId() {
        return guestId;
    }

    public void setGuestId(Long guestId) {
        this.guestId = guestId;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public LocalDate getDataBorn() {
        return dataBorn;
    }

    public void setDataBorn(LocalDate dataBorn) {
        this.dataBorn = dataBorn;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }
}
