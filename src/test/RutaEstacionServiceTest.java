package co.edu.uniandes.dse.parcial1.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.parcial1.entities.EstacionEntity;
import co.edu.uniandes.dse.parcial1.entities.RutaEntity;
import co.edu.uniandes.dse.parcial1.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcial1.repositories.EstacionRepository;
import co.edu.uniandes.dse.parcial1.repositories.RutaRepository;

@SpringBootTest
@Transactional
public class RutaEstacionServiceTest {

    @Autowired private RutaEstacionService service;
    @Autowired private RutaRepository rutaRepo;
    @Autowired private EstacionRepository estacionRepo;

    private RutaEntity ruta;
    private EstacionEntity estacion;

    @BeforeEach
    void init() {
        ruta = new RutaEntity();
        ruta.setNombre("Ruta A");
        ruta.setColor("Rojo");
        ruta.setTipo("Diurna");
        ruta = rutaRepo.save(ruta);

        estacion = new EstacionEntity();
        estacion.setName("Estación 1");
        estacion.setDireccion("Calle 1 #2-3");
        estacion.setCapacidad(100);
        estacion = estacionRepo.save(estacion);
    }

    // addEstacionRuta
    @Test
    void testAddEstacionRuta_ok() throws Exception {
        assertDoesNotThrow(() -> service.addEstacionRuta(estacion.getId(), ruta.getId()));
        RutaEntity pr = rutaRepo.findById(ruta.getId()).get();
        EstacionEntity pe = estacionRepo.findById(estacion.getId()).get();
        assertTrue(pr.getEstaciones().stream().anyMatch(e -> e.getId().equals(pe.getId())));
        assertTrue(pe.getRutas().stream().anyMatch(r -> r.getId().equals(pr.getId())));
    }

    @Test
    void testAddEstacionRuta_estacionNoExiste() {
        assertThrows(EntityNotFoundException.class,
            () -> service.addEstacionRuta(99999L, ruta.getId()));
    }

    @Test
    void testAddEstacionRuta_rutaNoExiste() {
        assertThrows(EntityNotFoundException.class,
            () -> service.addEstacionRuta(estacion.getId(), 99999L));
    }

    @Test
    void testAddEstacionRuta_yaAsociada() throws Exception {
        service.addEstacionRuta(estacion.getId(), ruta.getId());
        assertThrows(IllegalOperationException.class,
            () -> service.addEstacionRuta(estacion.getId(), ruta.getId()));
    }

    // removeEstacionRuta
    @Test
    void testRemoveEstacionRuta_ok() throws Exception {
        service.addEstacionRuta(estacion.getId(), ruta.getId());
        assertDoesNotThrow(() -> service.removeEstacionRuta(estacion.getId(), ruta.getId()));
        RutaEntity pr = rutaRepo.findById(ruta.getId()).get();
        EstacionEntity pe = estacionRepo.findById(estacion.getId()).get();
        assertFalse(pr.getEstaciones().stream().anyMatch(e -> e.getId().equals(pe.getId())));
        assertFalse(pe.getRutas().stream().anyMatch(r -> r.getId().equals(pr.getId())));
    }

    @Test
    void testRemoveEstacionRuta_estacionNoExiste() {
        assertThrows(EntityNotFoundException.class,
            () -> service.removeEstacionRuta(99999L, ruta.getId()));
    }

    @Test
    void testRemoveEstacionRuta_rutaNoExiste() {
        assertThrows(EntityNotFoundException.class,
            () -> service.removeEstacionRuta(estacion.getId(), 99999L));
    }

    @Test
    void testRemoveEstacionRuta_noAsociada() {
        assertThrows(IllegalOperationException.class,
            () -> service.removeEstacionRuta(estacion.getId(), ruta.getId()));
    }
}
