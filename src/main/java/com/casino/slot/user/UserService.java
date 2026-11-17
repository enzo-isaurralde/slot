package com.casino.slot.user;

import com.casino.slot.wallet.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final WalletService walletService; // <-- NUEVO

    public User registrar(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("PLAYER");

        User savedUser = userRepository.save(user);

        // ¡ACÁ ESTÁ LA MAGIA! Creamos la billetera con saldo inicial
        walletService.crearBilletera(savedUser.getId());

        return savedUser;
    }

    public boolean autenticar(String username, String password) {
        return userRepository.findByUsername(username)
                .map(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(false);
    }
}