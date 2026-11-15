
package cl.infrawatch.backend.service;

import cl.infrawatch.backend.model.Metrica;
import cl.infrawatch.backend.model.Servidor;
import cl.infrawatch.backend.repository.MetricaRepository;
import cl.infrawatch.backend.repository.ServidorRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MetricaService {

    private final MetricaRepository metricaRepository;
    private final ServidorRepository servidorRepository;
    private final AlertaService alertaService;

    public MetricaService(
            MetricaRepository metricaRepository,
            ServidorRepository servidorRepository,
            AlertaService alertaService) {

        this.metricaRepository = metricaRepository;
        this.servidorRepository = servidorRepository;
        this.alertaService = alertaService;
    }

    public List<Metrica> listar() {
        return metricaRepository.findAll();
    }

    public Optional<Metrica> buscarPorId(Long id) {
        return metricaRepository.findById(id);
    }

    public List<Metrica> listarPorServidor(Long servidorId) {
        return metricaRepository
                .findByServidorIdOrderByFechaRegistroDesc(servidorId);
    }

    @Transactional
    public Optional<Metrica> crear(Metrica metrica) {

        if (metrica == null ||
                metrica.getServidor() == null ||
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

        Metrica metricaGuardada = metricaRepository.save(metrica);

        alertaService.evaluarMetrica(metricaGuardada);

        return Optional.of(metricaGuardada);
    }
}
