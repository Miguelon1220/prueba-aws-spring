package com.pruebaaws.prueba.repository;

import com.pruebaaws.prueba.entity.CarritoItem;
import com.pruebaaws.prueba.entity.Producto;
import com.pruebaaws.prueba.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarritoRepository extends JpaRepository<CarritoItem, Long> {

    List<CarritoItem> findByUsuario(Usuario usuario);

    Optional<CarritoItem> findByUsuarioAndProducto(Usuario usuario, Producto producto);
}
