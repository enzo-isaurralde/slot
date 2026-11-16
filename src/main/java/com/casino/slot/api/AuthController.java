package com.casino.slot.api;

import com.casino.slot.user.RegisterRequest;
import com.casino.slot.user.User;
import com.casino.slot.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public User registrar(@RequestBody RegisterRequest request) {
        return userService.registrar(request);
    }

    @PostMapping("/login")
    public String login(@RequestBody RegisterRequest request) {
        if (userService.autenticar(request.getUsername(), request.getPassword())) {
            return "Login exitoso";
        }
        throw new RuntimeException("Credenciales inválidas");
    }
}