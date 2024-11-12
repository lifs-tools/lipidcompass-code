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

import com.arangodb.model.AqlQueryOptions;
import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;

import org.lipidcompass.backend.repository.SecuredArangoRepository;
import org.lipidcompass.data.model.ArangoBaseEntity;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import static org.lipidcompass.data.model.Roles.*;
import org.lipidcompass.data.model.Visibility;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author nilshoffmann
 * @param <T>
 * @param <U>
 */
@Slf4j
public class AbstractArangoController<T extends ArangoBaseEntity, U extends SecuredArangoRepository<T>> {
    
    protected final U repository;
    protected final EntityLinks entityLinks;
    protected final ArangoEntityRepresentationModelAssembler<T> assembler;
    
    public AbstractArangoController(U arangoRepository,
            EntityLinks entityLinks) {
        this.repository = arangoRepository;
        this.entityLinks = entityLinks;
        this.assembler = new ArangoEntityRepresentationModelAssembler<>(entityLinks);
    }
    
    public AbstractArangoController(U arangoRepository,
            EntityLinks entityLinks, ArangoEntityRepresentationModelAssembler<T> assembler) {
        this.repository = arangoRepository;
        this.entityLinks = entityLinks;
        this.assembler = assembler;
    }
    
    @PostMapping("/findOneByExample")
    public ResponseEntity<EntityModel<T>> findOneByExample(@RequestBody T example, Authentication authentication) {
        Optional<T> opt = repository.findOne(Example.of(example));
        if (opt.isPresent()) {
            return ResponseEntity.ok(assembler.toModel(opt.get()));
        }
        return ResponseEntity.notFound().
                build();
    }
    
    @GetMapping("/count")
    public ResponseEntity<Long> count(Authentication authentication) {
        return ResponseEntity.ok(repository.countPublic());
    }
    
    @PostMapping("/countByExample")
    public ResponseEntity<Long> countByExample(@RequestBody T entity, Authentication authentication) {
        return ResponseEntity.ok(repository.count(Example.of(entity)));
    }
    
    @Secured({ROLE_ADMIN})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") String id, Authentication authentication) {
        repository.deleteById(id);
        return ResponseEntity.noContent().
                build();
    }
    
    @Secured({ROLE_ADMIN, ROLE_CURATOR, ROLE_USER})
    @PostMapping("/save")
    public ResponseEntity<EntityModel<T>> saveSingle(@RequestBody T entity, Authentication authentication) {
        log.debug("Saving entity of type={} with id={}",
                entity.getClass().getCanonicalName(), entity.getId());
        log.debug("{}", entity.toString());
        T l = repository.save(entity);
        if (l != null) {
            log.debug("Returning saved entity of type={} with id={}",
                    entity.getClass().getCanonicalName(), entity.getId());
            EntityModel<T> res = assembler.toModel(l);
            return ResponseEntity.ok(res);
        } else {
            return ResponseEntity.unprocessableEntity().
                    build();
        }
    }
    
    @Secured({ROLE_ADMIN})
    @PostMapping("/saveAll")
    public ResponseEntity<CollectionModel<EntityModel<T>>> saveAll(
            @RequestBody Collection<T> entities, Authentication authentication) {
        return ResponseEntity.ok(assembler.toCollectionModel(repository.
                saveAll(entities)));
    }
    
    @Secured({ROLE_USER, ROLE_ADMIN, ROLE_CURATOR})
    @ResponseBody
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<? extends T>> getById(
            @PathVariable("id") String id, Authentication authentication) {
        Optional<T> opt = repository.findById(id);
        if (hasAnyGroups(authentication, "ROLE_ADMIN", "ROLE_CURATOR") || opt.get().getVisibility() == Visibility.PUBLIC) {
            if (opt.isPresent()) {                
                return ResponseEntity.ok(assembler.toModel(opt.get()));
            }
        }
        return ResponseEntity.notFound().
                build();
    }
    
    @PageableAsQueryParam
    @Secured({ROLE_USER, ROLE_ADMIN, ROLE_CURATOR})
    @ResponseBody
    @GetMapping(path = {"", "/"})
    public ResponseEntity<PagedModel<EntityModel<T>>> get(@ParameterObject Pageable p, Authentication authentication) {
        Page<T> page = Page.empty();
        log.info("Authenticated user has any groups: " + hasAnyGroups(authentication, "ROLE_ADMIN", "ROLE_CURATOR"));
        log.info("Authenticated user has groups: " + authentication.getAuthorities());
        if (hasAnyGroups(authentication, "ROLE_ADMIN", "ROLE_CURATOR")) {
            page = repository.findAll(p);
        } else {
            page = repository.findAllPublic(p);
        }
        CollectionModel<T> cm = CollectionModel.of(
                page,
                WebMvcLinkBuilder.linkTo(this.getClass()).withSelfRel()
        );
        PageMetadata meta = new PagedModel.PageMetadata(page.getNumberOfElements(), page.getNumber(), page.getTotalElements(), page.getTotalPages());
        PagedModel<EntityModel<T>> pagedResources = PagedModel.wrap(cm, meta);
        pagedResources.add(linkTo(methodOn(this.getClass()).
                deleteById(null, authentication)).
                withRel("delete"),
                linkTo(methodOn(this.getClass()).
                        count(authentication)).
                        withRel("count"),
                //                linkTo(methodOn(this.getClass()).
                //                        countByExample(null, authentication)).
                //                        withRel("countByExample"),
                linkTo(methodOn(this.getClass()).
                        saveSingle(null, authentication)).
                        withRel("saveSingle"),
                linkTo(methodOn(this.getClass()).
                        saveAll(null, authentication)).
                        withRel("saveAll"),
                linkTo(methodOn(this.getClass()).
                        getById(null, authentication)).
                        withRel("get"),
                linkTo(methodOn(this.getClass()).
                        getRandom(authentication)).
                        withRel("random")
        );
        return ResponseEntity.ok(pagedResources);
    }
    
    @Secured({ROLE_USER})
    public ResponseEntity<EntityModel<T>> getRandom(Authentication authentication) {
        Optional<T> t = Optional.ofNullable(repository.random(
                new AqlQueryOptions()));
        if (t.isPresent()) {
            // This acts as a safety net, since repository.random should only ever return 
            // entities with PUBLIC visibility.
            if (t.get().getVisibility()==Visibility.PUBLIC) {
                return ResponseEntity.ok(assembler.toModel(t.get()));
            }
        }
        return ResponseEntity.notFound().build();
    }
    
    protected boolean hasAnyGroups(Authentication authentication, String... groups) {
        if (authentication == null || groups.length == 0) {
            return false;
        }
        Set<String> wantedGroups = new HashSet<>(Arrays.asList(groups));
        Set<String> grantedGroups = authentication.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toSet());
        return wantedGroups.stream().anyMatch(a -> grantedGroups.contains(a));
    }
    
}
