package com.baufest.tennis.springtennis.controller;


import com.baufest.tennis.springtennis.dto.EntrenadorDTO;
import com.baufest.tennis.springtennis.service.EntrenadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("springtennis/api/v1/entrenador")
public class EntrenadorController {

    private final EntrenadorService entrenadorService;

    @Autowired
    public EntrenadorController(EntrenadorService entrenadorService) {
        this.entrenadorService = entrenadorService;
    }

    //Rutas
    @GetMapping
    public ResponseEntity<List<EntrenadorDTO>> listAll() {
        return ResponseEntity.ok(entrenadorService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntrenadorDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(entrenadorService.getById(id));
    }

    @PostMapping
    public ResponseEntity<EntrenadorDTO> save(@RequestBody EntrenadorDTO entrenadorDTO) {
        EntrenadorDTO savedEntrenador = entrenadorService.save(entrenadorDTO);
        return new ResponseEntity<>(savedEntrenador, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntrenadorDTO> update(@PathVariable Long id, @RequestBody EntrenadorDTO entrenadorDTO) {
        entrenadorDTO.setId(id);
        EntrenadorDTO updatedJugador = entrenadorService.update(entrenadorDTO);
        return ResponseEntity.ok(updatedJugador);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        entrenadorService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
