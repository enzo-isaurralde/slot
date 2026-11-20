package com.casino.slot.user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RateLimiterTest {

    @Test
    void debeBloquearDespuesDeMaximosIntentos() {
        RateLimiter rateLimiter = new RateLimiter();
        String key = "test-user";

        // Los primeros 5 intentos deben pasar
        for (int i = 0; i < 5; i++) {
            assertTrue(rateLimiter.tryConsume(key), "Intento " + (i + 1) + " debería pasar");
        }

        // El sexto intento debe fallar
        assertFalse(rateLimiter.tryConsume(key), "El sexto intento debería ser bloqueado");

        // Si reseteamos la clave, debería permitir de nuevo
        rateLimiter.reset(key);
        assertTrue(rateLimiter.tryConsume(key), "Después del reset debería permitir");
    }
}