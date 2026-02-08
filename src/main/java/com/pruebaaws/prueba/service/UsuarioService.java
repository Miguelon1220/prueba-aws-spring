package com.pruebaaws.prueba.service;

import com.pruebaaws.prueba.entity.Usuario;

import java.util.List;

public interface UsuarioService {
    Usuario guardar(Usuario usuario);
    Usuario buscarPorEmail(String email);
    List<Usuario> listar();
}
