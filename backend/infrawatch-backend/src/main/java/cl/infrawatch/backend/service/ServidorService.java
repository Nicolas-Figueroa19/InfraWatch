
package cl.infrawatch.backend.service;

import cl.infrawatch.backend.model.Servidor;
import cl.infrawatch.backend.repository.ServidorRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServidorService {

    private final ServidorRepository servidorRepository;

    public ServidorService(ServidorRepository servidorRepository) {
        this.servidorRepository = servidorRepository;
    }

    public List<Servidor> listar() {
        return servidorRepository.findAll();
    }

    public Optional<Servidor> buscarPorId(Long id) {
        return servidorRepository.findById(id);
    }

    public Servidor crear(Servidor servidor) {
        return servidorRepository.save(servidor);
    }

    public Optional<Servidor> actualizar(Long id, Servidor datos) {

        return servidorRepository.findById(id)
                .map(servidor -> {

                    servidor.setNombre(datos.getNombre());
                    servidor.setIp(datos.getIp());
                    servidor.setSistemaOperativo(datos.getSistemaOperativo());
                    servidor.setDescripcion(datos.getDescripcion());

                    return servidorRepository.save(servidor);
                });
    }

    public boolean eliminar(Long id) {

        if (!servidorRepository.existsById(id)) {
            return false;
        }

        servidorRepository.deleteById(id);
        return true;
    }
}
