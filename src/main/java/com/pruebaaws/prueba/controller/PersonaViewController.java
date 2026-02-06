package com.pruebaaws.prueba.controller;

import com.pruebaaws.prueba.service.PersonaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view-personas")
public class PersonaViewController {

    private final PersonaService personaService;

    public PersonaViewController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("personas", personaService.listar());
        return "personas"; // personas.html
    }
}
