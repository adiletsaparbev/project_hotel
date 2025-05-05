package com.example.it_proger.servise;

import com.example.it_proger.models.Booking;
import com.example.it_proger.repo.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking addBooking(Booking booking) {
        // Валидация или другие логики можно добавить здесь
        return bookingRepository.save(booking);
    }


    // Метод для получения всех бронирований
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }



    public List<Booking> searchByRoomNumber(String roomNumber) {
        return bookingRepository.findByRoomNumber(roomNumber);
    }

    public List<Booking> searchByDate(LocalDate date) {
        return bookingRepository.findByDate(date);
    }
}
