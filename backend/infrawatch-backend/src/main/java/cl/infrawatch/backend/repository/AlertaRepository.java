
package cl.infrawatch.backend.repository;

import cl.infrawatch.backend.model.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {

    List<Alerta> findByServidorIdOrderByFechaCreacionDesc(Long servidorId);

    List<Alerta> findByActivaTrueOrderByFechaCreacionDesc();

    Optional<Alerta> findByServidorIdAndRecursoAndActivaTrue(
            Long servidorId,
            String recurso
    );
}
