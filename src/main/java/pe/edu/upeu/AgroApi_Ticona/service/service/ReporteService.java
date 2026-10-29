package pe.edu.upeu.AgroApi_Ticona.service.service;

import pe.edu.upeu.AgroApi_Ticona.dto.reporte.LaboresPorTipoDTO;
import pe.edu.upeu.AgroApi_Ticona.dto.reporte.LaboresPorValleDTO;

import java.time.LocalDate;
import java.util.List;

public interface ReporteService {

    List<LaboresPorValleDTO> laboresPorValle(
            LocalDate desde,
            LocalDate hasta
    );

    List<LaboresPorTipoDTO> laboresPorTipo(
            LocalDate desde,
            LocalDate hasta
    );
}
