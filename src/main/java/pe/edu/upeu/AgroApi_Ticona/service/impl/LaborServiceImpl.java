
package pe.edu.upeu.AgroApi_Ticona.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.upeu.AgroApi_Ticona.dto.LaborRequestDTO;
import pe.edu.upeu.AgroApi_Ticona.dto.LaborResponseDTO;
import pe.edu.upeu.AgroApi_Ticona.dto.PaginaResponseDTO;
import pe.edu.upeu.AgroApi_Ticona.entity.Labor;
import pe.edu.upeu.AgroApi_Ticona.entity.Lote;
import pe.edu.upeu.AgroApi_Ticona.entity.Usuario;
import pe.edu.upeu.AgroApi_Ticona.enums.TipoLabor;
import pe.edu.upeu.AgroApi_Ticona.exception.RecursoNoEncontradoException;
import pe.edu.upeu.AgroApi_Ticona.exception.ReglaNegocioException;
import pe.edu.upeu.AgroApi_Ticona.exception.ValidacionNegocioException;
import pe.edu.upeu.AgroApi_Ticona.mapper.LaborMapper;
import pe.edu.upeu.AgroApi_Ticona.repository.LaborRepository;
import pe.edu.upeu.AgroApi_Ticona.repository.LoteRepository;
import pe.edu.upeu.AgroApi_Ticona.repository.UsuarioRepository;
import pe.edu.upeu.AgroApi_Ticona.service.service.LaborService;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class LaborServiceImpl implements LaborService {

    private static final Logger log =
            LoggerFactory.getLogger(LaborServiceImpl.class);

    private final LaborRepository laborRepository;
    private final LoteRepository loteRepository;
    private final UsuarioRepository usuarioRepository;
    private final LaborMapper laborMapper;

    public LaborServiceImpl(
            LaborRepository laborRepository,
            LoteRepository loteRepository,
            UsuarioRepository usuarioRepository,
            LaborMapper laborMapper) {

        this.laborRepository = laborRepository;
        this.loteRepository = loteRepository;
        this.usuarioRepository = usuarioRepository;
        this.laborMapper = laborMapper;
    }

    /*
     * Registrar una labor agricola.
     * No se permite registrar en campañas cerradas.
     * Las fumigaciones deben respetar la dosis maxima.
     */
    @Override
    @Transactional
    public LaborResponseDTO crear(
            LaborRequestDTO request,
            String username) {

        log.info("Registrando labor tipo={} loteId={} usuario={}",
                request.getTipo(),
                request.getLoteId(),
                username);

        Usuario usuario = buscarUsuario(username);
        Lote lote = buscarLote(request.getLoteId());

        validarCampania(lote);
        validarFumigacion(request, lote);

        Labor labor = laborMapper.toLaborEntity(
                request,
                lote,
                usuario
        );

        Labor guardada = laborRepository.save(labor);

        log.info("Labor registrada id={} tipo={} loteId={}",
                guardada.getId(),
                guardada.getTipo(),
                lote.getId());

        return laborMapper.toLaborResponse(guardada);
    }

    /*
     * Listar las labores de un lote con paginacion.
     * El repositorio ordena por fecha descendente.
     */
    @Override
    @Transactional(readOnly = true)
    public PaginaResponseDTO<LaborResponseDTO> listarPorLote(
            Long loteId,
            int pagina,
            int tamanio) {

        buscarLote(loteId);

        validarPaginacion(pagina, tamanio);

        Pageable pageable = PageRequest.of(pagina, tamanio);

        Page<Labor> resultado =
                laborRepository.findByLoteId(loteId, pageable);

        return PaginaResponseDTO.de(
                resultado.map(laborMapper::toLaborResponse)
        );
    }

    /*
     * Obtener una labor por su identificador.
     */
    @Override
    @Transactional(readOnly = true)
    public LaborResponseDTO obtenerPorId(Long id) {

        return laborMapper.toLaborResponse(
                buscarLabor(id)
        );
    }

    /*
     * Actualizar una labor.
     *
     * ADMIN y JEFE_CAMPO pueden editar cualquier labor.
     * TECNICO solo puede editar sus propias labores
     * dentro de las primeras 24 horas.
     *
     * No se permite modificar campañas cerradas.
     */
    @Override
    @Transactional
    public LaborResponseDTO actualizar(
            Long id,
            LaborRequestDTO request,
            String username) {

        Labor labor = buscarLabor(id);
        Usuario usuario = buscarUsuario(username);

        validarPermisoEdicion(labor, usuario);

        // Verificar que la campaña original esté abierta
        validarCampania(labor.getLote());

        // Verificar el lote al que se asignará la labor
        Lote nuevoLote = buscarLote(request.getLoteId());

        validarCampania(nuevoLote);
        validarFumigacion(request, nuevoLote);

        laborMapper.actualizarLabor(
                labor,
                request,
                nuevoLote
        );

        Labor actualizada = laborRepository.save(labor);

        log.info("Labor actualizada id={} usuario={}",
                id, username);

        return laborMapper.toLaborResponse(actualizada);
    }

    /*
     * Eliminar una labor.
     * Solo ADMIN y JEFE_CAMPO.
     *
     * No se permite eliminar labores de campañas cerradas.
     */
    @Override
    @Transactional
    public void eliminar(Long id, String username) {

        Labor labor = buscarLabor(id);
        Usuario usuario = buscarUsuario(username);

        if (!tieneRol(usuario, "ADMIN")
                && !tieneRol(usuario, "JEFE_CAMPO")) {

            throw new AccessDeniedException(
                    "Solo ADMIN o JEFE_CAMPO pueden eliminar labores"
            );
        }

        validarCampania(labor.getLote());

        laborRepository.delete(labor);

        log.info("Labor eliminada id={} usuario={}",
                id, username);
    }

    /*
     * Validar que la campaña del lote esté abierta.
     * HTTP 409 Conflict.
     */
    private void validarCampania(Lote lote) {

        if (Boolean.TRUE.equals(lote.getCampaniaCerrada())) {

            throw new ReglaNegocioException(
                    "No se pueden registrar, modificar ni eliminar "
                            + "labores porque la campaña está cerrada"
            );
        }
    }

    /*
     * Validar fumigaciones.
     *
     * Cantidad maxima permitida:
     * hectareas * dosisMaximaPorHectarea
     *
     * Los errores de validacion devuelven HTTP 400.
     */
    private void validarFumigacion(
            LaborRequestDTO request,
            Lote lote) {

        if (request.getTipo() != TipoLabor.FUMIGACION) {
            return;
        }

        if (request.getCantidad() == null
                || !Double.isFinite(request.getCantidad())
                || request.getCantidad() <= 0) {

            throw new ValidacionNegocioException(
                    "La cantidad de fumigación debe ser mayor a cero",
                    Map.of(
                            "cantidad",
                            "Debe ser un número válido mayor a cero"
                    )
            );
        }

        if (lote.getHectareas() == null
                || lote.getDosisMaximaPorHectarea() == null
                || !Double.isFinite(lote.getHectareas())
                || !Double.isFinite(lote.getDosisMaximaPorHectarea())
                || lote.getHectareas() <= 0
                || lote.getDosisMaximaPorHectarea() <= 0) {

            throw new ValidacionNegocioException(
                    "El lote no tiene configurada una dosis máxima válida",
                    Map.of(
                            "loteId",
                            "El lote debe tener hectáreas y dosis máxima válidas"
                    )
            );
        }

        double dosisMaxima =
                lote.getHectareas()
                        * lote.getDosisMaximaPorHectarea();

        if (request.getCantidad() > dosisMaxima) {

            throw new ValidacionNegocioException(
                    "La cantidad de fumigación supera la dosis máxima "
                            + "permitida para el lote: " + dosisMaxima,
                    Map.of(
                            "cantidad",
                            "No puede superar " + dosisMaxima
                    )
            );
        }
    }

    /*
     * Validar permisos de edicion.
     *
     * ADMIN y JEFE_CAMPO pueden editar cualquier labor.
     * TECNICO solo puede editar labores propias
     * durante las primeras 24 horas.
     *
     * HTTP 403 Forbidden.
     */
    private void validarPermisoEdicion(
            Labor labor,
            Usuario usuario) {

        // ADMIN tiene todos los permisos
        if (tieneRol(usuario, "ADMIN")) {
            return;
        }

        // JEFE_CAMPO puede editar cualquier labor
        if (tieneRol(usuario, "JEFE_CAMPO")) {
            return;
        }

        // Solo TECNICO puede continuar con las restricciones
        if (!tieneRol(usuario, "TECNICO")) {

            throw new AccessDeniedException(
                    "El usuario no tiene permiso para editar labores"
            );
        }

        // El tecnico debe ser el creador de la labor
        if (labor.getUsuario() == null
                || !labor.getUsuario().getId().equals(usuario.getId())) {

            throw new AccessDeniedException(
                    "El técnico solo puede editar sus propias labores"
            );
        }

        // La labor debe tener fecha de creacion
        if (labor.getFechaCreacion() == null) {

            throw new AccessDeniedException(
                    "No se puede verificar el plazo de edición de la labor"
            );
        }

        // Comprobar el plazo de 24 horas
        LocalDateTime fechaLimite =
                labor.getFechaCreacion().plusHours(24);

        if (LocalDateTime.now().isAfter(fechaLimite)) {

            throw new AccessDeniedException(
                    "El plazo de 24 horas para editar la labor ha vencido"
            );
        }
    }

    /*
     * Comprobar si un usuario tiene un rol determinado.
     */
    private boolean tieneRol(
            Usuario usuario,
            String nombreRol) {

        return usuario.getRoles()
                .stream()
                .anyMatch(rol ->
                        rol.getNombre().equalsIgnoreCase(nombreRol));
    }

    /*
     * Validar los parametros de paginacion.
     */
    private void validarPaginacion(
            int pagina,
            int tamanio) {

        if (pagina < 0) {

            throw new ValidacionNegocioException(
                    "El número de página no puede ser negativo",
                    Map.of(
                            "pagina",
                            "Debe ser mayor o igual a cero"
                    )
            );
        }

        if (tamanio <= 0) {

            throw new ValidacionNegocioException(
                    "El tamaño de página debe ser mayor a cero",
                    Map.of(
                            "tamanio",
                            "Debe ser mayor a cero"
                    )
            );
        }
    }

    /*
     * Buscar labor por identificador.
     * HTTP 404 si no existe.
     */
    private Labor buscarLabor(Long id) {

        return laborRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Labor no encontrada con id: " + id
                        ));
    }

    /*
     * Buscar lote por identificador.
     * HTTP 404 si no existe.
     */
    private Lote buscarLote(Long id) {

        return loteRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Lote no encontrado con id: " + id
                        ));
    }

    /*
     * Buscar usuario autenticado.
     */
    private Usuario buscarUsuario(String username) {

        return usuarioRepository
                .findByUsernameIgnoreCase(username)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Usuario no encontrado: " + username
                        ));
    }
}
