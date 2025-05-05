package com.example.it_proger.repo;

import com.example.it_proger.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByCardNumberAndCvv(String cardNumber, String cvv);

}