package com.example.it_proger.controller;

import com.example.it_proger.models.AppUser;
import com.example.it_proger.models.Booking;
import com.example.it_proger.models.Document;
import com.example.it_proger.repo.AppUserRepository;
import com.example.it_proger.repo.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @GetMapping("/profile")
    public String userProfile(Model model, Principal principal) {
        String email = principal.getName(); // текущий залогиненный пользователь
        AppUser user = appUserRepository.findByEmail(email); // метод нужно реализовать в репозитории
        model.addAttribute("bookings", user.getBookings());
        model.addAttribute("user", user);
        model.addAttribute("documents", user.getDocuments());
        return "profile/profile_user";
    }

    @GetMapping("/users/{id}/documents")
    public String getUserDocuments(@PathVariable("id") int userId, Model model) {
        AppUser user = appUserRepository.findById(userId);
        if (user == null) {
            return "error/404";
        }
        model.addAttribute("user", user);
        model.addAttribute("documents", user.getDocuments());
        return "profile/user-documents";
    }
}
