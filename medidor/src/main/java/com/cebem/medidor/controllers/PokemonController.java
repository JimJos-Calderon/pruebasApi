package com.cebem.medidor.controllers;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cebem.medidor.models.Pokemon;

@RestController
public class PokemonController {

    private final RestTemplate restTemplate;
    private final Random random = new Random();

    @Autowired
    public PokemonController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/pokemon/random")
public Pokemon getRandomPokemon() {
    // Obtener un ID aleatorio (hay aproximadamente 1000 Pokémon)
    int randomId = random.nextInt(1000) + 1;
    
    // Consultar la PokeAPI para datos básicos
    String pokemonUrl = "https://pokeapi.co/api/v2/pokemon/" + randomId;
    Map<String, Object> pokemonResponse = restTemplate.getForObject(pokemonUrl, Map.class);
    
    // Consultar la PokeAPI para datos de especie (que incluye generación)
    Map<String, String> speciesInfo = (Map<String, String>) pokemonResponse.get("species");
    String speciesUrl = speciesInfo.get("url");
    Map<String, Object> speciesResponse = restTemplate.getForObject(speciesUrl, Map.class);
    
    // Construir el objeto Pokemon
    Pokemon pokemon = new Pokemon();
    pokemon.setId(randomId);
    pokemon.setName(pokemonResponse.get("name").toString());
    pokemon.setHeight(Integer.parseInt(pokemonResponse.get("height").toString()));
    pokemon.setWeight(Integer.parseInt(pokemonResponse.get("weight").toString()));
    pokemon.setSprite(((Map<String, String>) pokemonResponse.get("sprites")).get("front_default"));
    
    // Obtener los tipos
    List<Map<String, Object>> types = (List<Map<String, Object>>) pokemonResponse.get("types");
    String[] typeNames = types.stream()
            .map(type -> ((Map<String, String>) type.get("type")).get("name"))
            .toArray(String[]::new);
    pokemon.setTypes(typeNames);
    
    // Obtener generación y región
    Map<String, Object> generationInfo = (Map<String, Object>) speciesResponse.get("generation");
    String generationUrl = generationInfo.get("url").toString();
    Map<String, Object> generationResponse = restTemplate.getForObject(generationUrl, Map.class);
    
    // Obtener el nombre en inglés de la generación
    List<Map<String, Object>> namesList = (List<Map<String, Object>>) generationResponse.get("names");
    String generationName = namesList.stream()
            .filter(name -> "en".equals(((Map<String, String>) name.get("language")).get("name")))
            .findFirst()
            .map(name -> (String) name.get("name"))
            .orElse("Unknown");
    
    pokemon.setGeneration(generationName);
    pokemon.setRegion(getRegionFromGeneration(generationName));
    
    return pokemon;
}

    @GetMapping("/pokemon/random/card")
    public String getRandomPokemonCard() {
        Pokemon pokemon = getRandomPokemon();
        return generatePokemonCard(pokemon);
    }

    private String generatePokemonCard(Pokemon pokemon) {
        StringBuilder card = new StringBuilder();
        // Añadimos estilo al body para fondo oscuro
        card.append("<body style=\"background-color: #121212; display: flex; justify-content: center; align-items: center; min-height: 100vh; margin: 0;\">");
        
        card.append("<div style=\"border: 2px solid #444; border-radius: 10px; padding: 20px; width: 300px; " +
                   "text-align: center; background: linear-gradient(135deg, #2c2c2c, #1e1e1e); " +
                   "box-shadow: 0 4px 8px rgba(0,0,0,0.5); color: #e0e0e0;\">");
        
        card.append("<h2 style=\"color: #ffffff; text-transform: capitalize; margin-top: 0;\">")
           .append(pokemon.getName()).append("</h2>");
        
        card.append("<p style=\"color: #aaaaaa;\">ID: #").append(pokemon.getId()).append("</p>");
        
        card.append("<img src=\"").append(pokemon.getSprite())
           .append("\" style=\"width: 150px; height: 150px; background-color: #333333; " +
                  "border-radius: 50%; padding: 10px; border: 3px solid #444;\">");
        
        card.append("<div style=\"margin: 15px 0;\">");
        for (String type : pokemon.getTypes()) {
            card.append("<span style=\"display: inline-block; padding: 5px 10px; margin: 0 5px; " +
                       "border-radius: 15px; background-color: ").append(getTypeColor(type))
               .append("; color: white; font-weight: bold;\">")
               .append(type)
               .append("</span>");
        }
        card.append("</div>");
        
        card.append("<div style=\"background-color: #333333; border-radius: 8px; padding: 10px; margin: 10px 0;\">");
        card.append("<p style=\"margin: 5px 0;\">Height: ").append(pokemon.getHeight() / 10.0).append(" m</p>");
        card.append("<p style=\"margin: 5px 0;\">Weight: ").append(pokemon.getWeight() / 10.0).append(" kg</p>");
        card.append("</div>");
        
        card.append("<div style=\"background-color: #333333; border-radius: 8px; padding: 10px;\">");
        card.append("<p style=\"margin: 5px 0;\">Region: ").append(pokemon.getRegion()).append("</p>");
        card.append("<p style=\"margin: 5px 0;\">Generation: ").append(pokemon.getGeneration()).append("</p>");
        card.append("</div>");
        
        card.append("</div>");
        card.append("</body>");
        return card.toString();
    }
    

    private String getTypeColor(String type) {
        switch (type.toLowerCase()) {
            case "fire": return "#F08030";
            case "water": return "#6890F0";
            case "grass": return "#78C850";
            case "electric": return "#F8D030";
            case "psychic": return "#F85888";
            case "ice": return "#98D8D8";
            case "dragon": return "#7038F8";
            case "dark": return "#705848";
            case "fairy": return "#EE99AC";
            case "normal": return "#A8A878";
            case "fighting": return "#C03028";
            case "flying": return "#A890F0";
            case "poison": return "#A040A0";
            case "ground": return "#E0C068";
            case "rock": return "#B8A038";
            case "bug": return "#A8B820";
            case "ghost": return "#705898";
            case "steel": return "#B8B8D0";
            default: return "#68A090";
        }
    }

    private String getRegionFromGeneration(String generation) {
        switch (generation.toLowerCase()) {
            case "generation i": return "Kanto";
            case "generation ii": return "Johto";
            case "generation iii": return "Hoenn";
            case "generation iv": return "Sinnoh";
            case "generation v": return "Unova";
            case "generation vi": return "Kalos";
            case "generation vii": return "Alola";
            case "generation viii": return "Galar";
            case "generation ix": return "Paldea";
            default: return "Unknown";
        }
    }
}
