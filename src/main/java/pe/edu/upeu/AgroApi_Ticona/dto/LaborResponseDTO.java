package pe.edu.upeu.AgroApi_Ticona.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.upeu.AgroApi_Ticona.enums.TipoLabor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LaborResponseDTO {

    private Long id;
    private Long loteId;
    private TipoLabor tipo;
    private LocalDate fecha;
    private String responsable;
    private Double cantidad;
    private String observacion;
    private Boolean pendiente;
}
