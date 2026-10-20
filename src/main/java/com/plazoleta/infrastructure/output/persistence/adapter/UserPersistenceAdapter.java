package com.plazoleta.infrastructure.output.persistence.adapter;

import com.plazoleta.application.port.output.UserPersistencePort;
import com.plazoleta.domain.model.User;
import com.plazoleta.infrastructure.output.persistence.entity.UserEntity;
import com.plazoleta.infrastructure.output.persistence.repository.UserRepository;

import java.util.Optional;

public class UserPersistenceAdapter implements UserPersistencePort {

    private final UserRepository userRepository;

    public UserPersistenceAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id).map(this::toModel);
    }

    @Override
    public Optional<User> findByCorreo(String correo) {
        return userRepository.findByCorreo(correo).map(this::toModel);
    }

    @Override
    public boolean existsByCorreo(String correo) {
        return userRepository.existsByCorreo(correo);
    }

    @Override
    public boolean existsByDocumento(String documento) {
        return userRepository.existsByDocumento(documento);
    }

    @Override
    public User save(User user) {
        return toModel(userRepository.save(toEntity(user)));
    }

    private User toModel(UserEntity e) {
        User u = new User(e.getId(), e.getNombre(), e.getApellido(), e.getDocumento(), e.getCelular(),
                e.getFechaNacimiento(), e.getCorreo(), e.getClave(), e.getRol());
        u.setIdRestaurante(e.getIdRestaurante());
        return u;
    }

    private UserEntity toEntity(User u) {
        UserEntity e = new UserEntity();
        e.setId(u.getId());
        e.setNombre(u.getNombre());
        e.setApellido(u.getApellido());
        e.setDocumento(u.getDocumento());
        e.setCelular(u.getCelular());
        e.setFechaNacimiento(u.getFechaNacimiento());
        e.setCorreo(u.getCorreo());
        e.setClave(u.getClave());
        e.setRol(u.getRol());
        e.setIdRestaurante(u.getIdRestaurante());
        return e;
    }
}
