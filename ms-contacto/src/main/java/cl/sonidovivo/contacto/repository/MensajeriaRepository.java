package cl.sonidovivo.contacto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.sonidovivo.contacto.model.Mensajeria;


public interface MensajeriaRepository extends JpaRepository<Mensajeria, Long>{
    
}
