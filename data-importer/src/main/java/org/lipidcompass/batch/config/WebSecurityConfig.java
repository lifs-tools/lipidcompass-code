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
package org.lipidcompass.batch.config;

import org.lipidcompass.batch.data.importer.utilities.AuthenticationPrincipalNameExtractor;
import lombok.extern.slf4j.Slf4j;
import org.lipidcompass.backend.repository.UserRepository;
import org.lipidcompass.service.core.servlet.LifsOauthAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 *
 * @author nilshoffmann
 */
@Slf4j
@Configuration
@EnableWebSecurity
@Import(LifsOauthAutoConfiguration.class)
@EnableMethodSecurity(jsr250Enabled = true, prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig {

    @Qualifier("jwtClientRoleConverter")
    @Autowired
    private Converter<Jwt, AbstractAuthenticationToken> converter;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Bean
    public AuthenticationPrincipalNameExtractor authenticationPrincipalNameExtractor() {
        return new AuthenticationPrincipalNameExtractor(userRepository);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.
            cors(cors -> cors.configurationSource(corsConfigurationSource)).
            authorizeHttpRequests(
                requests -> requests.requestMatchers(new AntPathRequestMatcher("/**")).permitAll()
            ).
            oauth2ResourceServer(
                oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(converter))
            );
        return http.build();
    }

}
