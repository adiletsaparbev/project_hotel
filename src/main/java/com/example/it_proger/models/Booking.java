package com.example.it_proger.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String roomNumber;
    private String firstName;
    private String lastName;
    private String hotel;
    private String address;
    private LocalDate dateOfBirth;
    private String passportNumber;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String password;
    private double totalAmount;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    public Booking(String userName, String userEmail, String checkInDate, String checkOutDate, Room room) {
    }


    public Booking() {
    }

    @PrePersist
    private void init() {
        if (status == null) {
            status = BookingStatus.PENDING; // по умолчанию статус "Ожидает оплаты"
        }
    }

}
