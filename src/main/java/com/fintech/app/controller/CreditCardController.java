package com.fintech.app.controller;

import com.fintech.app.entity.CreditCard;
import com.fintech.app.repository.CreditCardRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cards")
public class CreditCardController {

    private final CreditCardRepository cardRepo;

    public CreditCardController(CreditCardRepository cardRepo) {
        this.cardRepo = cardRepo;
    }

    // Show credit card creation form (GET /cards/new)
    @GetMapping("/new")
    public String showAddCardForm(Model model) {
        model.addAttribute("card", new CreditCard());
        return "credit-card"; // maps to templates/credit-card.html
    }

    // Show card list and creation form (GET /cards)
    @GetMapping
    public String showCardForm(Model model) {
        model.addAttribute("card", new CreditCard());
        model.addAttribute("cards", cardRepo.findAll());
        return "credit-card"; // updated to match the actual template name
    }

    // Save new card (POST /cards)
    @PostMapping
    public String addCard(
            @Valid @ModelAttribute("card") CreditCard card,
            org.springframework.validation.BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("cards", cardRepo.findAll());
            model.addAttribute("errorMessage", "Please correct the highlighted fields and try again.");
            return "credit-card";
        }
        cardRepo.save(card);
        redirectAttributes.addFlashAttribute("successMessage", "Credit card added successfully.");
        return "redirect:/cards";
    }
}
