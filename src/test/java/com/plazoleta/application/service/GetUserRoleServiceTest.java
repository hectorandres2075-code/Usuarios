package com.plazoleta.application.service;

import com.plazoleta.application.port.output.UserPersistencePort;
import com.plazoleta.domain.exception.UserNotFoundException;
import com.plazoleta.domain.model.Role;
import com.plazoleta.domain.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserRoleServiceTest {

    @Mock private UserPersistencePort userPersistencePort;
    @InjectMocks private GetUserRoleService service;

    @Test
    void getRole_usuarioExistente_devuelveSuRol() {
        User user = new User();
        user.setRol(Role.PROPIETARIO);
        when(userPersistencePort.findById(5L)).thenReturn(Optional.of(user));

        assertEquals(Role.PROPIETARIO, service.getRole(5L));
    }

    @Test
    void getRole_usuarioInexistente_lanzaExcepcion() {
        when(userPersistencePort.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.getRole(99L));
    }
}
