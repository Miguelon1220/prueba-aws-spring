package com.pruebaaws.prueba.controller;

import com.pruebaaws.prueba.entity.Producto;
import com.pruebaaws.prueba.entity.Usuario;
import com.pruebaaws.prueba.service.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    // ===============================
    // LISTAR PRODUCTOS (LIBRE)
    // ===============================
    @GetMapping
    public List<Producto> listar() {
        return service.listar();
    }

    // ===============================
    // BUSCAR POR ID (LIBRE)
    // ===============================
    @GetMapping("/{id}")
    public Producto buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    // ===============================
    // AGREGAR PRODUCTO (SOLO ADMIN)
    // ===============================
    @PostMapping
    public Producto guardar(
            @RequestBody Producto producto,
            HttpSession session
    ) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null || !"ADMIN".equals(usuario.getRol())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Acceso denegado: solo administradores"
            );
        }

        return service.guardar(producto);
    }

    // ===============================
    // ELIMINAR PRODUCTO (SOLO ADMIN)
    // ===============================
    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id,
            HttpSession session
    ) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null || !"ADMIN".equals(usuario.getRol())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Acceso denegado: solo administradores"
            );
        }

        service.eliminar(id);
    }
}
