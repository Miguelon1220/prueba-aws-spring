package com.pruebaaws.prueba.controller;

import com.pruebaaws.prueba.entity.Producto;
import com.pruebaaws.prueba.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


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
    public String agregar(@ModelAttribute("productoNuevo") Producto producto) {
        service.guardar(producto);
        return "redirect:/view-productos"; // refresca la página mostrando el nuevo producto
    }
}
