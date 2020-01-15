package io.hsjang.rsocketdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class TestApiController {
    
    @Autowired
    TestService service;
    
    @Autowired
    Mono<RSocketRequester> requester;

    @GetMapping(value = "/message/{page}", produces = "application/json")
    public Mono<Void> addmq(@PathVariable String page) {
        return service.sendMessage(page, "HEllo");
    }
    
}