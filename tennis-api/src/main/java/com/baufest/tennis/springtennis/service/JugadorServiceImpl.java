package com.baufest.tennis.springtennis.service;

import com.baufest.tennis.springtennis.dto.JugadorConGanancia;
import com.baufest.tennis.springtennis.dto.JugadorDTO;
import com.baufest.tennis.springtennis.enums.Estado;
import com.baufest.tennis.springtennis.mapper.JugadorMapper;
import com.baufest.tennis.springtennis.model.Jugador;
import com.baufest.tennis.springtennis.model.Partido;
import com.baufest.tennis.springtennis.repository.JugadorRepository;
import com.baufest.tennis.springtennis.repository.PartidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

//Aca va la anotacion @Service para declarar que es un servicio y pueda ser detectado por el autowired
@Service
public class JugadorServiceImpl implements JugadorService {

    //String que se utilizan para las respuestas de los exception handler
    public static final String PLAYER_WITH_ID = "Player with id = ";
    public static final String DOES_NOT_EXIST = " does not exist.";
    public static final String ALREADY_EXISTS = " already exists.";

    //Estas son las variables donde se alojaran las instanciaciones del repository y el mapper al momento
    //de utilizarlas por medio de Autowired, son final para que no se puedan modificar una vez instanciadas
    private final JugadorRepository jugadorRepository;
    private final PartidoRepository partidoRepository;
    private final JugadorMapper jugadorMapper;

    /*
    Aca se utiliza la anotacion Autowired, esta anotacion de springboot se encarga de enlazar todos los componentes,
    va ligada a la inyeccion de dependencias, en este caso como se usa por constructor se declara el Spring Prototype
    Autowired arriba del constructor, de esta forma se asegura que al momento de instanciacion del componente, los modulos
    a los cuales se declara y se necesitan usar esten disponibles para su instanciacion, de este modo al momento de ser llamados
    se instancian momentaneamente, se llama a la funcion requerida y se desinstancia, es el concepto de hollywood, IoC inversion
    of control, No nos llames; nosotros te llamaremos
     */
    @Autowired
    public JugadorServiceImpl(JugadorRepository jugadorRepository,
                              PartidoRepository partidoRepository,
                              JugadorMapper jugadorMapper) {
        this.jugadorRepository = jugadorRepository;
        this.partidoRepository = partidoRepository;
        this.jugadorMapper = jugadorMapper;
    }


    @Override
    public List<JugadorConGanancia> listAll() {

        List<Partido> partidos = partidoRepository.findAll();
        List<JugadorDTO> jugadores = jugadorRepository.findAllByOrderByNombreAsc().stream()
                .map(this.jugadorMapper::toDTO)
                .collect(Collectors.toList());

        List<JugadorConGanancia> jugadoresConGanancia = new ArrayList<>();

       for (JugadorDTO J : jugadores) {
            List<Partido> trescientos = listPartidosGanadosMasTresGames(J.getId(), partidos);
            List<Partido> doscientos = listPartidosGanadosMasTresGames(J.getId(), partidos);
            int ganancia = trescientos.size() * 300 + doscientos.size() * 200;

            JugadorConGanancia jugadorConGanancia = new JugadorConGanancia();
            jugadorConGanancia.setGanancia(ganancia);
            jugadorConGanancia.setId(J.getId());
            jugadorConGanancia.setNombre(J.getNombre());
            jugadorConGanancia.setEntrenador(J.getEntrenador());
            jugadorConGanancia.setNombre(J.getNombre());

           jugadoresConGanancia.add(jugadorConGanancia);
        }


        return jugadoresConGanancia;

    }


    private List<Partido> listPartidosGanadosMasTresGames(Long id, List<Partido> partidos) {
        List<Partido> partidosFiltrados = new ArrayList<>();
        for(Partido p : partidos){
            if(p.getEstado() == Estado.FINALIZADO && p.getJugadorLocal().getId().equals(id) && p.getCantidadGamesLocal() == 6)
                if(p.getCantidadGamesLocal() - p.getCantidadGamesVisitante() >= 3 ){
                    partidosFiltrados.add(p);
                }
            if(p.getEstado() == Estado.FINALIZADO && p.getJugadorVisitante().getId().equals(id) && p.getCantidadGamesVisitante() == 6)
                if(p.getCantidadGamesVisitante() - p.getCantidadGamesLocal() >= 3 ){
                    partidosFiltrados.add(p);
                }
        }
        return partidosFiltrados;
    }

    private List<Partido> listPartidosGanadosMenosTresGames(Long id, List<Partido> partidos) {
        List<Partido> partidosFiltrados = new ArrayList<>();
        for(Partido p : partidos){
            if(p.getEstado() == Estado.FINALIZADO && p.getJugadorLocal().getId().equals(id) && p.getCantidadGamesLocal() == 6)
                if(p.getCantidadGamesLocal() - p.getCantidadGamesVisitante() < 3 ){
                    partidosFiltrados.add(p);
                }
            if(p.getEstado() == Estado.FINALIZADO && p.getJugadorVisitante().getId().equals(id) && p.getCantidadGamesVisitante() == 6)
                if(p.getCantidadGamesVisitante() - p.getCantidadGamesLocal() < 3 ){
                    partidosFiltrados.add(p);
                }
        }
        return partidosFiltrados;
    }


    @Override
    public JugadorDTO getById(Long id) {
        /*Llama al repository en el metodo findById, para obtener una entidad jugador, la cual la transforma
         * a DTO con el .map() en caso de que el .map arroje conjunto vacio arroja una excepcion de tipo NoSuchElementException*/
        return jugadorRepository.findById(id).map(this.jugadorMapper::toDTO)
                .orElseThrow(() -> new NoSuchElementException(PLAYER_WITH_ID + id + DOES_NOT_EXIST));
    }


    @Override
    public JugadorDTO save(JugadorDTO jugador) {
        /*
         * instancia un booleano llamado exist, el cual lo iguala a la respuesta de getById del DTO entrante por parametro
         * get id, si es distinto de null y ademas el repository en metodo existById devuelve un ID se carga con True caso contrario false*/
        boolean exists = jugador.getId() != null && jugadorRepository.existsById(jugador.getId());
        if (exists) { //Si existe arroja una nueva excepcion del tipo IllegalArgumentException
            throw new IllegalArgumentException(PLAYER_WITH_ID + jugador.getId() + ALREADY_EXISTS);
        }
        //En caso de que la verificacion anterior no suceda, continua el flujo y guarda la entidad con el save
        //Tambien devuelve el jugador guardado ya que el repository lo devuelve....
        return this.jugadorMapper.toDTO(jugadorRepository.save(this.jugadorMapper.fromDTO(jugador)));
    }


    @Override
    public JugadorDTO update(JugadorDTO jugador) {
        boolean exists = jugadorRepository.existsById(jugador.getId());
        if (!exists) {
            throw new NoSuchElementException(PLAYER_WITH_ID + jugador.getId() + DOES_NOT_EXIST);
        }
        return this.jugadorMapper.toDTO(jugadorRepository.save(this.jugadorMapper.fromDTO(jugador)));
    }

    @Override
    public void delete(Long id) {
        boolean exists = jugadorRepository.existsById(id);
        if (!exists) {
            throw new NoSuchElementException(PLAYER_WITH_ID + id + DOES_NOT_EXIST);
        }
        jugadorRepository.deleteById(id);
    }

    @Override
    public JugadorDTO recalculateRanking(Long id) {
        Optional<Jugador> optJugador = jugadorRepository.findById(id);
        if (optJugador.isPresent()) {
            Jugador jugador = optJugador.get();

            List<Partido> partidos = partidoRepository.findAll();

            // Define las constantes con los indices definidos para el calculo
            final int INDICADOR_GANADOS_LOCAL = 10;
            final int INDICADOR_GANADOS_VISITANTE = 15;
            final int INDICADOR_PERDIDOS_LOCAL = -5;

            // Inicializa el ranking inicial
            int ranking = 0;

            List<Partido> partidosGanadosLocal;
            List<Partido> partidosGanadosVisitante;
            List<Partido> partidosPerdidosLocal;

            partidosGanadosLocal = listPartidosGanadosLocalDeJugador(id, partidos);
            partidosGanadosVisitante = listPartidosGanadosVisitanteDeJugador(id, partidos);
            partidosPerdidosLocal = listPartidosPerdidosLocalDeJugador(id, partidos);

            // Realiza el calculo por cada indicador
            ranking += partidosGanadosLocal.size() * INDICADOR_GANADOS_LOCAL;
            ranking += partidosGanadosVisitante.size() * INDICADOR_GANADOS_VISITANTE;
            ranking += partidosPerdidosLocal.size() * INDICADOR_PERDIDOS_LOCAL;

            // Si el ranking obtenido es menor a 0 se setea en 0
            ranking = Math.max(ranking, 0);

            // Se resetean los puntos en el jugador
            jugador.setPuntos(ranking);
            return this.jugadorMapper.toDTO(jugadorRepository.save(jugador));
        } else{
            throw new NoSuchElementException(PLAYER_WITH_ID + id + DOES_NOT_EXIST);
        }
    }

    private List<Partido> listPartidosGanadosLocalDeJugador(Long id, List<Partido> partidos) {
        List<Partido> partidosFiltrados = new ArrayList<>();
        for(Partido p : partidos){
            if(p.getEstado() == Estado.FINALIZADO && p.getJugadorLocal().getId().equals(id) && p.getCantidadGamesLocal() == 6)
                partidosFiltrados.add(p);
        }
        return partidosFiltrados;
    }

    private List<Partido> listPartidosGanadosVisitanteDeJugador(Long id, List<Partido> partidos) {
        List<Partido> partidosFiltrados = new ArrayList<>();
        for(Partido p : partidos){
            if(p.getEstado() == Estado.FINALIZADO && p.getJugadorVisitante().getId().equals(id) && p.getCantidadGamesVisitante() == 6)
                partidosFiltrados.add(p);
        }
        return partidosFiltrados;
    }

    private List<Partido> listPartidosPerdidosLocalDeJugador(Long id, List<Partido> partidos) {
        List<Partido> partidosFiltrados = new ArrayList<>();
        for(Partido p : partidos){
            if(p.getEstado() == Estado.FINALIZADO && p.getJugadorLocal().getId().equals(id) && p.getCantidadGamesVisitante() == 6)
                partidosFiltrados.add(p);
        }
        return partidosFiltrados;
    }
}