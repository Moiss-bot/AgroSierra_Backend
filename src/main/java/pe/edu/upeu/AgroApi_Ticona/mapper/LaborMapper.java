package pe.edu.upeu.AgroApi_Ticona.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.AgroApi_Ticona.dto.LaborRequestDTO;
import pe.edu.upeu.AgroApi_Ticona.dto.LaborResponseDTO;
import pe.edu.upeu.AgroApi_Ticona.entity.Labor;
import pe.edu.upeu.AgroApi_Ticona.entity.Lote;
import pe.edu.upeu.AgroApi_Ticona.entity.Usuario;

@Component
public class LaborMapper {

    public LaborResponseDTO toLaborResponse(Labor labor) {
        return new LaborResponseDTO(
                labor.getId(),
                labor.getLote().getId(),
                labor.getTipo(),
                labor.getFecha(),
                labor.getResponsable(),
                labor.getCantidad(),
                labor.getObservacion(),
                labor.getPendiente()
        );
    }

    public Labor toLaborEntity(
            LaborRequestDTO dto,
            Lote lote,
            Usuario usuario
    ) {
        Labor labor = new Labor();

        labor.setLote(lote);
        labor.setTipo(dto.getTipo());
        labor.setFecha(dto.getFecha());
        labor.setResponsable(dto.getResponsable());
        labor.setCantidad(dto.getCantidad());
        labor.setObservacion(dto.getObservacion());
        labor.setPendiente(false);
        labor.setUsuario(usuario);

        return labor;
    }

    public void actualizarLabor(
            Labor labor,
            LaborRequestDTO dto,
            Lote lote
    ) {
        labor.setLote(lote);
        labor.setTipo(dto.getTipo());
        labor.setFecha(dto.getFecha());
        labor.setResponsable(dto.getResponsable());
        labor.setCantidad(dto.getCantidad());
        labor.setObservacion(dto.getObservacion());
    }
}
