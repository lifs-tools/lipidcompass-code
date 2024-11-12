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
package org.lipidcompass.backend.rest;

import org.lipidcompass.data.model.ArangoBaseEntity;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.LinkBuilder;
import org.springframework.hateoas.EntityModel;

/**
 *
 * @author nilshoffmann
 * @param <T>
 */
public class ArangoEntityRepresentationModelAssembler<T extends ArangoBaseEntity> extends DtoRepresentationModelAssembler<T> {

    public ArangoEntityRepresentationModelAssembler(EntityLinks entityLinks) {
        super(entityLinks);
    }

    @Override
    public EntityModel<T> toModel(T entity) {
        EntityModel<T> resource = EntityModel.of(entity);
        final LinkBuilder lb
                = entityLinks.linkForItemResource(entity.getClass(), entity.getId());
        resource.add(lb.withSelfRel());
        return resource;
    }

}
