
package cl.infrawatch.backend.controller;

import cl.infrawatch.backend.model.Servidor;
import cl.infrawatch.backend.service.ServidorService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servidores")
public class ServidorController {

    private final ServidorService servidorService;

    public ServidorController(ServidorService servidorService) {
        this.servidorService = servidorService;
    }

    @GetMapping
    public List<Servidor> listar() {
        return servidorService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Servidor> buscarPorId(@PathVariable Long id) {

        return servidorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Servidor> crear(@RequestBody Servidor servidor) {

        Servidor nuevoServidor = servidorService.crear(servidor);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevoServidor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Servidor> actualizar(
            @PathVariable Long id,
            @RequestBody Servidor datos) {

        return servidorService.actualizar(id, datos)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        if (!servidorService.eliminar(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
