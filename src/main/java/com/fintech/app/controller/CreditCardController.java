package com.fintech.app.controller;

import com.fintech.app.entity.CreditCard;
import com.fintech.app.repository.CreditCardRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cards")
public class CreditCardController {

    private final CreditCardRepository cardRepo;

    public CreditCardController(CreditCardRepository cardRepo) {
        this.cardRepo = cardRepo;
    }

    // 🔍 Show card creation form and list
    @GetMapping
    public String showCardForm(Model model) {
        model.addAttribute("card", new CreditCard());
        model.addAttribute("cards", cardRepo.findAll());
        return "cards"; // maps to cards.html in templates/
    }

    // ➕ Add new credit card
    @PostMapping
    public String addCard(@ModelAttribute CreditCard card) {
        cardRepo.save(card);
        return "redirect:/cards";
    }
}
