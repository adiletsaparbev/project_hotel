// CityRepository.java
package com.example.it_proger.repo;
import com.example.it_proger.models.City;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Integer> {
    List<City> findByCountryId(Integer countryId);
    Optional<City> findByNameIgnoreCase(String name);
}
