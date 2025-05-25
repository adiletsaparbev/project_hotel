package com.example.it_proger.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Тип документа: например, ПАСПОРТ, ЗАГРАНПАСПОРТ, ID-КАРТА и т.д.
    @Column(nullable = false)
    private String documentType;

    // Серия и номер паспорта
    @Column(nullable = false, unique = true)
    private String passportSeries;

    @Column(nullable = false, unique = true)
    private String passportNumber;

    // Кем выдан
    private String issuedBy;

    // Дата выдачи
    private LocalDate issueDate;

    // Дата окончания срока действия
    private LocalDate expiryDate;

    // Место рождения (по паспорту)
    private String placeOfBirth;

    // Гражданство
    private String nationality;

    // Связь с пользователем
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    public Document(){};
}
