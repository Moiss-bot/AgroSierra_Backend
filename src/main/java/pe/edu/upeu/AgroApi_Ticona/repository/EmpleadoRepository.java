package pe.edu.upeu.AgroApi_Ticona.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.AgroApi_Ticona.entity.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
}
