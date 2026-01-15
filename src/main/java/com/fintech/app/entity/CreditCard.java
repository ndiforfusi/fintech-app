package com.fintech.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Entity
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Card holder name is required.")
    @Size(max = 100, message = "Card holder name must be 100 characters or fewer.")
    @Column(nullable = false)
    private String cardHolderName;

    @NotBlank(message = "Masked number is required.")
    @Pattern(
            regexp = "^[*]{4}(\\s?[*]{4}){2}\\s?\\d{4}$",
            message = "Masked number must look like **** **** **** 1234."
    )
    @Column(nullable = false)
    private String maskedNumber;

    @NotBlank(message = "Card type is required.")
    @Size(max = 30, message = "Card type must be 30 characters or fewer.")
    @Column(nullable = false)
    private String cardType;

    @NotNull(message = "Expiry date is required.")
    @Future(message = "Expiry date must be in the future.")
    @Column(nullable = false)
    private LocalDate expiry;

    @Positive(message = "Credit limit must be greater than zero.")
    @Column(nullable = false)
    private double creditLimit; // 💳 Spending cap for card

    // ====== Getters and Setters ======

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getMaskedNumber() {
        return maskedNumber;
    }

    public void setMaskedNumber(String maskedNumber) {
        this.maskedNumber = maskedNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public LocalDate getExpiry() {
        return expiry;
    }

    public void setExpiry(LocalDate expiry) {
        this.expiry = expiry;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }
}
