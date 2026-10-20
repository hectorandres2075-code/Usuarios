package com.plazoleta.application.port.output;

import com.plazoleta.domain.model.User;

public interface TokenPort {

    String generate(User user);
}
