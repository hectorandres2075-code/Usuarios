package com.plazoleta.application.service;

import com.plazoleta.application.port.input.LoginUseCase;
import com.plazoleta.application.port.output.PasswordEncoderPort;
import com.plazoleta.application.port.output.TokenPort;
import com.plazoleta.application.port.output.UserPersistencePort;
import com.plazoleta.domain.exception.InvalidCredentialsException;
import com.plazoleta.domain.model.User;

public class LoginService implements LoginUseCase {

    private final UserPersistencePort userPersistencePort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final TokenPort tokenPort;

    public LoginService(UserPersistencePort userPersistencePort,
                        PasswordEncoderPort passwordEncoderPort,
                        TokenPort tokenPort) {
        this.userPersistencePort = userPersistencePort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.tokenPort = tokenPort;
    }

    @Override
    public String login(String correo, String clave) {
        // Mismo error si el correo no existe o la clave es mala: no revelamos cuál falló.
        User user = userPersistencePort.findByCorreo(correo)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoderPort.matches(clave, user.getClave())) {
            throw new InvalidCredentialsException();
        }
        return tokenPort.generate(user);
    }
}
