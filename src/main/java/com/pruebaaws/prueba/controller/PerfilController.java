package com.pruebaaws.prueba.controller;

import com.pruebaaws.prueba.entity.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PerfilController {

    @GetMapping("/perfil")
    public String verPerfil(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("esAdmin", "ADMIN".equals(usuario.getRol()));

        return "perfil";
    }
}
