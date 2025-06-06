package br.com.loja.pedidos.domain;

import br.com.loja.pedidos.domain.entities.PedidoEntity;
import br.com.loja.pedidos.infra.repositories.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável por conter as regras de negócio relacionadas aos pedidos.
 * Atua como camada intermediária entre os controladores e o repositório de dados.
 * 
 * Esta classe fornece funcionalidades para:
 * - Criar novos pedidos
 * - Listar todos os pedidos
 * - Buscar pedidos por ID
 * - Atualizar o status de pedidos existentes
 * 
 * Exceções são lançadas para sinalizar situações onde o pedido não é encontrado.
 */
@Service
public class DomainServices {

    private final PedidoRepository pedidoRepository;

    /**
     * Construtor que injeta o repositório de pedidos.
     *
     * @param pedidoRepository Instância do repositório que acessa a base de dados.
     */
    @Autowired
    public DomainServices(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    /**
     * Cria e persiste um novo pedido no banco de dados.
     *
     * @param pedido Objeto {@link PedidoEntity} contendo as informações do pedido a ser salvo.
     * @return O objeto {@link PedidoEntity} persistido com ID gerado.
     */
    public PedidoEntity criarPedido(PedidoEntity pedido) {
        return pedidoRepository.save(pedido);
    }

    /**
     * Retorna a lista de todos os pedidos cadastrados no banco.
     *
     * @return Lista de {@link PedidoEntity} contendo todos os pedidos encontrados.
     */
    public List<PedidoEntity> listarPedidos() {
        return pedidoRepository.findAll();
    }

    /**
     * Busca um pedido específico com base no seu ID.
     *
     * @param id Identificador único do pedido.
     * @return Objeto {@link PedidoEntity} correspondente ao ID informado.
     * @throws EntityNotFoundException Caso o pedido não seja encontrado no banco.
     */
    public PedidoEntity buscaPedidoPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com ID: " + id));
    }

    /**
     * Atualiza o status de um pedido previamente cadastrado.
     *
     * @param idPedido   Identificador único do pedido a ser atualizado.
     * @param novoStatus Novo status que será atribuído ao pedido (ex: "APROVADO", "CANCELADO", etc.).
     * @return Objeto {@link PedidoEntity} com o status atualizado.
     * @throws RuntimeException Caso o pedido com o ID informado não seja encontrado.
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
