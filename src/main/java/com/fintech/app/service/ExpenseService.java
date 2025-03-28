package com.fintech.app.service;

import com.fintech.app.entity.Expense;
import com.fintech.app.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseService {
    private final ExpenseRepository repo;

    public ExpenseService(ExpenseRepository repo) {
        this.repo = repo;
    }

    public List<Expense> findMonthlyExpenses() {
        LocalDate start = LocalDate.now().withDayOfMonth(1);
        LocalDate end = LocalDate.now();
        return repo.findByDateBetween(start, end);
    }

    public Expense save(Expense expense) {
        return repo.save(expense);
    }
}
