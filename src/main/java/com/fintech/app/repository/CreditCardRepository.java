package com.fintech.app.repository;

import com.fintech.app.entity.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {

    // 🔍 Search by card holder name (optional feature for UI filtering)
    List<CreditCard> findByCardHolderNameContainingIgnoreCase(String name);
}
