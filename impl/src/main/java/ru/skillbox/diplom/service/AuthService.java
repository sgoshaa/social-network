package ru.skillbox.diplom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.model.User;
import ru.skillbox.diplom.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static String getCurrentEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String mail;
        if (principal instanceof UserDetails) {
            mail = ((UserDetails) principal).getUsername();
        } else {
            mail = principal.toString();
        }
        return mail;
    }

    public User getCurrentUser() {
        String email = getCurrentEmail();
        return userRepository.findByEmail(email).get();
    }
}
