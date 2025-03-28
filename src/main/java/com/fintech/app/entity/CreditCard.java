package com.fintech.app.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cardHolderName;
    private String maskedNumber;
    private String cardType;
    private LocalDate expiry;

    // Getters & Setters
}
