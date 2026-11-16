package com.casino.slot.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void debeRegistrarYAutenticarUnUsuario() {
        // 1. Registramos
        RegisterRequest request = new RegisterRequest();
        request.setUsername("enzo");
        request.setPassword("123456");

        User user = userService.registrar(request);
        assertNotNull(user.getId());
        assertEquals("enzo", user.getUsername());
        assertEquals("PLAYER", user.getRole());

        // 2. Autenticamos con la contraseña correcta
        assertTrue(userService.autenticar("enzo", "123456"));

        // 3. Autenticamos con la contraseña incorrecta
        assertFalse(userService.autenticar("enzo", "incorrecta"));
    }
}