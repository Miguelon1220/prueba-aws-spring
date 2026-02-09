package com.pruebaaws.prueba.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalController {

    @ModelAttribute
    public void agregarUsuarioLogueado(Model model, HttpSession session) {
        model.addAttribute("usuarioLogueado",
                session.getAttribute("usuarioLogueado"));
    }
}
