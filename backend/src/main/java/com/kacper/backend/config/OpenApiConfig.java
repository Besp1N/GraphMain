package com.kacper.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Kacper Karabinowski",
                        email = "kacper.karabinowski@globallogic.com"
                ),
                description = "GraphMain backend documentation",
                title = "GraphMain API"
        ),
        servers = @Server(
                url = "http://localhost:8080",
                description = "Local server"
        )
)
public class OpenApiConfig
{
}
