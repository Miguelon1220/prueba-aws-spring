package com.pruebaaws.prueba.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // HOME
    @GetMapping("/")
    public String home() {
        return "home"; // home.html
    }

    // CONTACTO
    @GetMapping("/contacto")
    public String contacto() {
        return "contacto"; // contacto.html
    }

    // PRODUCTOS (vista)
    @GetMapping("/productos")
    public String productos() {
        return "productos"; // productos.html
    }
}
