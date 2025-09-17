package co.edu.uniandes.dse.parcial1.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Data
@Entity
public class RutaEntity extends BaseEntity {

    private String nombre;
    private String color;
    private String tipo;

    // Una ruta puede parar en varias estaciones y viceversa
    @ManyToMany
    @JoinTable(
        name = "ruta_estacion",
        joinColumns = @JoinColumn(name = "ruta_id"),
        inverseJoinColumns = @JoinColumn(name = "estacion_id")
    )
    private Set<EstacionEntity> estaciones = new HashSet<>();
}