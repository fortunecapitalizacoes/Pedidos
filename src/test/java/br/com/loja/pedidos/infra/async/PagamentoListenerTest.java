package br.com.loja.pedidos.infra.async;

import br.com.loja.pedidos.application.dtos.EventoPagamentoDTO;
import br.com.loja.pedidos.domain.DomainServices;
import br.com.loja.pedidos.infra.async.PagamentoListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PagamentoListenerTest {

    private DomainServices domainServices;
    private PagamentoListener pagamentoListener;

    @BeforeEach
    void setUp() {
        domainServices = mock(DomainServices.class);
        pagamentoListener = new PagamentoListener(domainServices);
    }

    @Test
    void testReceberPedido_DeveAtualizarStatus() {
        // Arrange
        EventoPagamentoDTO evento = new EventoPagamentoDTO();
        evento.setIdPedido(123L);
        evento.setStatus("PAGO");

        // Act
        pagamentoListener.receberPedido(evento);

        // Assert
        verify(domainServices, times(1)).atualizarStatus(123L, "PAGO");

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> statusCaptor = ArgumentCaptor.forClass(String.class);

        verify(domainServices).atualizarStatus(idCaptor.capture(), statusCaptor.capture());

        assertEquals(123L, idCaptor.getValue());
        assertEquals("PAGO", statusCaptor.getValue());
    }
}
