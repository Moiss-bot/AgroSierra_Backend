package pe.edu.upeu.AgroApi_Ticona.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.AgroApi_Ticona.dto.reporte.LaboresPorTipoDTO;
import pe.edu.upeu.AgroApi_Ticona.dto.reporte.LaboresPorValleDTO;
import pe.edu.upeu.AgroApi_Ticona.exception.ReglaNegocioException;
import pe.edu.upeu.AgroApi_Ticona.repository.LaborRepository;
import pe.edu.upeu.AgroApi_Ticona.service.service.ReporteService;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReporteServiceImpl implements ReporteService {

    private static final Logger log =
            LoggerFactory.getLogger(ReporteServiceImpl.class);

    private final LaborRepository laborRepository;

    public ReporteServiceImpl(LaborRepository laborRepository) {
        this.laborRepository = laborRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LaboresPorValleDTO> laboresPorValle(
            LocalDate desde,
            LocalDate hasta) {

        long inicio = System.currentTimeMillis();

        log.info("Inicio reporte labores por valle | desde={} | hasta={}",
                desde, hasta);

        validarRango(desde, hasta);

        List<LaboresPorValleDTO> resultado =
                laborRepository.reporteLaboresPorValle(
                        desde,
                        hasta
                );

        log.info("Fin reporte labores por valle | desde={} | hasta={} | "
                        + "filas={} | duracionMs={}",
                desde, hasta,
                resultado.size(),
                System.currentTimeMillis() - inicio);

        return resultado;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LaboresPorTipoDTO> laboresPorTipo(
            LocalDate desde,
            LocalDate hasta) {

        long inicio = System.currentTimeMillis();

        log.info("Inicio reporte labores por tipo | desde={} | hasta={}",
                desde, hasta);

        validarRango(desde, hasta);

        List<LaboresPorTipoDTO> resultado =
                laborRepository.reporteLaboresPorTipo(
                        desde,
                        hasta
                );

        log.info("Fin reporte labores por tipo | desde={} | hasta={} | "
                        + "filas={} | duracionMs={}",
                desde, hasta,
                resultado.size(),
                System.currentTimeMillis() - inicio);

        return resultado;
    }

    private void validarRango(LocalDate desde, LocalDate hasta) {

        if (desde != null
                && hasta != null
                && desde.isAfter(hasta)) {

            throw new ReglaNegocioException(
                    "El rango de fechas es inválido: 'desde' ("
                            + desde
                            + ") es posterior a 'hasta' ("
                            + hasta + ")"
            );
        }
    }
}
