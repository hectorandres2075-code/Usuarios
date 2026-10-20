package com.plazoleta.application.port.input;

import com.plazoleta.domain.model.User;

public interface CreateEmployeeUseCase {

    /** Crea un EMPLEADO ligado al restaurante indicado en employee.idRestaurante. */
    User createEmployee(User employee);
}
