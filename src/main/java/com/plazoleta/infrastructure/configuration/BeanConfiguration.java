package com.plazoleta.infrastructure.configuration;

import com.plazoleta.application.port.input.LoginUseCase;
import com.plazoleta.application.port.output.PasswordEncoderPort;
import com.plazoleta.application.port.output.TokenPort;
import com.plazoleta.application.port.output.UserPersistencePort;
import com.plazoleta.application.service.LoginService;
import com.plazoleta.infrastructure.output.persistence.adapter.UserPersistenceAdapter;
import com.plazoleta.infrastructure.output.persistence.repository.UserRepository;
import com.plazoleta.infrastructure.output.security.BCryptAdapter;
import com.plazoleta.infrastructure.output.security.JwtAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Aquí se "cablea" la arquitectura: el dominio/aplicación no conocen Spring, solo esta clase. */
@Configuration
public class BeanConfiguration {

    @Bean
    public UserPersistencePort userPersistencePort(UserRepository userRepository) {
        return new UserPersistenceAdapter(userRepository);
    }

    @Bean
    public PasswordEncoderPort passwordEncoderPort() {
        return new BCryptAdapter();
    }

    @Bean
    public TokenPort tokenPort(@Value("${jwt.secret}") String secret,
                               @Value("${jwt.expiration-ms}") long expirationMs) {
        return new JwtAdapter(secret, expirationMs);
    }

    @Bean
    public LoginUseCase loginUseCase(UserPersistencePort userPersistencePort,
                                     PasswordEncoderPort passwordEncoderPort,
                                     TokenPort tokenPort) {
        return new LoginService(userPersistencePort, passwordEncoderPort, tokenPort);
    }
}
