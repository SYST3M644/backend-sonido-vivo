package cl.sonidovivo.usuarios.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 150, unique = true)
    private String correo;

    @Column(nullable = false, length = 100)
    private String clave;

    @Column(nullable = false, length = 20)
    private String rol;

    @Column(nullable = false)
    private Boolean activo = true;
}
