package br.com.loja.pedidos.infra.repositories;

import br.com.loja.pedidos.domain.entities.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, Long> {
    // Aqui você pode adicionar métodos personalizados se necessário
}