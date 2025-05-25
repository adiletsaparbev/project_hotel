package com.example.it_proger.controller;
import com.example.it_proger.models.AppUser;
import com.example.it_proger.models.Document;
import com.example.it_proger.repo.AppUserRepository;
import com.example.it_proger.repo.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;

@Controller
@RequestMapping("/document")
public class DocumentController {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private DocumentRepository documentRepository;

    // Показать форму добавления документа по userId
    @GetMapping("/add/{userId}")
    public String showAddDocumentForm(@PathVariable("userId") int userId, Model model) {
        AppUser user = userRepository.findById(userId);
        if (user == null) {
            model.addAttribute("error", "Пользователь не найден.");
            return "error";
        }
        model.addAttribute("user", user);
        return "profile/add-document";  // это имя Thymeleaf шаблона (add-document.html)
    }
    @PostMapping("/document/add")
    public String addDocument(@RequestParam("documentType") String documentType,
                              @RequestParam("passportSeries") String passportSeries,
                              @RequestParam("passportNumber") String passportNumber,
                              @RequestParam("issuedBy") String issuedBy,
                              @RequestParam("issueDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate issueDate,
                              @RequestParam("expiryDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expiryDate,
                              @RequestParam("placeOfBirth") String placeOfBirth,
                              @RequestParam("nationality") String nationality,
                              Principal principal,
                              Model model) {

        AppUser user = userRepository.findByEmail(principal.getName());
        if (user == null) {
            model.addAttribute("message", "Пользователь не найден");
            return "add-document";
        }

        Document document = new Document();
        document.setUser(user);
        document.setDocumentType(documentType);
        document.setPassportSeries(passportSeries);
        document.setPassportNumber(passportNumber);
        document.setIssuedBy(issuedBy);
        document.setIssueDate(issueDate);
        document.setExpiryDate(expiryDate);
        document.setPlaceOfBirth(placeOfBirth);
        document.setNationality(nationality);

        documentRepository.save(document);

        model.addAttribute("message", "Документ успешно сохранён!");
        return "redirect:/profile";
    }
}
