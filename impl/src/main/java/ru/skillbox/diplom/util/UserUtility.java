package ru.skillbox.diplom.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.exception.UserIsNotAuthorized;
import ru.skillbox.diplom.model.User;
import ru.skillbox.diplom.repository.UserRepository;

import java.util.Optional;

@Service
public class UserUtility {

    private static UserRepository userRepository;

    public UserUtility(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public static Integer getIdCurrentUser() {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if (authentication  instanceof AnonymousAuthenticationToken) {

            throw new UserIsNotAuthorized("anonymousUser");

        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Optional<User> userOptional = userRepository.findByEmail(userDetails.getUsername());
        User user = userOptional.get();

        return user.getId();
    }

    public static User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            var securityUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            return userRepository.findByEmail(securityUser.getUsername()).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

}
