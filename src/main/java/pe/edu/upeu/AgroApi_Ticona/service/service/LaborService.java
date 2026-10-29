
package pe.edu.upeu.AgroApi_Ticona.service.service;

import pe.edu.upeu.AgroApi_Ticona.dto.LaborRequestDTO;
import pe.edu.upeu.AgroApi_Ticona.dto.LaborResponseDTO;
import pe.edu.upeu.AgroApi_Ticona.dto.PaginaResponseDTO;

public interface LaborService {

    LaborResponseDTO crear(
            LaborRequestDTO request,
            String username
    );

    PaginaResponseDTO<LaborResponseDTO> listarPorLote(
            Long loteId,
            int pagina,
            int tamanio
    );

    LaborResponseDTO obtenerPorId(Long id);

    LaborResponseDTO actualizar(
            Long id,
            LaborRequestDTO request,
            String username
    );

    void eliminar(
            Long id,
            String username
    );
}
