package co.edu.uniandes.dse.parcial1.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Data
@Entity
public class EstacionEntity extends BaseEntity {
    private String name;
    private String direccion;
    private Integer capacidad;

    // En una estación paran varias rutas
    @ManyToMany(mappedBy = "estaciones")
    private Set<RutaEntity> rutas = new HashSet<>();
}