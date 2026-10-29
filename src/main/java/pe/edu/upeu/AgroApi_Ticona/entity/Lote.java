package pe.edu.upeu.AgroApi_Ticona.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lotes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false)
    private Double hectareas;

    @Column(nullable = false, length = 100)
    private String cultivo;

    @Column(nullable = false, length = 100)
    private String valle;

    @Column(name = "campania_cerrada", nullable = false)
    private Boolean campaniaCerrada;

    @Column(name = "dosis_maxima_por_hectarea", nullable = false)
    private Double dosisMaximaPorHectarea;

    @PrePersist
    public void prePersist() {
        if (campaniaCerrada == null) {
            campaniaCerrada = false;
        }
    }
}
