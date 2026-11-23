package cl.sonidovivo.contacto.service;

import java.util.List;

import org.springframework.stereotype.Service;

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

   public Mensajeria actualizarEstado(Long id) {
    Mensajeria contacto = repository.findById(id).orElseThrow(() -> new RuntimeException("Contacto no encontrado con id: " + id));
    contacto.setEstado("RECIBIDO");
    return repository.save(contacto);
}

}
