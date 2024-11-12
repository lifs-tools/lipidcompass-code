/*
 * Copyright 2021 LIFS.
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
package org.lipidcompass.service.core.docker;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.logging.DeferredLog;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

/**
 *
 * @author nilshoffmann
 */
public class DockerSwarmSecretPostProcessor implements EnvironmentPostProcessor, ApplicationListener<ApplicationEvent> {

    private static final DeferredLog log = new DeferredLog();

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String dockerSecretPath = environment.getProperty("docker-secret.bind-path", "/run/secrets");

        log.debug("value of \"docker-secret.bind-path\" property:" + dockerSecretPath);

        if (dockerSecretPath != null) {
            Path bindPath = Paths.get(dockerSecretPath);
            if (Files.isDirectory(bindPath)) {
                Map<String, Object> dockerSecrets;
                log.info("Mapping docker swarm secrets to docker-secret prefix in configuration.");
                try {
                    dockerSecrets
                            = Files.list(bindPath)
                                    .collect(
                                            Collectors.toMap(
                                                    path -> {
                                                        File secretFile = path.toFile();
                                                        return "docker-secret." + secretFile.getName();
                                                    },
                                                    path -> {
                                                        try {
                                                            return Files.readString(path, StandardCharsets.UTF_8);
                                                        } catch (IOException ex) {
                                                            throw new RuntimeException(ex);
                                                        }
                                                    }
                                            ));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                dockerSecrets
                        .entrySet()
                        .forEach(entry -> {
                            log.debug(entry.getKey() + "=\"" + entry.getValue() + "\"");
                        });

                MapPropertySource propertySource = new MapPropertySource("docker-secrets", dockerSecrets);
                environment.getPropertySources().addLast(propertySource);
            }
        }
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        log.replayTo(DockerSwarmSecretPostProcessor.class);
    }

}
