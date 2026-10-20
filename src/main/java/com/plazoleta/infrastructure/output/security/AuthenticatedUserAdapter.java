package com.plazoleta.infrastructure.output.security;

import com.plazoleta.application.port.output.AuthenticatedUserPort;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticatedUserAdapter implements AuthenticatedUserPort {

    @Override
    public Long currentUserId() {
        // El filtro JWT guarda el id del usuario como "principal".
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
