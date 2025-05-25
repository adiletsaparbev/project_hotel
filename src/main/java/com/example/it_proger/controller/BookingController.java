package com.example.it_proger.controller;

import com.example.it_proger.models.*;
import com.example.it_proger.repo.*;
import com.example.it_proger.servise.BookingService;
import com.example.it_proger.servise.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/book")
public class BookingController {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private CardRepository cardRepository;
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }
    @GetMapping("/bookings")
    public String showAllBookings(Model model) {
        List<Booking> bookings = bookingService.getAllBookings();
        model.addAttribute("bookings", bookings);
        return "bookings"; // Возвращаем название HTML-страницы
    }

    @GetMapping("/search")
    public String searchBookings(
            @RequestParam(required = false) String roomNumber,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {

        List<Booking> results;

        if (roomNumber != null && !roomNumber.isEmpty()) {
            results = bookingService.searchByRoomNumber(roomNumber);
        } else if (date != null) {
            results = bookingService.searchByDate(date);
        } else {
            results = bookingService.getAllBookings(); // Все бронирования, если не задан фильтр
        }
        model.addAttribute("bookings", results);
        return "bookings"; // Возвращает ту же страницу со списком
    }
    // Отображение страницы с формой
    @GetMapping("/add-booking/{roomId}")
    public String showBookingForm(@PathVariable int roomId, Model model) {
        Room room = roomRepository.findById(roomId).orElse(null);
        if (room == null) {
            model.addAttribute("message", "Комната не найдена");
            return "error";
        }
        model.addAttribute("room", room);
        model.addAttribute("booking", new Booking()); // пустой объект для формы
        return "add-booking"; // твой шаблон формы бронирования
    }

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CardService cardService;
    @Autowired
    private RoomRepository roomRepository;

    @PostMapping("/add-booking")
    public String bookRoom(@RequestParam("roomId") int roomId,
                           @RequestParam("checkInDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
                           @RequestParam("checkOutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
                           @RequestParam("totalAmount") double totalAmount,
                           @RequestParam String cardNumber,
                           @RequestParam String cvv,
                           Principal principal,
                           Model model) {

        Room room = roomRepository.findById(roomId).orElse(null);
        if (room == null) {
            model.addAttribute("message", "Комната не найдена");
            return "add-booking";
        }

        AppUser appUser = appUserRepository.findByEmail(principal.getName());
        List<Document> documents = documentRepository.findByUser(appUser);

        // Найти документ типа "ПАСПОРТ"
        Optional<Document> passportDocOpt = documents.stream()
                .filter(doc -> "ПАСПОРТ".equalsIgnoreCase(doc.getDocumentType()))
                .findFirst();

        if (passportDocOpt.isEmpty()) {
            model.addAttribute("message", "У пользователя нет документа типа 'ПАСПОРТ'");
            return "add-booking";
        }

        Document passport = passportDocOpt.get();
        Hotel hotel = room.getHotel(); // Получаем отель из комнаты


        LocalDate today = LocalDate.now();
        Period age = Period.between(appUser.getBirthday(), today);
        if (age.getYears() < 18) {
            model.addAttribute("message", "Возраст должен быть старше 18 лет");
            return "add-booking";
        }

        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        if (nights <= 0) {
            model.addAttribute("message", "Неправильно введены даты");
            return "add-booking";
        }

        List<Booking> existingBookings = bookingRepository.findActiveBookingsByRoomNumber(room.getRoomNumber());
        for (Booking booking : existingBookings) {
            if ((checkInDate.isBefore(booking.getCheckOutDate()) && checkOutDate.isAfter(booking.getCheckInDate())) ||
                    checkInDate.equals(booking.getCheckInDate()) || checkOutDate.equals(booking.getCheckOutDate())) {
                model.addAttribute("message", "Комната уже забронирована на эти даты");
                return "add-booking";
            }
        }

        double totalBookingCost = nights * room.getPricePerNight();
        double minimumPrepayment = totalBookingCost * 0.3;

        if (totalAmount < minimumPrepayment) {
            model.addAttribute("message", "Цена должна быть больше 30% от общей суммы");
            return "add-booking";
        }

        Card card = cardRepository.findByCardNumberAndCvv(cardNumber, cvv).orElse(null);
        if (card == null) {
            model.addAttribute("message", "Карта не найдена! Введите данные правильно");
            return "add-booking";
        }

        if (card.getBalance() < totalAmount) {
            model.addAttribute("message", "Недостаточно средств на карте");
            return "add-booking";
        }

        card.setBalance(card.getBalance() - totalAmount);
        cardRepository.save(card);

        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setRoomNumber(room.getRoomNumber());
        booking.setFirstName(appUser.getFirstName());
        booking.setLastName(appUser.getLastName());
        booking.setDateOfBirth(appUser.getBirthday());
        booking.setPassportNumber(passport.getPassportNumber());
        booking.setCheckInDate(checkInDate);
        booking.setCheckOutDate(checkOutDate);
        booking.setTotalAmount(totalAmount);
        booking.setHotel(hotel.getNumber()); // или hotel.getName(), если есть такое поле
        booking.setAddress(hotel.getAddress());
        booking.setUser(appUser);
        booking.setStatus(totalAmount >= totalBookingCost ? BookingStatus.PAID : BookingStatus.PENDING);
        bookingRepository.save(booking);

        model.addAttribute("message", "Оплата прошла успешно! Статус бронирования: " +
                (totalAmount >= totalBookingCost ? "PAID" : "PENDING_PAYMENT") + ".");
        return "redirect:/profile";
    }
}
