package cl.sonidovivo.contacto.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import cl.sonidovivo.contacto.model.Mensajeria;
import cl.sonidovivo.contacto.repository.MensajeriaRepository;

@Service
public class MensajeriaService {
    private final MensajeriaRepository repository;

    public MensajeriaService(MensajeriaRepository repository){
        this.repository = repository;
    }

    public Mensajeria guardar(Mensajeria contacto){
        contacto.setId(null);
        contacto.setEstado("PENDIENTE");
        return repository.save(contacto);
    }

    public List<Mensajeria> listar(){
        return repository.findAll();
    }

    public Mensajeria buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Mensaje no encontrado con id: " + id));
    }

    public Mensajeria actualizarEstado(Long id) {
        Mensajeria contacto = buscar(id);
        contacto.setEstado("RECIBIDO");
        return repository.save(contacto);
    }

    public void eliminar(Long id) {
        Mensajeria contacto = buscar(id);
        repository.delete(contacto);
    }
}
