package com.project.HotelManagementSystem.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Hotel Management System API",
                version = "1.0.0",
                description = """
                        REST API for the Hotel Management System.

                        Features
                        - Authentication & Authorization
                        - Hotel Management
                        - Room Management
                        - Booking Management
                        - Customer Management
                        - Payment Management
                        - Reviews
                        """,
                contact = @Contact(
                        name = "Hotel Management System",
                        email = "support@hotel.com"
                ),
                license = @License(
                        name = "Private API"
                )
        )
)
@SecurityScheme(
        name = "jwtCookie",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.COOKIE,
        paramName = "springBootHms"
)
public class SwaggerConfig {

}
