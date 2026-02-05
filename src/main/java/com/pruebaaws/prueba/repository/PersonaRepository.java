package com.pruebaaws.prueba.repository;

import com.pruebaaws.prueba.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
}
