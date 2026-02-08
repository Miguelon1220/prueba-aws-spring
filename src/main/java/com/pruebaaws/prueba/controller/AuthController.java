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

    // ================= REGISTRO =================

    @GetMapping("/register")
    public String mostrarRegistro(Model model) {
        Usuario usuario = new Usuario();
        usuario.setPersona(new Persona()); // 🔥 obligatorio
        model.addAttribute("usuario", usuario);
        return "register";
    }

    @PostMapping("/register")
    public String registrarUsuario(@ModelAttribute Usuario usuario) {
        usuario.setRol("USER");
        usuarioRepository.save(usuario);
        return "redirect:/login"; // 👈 después del registro
    }

    // ================= LOGIN =================

    @GetMapping("/login")
    public String mostrarLogin(Model model) {
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

        // Guardamos usuario en sesión
        session.setAttribute("usuarioLogueado", usuario);

        // Redirección según rol
        if ("ADMIN".equals(usuario.getRol())) {
            return "redirect:/admin";
        }

        return "redirect:/view-productos";
    }

    // ================= LOGOUT =================

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
