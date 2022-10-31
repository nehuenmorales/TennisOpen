package com.baufest.tennis.springtennis.model;

import javax.persistence.*;

@Entity
@Table(name = "Entrenador")
public class Entrenador {

    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String nombre;


    //Constructores

    public Entrenador() {
    }

    public Entrenador(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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
}
