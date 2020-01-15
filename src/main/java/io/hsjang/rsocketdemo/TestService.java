package io.hsjang.rsocketdemo;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.Receiver;
import reactor.rabbitmq.Sender;

@Service
public class TestService implements InitializingBean{
    
    @Autowired
    Receiver receiver;

    @Autowired
    Sender sender;

    

    // @Bean
    // public Publisher<Message<String>> jmsReactiveSource() {
    //     return IntegrationFlows
    //             .from(Jms.messageDrivenChannelAdapter(this.connectionFactory)
    //                     .destination("testQueue"))
    //             .channel(MessageChannels.queue())
    //             .log(LoggingHandler.Level.DEBUG)
    //             .log()
    //             .toReactivePublisher();
    // }

    // public Disposable getDatasFromMQ() {
    //     return RabbitFlux.createReceiver(new ReceiverOptions().connectionSubscriptionScheduler(Schedulers.elastic()))
    //                      .consumeNoAck(RabbitMQConfig.QUEUE,new ConsumeOptions())
    //                      .subscribe(d->{
    //                        // System.out.println(d);
    //                      });    
    // }

    static int  count = 0;
    public Mono<Void> sendMessage(String page, String message) {

        // Flux<OutboundMessageResult> confirmations = sender.sendWithPublishConfirms(Flux.range(1, count++)
        //     .map(i -> new OutboundMessage("amq.topic", "reactive.queue", ("Message_" + i).getBytes())));

        // sender.declare(ExchangeSpecification.exchange("fanout"))
        //     .then()
        //         .doOnError(e -> LOGGER.error("Send failed", e))
        //         .subscribe(r -> {
        //             System.out.println(r);
        //             // if (r.isAck()) {
        //             //     LOGGER.info("Message {} sent successfully", new String(r.getOutboundMessage().getBody()));
        //             //     //latch.countDown();
        //             // }
        //         });


        OutboundMessage msg = new OutboundMessage("windvane","page."+page+".",message.getBytes());
        System.out.println("@@@@@@@@@@@@@@@@"+msg);
        sender.send(Mono.just(msg)).subscribe(System.out::println);
        return Mono.empty();

        // Flux<OutboundMessage> outboundFlux  =
        // Flux.range(1, 10)
        //     .map(i -> new OutboundMessage(
        //         "amq.direct",
        //         "routing.key", ("Message " + i).getBytes()
        //     ));

        //     sender.declare(QueueSpecification.queue("reactive.queue"));
        // return sender.send(outboundFlux);
    }




    // public Mono<Boolean> setDatasq() {
    //     return Mono.just(queue.add(new MData("@@"+(++idx),"$$"+(++idx))));
    // }

    @Override
    public void afterPropertiesSet() throws Exception {
        //getDatasFromMQ();
    }

}