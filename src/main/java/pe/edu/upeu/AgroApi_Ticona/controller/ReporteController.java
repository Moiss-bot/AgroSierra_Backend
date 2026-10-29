
package pe.edu.upeu.AgroApi_Ticona.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upeu.AgroApi_Ticona.dto.reporte.LaboresPorTipoDTO;
import pe.edu.upeu.AgroApi_Ticona.dto.reporte.LaboresPorValleDTO;
import pe.edu.upeu.AgroApi_Ticona.service.service.ReporteService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    /*
     * Cantidad de lotes y labores agrupadas por valle.
     * El rango de fechas es opcional.
     */
    @GetMapping("/labores-por-valle")
    public ResponseEntity<List<LaboresPorValleDTO>> laboresPorValle(

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate desde,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate hasta) {

        return ResponseEntity.ok(
                reporteService.laboresPorValle(desde, hasta)
        );
    }

    /*
     * Cantidad de labores y cantidad total agrupadas por tipo.
     */
    @GetMapping("/labores-por-tipo")
    public ResponseEntity<List<LaboresPorTipoDTO>> laboresPorTipo(

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate desde,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate hasta) {

        return ResponseEntity.ok(
                reporteService.laboresPorTipo(desde, hasta)
        );
    }
}
