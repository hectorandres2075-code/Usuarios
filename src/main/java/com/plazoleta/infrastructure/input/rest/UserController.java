package com.plazoleta.infrastructure.input.rest;

import com.plazoleta.application.port.input.CreateOwnerUseCase;
import com.plazoleta.domain.model.User;
import com.plazoleta.infrastructure.input.rest.dto.OwnerRequest;
import com.plazoleta.infrastructure.input.rest.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Usuarios")
public class UserController {

    private final CreateOwnerUseCase createOwnerUseCase;

    public UserController(CreateOwnerUseCase createOwnerUseCase) {
        this.createOwnerUseCase = createOwnerUseCase;
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
