package com.plazoleta.infrastructure.configuration;

import com.plazoleta.application.port.output.PasswordEncoderPort;
import com.plazoleta.application.port.output.UserPersistencePort;
import com.plazoleta.domain.model.Role;
import com.plazoleta.domain.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

/** Crea el administrador inicial al arrancar (no hay endpoint público para crearlo). */
@Configuration
public class AdminInitializer {

    private static final String ADMIN_CORREO = "admin@plazoleta.com";
    private static final String ADMIN_CLAVE = "Admin123*";

    @Bean
    public CommandLineRunner createAdmin(UserPersistencePort users, PasswordEncoderPort encoder) {
        return args -> {
            if (!users.existsByCorreo(ADMIN_CORREO)) {
                users.save(new User(null, "Admin", "Plazoleta", "1000000000", "+573000000000",
                        LocalDate.of(1990, 1, 1), ADMIN_CORREO, encoder.encode(ADMIN_CLAVE), Role.ADMINISTRADOR));
            }
        };
    }
}
