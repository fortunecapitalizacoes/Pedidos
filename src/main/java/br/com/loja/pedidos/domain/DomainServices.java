package br.com.loja.pedidos.domain;

import br.com.loja.pedidos.domain.entities.PedidoEntity;
import br.com.loja.pedidos.infra.repositories.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class DomainServices {

    private final PedidoRepository pedidoRepository;

    @Autowired
    public DomainServices(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    /**
     * Cria e persiste um novo pedido no banco.
     */
    public PedidoEntity criarPedido(PedidoEntity pedido) {
        return pedidoRepository.save(pedido);
    }

    /**
     * Lista todos os pedidos existentes.
     */
    public List<PedidoEntity> listarPedidos() {
        return pedidoRepository.findAll();
    }

    /**
     * Busca por pedido por ID.
     */
    public PedidoEntity buscaPedidoPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com ID: " + id));
    }
    
    
    /**
     * Atualiza o status de um pedido existente.
     */
    public PedidoEntity atualizarStatus(Long idPedido, String novoStatus) {
        Optional<PedidoEntity> pedidoOptional = pedidoRepository.findById(idPedido);
        if (pedidoOptional.isPresent()) {
            PedidoEntity pedido = pedidoOptional.get();
            pedido.setStatus(novoStatus);
            return pedidoRepository.save(pedido);
        } else {
            throw new RuntimeException("Pedido com ID " + idPedido + " não encontrado.");
        }
    }
}
