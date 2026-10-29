package pe.edu.upeu.AgroApi_Ticona.service.service;

import pe.edu.upeu.AgroApi_Ticona.dto.LoteRequestDTO;
import pe.edu.upeu.AgroApi_Ticona.dto.LoteResponseDTO;
import pe.edu.upeu.AgroApi_Ticona.dto.PaginaResponseDTO;

public interface LoteService {

    LoteResponseDTO crear(LoteRequestDTO request);

    PaginaResponseDTO<LoteResponseDTO> listar(
            int pagina,
            int tamanio,
            String valle,
            Boolean campaniaCerrada
    );

    LoteResponseDTO obtenerPorId(Long id);

    LoteResponseDTO actualizar(
            Long id,
            LoteRequestDTO request
    );

    void eliminar(Long id);

    LoteResponseDTO cerrarCampania(Long id);
}
