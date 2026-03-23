package com.DigiClassRoom.CloudGateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain spirngSecurityWebFilterChain(ServerHttpSecurity serverHttpSecurity){
            serverHttpSecurity.authorizeExchange(exchanges ->
                exchanges.pathMatchers( HttpMethod.GET).permitAll()
                        .pathMatchers("/foodies/product/**").authenticated()
                        .pathMatchers("/foodies/order/**").authenticated()
                        .pathMatchers("/foodies/payment/**").authenticated()
                    )
                    .oauth2ResourceServer(
                            oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec.
                                    jwt(Customizer.withDefaults()))
                    .csrf(csrfSpec -> csrfSpec.disable());

            return serverHttpSecurity.build();
    }
}
