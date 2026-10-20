package com.plazoleta.infrastructure.input.rest;

import com.plazoleta.application.port.input.CreateClientUseCase;
import com.plazoleta.application.port.input.CreateEmployeeUseCase;
import com.plazoleta.application.port.input.CreateOwnerUseCase;
import com.plazoleta.application.port.input.GetUserRoleUseCase;
import com.plazoleta.domain.model.User;
import com.plazoleta.infrastructure.input.rest.dto.ClientRequest;
import com.plazoleta.infrastructure.input.rest.dto.EmployeeRequest;
import com.plazoleta.infrastructure.input.rest.dto.OwnerRequest;
import com.plazoleta.infrastructure.input.rest.dto.RoleResponse;
import com.plazoleta.infrastructure.input.rest.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Usuarios")
public class UserController {

    private final CreateOwnerUseCase createOwnerUseCase;
    private final GetUserRoleUseCase getUserRoleUseCase;
    private final CreateEmployeeUseCase createEmployeeUseCase;
    private final CreateClientUseCase createClientUseCase;

    public UserController(CreateOwnerUseCase createOwnerUseCase, GetUserRoleUseCase getUserRoleUseCase,
                          CreateEmployeeUseCase createEmployeeUseCase, CreateClientUseCase createClientUseCase) {
        this.createOwnerUseCase = createOwnerUseCase;
        this.getUserRoleUseCase = getUserRoleUseCase;
        this.createEmployeeUseCase = createEmployeeUseCase;
        this.createClientUseCase = createClientUseCase;
    }

    @Operation(summary = "Registrar cliente", description = "Público: cualquier persona puede registrarse como CLIENTE")
    @PostMapping("/clients")
    public ResponseEntity<UserResponse> createClient(@Valid @RequestBody ClientRequest request) {
        User client = new User();
        client.setNombre(request.getNombre());
        client.setApellido(request.getApellido());
        client.setDocumento(request.getDocumento());
        client.setCelular(request.getCelular());
        client.setCorreo(request.getCorreo());
        client.setClave(request.getClave());

        User saved = createClientUseCase.createClient(client);
        UserResponse body = new UserResponse(saved.getId(), saved.getNombre(), saved.getApellido(),
                saved.getCorreo(), saved.getRol().name());
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @Operation(summary = "Crear empleado", description = "Solo el PROPIETARIO, y únicamente para su propio restaurante")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/employees")
    public ResponseEntity<UserResponse> createEmployee(@Valid @RequestBody EmployeeRequest request) {
        User employee = new User();
        employee.setNombre(request.getNombre());
        employee.setApellido(request.getApellido());
        employee.setDocumento(request.getDocumento());
        employee.setCelular(request.getCelular());
        employee.setFechaNacimiento(request.getFechaNacimiento());
        employee.setCorreo(request.getCorreo());
        employee.setClave(request.getClave());
        employee.setIdRestaurante(request.getIdRestaurante());

        User saved = createEmployeeUseCase.createEmployee(employee);
        UserResponse body = new UserResponse(saved.getId(), saved.getNombre(), saved.getApellido(),
                saved.getCorreo(), saved.getRol().name());
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @Operation(summary = "Consultar el rol de un usuario", description = "Lo usa el microservicio de Plazoleta")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}/role")
    public RoleResponse getRole(@PathVariable Long id) {
        return new RoleResponse(getUserRoleUseCase.getRole(id).name());
    }

    @Operation(summary = "Crear propietario", description = "Solo el ADMINISTRADOR puede crear cuentas de propietario")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/owners")
    public ResponseEntity<UserResponse> createOwner(@Valid @RequestBody OwnerRequest request) {
        User owner = new User();
        owner.setNombre(request.getNombre());
        owner.setApellido(request.getApellido());
        owner.setDocumento(request.getDocumento());
        owner.setCelular(request.getCelular());
        owner.setFechaNacimiento(request.getFechaNacimiento());
        owner.setCorreo(request.getCorreo());
        owner.setClave(request.getClave());

        User saved = createOwnerUseCase.createOwner(owner);
        UserResponse body = new UserResponse(saved.getId(), saved.getNombre(), saved.getApellido(),
                saved.getCorreo(), saved.getRol().name());
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }
}
