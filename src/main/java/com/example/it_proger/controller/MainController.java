package com.example.it_proger.controller;
import com.example.it_proger.models.Amenity;
import com.example.it_proger.models.Room;
import com.example.it_proger.servise.AmenityService;
import com.example.it_proger.servise.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final RoomService roomService;
    private final AmenityService amenityService;

    @GetMapping("/")
    public String getRooms(Model model) {
        // Получаем список уникальных удобств (с учетом регистра)
        List<String> amenities = amenityService.getUniqueAmenities().stream()
                .map(String::toLowerCase) // Приводим все удобства к нижнему регистру
                .distinct() // Убираем дубликаты
                .collect(Collectors.toList());
        model.addAttribute("amenities", amenities);

        // Получаем все доступные комнаты
        List<Room> rooms = roomService.getAvailableRooms();
        model.addAttribute("rooms", rooms);

        return "main"; // Название Thymeleaf-шаблона
    }

    @PostMapping("/")
    public String getFilteredRooms(@RequestParam(required = false) Double minPrice,
                                   @RequestParam(required = false) Double maxPrice,
                                   @RequestParam(required = false) List<String> amenities,
                                   Model model) {
        // Получаем все доступные комнаты
        List<Room> filteredRooms = roomService.getAvailableRooms();

        // Фильтрация по цене
        if (minPrice != null) {
            filteredRooms = filteredRooms.stream()
                    .filter(room -> room.getPricePerNight() >= minPrice)
                    .collect(Collectors.toList());
        }

        if (maxPrice != null) {
            filteredRooms = filteredRooms.stream()
                    .filter(room -> room.getPricePerNight() <= maxPrice)
                    .collect(Collectors.toList());
        }

        // Фильтрация по удобствам (с учетом регистра)
        if (amenities != null && !amenities.isEmpty()) {
            filteredRooms = filteredRooms.stream()
                    .filter(room -> {
                        Set<String> roomAmenities = room.getAmenities().stream()
                                .map(amenity -> amenity.getName().toLowerCase()) // Приводим к нижнему регистру
                                .collect(Collectors.toSet());
                        // Проверяем, что все выбранные удобства присутствуют в комнате
                        return roomAmenities.containsAll(amenities.stream()
                                .map(String::toLowerCase)
                                .collect(Collectors.toSet()));
                    })
                    .collect(Collectors.toList());
        }

        // Получаем список всех удобств для фильтрации
        List<Amenity> allAmenities = amenityService.getAllAmenities();

        // Добавляем данные в модель
        model.addAttribute("rooms", filteredRooms);
        model.addAttribute("amenities", allAmenities);

        return "main"; // Перенаправляем на страницу с результатами фильтрации
    }


    @GetMapping("/admin")
    public String adminPage() {
        return "admin";
    }
}

