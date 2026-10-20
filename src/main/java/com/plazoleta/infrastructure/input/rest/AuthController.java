package com.plazoleta.infrastructure.input.rest;

import com.plazoleta.application.port.input.LoginUseCase;
import com.plazoleta.infrastructure.input.rest.dto.LoginRequest;
import com.plazoleta.infrastructure.input.rest.dto.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticación")
public class AuthController {

    private final LoginUseCase loginUseCase;

    public AuthController(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @Operation(summary = "Iniciar sesión", description = "Devuelve un token JWT si las credenciales son correctas")
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return new LoginResponse(loginUseCase.login(request.getCorreo(), request.getClave()));
    }
}
