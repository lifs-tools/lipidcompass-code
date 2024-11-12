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

import de.isas.mztab2.cvmapping.CvParameterLookupService;
import de.isas.mztab2.io.serialization.ParameterConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.ac.ebi.pride.utilities.ols.web.service.client.OLSClient;
import uk.ac.ebi.pride.utilities.ols.web.service.config.OLSWsConfig;

/**
 *
 * @author nilshoffmann
 */
@Configuration
public class OntologyLookupServiceConfiguration {

    @Bean
    public OLSClient olsClient() {
        OLSWsConfig config = new OLSWsConfig();
        OLSClient client = new OLSClient(config);
        return client;
    }

    @Bean
    public CvParameterLookupService cvParameterLookupService(@Autowired OLSClient client) {
        return new CvParameterLookupService(client);
    }

    @Bean
    public ParameterConverter parameterConverter() {
        return new ParameterConverter();
    }

}
