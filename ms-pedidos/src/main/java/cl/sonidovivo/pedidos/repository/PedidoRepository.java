package cl.sonidovivo.pedidos.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.sonidovivo.pedidos.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByCliente(String cliente);

    List<Pedido> findByFechaBetween(LocalDate desde, LocalDate hasta);
}
