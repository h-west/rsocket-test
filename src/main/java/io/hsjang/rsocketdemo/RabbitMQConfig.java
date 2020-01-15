package io.hsjang.rsocketdemo;

import java.net.InetAddress;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Delivery;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.RabbitFlux;
import reactor.rabbitmq.Receiver;
import reactor.rabbitmq.ReceiverOptions;
import reactor.rabbitmq.Sender;
import reactor.rabbitmq.SenderOptions;

@Configuration
public class RabbitMQConfig {

    String HOST_NAME = ".hostname";
    TopicExchange exchange = null;
    
    @Autowired
    Mono<Connection> connectionMono;

    @Autowired
    AmqpAdmin amqpAdmin;
	
    @Bean
    Mono<Connection> connectionMono() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        return Mono.fromCallable(() -> connectionFactory.newConnection("reactor-rabbit")).cache();
    }
	
    @Bean
    Sender sender() {
        return RabbitFlux.createSender(new SenderOptions().connectionMono(connectionMono));
    }

    @Bean
    Receiver receiver() {
        return RabbitFlux.createReceiver(new ReceiverOptions().connectionMono(connectionMono));
    }

    
    @Bean
    Flux<Delivery> gate(Receiver receiver) {
        String qName  = createQueueNBindGateExchange("gate");
        return receiver.consumeNoAck(qName).publish().autoConnect();
    }

       
    @PostConstruct
    public void init() {
        try{
            HOST_NAME = "."+InetAddress.getLocalHost().getHostName();
        }catch(Exception e){
            // 
        }

        exchange = new TopicExchange("windvane"); // amq.topic ?
        amqpAdmin.declareExchange(exchange);
    }
	
    @PreDestroy
    public void close() throws Exception {
        connectionMono.block().close();
    }

    public String createQueueNBindGateExchange(String name) {
        String qName = name + HOST_NAME;
        Queue q = new Queue(qName, false, false, true);
        amqpAdmin.declareQueue(q);
        amqpAdmin.declareBinding(BindingBuilder.bind(q).to(exchange).with(name+".*"));
        return qName;
    }
}