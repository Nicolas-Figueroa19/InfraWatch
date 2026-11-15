
package cl.infrawatch.backend.controller;

import cl.infrawatch.backend.model.Alerta;
import cl.infrawatch.backend.service.AlertaService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alertas")
@CrossOrigin(origins = "http://localhost:4200")
public class AlertaController {

    private final AlertaService alertaService;

    public AlertaController(AlertaService alertaService) {
        this.alertaService = alertaService;
    }

    @GetMapping
    public List<Alerta> listarTodas() {
        return alertaService.listarTodas();
    }

    @GetMapping("/activas")
    public List<Alerta> listarActivas() {
        return alertaService.listarActivas();
    }

    @GetMapping("/servidor/{servidorId}")
    public List<Alerta> listarPorServidor(
            @PathVariable Long servidorId) {

        return alertaService.listarPorServidor(servidorId);
    }
}
