package com.pruebaaws.prueba.service;

import com.pruebaaws.prueba.entity.Usuario;
import com.pruebaaws.prueba.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repo;

    public UsuarioServiceImpl(UsuarioRepository repo) {
        this.repo = repo;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        return repo.save(usuario);
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        return repo.findByEmail(email);
    }

    @Override
    public List<Usuario> listar() {
        return repo.findAll();
    }
}
