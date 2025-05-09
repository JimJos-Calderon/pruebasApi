package com.cebem.medidor.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cebem.medidor.models.Pokemon;

@Service
public class PokemonService {

    private final RestTemplate restTemplate;

    public PokemonService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Pokemon  getPokemonRandom() {
        String url = "https://pokeapi.co/api/v2/pokemon/" + (int) (Math.random() * 898 + 1); // Hay 898 pokemones en la
                                                                                             // API
        return restTemplate.getForObject(url, Pokemon.class);
    }
}
