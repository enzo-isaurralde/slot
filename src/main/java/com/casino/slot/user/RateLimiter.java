package com.casino.slot.user;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementación simple de un Rate Limiter basado en el algoritmo Token Bucket.
 * Limita la cantidad de intentos por clave (ej: IP o username) en una ventana de tiempo.
 */
@Component
public class RateLimiter {

    private static final int MAX_ATTEMPTS = 5;
    private static final long WINDOW_MS = 15 * 60 * 1000; // 15 minutos

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public boolean tryConsume(String key) {
        Bucket bucket = buckets.computeIfAbsent(key, k -> new Bucket());
        return bucket.tryConsume();
    }

    public void reset(String key) {
        buckets.remove(key);
    }

    private static class Bucket {
        private int tokens;
        private long lastRefill;

        public Bucket() {
            this.tokens = MAX_ATTEMPTS;
            this.lastRefill = System.currentTimeMillis();
        }

        public synchronized boolean tryConsume() {
            refill();
            if (tokens > 0) {
                tokens--;
                return true;
            }
            return false;
        }

        private void refill() {
            long now = System.currentTimeMillis();
            long elapsed = now - lastRefill;
            if (elapsed > WINDOW_MS) {
                tokens = MAX_ATTEMPTS;
                lastRefill = now;
            }
        }
    }
}