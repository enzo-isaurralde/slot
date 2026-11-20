package com.casino.slot.api;

import com.casino.slot.user.RegisterRequest;
import com.casino.slot.user.RateLimiter;
import com.casino.slot.user.User;
import com.casino.slot.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final RateLimiter rateLimiter;

    @PostMapping("/register")
    public User registrar(@RequestBody RegisterRequest request) {
        return userService.registrar(request);
    }

    @PostMapping("/login")
    public String login(@RequestBody RegisterRequest request, HttpServletRequest httpRequest) {
        String ip = httpRequest.getRemoteAddr();
        if (!rateLimiter.tryConsume(ip)) {
            throw new RuntimeException("Demasiados intentos. Intente más tarde.");
        }

        if (userService.autenticar(request.getUsername(), request.getPassword())) {
            rateLimiter.reset(ip); // Si el login es exitoso, reseteamos el contador
            return "Login exitoso";
        }
        throw new RuntimeException("Credenciales inválidas");
    }
}