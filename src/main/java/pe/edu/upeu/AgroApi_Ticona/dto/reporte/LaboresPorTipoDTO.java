package pe.edu.upeu.AgroApi_Ticona.dto.reporte;

import pe.edu.upeu.AgroApi_Ticona.enums.TipoLabor;

public record LaboresPorTipoDTO(

        TipoLabor tipo,
        Long cantidadLabores,
        Double cantidadTotal
) {
}
