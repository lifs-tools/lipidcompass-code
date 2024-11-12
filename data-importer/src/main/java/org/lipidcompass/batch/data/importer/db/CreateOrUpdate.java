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
package org.lipidcompass.batch.data.importer.db;

import org.lipidcompass.backend.repository.SecuredArangoRepository;
import org.lipidcompass.data.model.ArangoBaseEntity;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.util.Pair;

/**
 * This class is not thread safe!
 *
 * @author nilshoffmann
 * @param <T>
 */
@Slf4j
@Value
public class CreateOrUpdate<T extends ArangoBaseEntity> {

    private final Set<T> entities;
    @Getter(AccessLevel.PRIVATE)
    private final SecuredArangoRepository<T> repository;
    @Getter(AccessLevel.PRIVATE)
    private final ArangoUtils utils = new ArangoUtils();
    @Getter(AccessLevel.PRIVATE)
    private final List<T> checkedEntities = new LinkedList();
    @Getter(AccessLevel.PRIVATE)
    private AtomicBoolean hasPersisted = new AtomicBoolean(false);
    @Getter(AccessLevel.PRIVATE)
    private final Optional<ExampleMatcher> exampleMatcher;

    public CreateOrUpdate(Set<T> entities, SecuredArangoRepository<T> repository, ExampleMatcher exampleMatcher) {
        log.info("Received {} entities to save", entities.size());
        this.entities = Collections.unmodifiableSet(entities);
        this.repository = repository;
        this.exampleMatcher = Optional.ofNullable(exampleMatcher);
    }

    public CreateOrUpdate(Set<T> entities, SecuredArangoRepository<T> repository) {
        this(entities, repository, null);
    }

    private Map<T, Boolean> checkExist() {
        return entities.stream().map((t) -> {
            if (exampleMatcher.isPresent()) {
                return Pair.of(t, utils.existsByExample(repository, t, exampleMatcher.get()));
            } else {
                return Pair.of(t, utils.existsByExample(repository, t));
            }

        }).collect(Pair.toMap());
    }

    public List<T> updateIds() {
        if (checkedEntities.isEmpty()) {
            log.info("Checking/Updating ids for {} entities", this.entities.size());
            this.checkedEntities.addAll(checkExist().entrySet().stream().map((t) -> {
                if (t.getValue()) {
                    Optional<T> persistent;
                    if (exampleMatcher.isPresent()) {
                        log.info("Using custom example matcher!");
                        persistent = utils.findOneByExample(repository, t.getKey(), exampleMatcher.get());
                    } else {
                        persistent = utils.findOneByExample(repository, t.getKey());
                    }

                    if (persistent.isPresent()) {
                        t.getKey().setId(persistent.get().getId());
                        log.info("Query: {}", t.getKey());
                        log.info("Found: {}", persistent.get());
                        throw new IllegalArgumentException();
//                        log.info("Updated entity id to {}", t.getKey().getId());
                    }
                }
                return t.getKey();
            }).collect(Collectors.toList()));
        }
        return this.checkedEntities;
    }

    public Iterable<T> apply() {
        if (this.hasPersisted.get()) {
            return repository.findAllById(
                    updateIds().stream().map((t) -> {
                        return t.getId();
                    }).collect(Collectors.toList()));
        }
        updateIds();
        log.info("Saving {} entities", this.checkedEntities.size());
        Iterable<T> iter = repository.saveAll(this.checkedEntities);
        this.hasPersisted.getAndSet(true);
        return iter;
    }
}
