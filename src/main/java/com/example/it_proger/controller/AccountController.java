package com.example.it_proger.controller;


import com.example.it_proger.models.AppUser;
import com.example.it_proger.models.RegisterDto;
import com.example.it_proger.models.Role;
import com.example.it_proger.repo.AppUserRepository;
import com.example.it_proger.servise.AppUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.List;

@Controller
public class AccountController {
    @Autowired
    private AppUserRepository repo;
    @Autowired
    private AppUserService userService;

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<AppUser> users = repo.findAll();
        model.addAttribute("users", users);
        return "users";
    }
    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") int userId) {
        userService.deleteUser(userId); // Call the service to delete the user
        return "redirect:/users"; // Redirect back to the users list
    }
    @GetMapping("/register")
    public String register(Model model) {
        RegisterDto registerDto = new RegisterDto();
        model.addAttribute(registerDto);
        model.addAttribute("success", false);
        return  "register";
    }

    @PostMapping("/register")
    public String register(
            Model model,
            @Valid @ModelAttribute RegisterDto registerDto,
            BindingResult result)
    {
        if(!registerDto.getPassword().equals(registerDto.getConfirmPassword())){
            result.addError(
                    new FieldError("registerDto", "ConfirmPassword",
                            "Password and Confirm Password do not match")
            );
        }

        AppUser appUser = repo.findByEmail(registerDto.getEmail());
        if (appUser != null){
            result.addError(
                    new FieldError("registerDto", "email",
                            "Email address is already used")
            );
        }
        if (result.hasErrors()){
            return "register";
        }

        try {
            var bCryptEncoder = new BCryptPasswordEncoder();


            AppUser newUser = new AppUser();
            newUser.setFirstName(registerDto.getFirstName());
            newUser.setLastName(registerDto.getLastName());
            newUser.setEmail(registerDto.getEmail());
            newUser.setPhone(registerDto.getPhone());
            newUser.setAddress(registerDto.getAddress());
            newUser.setRole(Role.USER);
            newUser.setCreatedAt(new Date());
            newUser.setPassword(bCryptEncoder.encode(registerDto.getPassword()));
            repo.save(newUser);

            model.addAttribute("registerDto", new RegisterDto());
            model.addAttribute("success", true);

        }catch (Exception ex){
            result.addError(
                    new FieldError("registerDto", "firstName",
                            ex.getMessage())
            );
        }

        return "register";

    }

    @GetMapping("/register_worker")
    public String registerWorker(Model model) {
        RegisterDto registerDto = new RegisterDto();
        model.addAttribute(registerDto);
        model.addAttribute("success", false);
        return  "register_worker";
    }

    @PostMapping("/register_worker")
    public String registerWorker(
            Model model,
            @Valid @ModelAttribute RegisterDto registerDto,
            BindingResult result)
    {
        if(!registerDto.getPassword().equals(registerDto.getConfirmPassword())){
            result.addError(
                    new FieldError("registerDto", "ConfirmPassword",
                            "Password and Confirm Password do not match")
            );
        }

        AppUser appUser = repo.findByEmail(registerDto.getEmail());
        if (appUser != null){
            result.addError(
                    new FieldError("registerDto", "email",
                            "Email address is already used")
            );
        }
        if (result.hasErrors()){
            return "register";
        }

        try {
            var bCryptEncoder = new BCryptPasswordEncoder();


            AppUser newUser = new AppUser();
            newUser.setFirstName(registerDto.getFirstName());
            newUser.setLastName(registerDto.getLastName());
            newUser.setEmail(registerDto.getEmail());
            newUser.setPhone(registerDto.getPhone());
            newUser.setAddress(registerDto.getAddress());
            newUser.setRole(Role.WORKER);
            newUser.setCreatedAt(new Date());
            newUser.setPassword(bCryptEncoder.encode(registerDto.getPassword()));
            repo.save(newUser);

            model.addAttribute("registerDto", new RegisterDto());
            model.addAttribute("success", true);

        }catch (Exception ex){
            result.addError(
                    new FieldError("registerDto", "firstName",
                            ex.getMessage())
            );
        }

        return "register_worker";

    }

    @GetMapping("/register_admin")
    public String registerAdmin(Model model) {
        RegisterDto registerDto = new RegisterDto();
        model.addAttribute(registerDto);
        model.addAttribute("success", false);
        return  "register_admin";
    }
    @PostMapping("/register_admin")
    public String registerAdmin(
            Model model,
            @Valid @ModelAttribute RegisterDto registerDto,
            BindingResult result)
    {
        if(!registerDto.getPassword().equals(registerDto.getConfirmPassword())){
            result.addError(
                    new FieldError("registerDto", "ConfirmPassword",
                            "Password and Confirm Password do not match")
            );
        }

        AppUser appUser = repo.findByEmail(registerDto.getEmail());
        if (appUser != null){
            result.addError(
                    new FieldError("registerDto", "email",
                            "Email address is already used")
            );
        }
        if (result.hasErrors()){
            return "register";
        }

        try {
            var bCryptEncoder = new BCryptPasswordEncoder();


            AppUser newUser = new AppUser();
            newUser.setFirstName(registerDto.getFirstName());
            newUser.setLastName(registerDto.getLastName());
            newUser.setEmail(registerDto.getEmail());
            newUser.setPhone(registerDto.getPhone());
            newUser.setAddress(registerDto.getAddress());
            newUser.setRole(Role.ADMIN);
            newUser.setCreatedAt(new Date());
            newUser.setPassword(bCryptEncoder.encode(registerDto.getPassword()));
            repo.save(newUser);

            model.addAttribute("registerDto", new RegisterDto());
            model.addAttribute("success", true);

        }catch (Exception ex){
            result.addError(
                    new FieldError("registerDto", "firstName",
                            ex.getMessage())
            );
        }

        return "register_admin";

    }



}

