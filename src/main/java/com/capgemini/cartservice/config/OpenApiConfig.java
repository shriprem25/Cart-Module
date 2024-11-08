package com.capgemini.cartservice.config;
 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
 
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
 
@Configuration
public class OpenApiConfig {
 
	@Bean
	public OpenAPI customAPI() {
		Info info = new Info();
		info.setTitle("CartService");
		info.setVersion("1.0");
		info.description("API documentation for the CartService application");
		return new OpenAPI().info(info);
	}
 
}
 