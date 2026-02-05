package com.pruebaaws.prueba.repository;

import com.pruebaaws.prueba.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
