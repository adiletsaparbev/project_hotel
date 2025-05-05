package com.example.it_proger.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cardNumber;       // Номер карты
    private String cardHolderName;   // Имя владельца карты
    private LocalDate expiryDate;    // Срок действия карты
    private double balance;          // Баланс карты
    private String cvv;              // CVV код

}