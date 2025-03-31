package com.fintech.app.controller;

import com.fintech.app.entity.CreditCard;
import com.fintech.app.entity.Expense;
import com.fintech.app.repository.CreditCardRepository;
import com.fintech.app.service.ExpenseService;
import com.fintech.app.service.ChartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class ExpenseController {

    private final ExpenseService service;
    private final CreditCardRepository cardRepo;
    private final ChartService chartService;

    public ExpenseController(ExpenseService service, CreditCardRepository cardRepo, ChartService chartService) {
        this.service = service;
        this.cardRepo = cardRepo;
        this.chartService = chartService;
    }

    // 🏠 Landing page
    @GetMapping("/")
    public String home() {
        return "index"; // maps to src/main/resources/templates/index.html
    }

    // 📁 Show all expenses for the current month (across all cards)
    @GetMapping("/expenses")
    public String list(Model model) {
        model.addAttribute("expenses", service.findMonthlyExpenses());
        model.addAttribute("expense", new Expense());
        model.addAttribute("cards", cardRepo.findAll());
        return "expenses"; // maps to expenses.html
    }

    // ➕ Add new expense
    @PostMapping("/expenses")
    public String addExpense(@ModelAttribute Expense expense) {
        service.save(expense);
        return "redirect:/expenses";
    }

    // 📊 Dashboard view with optional card filter
    @GetMapping("/expenses/dashboard")
    public String dashboard(@RequestParam(required = false) Long cardId, Model model) {
        List<CreditCard> cards = cardRepo.findAll();
        model.addAttribute("cards", cards);

        if (cardId != null) {
            model.addAttribute("expenses", service.findExpensesByCardIdThisMonth(cardId));
            model.addAttribute("totalSpent", service.calculateTotalForCardThisMonth(cardId));
            model.addAttribute("selectedCardId", cardId);

            CreditCard card = cardRepo.findById(cardId).orElse(null);
            model.addAttribute("limit", card != null ? card.getCreditLimit() : 0);

            // 📊 Inject chart data
            Map<String, Double> categoryData = chartService.getCategoryTotals(cardId);
            model.addAttribute("categoryLabels", categoryData.keySet());
            model.addAttribute("categoryData", categoryData.values());

            Map<String, Double> vendorData = chartService.getVendorTotals(cardId);
            model.addAttribute("vendorLabels", vendorData.keySet());
            model.addAttribute("vendorTotals", vendorData.values());

            Map<String, Double> dailyData = chartService.getDailySpend(cardId);
            model.addAttribute("dailyLabels", dailyData.keySet());
            model.addAttribute("dailyTotals", dailyData.values());
        }

        return "dashboard"; // maps to dashboard.html
    }
}
