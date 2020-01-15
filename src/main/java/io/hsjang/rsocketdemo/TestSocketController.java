package io.hsjang.rsocketdemo;

import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.Delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import reactor.core.publisher.Flux;
import reactor.rabbitmq.Receiver;

@Controller
public class TestSocketController {

    static int i=0;

    @Autowired
    TestService service;

    @Autowired
    Flux<Delivery> gate;

    @Autowired
    Receiver receiver;

    @Autowired
    RabbitMQConfig rabbitMQConfig;

    Map<String, Flux<Delivery>> pool = new HashMap<String, Flux<Delivery>>();

    @MessageMapping("datas.mq")
    public Flux<Delivery> getDatas(Map<String,Object> data) {
        
        //getQueue((String)data.get("page"));
        return getQueue((String)data.get("page")).mergeWith(gate); 
    }

    public Flux<Delivery> getQueue(String page){
        if(!pool.containsKey(page)){
            String qName = rabbitMQConfig.createQueueNBindGateExchange("page."+page);
            Flux<Delivery> pageFlux = receiver.consumeNoAck(qName).publish().autoConnect();
            pool.put(page, pageFlux);
        }
        return pool.get(page);
    }
}