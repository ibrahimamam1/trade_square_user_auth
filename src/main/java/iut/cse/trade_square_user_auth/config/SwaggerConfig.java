package iut.cse.trade_square_user_auth.config;

import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI tradeSquareOpenAPI() {
        return new OpenAPI().info(new Info().title("Trade Square User Auth API")
        .description("Api for Trade Square User Auth API")
                .version("v1.0")
                .contact(new Contact().name("Trade Square Support")))
                .servers(List.of(
                        new Server().url("/").description("Default Server URL")
                ));
    }
}
