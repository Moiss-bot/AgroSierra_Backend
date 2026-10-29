
package pe.edu.upeu.AgroApi_Ticona.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.upeu.AgroApi_Ticona.enums.TipoLabor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "labores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Labor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lote_id", nullable = false)
    private Lote lote;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoLabor tipo;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false, length = 100)
    private String responsable;

    @Column(nullable = false)
    private Double cantidad;

    @Column(length = 500)
    private String observacion;

    @Column(nullable = false)
    private Boolean pendiente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();

        if (pendiente == null) {
            pendiente = false;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaModificacion = LocalDateTime.now();
    }
}
