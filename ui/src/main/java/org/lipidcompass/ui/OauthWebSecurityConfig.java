/* 
 * Copyright 2021 The LipidCompass Developers.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lipidcompass.ui;

import org.lipidcompass.service.core.reactive.ReactiveLifsOauthAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.RouterFunction;
import static org.springframework.web.reactive.function.server.RouterFunctions.resources;
import reactor.core.publisher.Mono;

/**
 *
 * @author nilshoffmann
 */
@Configuration
@Import(ReactiveLifsOauthAutoConfiguration.class)
@EnableWebFluxSecurity
public class OauthWebSecurityConfig {

    @Bean
    RouterFunction<ServerResponse> staticResourceRouter() {
        return resources("/**", new ClassPathResource("static/"));
    }

    @Qualifier("jwtAuthenticationConverter")
    @Autowired
    private Converter<Jwt, Mono<AbstractAuthenticationToken>> converter;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
            ReactiveClientRegistrationRepository clientRegistrationRepository) {
        return http.cors(cors -> cors.configurationSource(corsConfigurationSource)).
                authorizeExchange(
                        authorize -> authorize.anyExchange().permitAll()
                ).
                oauth2ResourceServer(
                        oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(converter))
                ).
                csrf(csrf -> csrf.csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse())).build();
    }
}
