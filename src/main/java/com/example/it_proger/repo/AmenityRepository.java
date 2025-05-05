package com.example.it_proger.repo;
import com.example.it_proger.models.Amenity;
import com.example.it_proger.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AmenityRepository extends JpaRepository<Amenity, Long> {
    public List<Amenity> findByRoom(Room room);
}
