package com.pruebaaws.prueba.controller;

import com.pruebaaws.prueba.entity.Producto;
import com.pruebaaws.prueba.entity.Usuario;
import com.pruebaaws.prueba.service.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/view-productos")
public class ProductoViewController {

    private final ProductoService service;

    public ProductoViewController(ProductoService service) {
        this.service = service;
    }

    // ========= LISTAR PRODUCTOS =========
    @GetMapping
    public String listar(Model model, HttpSession session) {

        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");

        model.addAttribute("productos", service.listar());
        model.addAttribute("productoNuevo", new Producto());
        model.addAttribute("usuarioLogueado", usuarioLogueado);

        return "productos";
    }

    // ========= AGREGAR PRODUCTO (SOLO ADMIN) =========
    @PostMapping("/agregar")
    public String agregar(
            @ModelAttribute("productoNuevo") Producto producto,
            @RequestParam("imagenArchivo") MultipartFile imagenArchivo,
            HttpSession session
    ) {

        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");

        // 🔒 Seguridad
        if (usuarioLogueado == null || !"ADMIN".equals(usuarioLogueado.getRol())) {
            return "redirect:/login";
        }

        try {
            if (!imagenArchivo.isEmpty()) {
                String nombreArchivo = imagenArchivo.getOriginalFilename();
                Path ruta = Paths.get("src/main/resources/static/images/" + nombreArchivo);
                Files.createDirectories(ruta.getParent());
                Files.write(ruta, imagenArchivo.getBytes());

                producto.setImagenUrl("/images/" + nombreArchivo);
            }

            service.guardar(producto);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/view-productos";
    }

    // ========= ELIMINAR PRODUCTO (SOLO ADMIN) =========
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session) {

        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");

        // 🔒 Seguridad
        if (usuarioLogueado == null || !"ADMIN".equals(usuarioLogueado.getRol())) {
            return "redirect:/login";
        }

        service.eliminar(id);
        return "redirect:/view-productos";
    }
}
