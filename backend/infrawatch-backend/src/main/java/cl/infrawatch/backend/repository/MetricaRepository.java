
package cl.infrawatch.backend.repository;

import cl.infrawatch.backend.model.Metrica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetricaRepository extends JpaRepository<Metrica, Long> {

    List<Metrica> findByServidorIdOrderByFechaRegistroDesc(Long servidorId);

}
