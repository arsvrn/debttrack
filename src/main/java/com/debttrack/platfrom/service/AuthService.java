// File: com.debttrack.platform.service.AuthService.java
package com.debttrack.platfrom.service;

import com.debttrack.platfrom.model.ConfirmationToken;
import com.debttrack.platfrom.model.RegisterRequest;
import com.debttrack.platfrom.model.User;
import com.debttrack.platfrom.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       ConfirmationTokenService confirmationTokenService, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.confirmationTokenService = confirmationTokenService;
        this.emailService = emailService;
    }

    public void register(RegisterRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setEnabled(false);

        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        String confirmationLink = "http://localhost:8080/api/auth/confirm?token=" + token;
        System.out.println(confirmationLink);
        emailService.sendEmail(user.getEmail(), buildEmail(user.getName(), confirmationLink));
    }

    public void confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
                .orElseThrow(() -> new IllegalStateException("Token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Token already confirmed");
        }

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expired");
        }

        confirmationTokenService.setConfirmedAt(token);

        User user = confirmationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
    }

    private String buildEmail(String name, String link) {
        return "<p>Hi " + name + ",</p>"
                + "<p>Thank you for registering. Please click the link below to activate your account:</p>"
                + "<a href=\"" + link + "\">Activate Now</a>"
                + "<p>This link will expire in 15 minutes.</p>";
    }
}