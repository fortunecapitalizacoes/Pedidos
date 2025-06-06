package br.com.loja.pedidos.infra.configurations;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String TOPIC_EXCHANGE_NAME = "ex_topico_pagamento";
    public static final String QUEUE_NAME = "fila_pagamento";
    public static final String ROUTING_KEY = "topico-pagamento";

    @Bean
    Queue queue() {
        return new Queue(QUEUE_NAME, true); // fila durável
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(TOPIC_EXCHANGE_NAME);
    }
    
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        // Associa a fila com a exchange pelo routing key "topico-pedidos"
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }
}
