package com.pruebaaws.prueba.controller;

import com.pruebaaws.prueba.entity.CarritoItem;
import com.pruebaaws.prueba.entity.Producto;
import com.pruebaaws.prueba.entity.Usuario;
import com.pruebaaws.prueba.repository.CarritoRepository;
import com.pruebaaws.prueba.repository.ProductoRepository;
import com.pruebaaws.prueba.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    private final CarritoRepository carritoRepo;
    private final ProductoRepository productoRepo;
    private final UsuarioRepository usuarioRepo;

    public CarritoController(CarritoRepository carritoRepo,
                             ProductoRepository productoRepo,
                             UsuarioRepository usuarioRepo) {
        this.carritoRepo = carritoRepo;
        this.productoRepo = productoRepo;
        this.usuarioRepo = usuarioRepo;
    }

    // =========================
    // VER CARRITO
    // =========================
    @GetMapping
    @Transactional(readOnly = true)
    public String verCarrito(HttpSession session, Model model) {

        Usuario usuarioEnSesion = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioEnSesion == null) return "redirect:/login";

        // Recarga usuario de DB
        Usuario usuario = usuarioRepo.findById(usuarioEnSesion.getId()).orElse(null);
        if (usuario == null) return "redirect:/login";

        List<CarritoItem> items = carritoRepo.findByUsuario(usuario);

        // Forzar carga de productos para evitar LazyInitializationException
        items.forEach(i -> {
            if (i.getProducto() != null) {
                i.getProducto().getNombre();
            }
        });

        double total = items.stream()
                .filter(i -> i.getProducto() != null)
                .mapToDouble(i -> i.getProducto().getPrecio() * i.getCantidad())
                .sum();

        model.addAttribute("items", items);
        model.addAttribute("total", total);

        return "carrito";
    }

    // =========================
    // AGREGAR PRODUCTO
    // =========================
    @PostMapping("/agregar/{id}")
    @Transactional
    public String agregarProducto(@PathVariable Long id, HttpSession session) {

        Usuario usuarioEnSesion = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioEnSesion == null) return "redirect:/login";

        // Recargar usuario para asegurar ID válido
        Usuario usuario = usuarioRepo.findById(usuarioEnSesion.getId()).orElse(null);
        if (usuario == null) return "redirect:/login";

        Producto producto = productoRepo.findById(id).orElse(null);
        if (producto == null || producto.getStock() == null || producto.getStock() <= 0) {
            return "redirect:/view-productos";
        }

        CarritoItem item = carritoRepo.findByUsuarioAndProducto(usuario, producto).orElse(null);

        if (item == null) {
            item = new CarritoItem();
            item.setUsuario(usuario);
            item.setProducto(producto);
            item.setCantidad(1);
            carritoRepo.save(item);
        } else {
            if (item.getCantidad() < producto.getStock()) {
                item.setCantidad(item.getCantidad() + 1);
                carritoRepo.save(item);
            }
        }

        return "redirect:/carrito";
    }

    // =========================
    // ELIMINAR PRODUCTO
    // =========================
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session) {

        Usuario usuarioEnSesion = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioEnSesion == null) return "redirect:/login";

        CarritoItem item = carritoRepo.findById(id).orElse(null);

        // Validar que el item pertenece al usuario
        if (item != null && item.getUsuario().getId().equals(usuarioEnSesion.getId())) {
            carritoRepo.delete(item);
        }

        return "redirect:/carrito";
    }
}
