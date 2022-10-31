package com.baufest.tennis.springtennis.service;

import com.baufest.tennis.springtennis.dto.EntrenadorDTO;
import com.baufest.tennis.springtennis.mapper.EntrenadorMapper;
import com.baufest.tennis.springtennis.repository.EntrenadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class EntrenadorServiceImpl implements EntrenadorService{

    private final EntrenadorRepository entrenadorRepository;
    private final EntrenadorMapper entrenadorMapper;

    @Autowired
    public EntrenadorServiceImpl(EntrenadorRepository entrenadorRepository, EntrenadorMapper entrenadorMapper) {
        this.entrenadorRepository = entrenadorRepository;
        this.entrenadorMapper = entrenadorMapper;
    }


    @Override
    public List<EntrenadorDTO> listAll() {
        return entrenadorRepository.findAll()
                .stream().map(this.entrenadorMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EntrenadorDTO getById(Long id) {
        return entrenadorRepository.findById(id).map(this.entrenadorMapper::toDTO)
                .orElseThrow(() -> new NoSuchElementException("El Entrenador con id:" + id + "NO EXISTE"));
    }

    @Override
    public EntrenadorDTO save(EntrenadorDTO entrenador) {

        boolean exists = entrenador.getId() != null && entrenadorRepository.existsById(entrenador.getId());
        if(exists){
            throw new IllegalArgumentException("El Entrenador ya existe");
        }
        return this.entrenadorMapper.toDTO(entrenadorRepository.save(this.entrenadorMapper.fromDTO(entrenador)));
    }

    @Override
    public EntrenadorDTO update(EntrenadorDTO entrenador) {
        boolean exists = entrenadorRepository.existsById(entrenador.getId());
        if(!exists){
            throw new IllegalArgumentException("El Entrenador que quieres modificar no Existe");
        }
        return this.entrenadorMapper.toDTO(entrenadorRepository.save(this.entrenadorMapper.fromDTO(entrenador)));
    }

    @Override
    public void delete(Long id) {
        boolean exists = entrenadorRepository.existsById(id);
        if (!exists) {
            throw new NoSuchElementException("El entrenador que quieres eliminar no existe");
        }
        entrenadorRepository.deleteById(id);

    }
}
