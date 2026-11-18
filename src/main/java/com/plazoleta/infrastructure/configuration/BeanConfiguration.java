package com.plazoleta.infrastructure.configuration;

import com.plazoleta.application.port.input.CreateClientUseCase;
import com.plazoleta.application.port.input.CreateEmployeeUseCase;
import com.plazoleta.application.port.input.CreateOwnerUseCase;
import com.plazoleta.application.port.input.GetUserRoleUseCase;
import com.plazoleta.application.port.input.LoginUseCase;
import com.plazoleta.application.port.output.AuthenticatedUserPort;
import com.plazoleta.application.port.output.PasswordEncoderPort;
import com.plazoleta.application.port.output.RestaurantServicePort;
import com.plazoleta.application.port.output.TokenPort;
import com.plazoleta.application.port.output.UserPersistencePort;
import com.plazoleta.application.service.CreateClientService;
import com.plazoleta.application.service.CreateEmployeeService;
import com.plazoleta.application.service.CreateOwnerService;
import com.plazoleta.application.service.GetUserRoleService;
import com.plazoleta.application.service.LoginService;
import com.plazoleta.infrastructure.output.client.RestaurantRestClientAdapter;
import com.plazoleta.infrastructure.output.persistence.adapter.UserPersistenceAdapter;
import com.plazoleta.infrastructure.output.persistence.repository.UserRepository;
import com.plazoleta.infrastructure.output.security.AuthenticatedUserAdapter;
import com.plazoleta.infrastructure.output.security.BCryptAdapter;
import com.plazoleta.infrastructure.output.security.JwtAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.plazoleta.application.port.input.GetUserPhoneUseCase;
import com.plazoleta.application.service.GetUserPhoneService;

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

    @Bean
    public CreateOwnerUseCase createOwnerUseCase(UserPersistencePort userPersistencePort,
                                                 PasswordEncoderPort passwordEncoderPort) {
        return new CreateOwnerService(userPersistencePort, passwordEncoderPort);
    }

    @Bean
    public GetUserRoleUseCase getUserRoleUseCase(UserPersistencePort userPersistencePort) {
        return new GetUserRoleService(userPersistencePort);
    }

    @Bean
    public AuthenticatedUserPort authenticatedUserPort() {
        return new AuthenticatedUserAdapter();
    }

    @Bean
    public RestaurantServicePort restaurantServicePort(
            @Value("${plazoleta.base-url:http://localhost:8082}") String baseUrl) {
        return new RestaurantRestClientAdapter(baseUrl);
    }

    @Bean
    public CreateEmployeeUseCase createEmployeeUseCase(UserPersistencePort userPersistencePort,
                                                       PasswordEncoderPort passwordEncoderPort,
                                                       RestaurantServicePort restaurantServicePort,
                                                       AuthenticatedUserPort authenticatedUserPort) {
        return new CreateEmployeeService(userPersistencePort, passwordEncoderPort,
                restaurantServicePort, authenticatedUserPort);
    }

    @Bean
    public CreateClientUseCase createClientUseCase(UserPersistencePort userPersistencePort,
                                                   PasswordEncoderPort passwordEncoderPort) {
        return new CreateClientService(userPersistencePort, passwordEncoderPort);
    }

    @Bean
    public GetUserPhoneUseCase getUserPhoneUseCase(UserPersistencePort userPersistencePort) {
        return new GetUserPhoneService(userPersistencePort);
    }
}
