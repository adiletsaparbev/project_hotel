package com.example.it_proger.repo;

import com.example.it_proger.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    public AppUser findByEmail(String email);
    public AppUser findById(int id);

}
