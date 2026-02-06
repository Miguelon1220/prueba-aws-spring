package com.pruebaaws.prueba.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String inicio() {
        return "home"; // index.html en /resources/templates
    }
}
