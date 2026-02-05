package com.pruebaaws.prueba.service;

import com.pruebaaws.prueba.entity.Persona;
import java.util.List;

public interface PersonaService {

    Persona guardar(Persona persona);

    List<Persona> listar();

    Persona buscarPorId(Long id);

    void eliminar(Long id);
}
