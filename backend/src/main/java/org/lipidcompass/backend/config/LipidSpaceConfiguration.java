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
package org.lipidcompass.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.lifstools.lipidspace.client.ApiClient;
import org.lifstools.lipidspace.client.api.DefaultApi;
import org.lipidcompass.lipidspace.config.LipidSpaceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author nils.hoffmann
 */
@Configuration
@EnableConfigurationProperties(LipidSpaceProperties.class)
public class LipidSpaceConfiguration {

    @Autowired
    private LipidSpaceProperties lipidSpaceProperties;
    
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean("lipidSpaceDefaultApi")
    public DefaultApi lipidSpaceDefaultApi(@Autowired RestTemplate restTemplate) {
        ApiClient client = new ApiClient(restTemplate);
        client.setBasePath(lipidSpaceProperties.getServiceUrl());
        return new DefaultApi(client);
    }
}
