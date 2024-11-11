package com.esprit.microservice.gatwayfinal.config;

import lombok.RequiredArgsConstructor;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
@KeycloakConfiguration
public class SecurityConfiguration {

    private final JwtAuthConverter jwtAuthConverter;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        KeycloakAuthenticationProvider provider = new KeycloakAuthenticationProvider();
        provider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(provider);
    }

    @Bean
    public SecurityFilterChain securityFilterChainMethod(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(CsrfConfigurer::disable)
                //  .authorizeHttpRequests(httpRequests -> httpRequests.anyRequest().permitAll())
                .authorizeHttpRequests(httpRequests -> httpRequests.anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2ResourceServer ->
                        oauth2ResourceServer.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(jwtAuthConverter)
                        )
                )
                .build();
    }

    @Bean
    public KeycloakConfigResolver keycloakConfigResolver(){
        return new KeycloakSpringBootConfigResolver();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        return (web) -> {
            web.ignoring().requestMatchers(
                    HttpMethod.GET,
                    "/auth/hello-2",
                    "/requestSupply/retrieveAllRequests",
                    "/auth/hello-2",
                    "/Offer/allOffers",
                    "/Offer/AcceptedOffer",
                    "/Offer/Offer/filterByCriteria/{criteria}",
                    "/user-images/**",
                    "/forum/**",
                    "/pack/**",
                    "/reclamation/feed",
                    "/reclamation/{reclamationId}/rating",
                    "/Offer/Offer/filterByCriteria/{criteria}"
            );
            web.ignoring().requestMatchers(
                    HttpMethod.POST,
                    "/auth/create-user",
                    "/auth/login",
                    "/pack/**",
                    "/auth/logout",
                    "/auth/refreshToken",
                    "/reclamation/contact/**",
                    "/pack/**",
                    "/forum/**",
                    "/reclamation/contact/**"
            );
            web.ignoring().requestMatchers(
                    HttpMethod.PUT,
                    "/auth/forgot-password",
                    "/forum/**",
                    "/pack/**"
                    //"/auth/addRoleToUser/**"
            );
            web.ignoring().requestMatchers(
                            HttpMethod.OPTIONS,
                            "/**"
                    )
                    .requestMatchers("/v3/api-docs/**", "/configuration/**", "/swagger-ui/**",
                            "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/api-docs/**");


        };
    }
}
