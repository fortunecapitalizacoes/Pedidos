package br.com.loja.pedidos.domain;

import br.com.loja.pedidos.domain.entities.PedidoEntity;
import br.com.loja.pedidos.infra.repositories.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DomainServiceTest {

    private PedidoRepository pedidoRepository;
    private DomainServices domainServices;

    @BeforeEach
    void setUp() {
        pedidoRepository = mock(PedidoRepository.class);
        domainServices = new DomainServices(pedidoRepository);
    }

    @Test
    void testCriarPedido() {
        PedidoEntity pedido = new PedidoEntity();
        pedido.setStatus("CRIADO");

        when(pedidoRepository.save(pedido)).thenReturn(pedido);

        PedidoEntity result = domainServices.criarPedido(pedido);

        assertNotNull(result);
        assertEquals("CRIADO", result.getStatus());
        verify(pedidoRepository, times(1)).save(pedido);
    }

    @Test
    void testListarPedidos() {
        PedidoEntity p1 = new PedidoEntity();
        PedidoEntity p2 = new PedidoEntity();

        when(pedidoRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<PedidoEntity> pedidos = domainServices.listarPedidos();

        assertEquals(2, pedidos.size());
        verify(pedidoRepository, times(1)).findAll();
    }

    @Test
    void testBuscaPedidoPorId_Existente() {
        PedidoEntity pedido = new PedidoEntity();
        pedido.setId(1L);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        PedidoEntity resultado = domainServices.buscaPedidoPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void testBuscaPedidoPorId_Inexistente() {
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                domainServices.buscaPedidoPorId(99L)
        );

        assertEquals("Pedido não encontrado com ID: 99", exception.getMessage());
    }

    @Test
    void testAtualizarStatus_ComSucesso() {
        PedidoEntity pedido = new PedidoEntity();
        pedido.setId(1L);
        pedido.setStatus("CRIADO");

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(PedidoEntity.class))).thenAnswer(i -> i.getArgument(0));

        PedidoEntity atualizado = domainServices.atualizarStatus(1L, "PAGO");

        assertEquals("PAGO", atualizado.getStatus());

        ArgumentCaptor<PedidoEntity> captor = ArgumentCaptor.forClass(PedidoEntity.class);
        verify(pedidoRepository).save(captor.capture());
        assertEquals("PAGO", captor.getValue().getStatus());
    }

    @Test
    void testAtualizarStatus_PedidoNaoEncontrado() {
        when(pedidoRepository.findById(123L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                domainServices.atualizarStatus(123L, "CANCELADO")
        );

        assertEquals("Pedido com ID 123 não encontrado.", exception.getMessage());
    }
}
