package com.example.it_proger.repo;

import com.example.it_proger.models.Guests;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestsRepository extends JpaRepository<Guests, Long> {
}
