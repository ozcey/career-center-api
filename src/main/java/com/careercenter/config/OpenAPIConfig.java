package com.careercenter.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

	private static final String TITLE = "Career Center App";
	private static final String VERSION = "V1";
	private static final String DESCRIPTION = "Career Center API";
	private static final String CONTACT_NAME = "Oz";
	private static final String LICENCE = "Apache 2.0";
	private static final String URL = "http://springdoc.org";
	private static final String JWT = "JWT";
	private static final String BEARER = "bearer";
	private static final String SECURITY_SCHEME_NAME = "bearerAuth";

	@Bean
	public OpenAPI customOpenAPI(){
		return new OpenAPI()
				.addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
				.addServersItem(new Server().url("/"))
				.components(
						new Components()
						.addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
							.name(SECURITY_SCHEME_NAME)
							.type(SecurityScheme.Type.HTTP)
								.scheme(BEARER)
								.bearerFormat(JWT))


				)
				.info(new Info()
					.title(TITLE)
					.version(VERSION)
					.description(DESCRIPTION)
					.contact(new Contact().name(CONTACT_NAME))
					.license(new License().name(LICENCE).url(URL))
				);
	}
}
