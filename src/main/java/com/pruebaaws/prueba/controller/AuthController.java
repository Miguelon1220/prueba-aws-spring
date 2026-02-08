package com.pruebaaws.prueba.controller;

import com.pruebaaws.prueba.entity.Persona;
import com.pruebaaws.prueba.entity.Usuario;
import com.pruebaaws.prueba.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // 👉 Mostrar formulario de login / register
    @GetMapping("/register")
    public String mostrarFormularioRegistro(Model model) {
        return "register"; // register.html
    }

    // 👉 Procesar registro de usuario
    @PostMapping("/register")
    public String registrarUsuario(
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam int edad,
            @RequestParam String email,
            @RequestParam String password
    ) {

        // 1️⃣ Crear Persona (datos adicionales)
        Persona persona = new Persona();
        persona.setNombre(nombre);
        persona.setApellido(apellido);
        persona.setEdad(edad);

        // 2️⃣ Crear Usuario
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setPassword(password); // luego se encripta
        usuario.setRol("USER");        // todos empiezan como USER
        usuario.setPersona(persona);

        // 3️⃣ Guardar (JPA guarda Persona + Usuario)
        usuarioService.guardar(usuario);

        // 4️⃣ Volver al home
        return "redirect:/";
    }

    // 👉 Vista admin (por ahora solo placeholder)
    @GetMapping("/admin")
    public String admin() {
        return "admin"; // admin.html (luego lo protegemos)
    }
}
