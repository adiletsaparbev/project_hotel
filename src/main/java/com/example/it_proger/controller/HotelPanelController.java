package com.example.it_proger.controller;

import com.example.it_proger.models.Booking;
import com.example.it_proger.models.City;
import com.example.it_proger.models.Hotel;
import com.example.it_proger.models.Room;
import com.example.it_proger.models.AppUser;
import com.example.it_proger.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

// HotelController.java (панель работников, выбор страны, города, отеля)
@Controller
@RequestMapping("/hotel-panel")
public class HotelPanelController {
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping("/locations")
    public String getCountries(Model model) {
        model.addAttribute("countries", countryRepository.findAll());
        return "worker/hotel_panel";
    }

    @GetMapping("/cities/{countryId}")
    public String getCities(@PathVariable Integer countryId, Model model) {
        List<City> cities = cityRepository.findByCountryId(countryId);
        model.addAttribute("cities", cities);
        return "fragments/cities :: cityTable";
    }

    @GetMapping("/hotels/{cityId}")
    public String getHotels(@PathVariable Integer cityId, Model model) {
        List<Hotel> hotels = hotelRepository.findByCityId(cityId);
        model.addAttribute("hotels", hotels);
        return "fragments/hotels :: hotelTable";
    }

    @GetMapping("/access")
    public String showPasswordForm(@RequestParam(required = false) Integer hotelId, Model model) {
        model.addAttribute("error", false);
        model.addAttribute("hotelId", hotelId);
        return "worker/hotel_password";
    }


    // Обработать ввод пароля
    @PostMapping("/access")
    public String checkHotelPassword(@RequestParam Integer hotelId,
                                     @RequestParam String password,
                                     Model model) {
        Optional<Hotel> hotelOpt = hotelRepository.findById(hotelId);

        if (hotelOpt.isPresent()) {
            Hotel hotel = hotelOpt.get();
            if (hotel.getPassword().equals(password)) {
                return "redirect:/hotel-panel/info/" + hotel.getId();
            } else {
                model.addAttribute("error", true);
                model.addAttribute("hotelId", hotelId);
                return "worker/hotel_password";
            }
        } else {
            // Отеля с таким id нет — можно вернуть ошибку или редирект
            return "redirect:/hotel-panel/locations";
        }
    }


    @GetMapping("/info/{hotelId}")
    public String hotelInfoPage(@PathVariable Integer hotelId, Model model) {
        Optional<Hotel> hotelOpt = hotelRepository.findById(hotelId);
        if (hotelOpt.isPresent()) {
            Hotel hotel = hotelOpt.get();
            model.addAttribute("hotel", hotel);
            model.addAttribute("rooms", hotel.getRooms());
            model.addAttribute("room", new Room()); // ← добавлено для формы
            return "worker/hotel_info";
        } else {
            return "redirect:/hotel-panel/locations";
        }
    }

    @GetMapping("/rooms/add/{hotelId}")
    public String showAddRoomForm(@PathVariable Integer hotelId, Model model) {
        Optional<Hotel> hotelOpt = hotelRepository.findById(hotelId);
        if (hotelOpt.isPresent()) {
            Room room = new Room();
            room.setHotel(hotelOpt.get());
            model.addAttribute("room", room);
            model.addAttribute("hotelId", hotelId);
            return "worker/add_room";
        } else {
            return "redirect:/hotel-panel/locations";
        }
    }

    @PostMapping("/rooms/add")
    public String addRoom(@ModelAttribute Room room, @RequestParam Integer hotelId) {
        Optional<Hotel> hotelOpt = hotelRepository.findById(hotelId);
        if (hotelOpt.isPresent()) {
            room.setHotel(hotelOpt.get());
            roomRepository.save(room);
        }
        return "redirect:/hotel-panel/info/" + hotelId;
    }

    @GetMapping("/rooms/search")
    public String searchRooms(
            @RequestParam Integer hotelId,
            @RequestParam(required = false) String roomNumber,
            @RequestParam(required = false) String roomType,
            @RequestParam(required = false) Integer roomId,
            Model model
    ) {
        Optional<Hotel> hotelOpt = hotelRepository.findById(hotelId);
        if (hotelOpt.isEmpty()) {
            return "redirect:/hotel-panel/locations";
        }

        Hotel hotel = hotelOpt.get();

        List<Room> filteredRooms = hotel.getRooms().stream()
                .filter(room -> (roomNumber == null || room.getRoomNumber().contains(roomNumber)))
                .filter(room -> (roomType == null || room.getRoomType().toLowerCase().contains(roomType.toLowerCase())))
                .filter(room -> (roomId == null || Objects.equals(room.getRoomId(), roomId)))
                .toList();

        model.addAttribute("hotel", hotel);
        model.addAttribute("rooms", filteredRooms);
        model.addAttribute("room", new Room()); // для формы создания

        return "worker/hotel_info";
    }



    @GetMapping("/bookings/{hotelId}")
    public String getHotelBookings(@PathVariable Integer hotelId, Model model) {
        Optional<Hotel> hotelOpt = hotelRepository.findById(hotelId);
        if (hotelOpt.isEmpty()) {
            return "redirect:/hotel-panel/locations";
        }

        Hotel hotel = hotelOpt.get();

        // Получаем все бронирования через комнаты отеля
        List<Booking> bookings = hotel.getRooms().stream()
                .flatMap(room -> room.getBookings().stream())
                .collect(Collectors.toList());

        model.addAttribute("hotel", hotel);
        model.addAttribute("bookings", bookings);

        return "worker/hotel_bookings"; // создадим эту страницу
    }
    @GetMapping("/booking/{id}")
    public String getBookingDetails(@PathVariable Long id, Model model) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Бронирование не найдено: " + id));

        Room room = booking.getRoom();
        AppUser user = booking.getUser(); // 👈 добавляем пользователя

        model.addAttribute("booking", booking);
        model.addAttribute("room", room);
        model.addAttribute("user", user); // 👈 передаём в шаблон

        return "worker/booking-details";
    }

}
