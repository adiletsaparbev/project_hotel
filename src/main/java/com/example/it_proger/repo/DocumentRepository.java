package com.example.it_proger.repo;

import com.example.it_proger.models.AppUser;
import com.example.it_proger.models.Country;
import com.example.it_proger.models.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Integer> {
    List<Document> findByUser(AppUser user);

}