package com.pruebaaws.prueba.service;

import com.pruebaaws.prueba.entity.Producto;
import java.util.List;

public interface ProductoService {
    List<Producto> listar();
    Producto guardar(Producto producto);
    Producto buscarPorId(Long id);
    void eliminar(Long id);
}
