package co.edu.uniandes.dse.parcial1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.parcial1.entities.EstacionEntity;
import co.edu.uniandes.dse.parcial1.entities.RutaEntity;
import co.edu.uniandes.dse.parcial1.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcial1.repositories.EstacionRepository;
import co.edu.uniandes.dse.parcial1.repositories.RutaRepository;

@Service
public class RutaEstacionService {

    @Autowired
    private RutaRepository rutaRepository;

    @Autowired
    private EstacionRepository estacionRepository;

    /** Agrega la estación a la ruta. */
    @Transactional
    public void addEstacionRuta(Long estacionId, Long rutaId)
            throws EntityNotFoundException, IllegalOperationException {

        RutaEntity ruta = rutaRepository.findById(rutaId)
                .orElseThrow(() -> new EntityNotFoundException("La ruta con id " + rutaId + " no existe"));

        EstacionEntity estacion = estacionRepository.findById(estacionId)
                .orElseThrow(() -> new EntityNotFoundException("La estación con id " + estacionId + " no existe"));

        if (ruta.getEstaciones().contains(estacion)) {
            throw new IllegalOperationException("La estación ya pertenece a la ruta");
        }
        ruta.getEstaciones().add(estacion);
        estacion.getRutas().add(ruta);

        rutaRepository.save(ruta);
        estacionRepository.save(estacion);
    }

    /** Elimina la asociación entre estación y ruta. */
    @Transactional
    public void removeEstacionRuta(Long estacionId, Long rutaId)
            throws EntityNotFoundException, IllegalOperationException {

        RutaEntity ruta = rutaRepository.findById(rutaId)
                .orElseThrow(() -> new EntityNotFoundException("La ruta con id " + rutaId + " no existe"));

        EstacionEntity estacion = estacionRepository.findById(estacionId)
                .orElseThrow(() -> new EntityNotFoundException("La estación con id " + estacionId + " no existe"));

        if (!ruta.getEstaciones().contains(estacion)) {
            throw new IllegalOperationException("La estación no pertenece a la ruta");
        }

        ruta.getEstaciones().remove(estacion);
        estacion.getRutas().remove(ruta);

        rutaRepository.save(ruta);
        estacionRepository.save(estacion);
    }
}
