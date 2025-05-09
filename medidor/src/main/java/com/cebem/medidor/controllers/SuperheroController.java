package com.cebem.medidor.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.cebem.medidor.models.Superhero;
import com.cebem.medidor.services.SuperheroService;

@Controller
public class SuperheroController {
    private final SuperheroService superheroService;
    
    public SuperheroController(SuperheroService superheroService) {
        this.superheroService = superheroService;
    }
    
    @GetMapping("/superheroes")
    public String compareSuperheroes(Model model) {
        Superhero hero1 = superheroService.getRandomSuperhero();
        Superhero hero2 = superheroService.getRandomSuperhero();
        
        model.addAttribute("hero1", hero1);
        model.addAttribute("hero2", hero2);
        model.addAttribute("winner", 
            hero1.getTotalPower() > hero2.getTotalPower() ? hero1 : hero2);
        
        return "comparison";
    }
}