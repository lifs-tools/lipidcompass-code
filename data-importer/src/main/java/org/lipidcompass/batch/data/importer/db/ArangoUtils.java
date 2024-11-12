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

import com.arangodb.model.DocumentReplaceOptions;
import com.arangodb.springframework.core.ArangoOperations;
import org.lipidcompass.backend.repository.SecuredArangoRepository;
import org.lipidcompass.data.model.ArangoBaseEntity;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

/**
 *
 * @author nils.hoffmann
 */
public class ArangoUtils {
    
    public <T extends ArangoBaseEntity> List<Optional<T>> createOrUpdate(ArangoOperations arangoOperations,
            SecuredArangoRepository<T> repository, List<T> l, String principal) {
        return l.stream().map((t) -> {
            return createOrUpdate(arangoOperations, repository, t, principal);
        }).collect(Collectors.toList());
    }
    
    public <T extends ArangoBaseEntity> Optional<T> createOrUpdate(ArangoOperations arangoOperations,
            SecuredArangoRepository<T> repository, T l,
            String principal) {
        return findByExampleOrSave(arangoOperations, repository, l, principal);
    }
    
    private <T extends ArangoBaseEntity> Optional<T> findByIdOrSave(ArangoOperations arangoOperations, SecuredArangoRepository<T> repository, T l, String principal) {
        if (l.getId() != null) {
            Optional<T> result = repository.findById(l.getId());
            if (!result.isPresent()) {
                return saveOrUpdate(result, arangoOperations, l, repository, principal);
            }
            return result;
        }
        return findByExampleOrSave(arangoOperations, repository, l, principal);
    }
    
    public <T extends ArangoBaseEntity> boolean existsByExample(SecuredArangoRepository<T> repository, T l, ExampleMatcher exampleMatcher) {
        return repository.exists(Example.of(l, exampleMatcher));
    }
    
    public <T extends ArangoBaseEntity> boolean existsByExample(SecuredArangoRepository<T> repository, T l) {
        return existsByExample(repository, l, ExampleMatcher.matchingAny()
                    .withIgnorePaths("dateCreated", "dateLastModified", "createdBy", "updatedBy", "transactionUuid"));
    }
    
    public <T extends ArangoBaseEntity> Optional<T> findOneByExample(
            SecuredArangoRepository<T> repository, T l, ExampleMatcher exampleMatcher) {
        return repository.findOne(Example.of(l, exampleMatcher));
    }
    
    public <T extends ArangoBaseEntity> Optional<T> findOneByExample(
            SecuredArangoRepository<T> repository, T l) {
        return findOneByExample(repository, l, ExampleMatcher.matchingAny()
                    .withIgnorePaths("dateCreated", "dateLastModified", "createdBy", "updatedBy", "transactionUuid"));
    }
    
    private <T extends ArangoBaseEntity> Optional<T> findByExampleOrSave(ArangoOperations arangoOperations, SecuredArangoRepository<T> repository, T l, String principal) {
        try {
            return saveOrUpdate(findOneByExample(repository, l), arangoOperations, l, repository, principal);
        } catch (java.util.NoSuchElementException nsee) {
            return Optional.of(repository.save(l));
        }
    }
    
    private <T extends ArangoBaseEntity> Optional<T> saveOrUpdate(Optional<T> result, ArangoOperations arangoOperations, T l, SecuredArangoRepository<T> repository, String principal) throws DataAccessException {
        // use update, standard repository uses repsert which may fail with
        // additional unique indices
        if (result.isPresent()) {
            String id = result.get().getId();
            l.setId(id);
            l.setUpdatedBy(principal);
            arangoOperations.replace(id, l);
            return repository.findById(id);
        } else {
            l.setCreatedBy(principal);
            l.setUpdatedBy(principal);
            return Optional.of(repository.save(l));
        }
    }
}
