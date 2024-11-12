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
package org.lipidcompass.gateway;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties.SwaggerUrl;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import static org.springdoc.core.utils.Constants.DEFAULT_API_DOCS_URL;
import static org.springdoc.core.utils.Constants.DEFAULT_SWAGGER_UI_PATH;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;

/**
 * Spring boot central application hook.
 *
 * @author nils.hoffmann
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    @Lazy(false)
    public Set<SwaggerUrl> apis(RouteDefinitionLocator locator, SwaggerUiConfigProperties swaggerUiConfigProperties) {
        Set<SwaggerUrl> urls = new HashSet<>();
        List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
        definitions.stream().filter(routeDefinition -> routeDefinition.getId().matches(".*-service")).forEach(routeDefinition -> {
            String name = routeDefinition.getId().replaceAll("-service", "");
            SwaggerUrl swaggerUrl = new SwaggerUrl(name, DEFAULT_API_DOCS_URL + "/" + name, null);
            urls.add(swaggerUrl);
        });
        swaggerUiConfigProperties.setUrls(urls);
        return urls;
    }

//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route("default", r -> r
//                .order(Ordered.LOWEST_PRECEDENCE)
//                .path("/**")
//                .and().not(p -> p.path(DEFAULT_API_DOCS_URL, DEFAULT_SWAGGER_UI_PATH))
//                .filters(
//                    f -> f.rewritePath("/lipidcompass-ui/(?<path>.*)", "/$\\{path}")
//                )
//                .uri("lb://lipidcompass-ui")
//                )
//                .build();
//    }

}
