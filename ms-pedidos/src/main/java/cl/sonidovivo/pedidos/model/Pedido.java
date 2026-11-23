package cl.sonidovivo.pedidos.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String cliente;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private Integer total;

    @Column(nullable = false, length = 30)
    private String entrega;

    @Column(nullable = false, length = 20)
    private String estado = "en preparacion";

    @Column(length = 150)
    private String correo;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoItem> items = new ArrayList<>();

    @PrePersist
    protected void asignarFecha() {
        if (this.fecha == null) {
            this.fecha = LocalDate.now();
        }
    }
}
