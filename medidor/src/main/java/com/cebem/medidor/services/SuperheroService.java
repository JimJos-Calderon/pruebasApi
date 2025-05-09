package com.cebem.medidor.services;

import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cebem.medidor.models.Superhero;

@Service
public class SuperheroService {
    private static final String API_URL = "https://superheroapi.com/api/";
    private static final String TOKEN = "fe14d9f4ccac83bb5a532f86c27a71ab"; // Necesitas registrarte para obtener un token
    
    private final RestTemplate restTemplate;
    private final Random random = new Random();
    
    public SuperheroService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public Superhero getRandomSuperhero() {
        int randomId = random.nextInt(731) + 1; // La API tiene 731 personajes
        String url = API_URL + TOKEN + "/" + randomId;
        return restTemplate.getForObject(url, Superhero.class);
    }
}