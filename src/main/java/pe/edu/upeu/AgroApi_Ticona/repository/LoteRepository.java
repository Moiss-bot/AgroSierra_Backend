package pe.edu.upeu.AgroApi_Ticona.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.AgroApi_Ticona.entity.Lote;

public interface LoteRepository extends JpaRepository<Lote, Long> {

    Page<Lote> findByValleIgnoreCase(
            String valle,
            Pageable pageable
    );

    Page<Lote> findByCampaniaCerrada(
            Boolean campaniaCerrada,
            Pageable pageable
    );

    Page<Lote> findByValleIgnoreCaseAndCampaniaCerrada(
            String valle,
            Boolean campaniaCerrada,
            Pageable pageable
    );
}
