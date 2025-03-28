package com.fintech.app.controller;

import com.fintech.app.entity.Expense;
import com.fintech.app.service.ExpenseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService service;

    public ExpenseController(ExpenseService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("expenses", service.findMonthlyExpenses());
        return "expenses";
    }

    @PostMapping
    public String addExpense(@ModelAttribute Expense expense) {
        service.save(expense);
        return "redirect:/expenses";
    }
}
