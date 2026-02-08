package com.pruebaaws.prueba.controller;

import com.pruebaaws.prueba.entity.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        model.addAttribute("usuarioLogueado",
                session.getAttribute("usuarioLogueado"));
        return "home";
    }

    @GetMapping("/contacto")
    public String contacto(HttpSession session, Model model) {
        model.addAttribute("usuarioLogueado",
                session.getAttribute("usuarioLogueado"));
        return "contacto";
    }

    @GetMapping("/productos")
    public String productos(HttpSession session, Model model) {
        model.addAttribute("usuarioLogueado",
                session.getAttribute("usuarioLogueado"));
        return "productos";
    }
}
