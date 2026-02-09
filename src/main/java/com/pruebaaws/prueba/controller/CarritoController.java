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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

        Usuario usuario = usuarioRepo.findById(usuarioEnSesion.getId()).orElse(null);
        if (usuario == null) return "redirect:/login";

        List<CarritoItem> items = carritoRepo.findByUsuario(usuario);

        // Calcular total
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
    public String agregarProducto(@PathVariable Long id,
                                  HttpSession session,
                                  RedirectAttributes redirectAttrs) {

        Usuario usuarioEnSesion = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioEnSesion == null) return "redirect:/login";

        Usuario usuario = usuarioRepo.findById(usuarioEnSesion.getId()).orElse(null);
        if (usuario == null) return "redirect:/login";

        Producto producto = productoRepo.findById(id).orElse(null);
        if (producto == null || producto.getStock() == null || producto.getStock() <= 0) {
            redirectAttrs.addFlashAttribute("mensaje", "Producto no disponible");
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
            } else {
                redirectAttrs.addFlashAttribute("mensaje", "No hay más stock disponible");
                return "redirect:/carrito";
            }
        }

        redirectAttrs.addFlashAttribute("mensaje", "Producto agregado al carrito");
        return "redirect:/carrito";
    }

    // =========================
    // DISMINUIR CANTIDAD
    // =========================
    @PostMapping("/disminuir/{id}")
    @Transactional
    public String disminuirProducto(@PathVariable Long id,
                                    HttpSession session,
                                    RedirectAttributes redirectAttrs) {

        Usuario usuarioEnSesion = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioEnSesion == null) return "redirect:/login";

        CarritoItem item = carritoRepo.findById(id).orElse(null);
        if (item != null && item.getUsuario().getId().equals(usuarioEnSesion.getId())) {
            if (item.getCantidad() > 1) {
                item.setCantidad(item.getCantidad() - 1);
                carritoRepo.save(item);
            } else {
                carritoRepo.delete(item);
            }
        }

        redirectAttrs.addFlashAttribute("mensaje", "Cantidad actualizada");
        return "redirect:/carrito";
    }

    // =========================
    // ELIMINAR PRODUCTO
    // =========================
    @PostMapping("/eliminar/{id}")
    @Transactional
    public String eliminar(@PathVariable Long id,
                           HttpSession session,
                           RedirectAttributes redirectAttrs) {

        Usuario usuarioEnSesion = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioEnSesion == null) return "redirect:/login";

        CarritoItem item = carritoRepo.findById(id).orElse(null);

        if (item != null && item.getUsuario().getId().equals(usuarioEnSesion.getId())) {
            carritoRepo.delete(item);
            redirectAttrs.addFlashAttribute("mensaje", "Producto eliminado del carrito");
        }

        return "redirect:/carrito";
    }

    // =========================
    // COMPRAR PRODUCTOS
    // =========================
    @PostMapping("/comprar")
    @Transactional
    public String comprar(HttpSession session, RedirectAttributes redirectAttrs) {

        Usuario usuarioEnSesion = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioEnSesion == null) return "redirect:/login";

        List<CarritoItem> items = carritoRepo.findByUsuario(usuarioEnSesion);

        if (items.isEmpty()) {
            redirectAttrs.addFlashAttribute("mensaje", "No hay productos en el carrito para comprar");
            return "redirect:/carrito";
        }

        // Restar stock
        for (CarritoItem item : items) {
            Producto producto = item.getProducto();
            if (producto != null) {
                int nuevoStock = producto.getStock() - item.getCantidad();
                producto.setStock(Math.max(nuevoStock, 0));
                productoRepo.save(producto);
            }
        }

        carritoRepo.deleteAll(items);
        redirectAttrs.addFlashAttribute("mensaje", "Compra realizada con éxito");

        return "redirect:/carrito";
    }
}
