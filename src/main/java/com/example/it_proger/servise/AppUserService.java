package com.example.it_proger.servise;


import com.example.it_proger.models.AppUser;
import com.example.it_proger.repo.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserService implements UserDetailsService {

    @Autowired
    private AppUserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { // Исправлено название метода
        AppUser appUser = repo.findByEmail(email);

        if (appUser != null) {
            var springUser = User.withUsername(appUser.getEmail())
                    .password(appUser.getPassword())
                    .roles(appUser.getRole().name())
                    .build();
            return springUser;
        }
        throw new UsernameNotFoundException("User not found with email: " + email); // Добавляем исключение, если пользователь не найден
    }
    public void deleteUser(int id) {
        repo.deleteById(id);
    }



}
