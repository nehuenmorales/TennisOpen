package com.baufest.tennis.springtennis.dto;

public class JugadorConGanancia {

    private Long id;

    private String nombre;

    private int puntos;

    private EntrenadorDTO entrenador;

    private int ganancia;

    //constructores

    public JugadorConGanancia(Long id, String nombre, int puntos, EntrenadorDTO entrenador, int ganancia) {
        this.id = id;
        this.nombre = nombre;
        this.puntos = puntos;
        this.entrenador = entrenador;
        this.ganancia = ganancia;
    }

    public JugadorConGanancia() {
    }

    //getters and setters


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

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public EntrenadorDTO getEntrenador() {
        return entrenador;
    }

    public void setEntrenador(EntrenadorDTO entrenador) {
        this.entrenador = entrenador;
    }

    public int getGanancia() {
        return ganancia;
    }

    public void setGanancia(int ganancia) {
        this.ganancia = ganancia;
    }
}
