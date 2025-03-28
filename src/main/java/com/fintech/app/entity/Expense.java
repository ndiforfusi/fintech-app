package com.fintech.app.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vendor;
    private double amount;
    private LocalDate date;
    private String category;

    @ManyToOne
    private CreditCard card;

    // Getters & Setters
}
