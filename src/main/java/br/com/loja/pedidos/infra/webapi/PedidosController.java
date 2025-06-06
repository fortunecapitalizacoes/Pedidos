package br.com.loja.pedidos.infra.webapi;

import br.com.loja.pedidos.domain.DomainServices;
import br.com.loja.pedidos.domain.entities.PedidoEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/pedidos")
public class PedidosController {

    private final DomainServices domainServices;

    @Autowired
    public PedidosController(DomainServices domainServices) {
        this.domainServices = domainServices;
    }

    /**
     * Cria um novo pedido.
     *
     * @param pedido Objeto {@link PedidoEntity} contendo os dados do pedido.
     * @return {@link ResponseEntity} com o pedido criado.
     */
    @PostMapping
    public ResponseEntity<PedidoEntity> criarPedido(@RequestBody PedidoEntity pedido) {
        log.info("Recebida requisição para criar pedido: {}", pedido);
        PedidoEntity novoPedido = domainServices.criarPedido(pedido);
        log.info("Pedido criado com sucesso. ID: {}", novoPedido.getId());
        return ResponseEntity.ok(novoPedido);
    }

    /**
     * Retorna todos os pedidos cadastrados.
     *
     * @return Lista de {@link PedidoEntity}.
     */
    @GetMapping
    public ResponseEntity<List<PedidoEntity>> listarPedidos() {
        log.info("Listando todos os pedidos.");
        List<PedidoEntity> pedidos = domainServices.listarPedidos();
        log.info("Total de pedidos encontrados: {}", pedidos.size());
        return ResponseEntity.ok(pedidos);
    }

    /**
     * Atualiza o status de um pedido existente.
     *
     * @param id     ID do pedido a ser atualizado.
     * @param status Novo status do pedido.
     * @return {@link ResponseEntity} com o pedido atualizado, ou 404 se não encontrado.
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<PedidoEntity> atualizarStatus(
            @PathVariable("id") Long id,
            @RequestParam(name = "status") String status) {

        log.info("Atualizando status do pedido com ID {} para '{}'", id, status);
        try {
            PedidoEntity atualizado = domainServices.atualizarStatus(id, status);
            log.info("Status do pedido ID {} atualizado com sucesso.", id);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            log.warn("Falha ao atualizar status do pedido ID {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Busca um pedido pelo seu ID.
     *
     * @param id ID do pedido.
     * @return {@link ResponseEntity} com o pedido encontrado, ou 404 se não encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPedidoPorId(@PathVariable("id") Long id) {
        log.info("Buscando pedido com ID {}", id);
        try {
            PedidoEntity pedido = domainServices.buscaPedidoPorId(id);
            log.info("Pedido encontrado: {}", pedido);
            return ResponseEntity.ok(pedido);
        } catch (EntityNotFoundException ex) {
            log.warn("Pedido não encontrado com ID {}: {}", id, ex.getMessage());
            return ResponseEntity
                    .status(404)
                    .body("Pedido não encontrado com ID: " + id);
        }
    }
}
