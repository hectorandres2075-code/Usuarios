package com.plazoleta.application.service;

import com.plazoleta.application.port.output.UserPersistencePort;
import com.plazoleta.domain.exception.UserNotFoundException;
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
class GetUserPhoneServiceTest {

    @Mock private UserPersistencePort userPersistencePort;
    @InjectMocks private GetUserPhoneService service;

    @Test
    void getPhone_usuarioExistente_devuelveSuCelular() {
        User user = new User();
        user.setCelular("+573001234567");
        when(userPersistencePort.findById(5L)).thenReturn(Optional.of(user));

        assertEquals("+573001234567", service.getPhone(5L));
    }

    @Test
    void getPhone_usuarioInexistente_lanzaExcepcion() {
        when(userPersistencePort.findById(9L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.getPhone(9L));
    }
}
