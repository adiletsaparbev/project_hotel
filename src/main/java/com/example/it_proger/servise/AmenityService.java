package com.example.it_proger.servise;


import com.example.it_proger.models.Amenity;
import com.example.it_proger.repo.AmenityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AmenityService {

    @Autowired
    private AmenityRepository amenityRepository;

    public List<Amenity> getAllAmenities() {
        return amenityRepository.findAll();
    }

    public List<String> getUniqueAmenities() {
        // Получаем все удобства, приводим их к нижнему регистру и фильтруем уникальные
        List<Amenity> amenities = amenityRepository.findAll();
        return amenities.stream()
                .map(amenity -> amenity.getName().toLowerCase())
                .distinct()
                .collect(Collectors.toList());
    }
}
