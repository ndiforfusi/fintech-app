package com.fintech.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Entity
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Vendor is required.")
    @Size(max = 100, message = "Vendor must be 100 characters or fewer.")
    private String vendor;

    @Positive(message = "Amount must be greater than zero.")
    private double amount;

    @NotNull(message = "Date is required.")
    @PastOrPresent(message = "Date cannot be in the future.")
    private LocalDate date;

    @NotBlank(message = "Category is required.")
    @Size(max = 60, message = "Category must be 60 characters or fewer.")
    private String category;

    @ManyToOne
    @NotNull(message = "Credit card selection is required.")
    private CreditCard card;

    // ====== Getters and Setters ======

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public CreditCard getCard() {
        return card;
    }

    public void setCard(CreditCard card) {
        this.card = card;
    }
}
