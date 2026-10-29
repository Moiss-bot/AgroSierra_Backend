
package pe.edu.upeu.AgroApi_Ticona.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.AgroApi_Ticona.dto.LoteRequestDTO;
import pe.edu.upeu.AgroApi_Ticona.dto.LoteResponseDTO;
import pe.edu.upeu.AgroApi_Ticona.entity.Lote;

@Component
public class LoteMapper {

    public LoteResponseDTO toLoteResponse(Lote lote) {
        return new LoteResponseDTO(
                lote.getId(),
                lote.getCodigo(),
                lote.getNombre(),
                lote.getHectareas(),
                lote.getCultivo(),
                lote.getValle(),
                lote.getCampaniaCerrada(),
                lote.getDosisMaximaPorHectarea()
        );
    }

    public Lote toLoteEntity(LoteRequestDTO dto) {
        Lote lote = new Lote();

        lote.setCodigo(dto.getCodigo());
        lote.setNombre(dto.getNombre());
        lote.setHectareas(dto.getHectareas());
        lote.setCultivo(dto.getCultivo());
        lote.setValle(dto.getValle());
        lote.setCampaniaCerrada(false);
        lote.setDosisMaximaPorHectarea(
                dto.getDosisMaximaPorHectarea()
        );

        return lote;
    }

    public void actualizarLote(
            Lote lote,
            LoteRequestDTO dto
    ) {
        lote.setCodigo(dto.getCodigo());
        lote.setNombre(dto.getNombre());
        lote.setHectareas(dto.getHectareas());
        lote.setCultivo(dto.getCultivo());
        lote.setValle(dto.getValle());
        lote.setDosisMaximaPorHectarea(
                dto.getDosisMaximaPorHectarea()
        );
    }
}
