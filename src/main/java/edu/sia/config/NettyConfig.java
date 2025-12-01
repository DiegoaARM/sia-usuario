package edu.sia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.resources.LoopResources;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;

import java.time.Duration;

@Configuration
public class NettyConfig {

    @Bean
    public WebServerFactoryCustomizer<NettyReactiveWebServerFactory> nettyCustomizer() {
        return factory -> {
            // número de hilos IO: típicamente núcleos * 2
            int ioThreads = Math.max(2, Runtime.getRuntime().availableProcessors() * 2);
            LoopResources loop = LoopResources.create("reactor-http", ioThreads, true);

            factory.addServerCustomizers(httpServer ->
                httpServer
                    .runOn(loop)
                    .idleTimeout(Duration.ofSeconds(120))
            );
        };
    }
}
