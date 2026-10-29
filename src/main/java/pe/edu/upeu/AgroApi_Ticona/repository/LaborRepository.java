package pe.edu.upeu.AgroApi_Ticona.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.upeu.AgroApi_Ticona.dto.reporte.LaboresPorTipoDTO;
import pe.edu.upeu.AgroApi_Ticona.dto.reporte.LaboresPorValleDTO;
import pe.edu.upeu.AgroApi_Ticona.entity.Labor;
import pe.edu.upeu.AgroApi_Ticona.enums.TipoLabor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LaborRepository
        extends JpaRepository<Labor, Long> {

    /*
     * Búsqueda paginada de labores por lote.
     * Se trae el lote en la misma consulta para evitar
     * consultas adicionales al convertir a DTO.
     */
    @Query(value = """
            select l
            from Labor l
            join fetch l.lote lo
            where lo.id = :loteId
            order by l.fecha desc, l.id desc
            """,
            countQuery = """
            select count(l)
            from Labor l
            where l.lote.id = :loteId
            """)
    Page<Labor> findByLoteId(
            @Param("loteId") Long loteId,
            Pageable pageable);

    /*
     * Verifica si un lote tiene una labor del tipo indicado.
     * Se utiliza para validar el cierre de campaña.
     */
    boolean existsByLoteIdAndTipo(
            Long loteId,
            TipoLabor tipo);

    /*
     * Obtiene una labor junto con su lote y usuario.
     */
    @Override
    @EntityGraph(attributePaths = {"lote", "usuario"})
    Optional<Labor> findById(Long id);

    /*
     * Reporte de labores agrupadas por valle.
     */
    @Query("""
            select new pe.edu.upeu.AgroApi_Ticona.dto.reporte.LaboresPorValleDTO(
                       lo.valle,
                       count(distinct lo.id),
                       count(l.id))
            from Labor l
            join l.lote lo
            where (:desde is null or l.fecha >= :desde)
              and (:hasta is null or l.fecha <= :hasta)
            group by lo.valle
            order by count(l.id) desc
            """)
    List<LaboresPorValleDTO> reporteLaboresPorValle(
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta);

    /*
     * Reporte de labores agrupadas por tipo.
     */
    @Query("""
            select new pe.edu.upeu.AgroApi_Ticona.dto.reporte.LaboresPorTipoDTO(
                       l.tipo,
                       count(l.id),
                       sum(l.cantidad))
            from Labor l
            where (:desde is null or l.fecha >= :desde)
              and (:hasta is null or l.fecha <= :hasta)
            group by l.tipo
            order by count(l.id) desc
            """)
    List<LaboresPorTipoDTO> reporteLaboresPorTipo(
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta);
}
