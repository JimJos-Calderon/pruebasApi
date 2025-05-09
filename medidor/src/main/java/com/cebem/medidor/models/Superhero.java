package com.cebem.medidor.models;
import lombok.Data;

@Data
public class Superhero {
    private String id;
    private String name;
    private Powerstats powerstats;
    private Biography biography;
    private Image image;
    
    @Data
    public static class Powerstats {
        private int intelligence;
        private int strength;
        private int speed;
        private int durability;
        private int power;
        private int combat;
    }
    
    @Data
    public static class Biography {
        private String fullName;
        private String publisher;
    }
    
    @Data
    public static class Image {
        private String url;
    }
    
    public int getTotalPower() {
        return powerstats.getIntelligence() + powerstats.getStrength() + 
               powerstats.getSpeed() + powerstats.getDurability() + 
               powerstats.getPower() + powerstats.getCombat();
    }
}