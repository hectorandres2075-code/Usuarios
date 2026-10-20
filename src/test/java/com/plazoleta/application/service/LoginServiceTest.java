package com.plazoleta.application.service;

import com.plazoleta.application.port.output.PasswordEncoderPort;
import com.plazoleta.application.port.output.TokenPort;
import com.plazoleta.application.port.output.UserPersistencePort;
import com.plazoleta.domain.exception.InvalidCredentialsException;
import com.plazoleta.domain.model.Role;
import com.plazoleta.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock private UserPersistencePort userPersistencePort;
    @Mock private PasswordEncoderPort passwordEncoderPort;
    @Mock private TokenPort tokenPort;

    private LoginService loginService;
    private User user;

    @BeforeEach
    void setUp() {
        loginService = new LoginService(userPersistencePort, passwordEncoderPort, tokenPort);
        user = new User();
        user.setCorreo("admin@plazoleta.com");
        user.setClave("hash");
        user.setRol(Role.ADMINISTRADOR);
    }

    @Test
    void login_conCredencialesCorrectas_devuelveToken() {
        when(userPersistencePort.findByCorreo("admin@plazoleta.com")).thenReturn(Optional.of(user));
        when(passwordEncoderPort.matches("Admin123*", "hash")).thenReturn(true);
        when(tokenPort.generate(user)).thenReturn("token-jwt");

        assertEquals("token-jwt", loginService.login("admin@plazoleta.com", "Admin123*"));
    }

    @Test
    void login_conClaveIncorrecta_lanzaExcepcion() {
        when(userPersistencePort.findByCorreo("admin@plazoleta.com")).thenReturn(Optional.of(user));
        when(passwordEncoderPort.matches("mala", "hash")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class,
                () -> loginService.login("admin@plazoleta.com", "mala"));
        verify(tokenPort, never()).generate(any());
    }

    @Test
    void login_conCorreoInexistente_lanzaExcepcion() {
        when(userPersistencePort.findByCorreo("no@existe.com")).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class,
                () -> loginService.login("no@existe.com", "x"));
    }
}
