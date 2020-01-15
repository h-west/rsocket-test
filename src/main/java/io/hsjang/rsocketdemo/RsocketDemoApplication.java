package io.hsjang.rsocketdemo;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Mustache.Collector;
import com.samskivert.mustache.Mustache.TemplateLoader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mustache.MustacheEnvironmentCollector;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class RsocketDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RsocketDemoApplication.class, args);
	}

	@Bean
	public Mustache.Compiler mustacheCompiler(TemplateLoader mustacheTemplateLoader, Environment environment) {
		return Mustache.compiler().withLoader(mustacheTemplateLoader).withCollector(collector(environment)).withDelims("{^ $}");
	}

	private Collector collector(Environment environment) {
		MustacheEnvironmentCollector collector = new MustacheEnvironmentCollector();
		collector.setEnvironment(environment);
		return collector;
	}

}
