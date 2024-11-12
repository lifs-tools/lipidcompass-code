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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.LinkBuilder;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

/**
 *
 * @author nilshoffmann
 * @param <T>
 */
public class DtoRepresentationModelAssembler<T> implements RepresentationModelAssembler<T, EntityModel<T>> {

    protected final EntityLinks entityLinks;

    @Autowired
    public DtoRepresentationModelAssembler(EntityLinks entityLinks) {
        this.entityLinks = entityLinks;
    }

    @Override
    public EntityModel<T> toModel(T entity) {
        EntityModel<T> resource = EntityModel.of(entity);
        return resource;
    }

    @Override
    public CollectionModel<EntityModel<T>> toCollectionModel(Iterable<? extends T> entities) {
        return StreamSupport.stream(entities.spliterator(), false).map(this::toModel).collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));
    }

    public PagedModel<EntityModel<T>> toPagedModel(Page<? extends T> page, Link... links) {
        CollectionModel<EntityModel<T>> cm = toCollectionModel(page);
        cm.add(links);
        PagedModel.PageMetadata meta = new PagedModel.PageMetadata(page.getNumberOfElements(), page.getNumber(), page.getTotalElements(), page.getTotalPages());
        return PagedModel.of(cm.getContent(), meta);
    }

}
