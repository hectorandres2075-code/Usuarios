package com.plazoleta.application.service;

import com.plazoleta.application.port.output.PasswordEncoderPort;
import com.plazoleta.application.port.output.UserPersistencePort;
import com.plazoleta.domain.exception.UserAlreadyExistsException;
import com.plazoleta.domain.model.Role;
import com.plazoleta.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateClientServiceTest {

    @Mock private UserPersistencePort userPersistencePort;
    @Mock private PasswordEncoderPort passwordEncoderPort;

    private CreateClientService service;
    private User client;

    @BeforeEach
    void setUp() {
        service = new CreateClientService(userPersistencePort, passwordEncoderPort);
        client = new User();
        client.setNombre("Carla");
        client.setApellido("Ruiz");
        client.setDocumento("777888");
        client.setCelular("+573001234567");
        client.setCorreo("carla@correo.com");
        client.setClave("clave123");
        client.setRol(Role.ADMINISTRADOR); // intento de colar otro rol: debe ignorarse
    }

    @Test
    void createClient_datosValidos_asignaRolClienteYEncriptaClave() {
        when(passwordEncoderPort.encode("clave123")).thenReturn("hash");
        when(userPersistencePort.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User result = service.createClient(client);

        assertEquals(Role.CLIENTE, result.getRol());
        assertEquals("hash", result.getClave());
    }

    @Test
    void createClient_correoRepetido_lanzaExcepcion() {
        when(userPersistencePort.existsByCorreo("carla@correo.com")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> service.createClient(client));
        verify(userPersistencePort, never()).save(any());
    }

    @Test
    void createClient_documentoRepetido_lanzaExcepcion() {
        when(userPersistencePort.existsByDocumento("777888")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> service.createClient(client));
        verify(userPersistencePort, never()).save(any());
    }
}
