package pe.edu.upeu.AgroApi_Ticona.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.AgroApi_Ticona.dto.LoteRequestDTO;
import pe.edu.upeu.AgroApi_Ticona.dto.LoteResponseDTO;
import pe.edu.upeu.AgroApi_Ticona.dto.PaginaResponseDTO;
import pe.edu.upeu.AgroApi_Ticona.entity.Lote;
import pe.edu.upeu.AgroApi_Ticona.enums.TipoLabor;
import pe.edu.upeu.AgroApi_Ticona.exception.RecursoNoEncontradoException;
import pe.edu.upeu.AgroApi_Ticona.exception.ReglaNegocioException;
import pe.edu.upeu.AgroApi_Ticona.mapper.LoteMapper;
import pe.edu.upeu.AgroApi_Ticona.repository.LaborRepository;
import pe.edu.upeu.AgroApi_Ticona.repository.LoteRepository;
import pe.edu.upeu.AgroApi_Ticona.service.service.LoteService;

@Service
public class LoteServiceImpl implements LoteService {

    private static final Logger log =
            LoggerFactory.getLogger(LoteServiceImpl.class);

    private final LoteRepository loteRepository;
    private final LaborRepository laborRepository;
    private final LoteMapper loteMapper;

    public LoteServiceImpl(
            LoteRepository loteRepository,
            LaborRepository laborRepository,
            LoteMapper loteMapper) {
        this.loteRepository = loteRepository;
        this.laborRepository = laborRepository;
        this.loteMapper = loteMapper;
    }

    @Override
    @Transactional
    public LoteResponseDTO crear(LoteRequestDTO request) {

        log.info("Registrando lote codigo={}", request.getCodigo());

        Lote lote = loteMapper.toLoteEntity(request);

        Lote guardado = loteRepository.save(lote);

        log.info("Lote registrado id={} codigo={}",
                guardado.getId(), guardado.getCodigo());

        return loteMapper.toLoteResponse(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginaResponseDTO<LoteResponseDTO> listar(
            int pagina,
            int tamanio,
            String valle,
            Boolean campaniaCerrada) {

        Pageable pageable = PageRequest.of(
                pagina,
                tamanio,
                Sort.by("id").descending()
        );

        Page<Lote> resultado;

        boolean tieneValle = valle != null && !valle.isBlank();

        if (tieneValle && campaniaCerrada != null) {

            resultado = loteRepository
                    .findByValleIgnoreCaseAndCampaniaCerrada(
                            valle.trim(),
                            campaniaCerrada,
                            pageable
                    );

        } else if (tieneValle) {

            resultado = loteRepository.findByValleIgnoreCase(
                    valle.trim(),
                    pageable
            );

        } else if (campaniaCerrada != null) {

            resultado = loteRepository.findByCampaniaCerrada(
                    campaniaCerrada,
                    pageable
            );

        } else {

            resultado = loteRepository.findAll(pageable);
        }

        return PaginaResponseDTO.de(
                resultado.map(loteMapper::toLoteResponse)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public LoteResponseDTO obtenerPorId(Long id) {
        return loteMapper.toLoteResponse(buscar(id));
    }

    @Override
    @Transactional
    public LoteResponseDTO actualizar(
            Long id,
            LoteRequestDTO request) {

        Lote lote = buscar(id);

        if (Boolean.TRUE.equals(lote.getCampaniaCerrada())) {
            throw new ReglaNegocioException(
                    "No se puede modificar un lote con campaña cerrada"
            );
        }

        loteMapper.actualizarLote(lote, request);

        Lote actualizado = loteRepository.save(lote);

        log.info("Lote actualizado id={}", id);

        return loteMapper.toLoteResponse(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {

        Lote lote = buscar(id);

        if (laborRepository.existsByLoteIdAndTipo(id, TipoLabor.RIEGO)
                || laborRepository.existsByLoteIdAndTipo(id, TipoLabor.FUMIGACION)
                || laborRepository.existsByLoteIdAndTipo(id, TipoLabor.COSECHA)) {

            throw new ReglaNegocioException(
                    "No se puede eliminar un lote que tiene labores registradas"
            );
        }

        loteRepository.delete(lote);

        log.info("Lote eliminado id={}", id);
    }

    @Override
    @Transactional
    public LoteResponseDTO cerrarCampania(Long id) {

        Lote lote = buscar(id);

        if (Boolean.TRUE.equals(lote.getCampaniaCerrada())) {
            throw new ReglaNegocioException(
                    "La campaña del lote ya está cerrada"
            );
        }

        if (!laborRepository.existsByLoteIdAndTipo(
                id, TipoLabor.COSECHA)) {

            throw new ReglaNegocioException(
                    "No se puede cerrar la campaña porque "
                            + "el lote no tiene una labor de COSECHA"
            );
        }

        lote.setCampaniaCerrada(true);

        Lote actualizado = loteRepository.save(lote);

        log.info("Campaña cerrada para lote id={}", id);

        return loteMapper.toLoteResponse(actualizado);
    }

    private Lote buscar(Long id) {
        return loteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Lote no encontrado con id: " + id
                ));
    }
}
