
package cl.infrawatch.backend.controller;

import cl.infrawatch.backend.model.Metrica;
import cl.infrawatch.backend.service.MetricaService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/metricas")
public class MetricaController {

    private final MetricaService metricaService;

    public MetricaController(MetricaService metricaService) {
        this.metricaService = metricaService;
    }

    @GetMapping
    public List<Metrica> listar() {
        return metricaService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Metrica> buscarPorId(@PathVariable Long id) {

        Optional<Metrica> metrica = metricaService.buscarPorId(id);

        if (metrica.isPresent()) {
            return ResponseEntity.ok(metrica.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Metrica metrica) {

        try {

            Optional<Metrica> nuevaMetrica = metricaService.crear(metrica);

            if (nuevaMetrica.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("El servidor indicado no existe.");
            }

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(nuevaMetrica.get());

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}
