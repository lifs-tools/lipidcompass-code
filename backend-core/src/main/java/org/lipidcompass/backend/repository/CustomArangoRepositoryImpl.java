/*
 * Copyright 2023 LIFS.
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
package org.lipidcompass.backend.repository;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.SetUtils;
import org.lipidcompass.data.model.ArangoBaseEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import com.arangodb.springframework.core.ArangoOperations;

/**
 *
 * @author nilshoffmann
 * @param <T>
 * @param <ID>
 */
@Repository
public class CustomArangoRepositoryImpl<T extends ArangoBaseEntity, ID> implements CustomArangoRepository<T, ID> {

    private final ArangoOperations arangoOperations;

    /**
     *
     * @param arangoOperations The template used to execute much of the
     * functionality of this class
     */
    public CustomArangoRepositoryImpl(final ArangoOperations arangoOperations) {
        super();
        this.arangoOperations = arangoOperations;
    }

    /**
     * Saves the given iterable of entities to the database using update from
     * the template
     *
     * @param <S>
     * @param entities the iterable of entities to be saved to the database
     * @param domainClass the entity / domain class to work on
     * @param attributeName
     * @param attributeMapperFunction returns the attributes value
     * @return the iterable of updated entities with any id/key/rev saved in
     * each entity
     */
    @Override
    public <S extends T> Iterable<S> createOrUpdateAll(Collection<S> entities, Class<S> domainClass, String attributeName, Function<T, String> attributeMapperFunction) {
//        System.out.println(String.format("Save %s additional entities", entities.size()));
        Map<String, S> attributeToNewEntities = new LinkedHashMap<>();
        List<String> newIds = entities.stream().map(attributeMapperFunction).toList();
        for (S c : entities) {
            attributeToNewEntities.put(attributeMapperFunction.apply(c), c);
        }
        // lookup all entities in database
//        System.out.println("Retrieving entries for new ids: " + newIds);
        Map<String, Object> bindVars = new LinkedHashMap<>();
        bindVars.put("attributeName", "nativeId");
        bindVars.put("attributeValues", newIds);
        bindVars.put("@col", domainClass);
        Iterable<S> existingEntities =  arangoOperations.query("FOR c in @@col FILTER c[@attributeName] IN @attributeValues RETURN c", bindVars, domainClass);
//        System.out.println("Found the following matches: " + existingEntities);
        Map<String, S> attributeToExistingEntities = new LinkedHashMap<>();
        for (S c : existingEntities) {
            attributeToExistingEntities.put(attributeMapperFunction.apply(c), c);
        }
        Set<String> exclusivelyNewEntities = SetUtils.difference(attributeToNewEntities.keySet(), attributeToExistingEntities.keySet());
//        System.out.println("Found " + exclusivelyNewEntities.size() + " new entities to be inserted!");
        Set<String> entititiesToUpdate = SetUtils.intersection(attributeToNewEntities.keySet(), attributeToExistingEntities.keySet());
//        System.out.println("Found " + entititiesToUpdate.size() + " entities to be updated!");
        //save new entities
        Collection<S> newEntities = exclusivelyNewEntities.stream().map((t) -> attributeToNewEntities.get(t)).toList();
        arangoOperations.repsertAll(newEntities, domainClass);
        //update existing entities
        Collection<S> updatedEntities = entititiesToUpdate.stream().map((t) -> {
            S newEntity = attributeToNewEntities.get(t);
            S oldEntity = attributeToExistingEntities.get(t);
            String oldId = oldEntity.getId();
            String oldArangoId = oldEntity.getArangoId();
            BeanUtils.copyProperties(newEntity, oldEntity, "dateCreated", "createdBy");
            oldEntity.setId(oldId);
            oldEntity.setArangoId(oldArangoId);
            return oldEntity;
        }).toList();
        arangoOperations.repsertAll(updatedEntities, domainClass);

        return IterableUtils.chainedIterable(newEntities, updatedEntities);
    }
//    
//    @Override
//    public <S extends T> S save(S entity) {
//        arangoOperations.update(, entity.getClass());
//        return entity;
//    }

}
