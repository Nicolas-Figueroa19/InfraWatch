
package cl.infrawatch.backend.service;

import cl.infrawatch.backend.model.Alerta;
import cl.infrawatch.backend.model.Metrica;
import cl.infrawatch.backend.repository.AlertaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AlertaService {

    private static final double UMBRAL_ADVERTENCIA = 80.0;
    private static final double UMBRAL_CRITICO = 90.0;

    private final AlertaRepository alertaRepository;

    public AlertaService(AlertaRepository alertaRepository) {
        this.alertaRepository = alertaRepository;
    }

    public List<Alerta> listarTodas() {
        return alertaRepository.findAll();
    }

    public List<Alerta> listarActivas() {
        return alertaRepository.findByActivaTrueOrderByFechaCreacionDesc();
    }

    public List<Alerta> listarPorServidor(Long servidorId) {
        return alertaRepository.findByServidorIdOrderByFechaCreacionDesc(
                servidorId
        );
    }

    @Transactional
    public void evaluarMetrica(Metrica metrica) {
        if (metrica == null || metrica.getServidor() == null
                || metrica.getServidor().getId() == null) {
            throw new IllegalArgumentException(
                    "La métrica debe tener un servidor válido"
            );
        }

        evaluarRecurso(
                metrica,
                "CPU",
                metrica.getUsoCpu()
        );

        evaluarRecurso(
                metrica,
                "RAM",
                metrica.getUsoMemoria()
        );

        evaluarRecurso(
                metrica,
                "DISCO",
                metrica.getUsoDisco()
        );
    }

    private void evaluarRecurso(
            Metrica metrica,
            String recurso,
            Double valor
    ) {
        if (valor == null || !Double.isFinite(valor)
                || valor < 0 || valor > 100) {
            throw new IllegalArgumentException(
                    "El porcentaje de " + recurso + " debe estar entre 0 y 100"
            );
        }

        Long servidorId = metrica.getServidor().getId();

        Optional<Alerta> alertaExistente =
                alertaRepository.findByServidorIdAndRecursoAndActivaTrue(
                        servidorId,
                        recurso
                );

        if (valor < UMBRAL_ADVERTENCIA) {
            if (alertaExistente.isPresent()) {
                Alerta alerta = alertaExistente.get();
                alerta.setActiva(false);
                alerta.setFechaResolucion(LocalDateTime.now());
                alertaRepository.save(alerta);
            }

            return;
        }

        String nivel = valor >= UMBRAL_CRITICO
                ? "CRITICO"
                : "ADVERTENCIA";

        String mensaje = recurso + " del servidor "
                + metrica.getServidor().getNombre()
                + " alcanzó un " + valor + "% de utilización";

        if (alertaExistente.isPresent()) {
            Alerta alerta = alertaExistente.get();

            if (!nivel.equals(alerta.getNivel())) {
                alerta.setNivel(nivel);
                alerta.setValor(valor);
                alerta.setMensaje(mensaje);
                alertaRepository.save(alerta);
            }

            return;
        }

        Alerta nuevaAlerta = new Alerta();
        nuevaAlerta.setServidor(metrica.getServidor());
        nuevaAlerta.setRecurso(recurso);
        nuevaAlerta.setNivel(nivel);
        nuevaAlerta.setValor(valor);
        nuevaAlerta.setMensaje(mensaje);
        nuevaAlerta.setActiva(true);

        alertaRepository.save(nuevaAlerta);
    }
}
