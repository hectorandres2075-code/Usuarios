package com.plazoleta.application.service;

import com.plazoleta.application.port.output.PasswordEncoderPort;
import com.plazoleta.application.port.output.UserPersistencePort;
import com.plazoleta.domain.exception.InvalidUserDataException;
import com.plazoleta.domain.exception.UserAlreadyExistsException;
import com.plazoleta.domain.model.Role;
import com.plazoleta.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateOwnerServiceTest {

    @Mock private UserPersistencePort userPersistencePort;
    @Mock private PasswordEncoderPort passwordEncoderPort;

    private CreateOwnerService service;
    private User owner;

    @BeforeEach
    void setUp() {
        service = new CreateOwnerService(userPersistencePort, passwordEncoderPort);
        owner = new User(null, "Ana", "Pérez", "123456", "+573001112233",
                LocalDate.now().minusYears(30), "ana@correo.com", "clave123", null);
    }

    @Test
    void createOwner_datosValidos_asignaRolPropietarioYEncriptaClave() {
        when(passwordEncoderPort.encode("clave123")).thenReturn("hash");
        when(userPersistencePort.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User result = service.createOwner(owner);

        assertEquals(Role.PROPIETARIO, result.getRol());
        assertEquals("hash", result.getClave());
    }

    @Test
    void createOwner_menorDeEdad_lanzaExcepcion() {
        owner.setFechaNacimiento(LocalDate.now().minusYears(17));

        assertThrows(InvalidUserDataException.class, () -> service.createOwner(owner));
        verify(userPersistencePort, never()).save(any());
    }

    @Test
    void createOwner_sinFechaNacimiento_lanzaExcepcion() {
        owner.setFechaNacimiento(null);

        assertThrows(InvalidUserDataException.class, () -> service.createOwner(owner));
    }

    @Test
    void createOwner_correoRepetido_lanzaExcepcion() {
        when(userPersistencePort.existsByCorreo("ana@correo.com")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> service.createOwner(owner));
        verify(userPersistencePort, never()).save(any());
    }

    @Test
    void createOwner_documentoRepetido_lanzaExcepcion() {
        when(userPersistencePort.existsByDocumento("123456")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> service.createOwner(owner));
        verify(userPersistencePort, never()).save(any());
    }
}
