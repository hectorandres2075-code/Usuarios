package com.plazoleta.application.service;

import com.plazoleta.application.port.output.AuthenticatedUserPort;
import com.plazoleta.application.port.output.PasswordEncoderPort;
import com.plazoleta.application.port.output.RestaurantServicePort;
import com.plazoleta.application.port.output.UserPersistencePort;
import com.plazoleta.domain.exception.InvalidUserDataException;
import com.plazoleta.domain.exception.NotRestaurantOwnerException;
import com.plazoleta.domain.exception.UserAlreadyExistsException;
import com.plazoleta.domain.model.Role;
import com.plazoleta.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateEmployeeServiceTest {

    @Mock private UserPersistencePort userPersistencePort;
    @Mock private PasswordEncoderPort passwordEncoderPort;
    @Mock private RestaurantServicePort restaurantServicePort;
    @Mock private AuthenticatedUserPort authenticatedUserPort;

    private CreateEmployeeService service;
    private User employee;

    @BeforeEach
    void setUp() {
        service = new CreateEmployeeService(userPersistencePort, passwordEncoderPort,
                restaurantServicePort, authenticatedUserPort);
        employee = new User(null, "Luis", "Gómez", "555666", "+573009998877",
                LocalDate.now().minusYears(25), "luis@correo.com", "clave123", null);
        employee.setIdRestaurante(1L);
    }

    @Test
    void createEmployee_propietarioDelRestaurante_creaEmpleadoConRolYClaveEncriptada() {
        when(restaurantServicePort.findOwnerIdByRestaurantId(1L)).thenReturn(Optional.of(7L));
        when(authenticatedUserPort.currentUserId()).thenReturn(7L);
        when(passwordEncoderPort.encode("clave123")).thenReturn("hash");
        when(userPersistencePort.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User result = service.createEmployee(employee);

        assertEquals(Role.EMPLEADO, result.getRol());
        assertEquals("hash", result.getClave());
        assertEquals(1L, result.getIdRestaurante());
    }

    @Test
    void createEmployee_menorDeEdad_lanzaExcepcion() {
        employee.setFechaNacimiento(LocalDate.now().minusYears(16));

        assertThrows(InvalidUserDataException.class, () -> service.createEmployee(employee));
        verify(userPersistencePort, never()).save(any());
    }

    @Test
    void createEmployee_restauranteInexistente_lanzaExcepcion() {
        when(restaurantServicePort.findOwnerIdByRestaurantId(1L)).thenReturn(Optional.empty());

        assertThrows(InvalidUserDataException.class, () -> service.createEmployee(employee));
        verify(userPersistencePort, never()).save(any());
    }

    @Test
    void createEmployee_propietarioDeOtroRestaurante_lanzaExcepcion() {
        when(restaurantServicePort.findOwnerIdByRestaurantId(1L)).thenReturn(Optional.of(7L));
        when(authenticatedUserPort.currentUserId()).thenReturn(99L);

        assertThrows(NotRestaurantOwnerException.class, () -> service.createEmployee(employee));
        verify(userPersistencePort, never()).save(any());
    }

    @Test
    void createEmployee_correoRepetido_lanzaExcepcion() {
        when(restaurantServicePort.findOwnerIdByRestaurantId(1L)).thenReturn(Optional.of(7L));
        when(authenticatedUserPort.currentUserId()).thenReturn(7L);
        when(userPersistencePort.existsByCorreo("luis@correo.com")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> service.createEmployee(employee));
        verify(userPersistencePort, never()).save(any());
    }
}
