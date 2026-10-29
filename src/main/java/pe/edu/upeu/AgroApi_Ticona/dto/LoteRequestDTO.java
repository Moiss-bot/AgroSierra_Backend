package pe.edu.upeu.AgroApi_Ticona.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoteRequestDTO {

    @NotBlank(message = "El código del lote es obligatorio")
    @Size(
            min = 3,
            max = 30,
            message = "El código debe tener entre 3 y 30 caracteres"
    )
    private String codigo;

    @NotBlank(message = "El nombre del lote es obligatorio")
    @Size(
            min = 3,
            max = 100,
            message = "El nombre debe tener entre 3 y 100 caracteres"
    )
    private String nombre;

    @NotNull(message = "Las hectáreas son obligatorias")
    @Positive(message = "Las hectáreas deben ser mayores que cero")
    private Double hectareas;

    @NotBlank(message = "El cultivo es obligatorio")
    @Size(
            max = 100,
            message = "El cultivo no debe superar los 100 caracteres"
    )
    private String cultivo;

    @NotBlank(message = "El valle es obligatorio")
    @Size(
            max = 100,
            message = "El valle no debe superar los 100 caracteres"
    )
    private String valle;

    @NotNull(message = "El estado de la campaña es obligatorio")
    private Boolean campaniaCerrada;

    @NotNull(message = "La dosis máxima es obligatoria")
    @Positive(message = "La dosis máxima debe ser mayor que cero")
    private Double dosisMaximaPorHectarea;
}
