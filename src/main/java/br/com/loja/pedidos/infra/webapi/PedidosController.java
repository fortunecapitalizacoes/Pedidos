package br.com.loja.pedidos.infra.webapi;

import br.com.loja.pedidos.domain.DomainServices;
import br.com.loja.pedidos.domain.entities.PedidoEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável por expor os endpoints relacionados à gestão de pedidos.
 * 
 * Permite criar, listar, atualizar status e buscar pedidos por ID através de chamadas HTTP.
 * Utiliza o serviço de domínio {@link DomainServices} para orquestrar as operações de negócio.
 */
@Slf4j
@RestController
@RequestMapping("/api/pedidos")
public class PedidosController {

    private final DomainServices domainServices;

    /**
     * Construtor com injeção de dependência do serviço de domínio.
     *
     * @param domainServices Serviço de domínio que contém a lógica de negócios para pedidos.
     */
    @Autowired
    public PedidosController(DomainServices domainServices) {
        this.domainServices = domainServices;
    }

    /**
     * Endpoint para criação de um novo pedido.
     *
     * @param pedido Objeto {@link PedidoEntity} enviado no corpo da requisição com os dados do novo pedido.
     * @return {@link ResponseEntity} contendo o pedido criado e status HTTP 200.
     */
    @PostMapping
    public ResponseEntity<PedidoEntity> criarPedido(@RequestBody PedidoEntity pedido) {
        log.info("Recebida requisição para criar pedido: {}", pedido);
        PedidoEntity novoPedido = domainServices.criarPedido(pedido);
        log.info("Pedido criado com sucesso. ID: {}", novoPedido.getId());
        return ResponseEntity.ok(novoPedido);
    }

    /**
     * Endpoint para listar todos os pedidos cadastrados.
     *
     * @return {@link ResponseEntity} com a lista de {@link PedidoEntity} e status HTTP 200.
     */
    @GetMapping
    public ResponseEntity<List<PedidoEntity>> listarPedidos() {
        log.info("Listando todos os pedidos.");
        List<PedidoEntity> pedidos = domainServices.listarPedidos();
        log.info("Total de pedidos encontrados: {}", pedidos.size());
        return ResponseEntity.ok(pedidos);
    }

    /**
     * Endpoint para atualizar o status de um pedido existente.
     *
     * @param id     ID do pedido a ser atualizado.
     * @param status Novo status desejado para o pedido.
     * @return {@link ResponseEntity} com o pedido atualizado ou status 404 se o pedido não for encontrado.
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
     * Endpoint para buscar um pedido pelo seu identificador único.
     *
     * @param id ID do pedido a ser buscado.
     * @return {@link ResponseEntity} com o pedido encontrado ou status 404 e mensagem de erro caso não exista.
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
