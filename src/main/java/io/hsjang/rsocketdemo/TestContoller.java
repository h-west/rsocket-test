package io.hsjang.rsocketdemo;

import com.samskivert.mustache.Mustache.Compiler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import reactor.core.publisher.Mono;


@Controller
public class TestContoller {

    @Autowired
    Compiler mustacheCompiler;

    @Autowired
    TestService service;
    
    @Autowired
    Mono<RSocketRequester> requester;

    /**
     *  PAGE
     */
    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/page/{page}")
    public String page() {
        return "test";
    }

    

}