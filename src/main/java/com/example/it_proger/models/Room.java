package com.example.it_proger.models;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roomId;
    private String roomNumber;
    private String roomType;
    private int bedCount;
    private double pricePerNight;
    private boolean availability;
    private String description;
    private boolean available;

    // Добавление поля для изображения

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Amenity> amenities;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "room")
    private List<Image> images = new ArrayList<>();
    private Long previewImageId;
    private LocalDateTime dateOfCreated;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @PrePersist
    private void init(){
        dateOfCreated = LocalDateTime.now();
    }

    public void addImageRoom(Image image){
        image.setRoom(this);
        images.add(image);
    }



    public void setAmenities(List<Amenity> amenities) {
        this.amenities = amenities;
        for (Amenity amenity : amenities) {
            amenity.setRoom(this);
        }
    }

    public boolean isAvailable(LocalDate checkInDate, LocalDate checkOutDate) {
        // Логика проверки доступности
        return available; // пример
    }

}
