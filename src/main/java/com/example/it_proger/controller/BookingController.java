package com.example.it_proger.controller;

import com.example.it_proger.models.Booking;
import com.example.it_proger.models.BookingStatus;
import com.example.it_proger.models.Card;
import com.example.it_proger.models.Room;
import com.example.it_proger.repo.BookingRepository;
import com.example.it_proger.repo.CardRepository;
import com.example.it_proger.repo.RoomRepository;
import com.example.it_proger.servise.BookingService;
import com.example.it_proger.servise.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/book")
public class BookingController {

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
    @GetMapping("/add-booking")
    public String booking() {
        return "add-booking"; // Страница с формой бронирования
    }
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CardService cardService;
    @Autowired
    private RoomRepository roomRepository;

    @PostMapping("/add-booking")
    public String bookRoom(@RequestParam("roomNumber") String roomNumber,
                           @RequestParam("firstName") String firstName,
                           @RequestParam("lastName") String lastName,
                           @RequestParam("dateOfBirth") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
                           @RequestParam("passportNumber") String passportNumber,
                           @RequestParam("checkInDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
                           @RequestParam("checkOutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
                           @RequestParam("totalAmount") double totalAmount,
                           @RequestParam String cardNumber,
                           @RequestParam String cvv,
                           Model model) {
        // Проверка возраста клиента
        LocalDate today = LocalDate.now();
        Period age = Period.between(dateOfBirth, today);
        if (age.getYears() < 18) {
            model.addAttribute("message", "Возраст должен быть старше 18 лет");
            return "add-booking";
        }

        // Проверка дат
        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        if (nights <= 0) {
            model.addAttribute("message", "Не правильно вводил дату");
            return "add-booking";
        }

        // Проверка наличия комнаты
        Room room = roomRepository.findByRoomNumber(roomNumber);
        if (room == null) {
            model.addAttribute("message", "Комната не найден");
            return "add-booking";
        }

        // Проверка пересечения с существующими бронированиями
        List<Booking> existingBookings = bookingRepository.findActiveBookingsByRoomNumber(roomNumber);
        for (Booking booking : existingBookings) {
            if ((checkInDate.isBefore(booking.getCheckOutDate()) && checkOutDate.isAfter(booking.getCheckInDate())) ||
                    checkInDate.equals(booking.getCheckInDate()) || checkOutDate.equals(booking.getCheckOutDate())) {
                model.addAttribute("message", "Комната бронирован на эти даты");
                return "add-booking";
            }
        }

        // Полная стоимость бронирования
        double totalBookingCost = nights * room.getPricePerNight();

        // Минимальная предоплата (30%)
        double minimumPrepayment = totalBookingCost * 0.3;

        // Проверка введенной суммы
        if (totalAmount < minimumPrepayment) {
            model.addAttribute("message", "Цена должен быть больше 30% общий суммы");
            return "add-booking";
        }

        // Проверка карты
        Card card = cardRepository.findByCardNumberAndCvv(cardNumber, cvv).orElse(null);
        if (card == null) {
            model.addAttribute("message", "Карта не найден ! Введите правильно");
            return "add-booking";
        }

        if (card.getBalance() < totalAmount) {
            model.addAttribute("message", "Не хватает денег.");
            return "add-booking";
        }

        // Обновление баланса карты
        card.setBalance(card.getBalance() - totalAmount);
        cardRepository.save(card);

        // Создание бронирования
        Booking booking = new Booking();
        booking.setRoomNumber(roomNumber);
        booking.setFirstName(firstName);
        booking.setLastName(lastName);
        booking.setDateOfBirth(dateOfBirth);
        booking.setPassportNumber(passportNumber);
        booking.setCheckInDate(checkInDate);
        booking.setCheckOutDate(checkOutDate);
        booking.setTotalAmount(totalAmount);
        booking.setStatus(totalAmount >= totalBookingCost ? BookingStatus.PAID : BookingStatus.PENDING);

        bookingRepository.save(booking);

        // Сообщение об успешной оплате
        model.addAttribute("message", "Payment successfully processed! Booking status: " +
                (totalAmount >= totalBookingCost ? "PAID" : "PENDING_PAYMENT") + ".");
        model.addAttribute("booking", booking);
        return "add-booking";
    }



}
