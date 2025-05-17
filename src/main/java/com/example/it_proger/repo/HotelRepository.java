// HotelRepository.java
package com.example.it_proger.repo;
import com.example.it_proger.models.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Integer> {
    List<Hotel> findByCityId(Integer cityId);
}
