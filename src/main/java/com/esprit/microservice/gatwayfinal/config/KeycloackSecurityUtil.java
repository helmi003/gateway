package com.esprit.microservice.gatwayfinal.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class KeycloackSecurityUtil {

    @Value("${keycloak.auth-server-url}")
    private String server_url;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.credentials.secret}")
    private String secret;

    @Bean
    public Keycloak getKeycloakInstance(){
        return KeycloakBuilder.builder()
                .serverUrl(server_url)
                .realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId("admin-cli")
                .clientSecret(secret)
                .build();
    }
}
