package com.fintech.app.config;

import com.fintech.app.entity.CreditCard;
import com.fintech.app.entity.Expense;
import com.fintech.app.repository.CreditCardRepository;
import com.fintech.app.service.ExpenseService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataSeeder {

    private final CreditCardRepository creditCardRepo;
    private final ExpenseService expenseService;

    public DataSeeder(CreditCardRepository creditCardRepo, ExpenseService expenseService) {
        this.creditCardRepo = creditCardRepo;
        this.expenseService = expenseService;
    }

    @PostConstruct
    public void seed() {
        if (creditCardRepo.count() == 0) {
            CreditCard card1 = new CreditCard();
            card1.setCardHolderName("Alex Johnson");
            card1.setCardType("Visa");
            card1.setCreditLimit(5000.0);
            card1.setMaskedNumber("**** **** **** 1234");
            card1.setExpiry(LocalDate.of(2026, 12, 31));
            creditCardRepo.save(card1);

            CreditCard card2 = new CreditCard();
            card2.setCardHolderName("Maria Gomez");
            card2.setCardType("MasterCard");
            card2.setCreditLimit(7500.0);
            card2.setMaskedNumber("**** **** **** 5678");
            card2.setExpiry(LocalDate.of(2025, 9, 30));
            creditCardRepo.save(card2);

            Expense expense = new Expense();
            expense.setAmount(85.25);
            expense.setCategory("Groceries");
            expense.setVendor("Whole Foods");
            expense.setDate(LocalDate.now().minusDays(2));
            expense.setCard(card1);

            expenseService.save(expense);

            System.out.println("✅ Sample data seeded: 2 cards + 1 expense");
        }
    }
}
