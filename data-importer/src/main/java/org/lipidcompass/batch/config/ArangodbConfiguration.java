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

import com.arangodb.ArangoDB;
import com.arangodb.springframework.annotation.EnableArangoAuditing;
import com.arangodb.springframework.annotation.EnableArangoRepositories;
import com.arangodb.springframework.config.ArangoConfiguration;
import org.lipidcompass.backend.audit.AuditorService;
import org.lipidcompass.backend.repository.LipidRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.CurrentDateTimeProvider;
import org.springframework.data.domain.AuditorAware;

/**
 *
 * @author nilshoffmann
 */
@Slf4j
@Configuration
@EnableArangoAuditing(
        auditorAwareRef = "auditorProvider",
        dateTimeProviderRef = "dateTimeProvider",
        modifyOnCreate = true,
        setDates = true
)
@EntityScan(basePackages = {"org.lipidcompass.data.model",
    "org.lipidcompass.data.model.relations"})
@EnableArangoRepositories(basePackageClasses = {LipidRepository.class})//, repositoryFactoryBeanClass = CustomArangoRepositoryFactoryBean.class)
public class ArangodbConfiguration implements ArangoConfiguration {

    @Value("${arangodb.user}")
    private String userName;

    @Value("${arangodb.password}")
    private String password;
    
    @Value("${arangodb.jwt}")
    private String jwt;

    @Value("${arangodb.host}")
    private String host;

    @Value("${arangodb.port}")
    private Integer port;

    @Value("${arangodb.database:lipidcompass}")
    private String database;

    @Value("${arangodb.maxconnections:20}")
    private Integer maxConnections;

    @Value("${arangodb.connectionTtl:300000}")
    private Long connectionTtl;

    @Value("${arangodb.keepAliveInterval:1800}")
    private Integer keepAliveInterval;
    
    @Value("${arangodb.chunksize:30000}")
    private Integer chunksize;

    @Override
    public ArangoDB.Builder arango() {
        log.info("Configuring ArangoDB with host: {}:{}, maximumConnections: {}, chunkSize: {}", host, port, maxConnections, chunksize);
        ArangoDB.Builder builder = new ArangoDB.Builder().
                acquireHostList(true).
                host(host, port).
                user(userName).
                password(password).
                chunkSize(chunksize).
                maxConnections(maxConnections).
                connectionTtl(connectionTtl).
                keepAliveInterval(keepAliveInterval);
        if(jwt!=null && !jwt.isBlank()) {
            log.info("Using jwt and aquireHostList: '{}'", jwt);
            builder.jwt(jwt);
        }
        return builder;
    }

    @Override
    public String database() {
        log.info("Arango database: " + database);
        return database;
    }

    @Bean("auditorProvider")
    public AuditorAware<String> auditorProvider() {
        return new AuditorService();
    }

    @Bean("dateTimeProvider")
    public CurrentDateTimeProvider dateTimeProvider() {
        return CurrentDateTimeProvider.INSTANCE;
    }

}
