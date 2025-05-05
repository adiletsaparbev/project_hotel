package com.example.it_proger.controller;


import com.example.it_proger.models.Amenity;
import com.example.it_proger.models.Guests;
import com.example.it_proger.models.Room;
import com.example.it_proger.repo.GuestsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class GuestController {

    @Autowired
    private GuestsRepository guestsRepository;

    @GetMapping("/guests")
    public String getAllRooms(Model model) {
        List<Guests> guests = guestsRepository.findAll();
        model.addAttribute("guests", guests);
        return "guests";
    }

    @PostMapping("/guests/add")
    public String addRoom(@ModelAttribute Guests guests, Model model) {
        guestsRepository.save(guests);
        model.addAttribute("message", "Room added successfully");
        return "redirect:/guests";
    }

    @PostMapping("/guests/delete/{id}")
    public String deleteGuest(@PathVariable Long id, Model model) {
        guestsRepository.deleteById(id);
        model.addAttribute("message", "Guest deleted successfully");
        return "redirect:/guests";
    }


    @GetMapping("/guests/{id}")
    public String getGuestDetails(@PathVariable Long id, Model model) {
        Guests guest = guestsRepository.findById(id).orElse(null);
        if (guest != null) {
            model.addAttribute("guest", guest);
            return "guest-details";  // Шаблон с деталями комнаты
        }
        model.addAttribute("message", "Guest not found");  // Если комната не найдена
        return "error";  // Ошибка, если комната не существует
    }

    @PostMapping("guests/update/{guestId}")
    public String updateGuest(@PathVariable Long guestId, @ModelAttribute Guests updatedGuest, Model model) {
        Guests guest = guestsRepository.findById(guestId).orElse(null);
        if (guest != null) {
            guest.setfName(updatedGuest.getfName());
            guest.setlName(updatedGuest.getlName());
            guest.setpName(updatedGuest.getpName());
            guest.setDataBorn(updatedGuest.getDataBorn());
            guest.setPhone(updatedGuest.getPhone());
            guest.setAddress(updatedGuest.getAddress());
            guest.setEmail(updatedGuest.getEmail());
            guest.setPassportNumber(updatedGuest.getPassportNumber());
            guestsRepository.save(guest);
            model.addAttribute("message", "Guest updated successfully");
        } else {
            model.addAttribute("message", "Guest not found");
        }
        return "redirect:/guests/" + guestId;
    }


}
