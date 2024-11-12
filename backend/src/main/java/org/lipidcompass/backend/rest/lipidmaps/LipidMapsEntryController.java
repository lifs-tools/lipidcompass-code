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
package org.lipidcompass.backend.rest.lipidmaps;

import com.google.common.collect.Lists;
import org.lipidcompass.backend.repository.LipidMapsEntryRepository;
import org.lipidcompass.backend.repository.LipidRepository;
import org.lipidcompass.backend.rest.AbstractArangoController;
import org.lipidcompass.backend.rest.lipid.LipidResourceLinks;
import org.lipidcompass.data.model.Lipid;
import org.lipidcompass.data.model.lipidmaps.LipidMapsEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import static org.lipidcompass.data.model.Roles.ROLE_USER;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nils.hoffmann
 */
@Slf4j
@RestController
@ExposesResourceFor(LipidMapsEntry.class)
@RequestMapping(value = "/lipidmaps",
        produces = "application/hal+json")
public class LipidMapsEntryController extends AbstractArangoController<LipidMapsEntry, LipidMapsEntryRepository> {

    private final LipidRepository lipidRepository;
    private final LipidResourceLinks lipidResourceLinks;

    @Autowired
    public LipidMapsEntryController(LipidMapsEntryRepository repository,
            EntityLinks entityLinks, LipidRepository lipidRepository, LipidResourceLinks lipidResourceLinks) {
        super(repository, entityLinks);
        this.lipidRepository = lipidRepository;
        this.lipidResourceLinks = lipidResourceLinks;
    }

    @PageableAsQueryParam
    @Secured(ROLE_USER)
    @ResponseBody
    @GetMapping("/{id}/children")
    public ResponseEntity<PagedModel<EntityModel<LipidMapsEntry>>> getChildren(@PathVariable String id, @ParameterObject @PageableDefault(size = 50) Pageable p, PagedResourcesAssembler<LipidMapsEntry> pagedResourcesAssembler, Authentication authentication) {
        LipidMapsEntry entry = repository.findById(id).get();
        log.info("Retrieved the following children for {}: {}", entry, entry.getChildren());
        Page<LipidMapsEntry> page = new PageImpl<LipidMapsEntry>(entry.getChildren().stream().filter((t) -> {
            return !id.equals(t.getId());
        }).collect(Collectors.toList()), p, 1);
        return ResponseEntity.ok(assembler.toPagedModel(page, WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(LipidMapsEntryController.class).getChildren(id, p, pagedResourcesAssembler, authentication)).
                withSelfRel()));
    }

    @PageableAsQueryParam
    @Secured(ROLE_USER)
    @ResponseBody
    @PostMapping("/query")
    public ResponseEntity<PagedModel<EntityModel<LipidMapsEntry>>> queryLipidMapsEntries(@RequestParam String normalizedName, @ParameterObject @PageableDefault(size = 50) Pageable p, Authentication authentication) throws Exception {
        Page<LipidMapsEntry> page = this.repository.findAllByNormalizedName(normalizedName, p);
        return ResponseEntity.ok(assembler.toPagedModel(page, WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(LipidMapsEntryController.class).queryLipidMapsEntries(normalizedName, p, authentication)).
                withSelfRel()));
    }

    @Secured(ROLE_USER)
    @ResponseBody
    @GetMapping("/levels")
    public ResponseEntity<CollectionModel<LipidMapsEntry.Level>> getLipidMapsEntryLevels(Authentication authentication) throws Exception {
        CollectionModel<LipidMapsEntry.Level> cm = CollectionModel.of(Arrays.asList(LipidMapsEntry.Level.values()));
        cm.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(LipidMapsEntryController.class).getLipidMapsEntryLevels(authentication)).
                withSelfRel());
        return ResponseEntity.ok(cm);
    }

    @PageableAsQueryParam
    @Secured(ROLE_USER)
    @ResponseBody
    @GetMapping("/levels/{level}")
    public ResponseEntity<PagedModel<EntityModel<LipidMapsEntry>>> getLipidMapsCategoriesForLevel(@PathVariable String level, @ParameterObject @PageableDefault(value = 50) Pageable p, Authentication authentication) throws Exception {
        log.info("Querying by level {}", level);
        Page<LipidMapsEntry> page = repository.findByLevel(level, p);
        log.info("Found {}", page.getTotalElements());
        PagedModel<EntityModel<LipidMapsEntry>> pagedResources = assembler.
                toPagedModel(page, WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(LipidMapsEntryController.class).getLipidMapsCategoriesForLevel(level, p, authentication)).
                        withSelfRel());
        return ResponseEntity.ok(pagedResources);
    }

    @PageableAsQueryParam
    @Secured(ROLE_USER)
    @ResponseBody
    @GetMapping("/{id}/lipidsForLevel")
    public ResponseEntity<PagedModel<EntityModel<Lipid>>> getLipidsForLipidMapsLevel(@PathVariable String id, @ParameterObject @PageableDefault(size = 50) Pageable p, Authentication authentication) {
        LipidMapsEntry entry = repository.findById(id).get();
        log.info("Retrieved the following lipidMapsEnty for {}", entry);
        Optional<Iterable<Lipid>> lipids = Optional.empty();
        Pageable customPageable = PageRequest.of(p.getPageNumber(), p.getPageSize(), p.getSort());
        Long totalEntries = 0L;
        switch (entry.getLevel()) {
            case CATEGORY:
                lipids = Optional.of(lipidRepository.findByLipidMapsCategory(entry.getId(), customPageable));
                totalEntries = lipidRepository.countByLipidMapsCategory(entry.getId());
                break;
            case MAIN_CLASS:
                lipids = Optional.of(lipidRepository.findByLipidMapsMainClass(entry.getId(), customPageable));
                totalEntries = lipidRepository.countByLipidMapsMainClass(entry.getId());
                break;
            case SUB_CLASS:
                lipids = Optional.of(lipidRepository.findByLipidMapsSubClass(entry.getId(), customPageable));
                totalEntries = lipidRepository.countByLipidMapsSubClass(entry.getId());
                break;
            default:
                throw new IllegalArgumentException("Unknown LipidMaps level: " + entry.getLevel());
        }
        List<Lipid> lipidsList = lipids.map((t) -> {
            return Lists.newArrayList(t);
        }).orElse(new ArrayList<Lipid>());
        log.info("Retrieved {} lipid entries!", lipidsList.size());
        Page<Lipid> page = new PageImpl<Lipid>(
                lipidsList,
                customPageable,
                totalEntries);
        PagedModel.PageMetadata meta = new PagedModel.PageMetadata(page.getNumberOfElements(), page.getNumber(), page.getTotalElements(), page.getTotalPages());
        PagedModel<EntityModel<Lipid>> pagedResources = PagedModel.wrap(page, meta);
        pagedResources.forEach((t) -> {
            t.add(lipidResourceLinks.getLinks(t.getContent(), authentication));
        });
        pagedResources.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(LipidMapsEntryController.class).getLipidsForLipidMapsLevel(id, customPageable, authentication)).
                withSelfRel());
        return ResponseEntity.ok(pagedResources);
    }

}
