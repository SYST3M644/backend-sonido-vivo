package cl.sonidovivo.contacto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Sonido Vivo - Contacto", version = "1.0", description = "Mensajes que los clientes envían desde el formulario de contacto."))
public class ContactoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContactoApplication.class, args);
	}

}
