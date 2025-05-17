// LocationController.java
package com.example.it_proger.controller;

import com.example.it_proger.models.*;
import com.example.it_proger.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LocationController {

    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;


// Просмотры и показ
    @GetMapping("/locations")
    public String getCountries(Model model) {
        model.addAttribute("countries", countryRepository.findAll());
        return "admin/locations";
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

    @GetMapping("locations/rooms/{hotelId}")
    public String getRooms(@PathVariable Integer hotelId, Model model) {
        List<Room> rooms = roomRepository.findByHotelId(hotelId);
        System.out.println("Rooms for hotel ID " + hotelId + ": " + rooms.size()); // лог
        model.addAttribute("rooms", rooms);
        return "fragments/rooms :: roomTable";
    }

// Показать форму добавления страны
    @GetMapping("/countries/add")
    public String showAddForm(Model model) {
        model.addAttribute("country", new Country());
        return "admin/addCountryForm"; // addCountryForm.html
    }

    // Обработка добавления страны
    @PostMapping("/countries/add")
    public String addCountry(@ModelAttribute Country country) {
        countryRepository.save(country);
        return "redirect:/locations"; // или куда вы хотите вернуться
    }

    @PostMapping("/cities/add")
    public String addCity(@RequestParam String cityName, @RequestParam int countryId, Model model) {
        Optional<Country> countryOpt = countryRepository.findById(countryId);
        if (countryOpt.isPresent()) {
            City city = new City();
            city.setName(cityName);
            city.setCountry(countryOpt.get());
            cityRepository.save(city);
            model.addAttribute("successMessage", "City added successfully.");
        } else {
            model.addAttribute("errorMessage", "Country with ID " + countryId + " not found.");
        }
        return "redirect:/locations";
    }


    @PostMapping("/hotels/add")
    public String addHotel(@RequestParam String hotelNumber,
                           @RequestParam String address,
                           @RequestParam String password,
                           @RequestParam String description,
                           @RequestParam String cityName, Model model) {
        Optional<City> cityOpt = cityRepository.findByNameIgnoreCase(cityName.trim());
        if (cityOpt.isPresent()) {
            Hotel hotel = new Hotel();
            hotel.setNumber(hotelNumber);
            hotel.setAddress(address);
            hotel.setPassword(password);
            hotel.setDescription(description);
            hotel.setCity(cityOpt.get());
            hotelRepository.save(hotel);
            model.addAttribute("successMessage", "Hotel added successfully.");
        } else {
            model.addAttribute("errorMessage", "City '" + cityName + "' not found.");
        }
        return "redirect:/locations";
    }

    @GetMapping("/countries/delete/{id}")
    public String deleteCountries(@PathVariable Integer id) {
        countryRepository.deleteById(id);
        return "redirect:/locations";
    }
    @GetMapping("/cities/delete/{id}")
    public String deleteCity(@PathVariable Integer id) {
        cityRepository.deleteById(id);
        return "redirect:/locations";
    }

    @GetMapping("/hotels/delete/{id}")
    public String deleteHotel(@PathVariable Integer id) {
        hotelRepository.deleteById(id);
        return "redirect:/locations";
    }
    @GetMapping("/countries/edit/{id}")
    public String editCountryForm(@PathVariable Integer id, Model model) {
        Country country = countryRepository.findById(id).orElse(null);
        if (country == null) {
            return "redirect:/locations"; // можно добавить сообщение об ошибке
        }
        model.addAttribute("country", country);
        return "admin/editCountryForm"; // создай этот шаблон
    }


    @PostMapping("/countries/edit/{id}")
    public String updateCountry(@PathVariable Integer id, @ModelAttribute Country updatedCountry) {
        Country country = countryRepository.findById(id).orElse(null);
        if (country != null) {
            country.setName(updatedCountry.getName());
            countryRepository.save(country);
        }
        return "redirect:/locations";
    }

    @GetMapping("/cities/edit/{id}")
    public String editCityForm(@PathVariable Integer id, Model model) {
        Optional<City> city = cityRepository.findById(id);
        city.ifPresent(value -> model.addAttribute("city", value));
        model.addAttribute("countries", countryRepository.findAll());
        return "admin/editCityForm"; // создать шаблон
    }

    @PostMapping("/cities/update/{id}")
    public String updateCity(@PathVariable Integer id, @ModelAttribute City updatedCity) {
        City city = cityRepository.findById(id).orElse(null);
        if (city != null){
            city.setName(updatedCity.getName());
            cityRepository.save(city);
        }
        return "redirect:/locations";
    }

    @GetMapping("/edit/{id}")
    public String editHotelForm(@PathVariable("id") int id, Model model) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid hotel ID"));
        List<City> cities = cityRepository.findAll();
        model.addAttribute("hotel", hotel);
        model.addAttribute("cities", cities);
        return "admin/edit-hotel";
    }

    @PostMapping("/update/{id}")
    public String updateHotel(@PathVariable("id") int id, @ModelAttribute Hotel updatedHotel) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid hotel ID"));
        hotel.setNumber(updatedHotel.getNumber());
        hotel.setAddress(updatedHotel.getAddress());
        hotel.setPassword(updatedHotel.getPassword());
        hotel.setHotelRank(updatedHotel.getHotelRank());
        hotel.setDescription(updatedHotel.getDescription());
        hotel.setCity(updatedHotel.getCity());
        hotelRepository.save(hotel);
        return "redirect:/locations";
    }
}
