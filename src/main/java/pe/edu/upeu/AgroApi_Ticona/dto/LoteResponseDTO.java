package pe.edu.upeu.AgroApi_Ticona.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoteResponseDTO {

    private Long id;
    private String codigo;
    private String nombre;
    private Double hectareas;
    private String cultivo;
    private String valle;
    private Boolean campaniaCerrada;
    private Double dosisMaximaPorHectarea;
}
