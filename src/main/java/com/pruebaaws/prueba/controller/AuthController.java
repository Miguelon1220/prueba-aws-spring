package com.pruebaaws.prueba.controller;

import com.pruebaaws.prueba.entity.Persona;
import com.pruebaaws.prueba.entity.Usuario;
import com.pruebaaws.prueba.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UsuarioRepository usuarioRepository;

    public AuthController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // ========= REGISTER =========
    @GetMapping("/register")
    public String mostrarRegistro(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogueado") != null) {
            return "redirect:/perfil";
        }
        Usuario usuario = new Usuario();
        usuario.setPersona(new Persona());
        model.addAttribute("usuario", usuario);
        return "register";
    }

    @PostMapping("/register")
    public String registrarUsuario(
            @ModelAttribute Usuario usuario,
            @RequestParam String tipo
    ) {

        if ("ADMIN".equals(tipo)) {
            if (!"0706094810".equals(usuario.getPersona().getCedula())) {
                return "redirect:/register?error";
            }
            usuario.setRol("ADMIN");
        } else {
            usuario.setRol("USER");
        }

        usuarioRepository.save(usuario);
        return "redirect:/login";
    }

    // ========= LOGIN =========
    @GetMapping("/login")
    public String mostrarLogin(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogueado") != null) {
            return "redirect:/perfil";
        }
        model.addAttribute("usuario", new Usuario());
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model
    ) {

        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null || !usuario.getPassword().equals(password)) {
            model.addAttribute("error", "Correo o contraseña incorrectos");
            return "login";
        }

        session.setAttribute("usuarioLogueado", usuario);
        return "redirect:/perfil";
    }

    // ========= LOGOUT =========
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
