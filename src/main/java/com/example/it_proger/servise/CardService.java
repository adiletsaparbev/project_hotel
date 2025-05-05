package com.example.it_proger.servise;

import com.example.it_proger.models.Card;
import com.example.it_proger.repo.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CardService {
    @Autowired
    private CardRepository cardRepository;

    public Optional<Card> findCardByNumberAndCvv(String cardNumber, String cvv) {
        return cardRepository.findByCardNumberAndCvv(cardNumber, cvv);
    }
}
