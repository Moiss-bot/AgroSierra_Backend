
package pe.edu.upeu.AgroApi_Ticona.dto;

import jakarta.validation.constraints.*;
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
public class LaborRequestDTO {

    @NotNull(message = "El lote es obligatorio")
    @Positive(message = "El identificador del lote debe ser válido")
    private Long loteId;

    @NotNull(message = "El tipo de labor es obligatorio")
    private TipoLabor tipo;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotBlank(message = "El responsable es obligatorio")
    @Size(
            min = 3,
            max = 100,
            message = "El responsable debe tener entre 3 y 100 caracteres"
    )
    private String responsable;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor que cero")
    private Double cantidad;

    @Size(
            max = 500,
            message = "La observación no debe superar los 500 caracteres"
    )
    private String observacion;
}
