package com.pruebaaws.prueba.controller;

import com.pruebaaws.prueba.entity.Producto;
import com.pruebaaws.prueba.service.ProductoService;
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

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", service.listar());
        model.addAttribute("productoNuevo", new Producto()); // para el formulario
        return "productos"; // productos.html en templates
    }

    @PostMapping("/agregar")
    public String agregar(@ModelAttribute("productoNuevo") Producto producto,
                          @RequestParam("imagenArchivo") MultipartFile imagenArchivo) {
        try {
            // Si el usuario subió un archivo desde su PC
            if (!imagenArchivo.isEmpty()) {
                // Nombre del archivo
                String nombreArchivo = imagenArchivo.getOriginalFilename();
                // Carpeta donde se guardarán las imágenes (crear si no existe)
                Path ruta = Paths.get("src/main/resources/static/images/" + nombreArchivo);
                Files.createDirectories(ruta.getParent()); // crea la carpeta si no existe
                Files.write(ruta, imagenArchivo.getBytes());

                // Guardar la ruta accesible desde HTML
                producto.setImagenUrl("/images/" + nombreArchivo);
            }
            // Si no sube archivo, se usará la URL que ingresó en el formulario
            service.guardar(producto);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/view-productos"; // refresca la página mostrando el nuevo producto
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return "redirect:/view-productos"; // refresca la página
    }
}
