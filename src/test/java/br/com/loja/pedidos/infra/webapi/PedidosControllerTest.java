package br.com.loja.pedidos.infra.webapi;

import br.com.loja.pedidos.domain.DomainServices;
import br.com.loja.pedidos.domain.entities.PedidoEntity;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidosControllerTest {

    private DomainServices domainServices;
    private PedidosController pedidosController;

    @BeforeEach
    void setUp() {
        domainServices = mock(DomainServices.class);
        pedidosController = new PedidosController(domainServices);
    }

    @Test
    void testCriarPedido() {
        PedidoEntity pedido = new PedidoEntity();
       
        PedidoEntity salvo = new PedidoEntity();
        salvo.setId(1L);
       
        when(domainServices.criarPedido(pedido)).thenReturn(salvo);

        ResponseEntity<PedidoEntity> response = pedidosController.criarPedido(pedido);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(salvo, response.getBody());
    }

    @Test
    void testListarPedidos() {
        PedidoEntity pedido1 = new PedidoEntity();
        pedido1.setId(1L);

        PedidoEntity pedido2 = new PedidoEntity();
        pedido2.setId(2L);

        when(domainServices.listarPedidos()).thenReturn(List.of(pedido1, pedido2));

        ResponseEntity<List<PedidoEntity>> response = pedidosController.listarPedidos();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testAtualizarStatusComSucesso() {
        Long pedidoId = 10L;
        String novoStatus = "ENVIADO";

        PedidoEntity atualizado = new PedidoEntity();
        atualizado.setId(pedidoId);
        atualizado.setStatus(novoStatus);

        when(domainServices.atualizarStatus(pedidoId, novoStatus)).thenReturn(atualizado);

        ResponseEntity<PedidoEntity> response = pedidosController.atualizarStatus(pedidoId, novoStatus);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(novoStatus, response.getBody().getStatus());
    }

    @Test
    void testAtualizarStatusNotFound() {
        Long pedidoId = 99L;
        String status = "CANCELADO";

        when(domainServices.atualizarStatus(pedidoId, status)).thenThrow(new RuntimeException("Pedido não encontrado"));

        ResponseEntity<PedidoEntity> response = pedidosController.atualizarStatus(pedidoId, status);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testBuscarPedidoPorIdComSucesso() {
        Long pedidoId = 1L;
        PedidoEntity pedido = new PedidoEntity();
        pedido.setId(pedidoId);

        when(domainServices.buscaPedidoPorId(pedidoId)).thenReturn(pedido);

        ResponseEntity<?> response = pedidosController.buscarPedidoPorId(pedidoId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(pedido, response.getBody());
    }

    @Test
    void testBuscarPedidoPorIdNaoEncontrado() {
        Long pedidoId = 2L;

        when(domainServices.buscaPedidoPorId(pedidoId)).thenThrow(new EntityNotFoundException("Pedido não encontrado"));

        ResponseEntity<?> response = pedidosController.buscarPedidoPorId(pedidoId);

        assertEquals(404, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Pedido não encontrado"));
    }
}
