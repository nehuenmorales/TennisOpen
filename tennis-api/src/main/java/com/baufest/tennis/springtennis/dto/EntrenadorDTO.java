package com.baufest.tennis.springtennis.dto;

public class EntrenadorDTO {

    //Atributos
    private Long id;

    private String nombre;

    //Constructores

    public EntrenadorDTO() {
    }

    public EntrenadorDTO(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    //Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
