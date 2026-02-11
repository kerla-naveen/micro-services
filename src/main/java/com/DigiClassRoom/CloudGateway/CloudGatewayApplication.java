package com.DigiClassRoom.CloudGateway;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

import java.time.Duration;
import java.time.LocalDateTime;

@SpringBootApplication
public class CloudGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudGatewayApplication.class, args);
	}

		@Bean
		public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer(){
			return factory -> factory.configureDefault(
					id -> new Resilience4JConfigBuilder(id).
							circuitBreakerConfig(
								CircuitBreakerConfig.ofDefaults()

					).build()
			);
		}

		@Bean
	public RouteLocator foodiesRouteConfig(RouteLocatorBuilder routeLocatorBuilder){
		return  routeLocatorBuilder.routes()
				.route(p-> p
						.path("/foodies/product/**")
						.filters( f ->f.rewritePath("/foodies/product(?<segment>.*)","/product${segment}")
								.addResponseHeader("X-Response-Header", LocalDateTime.now().toString())
								.retry(retryConfig -> retryConfig.setRetries(3)
										.setMethods(HttpMethod.GET,HttpMethod.DELETE)
										.setBackoff(Duration.ofMillis(100),Duration.ofMillis(1000),2,true)
								)
						)
						.uri("lb://PRODUCT-SERVICE")
				)
				.route(p->p
						.path("/foodies/order/**")
						.filters(f-> f.rewritePath("/foodies/order(?<segment>.*)", "/order${segment}")
								.addResponseHeader("X-Response-Header",LocalDateTime.now().toString())
						)
						.uri("lb://ORDER-SERVICE")
				)
				.route(p->p
						.path("/foodies/payment/**")
						.filters(f-> f.rewritePath("/foodies/payment(?<segment>.*)","/payment${segment}")
								.addResponseHeader("X-Response-Header",LocalDateTime.now().toString())
						)
						.uri("lb://PAYMENT-SERVICE")
				)
				.build();

	}
}
