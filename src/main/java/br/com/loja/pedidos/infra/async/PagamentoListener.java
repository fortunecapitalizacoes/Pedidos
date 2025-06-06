package br.com.loja.pedidos.infra.async;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import br.com.loja.pedidos.application.dtos.EventoPagamentoDTO;
import br.com.loja.pedidos.domain.DomainServices;
import br.com.loja.pedidos.infra.configurations.RabbitConfig;
import lombok.AllArgsConstructor;

/**
 * Componente responsável por escutar mensagens da fila RabbitMQ relacionadas a pagamentos.
 * 
 * Ao receber uma mensagem com os dados do pagamento, o listener atualiza o status do pedido
 * correspondente utilizando o serviço de domínio {@link DomainServices}.
 * 
 * A fila escutada é definida pela constante {@code QUEUE_NAME} da classe {@link RabbitConfig}.
 */
@Component
@AllArgsConstructor
public class PagamentoListener {

    /** Serviço de domínio usado para manipulação de pedidos. */
	private final DomainServices domainServices;

    /**
     * Método que escuta a fila de pagamentos no RabbitMQ.
     * Ao receber uma mensagem contendo o ID do pedido e o novo status, 
     * o método atualiza o status do pedido correspondente.
     *
     * @param mensagem Objeto {@link EventoPagamentoDTO} recebido da fila contendo o ID do pedido e seu novo status.
     */
    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void receberPedido(EventoPagamentoDTO mensagem) {
        System.out.println("Mensagem recebida no tópico 'topico-pedidos': " + mensagem);
        domainServices.atualizarStatus(mensagem.getIdPedido(), mensagem.getStatus());
    }
}
