package com.cebem.medidor.models;

import java.util.Map;

import lombok.Data;

@Data
public class Pokemon {
    private int id;
    private String name;
    private int height;
    private int weight;
    private String sprite;
    private String[] types;
    private String region;
    private String generation;
    private Map<String, Integer> stats; // Nuevo campo para estadísticas
}