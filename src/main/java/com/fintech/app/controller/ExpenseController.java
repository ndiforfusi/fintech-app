package com.fintech.app.controller;

import com.fintech.app.entity.CreditCard;
import com.fintech.app.entity.Expense;
import com.fintech.app.repository.CreditCardRepository;
import com.fintech.app.service.ChartService;
import com.fintech.app.service.ExpenseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Controller
public class ExpenseController {

    private final ExpenseService expenseService;
    private final CreditCardRepository cardRepo;
    private final ChartService chartService;

    public ExpenseController(ExpenseService expenseService, CreditCardRepository cardRepo, ChartService chartService) {
        this.expenseService = expenseService;
        this.cardRepo = cardRepo;
        this.chartService = chartService;
    }

    // 🏠 Landing Page
    @GetMapping("/")
    public String home() {
        return "index";
    }

    // 📁 Expense List
    @GetMapping("/expenses")
    public String listExpenses(Model model) {
        List<Expense> expenses = expenseService.findMonthlyExpenses();
        List<CreditCard> cards = cardRepo.findAll();

        model.addAttribute("expenses", expenses);
        model.addAttribute("expense", new Expense());
        model.addAttribute("cards", cards);

        return "expenses";
    }

    // ➕ Add Expense
    @PostMapping("/expenses")
    public String addExpense(
            @Valid @ModelAttribute("expense") Expense expense,
            org.springframework.validation.BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request
    ) {
        if (expense.getCard() != null && expense.getCard().getId() != null) {
            cardRepo.findById(expense.getCard().getId()).ifPresentOrElse(
                    expense::setCard,
                    () -> bindingResult.rejectValue("card", "card.notFound", "Selected credit card was not found.")
            );
        } else {
            bindingResult.rejectValue("card", "card.required", "Please select a credit card.");
        }

        if (bindingResult.hasErrors()) {
            if (isDashboardRequest(request)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Please correct the highlighted fields and try again.");
                return "redirect:/expenses/dashboard";
            }

            model.addAttribute("expenses", expenseService.findMonthlyExpenses());
            model.addAttribute("cards", cardRepo.findAll());
            model.addAttribute("errorMessage", "Please correct the highlighted fields and try again.");
            return "expenses";
        }

        expenseService.save(expense);
        redirectAttributes.addFlashAttribute("successMessage", "Expense added successfully.");
        return "redirect:" + resolveRedirectTarget(request, expense);
    }

    // 📊 Dashboard View
    @GetMapping("/expenses/dashboard")
    public String dashboard(@RequestParam(required = false) Long cardId, Model model) {
        List<CreditCard> cards = cardRepo.findAll();
        model.addAttribute("cards", cards);
        model.addAttribute("expense", new Expense()); // ✅ Needed for form binding

        if (cardId != null) {
            model.addAttribute("selectedCardId", cardId);
            model.addAttribute("expenses", expenseService.findExpensesByCardIdThisMonth(cardId));
            model.addAttribute("totalSpent", expenseService.calculateTotalForCardThisMonth(cardId));

            CreditCard selectedCard = cardRepo.findById(cardId).orElse(null);
            model.addAttribute("limit", selectedCard != null ? selectedCard.getCreditLimit() : 0.0);

            // 📊 Chart Data
            Map<String, Double> categoryData = chartService.getCategoryTotals(cardId);
            model.addAttribute("categoryLabels", categoryData.keySet());
            model.addAttribute("categoryData", categoryData.values());

            Map<String, Double> vendorData = chartService.getVendorTotals(cardId);
            model.addAttribute("vendorLabels", vendorData.keySet());
            model.addAttribute("vendorTotals", vendorData.values());

            Map<String, Double> dailyData = chartService.getDailySpend(cardId);
            model.addAttribute("dailyLabels", dailyData.keySet());
            model.addAttribute("dailyTotals", dailyData.values());
        } else {
            // 👇 Prevent Thymeleaf binding errors when no card selected
            model.addAttribute("expenses", List.of());
            model.addAttribute("totalSpent", 0.0);
            model.addAttribute("limit", 0.0);
            model.addAttribute("categoryLabels", List.of());
            model.addAttribute("categoryData", List.of());
            model.addAttribute("vendorLabels", List.of());
            model.addAttribute("vendorTotals", List.of());
            model.addAttribute("dailyLabels", List.of());
            model.addAttribute("dailyTotals", List.of());
        }

        return "dashboard";
    }

    // ⬇️ CSV Export
    @GetMapping("/expenses/export")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=expenses.csv");

        List<Expense> expenses = expenseService.findMonthlyExpenses();
        PrintWriter writer = response.getWriter();
        writer.println("Vendor,Amount,Date,Category,Credit Card");

        for (Expense e : expenses) {
            writer.printf("%s,%.2f,%s,%s,%s%n",
                    csvSafe(e.getVendor()),
                    e.getAmount(),
                    csvSafe(e.getDate() != null ? e.getDate().toString() : ""),
                    csvSafe(e.getCategory()),
                    csvSafe(e.getCard() != null ? e.getCard().getMaskedNumber() : "N/A"));
        }

        writer.flush();
        writer.close();
    }

    private boolean isDashboardRequest(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        return referer != null && referer.contains("/expenses/dashboard");
    }

    private String resolveRedirectTarget(HttpServletRequest request, Expense expense) {
        if (isDashboardRequest(request) && expense.getCard() != null && expense.getCard().getId() != null) {
            return "/expenses/dashboard?cardId=" + expense.getCard().getId();
        }
        return "/expenses";
    }

    private String csvSafe(String value) {
        if (value == null) {
            return "";
        }
        String sanitized = value.replaceAll("[\\r\\n]+", " ").trim();
        if (!sanitized.isEmpty()) {
            char first = sanitized.charAt(0);
            if (first == '=' || first == '+' || first == '-' || first == '@') {
                sanitized = "'" + sanitized;
            }
        }
        if (sanitized.contains("\"")) {
            sanitized = sanitized.replace("\"", "\"\"");
        }
        if (sanitized.contains(",") || sanitized.contains("\"")) {
            sanitized = "\"" + sanitized + "\"";
        }
        return sanitized;
    }
}
