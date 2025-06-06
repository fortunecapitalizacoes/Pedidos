package br.com.loja.pedidos.infra.async;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import br.com.loja.pedidos.application.dtos.EventoPagamentoDTO;
import br.com.loja.pedidos.domain.DomainServices;
import br.com.loja.pedidos.infra.configurations.RabbitConfig;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class PagamentoListener {

	private final DomainServices domainServices;
	
    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void receberPedido(EventoPagamentoDTO mensagem) {
        System.out.println("Mensagem recebida no tópico 'topico-pedidos': " + mensagem);
        domainServices.atualizarStatus(mensagem.getIdPedido(), mensagem.getStatus());
        
    }
}
