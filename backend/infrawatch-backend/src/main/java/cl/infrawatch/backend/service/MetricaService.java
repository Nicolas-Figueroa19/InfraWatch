
package cl.infrawatch.backend.service;

import cl.infrawatch.backend.model.Metrica;
import cl.infrawatch.backend.model.Servidor;
import cl.infrawatch.backend.repository.MetricaRepository;
import cl.infrawatch.backend.repository.ServidorRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MetricaService {

    private final MetricaRepository metricaRepository;
    private final ServidorRepository servidorRepository;

    public MetricaService(
            MetricaRepository metricaRepository,
            ServidorRepository servidorRepository) {

        this.metricaRepository = metricaRepository;
        this.servidorRepository = servidorRepository;
    }

    public List<Metrica> listar() {
        return metricaRepository.findAll();
    }

    public Optional<Metrica> buscarPorId(Long id) {
        return metricaRepository.findById(id);
    }

    public Optional<Metrica> crear(Metrica metrica) {

        if (metrica.getServidor() == null ||
                metrica.getServidor().getId() == null) {

            throw new IllegalArgumentException(
                    "Debes indicar el ID del servidor."
            );
        }

        Optional<Servidor> servidor = servidorRepository
                .findById(metrica.getServidor().getId());

        if (servidor.isEmpty()) {
            return Optional.empty();
        }

        metrica.setServidor(servidor.get());

        return Optional.of(metricaRepository.save(metrica));
    }
}
