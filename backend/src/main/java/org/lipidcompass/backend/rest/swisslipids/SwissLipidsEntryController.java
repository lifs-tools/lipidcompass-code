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
package org.lipidcompass.backend.rest.swisslipids;

import org.lipidcompass.backend.repository.SwissLipidsEntryRepository;
import org.lipidcompass.backend.rest.AbstractArangoController;
import org.lipidcompass.data.model.swisslipids.SwissLipidsEntry;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import static org.lipidcompass.data.model.Roles.ROLE_USER;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
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
@ExposesResourceFor(SwissLipidsEntry.class)
@RequestMapping(value = "/swisslipids",
        produces = "application/hal+json")
public class SwissLipidsEntryController extends AbstractArangoController<SwissLipidsEntry, SwissLipidsEntryRepository> {

    @Autowired
    public SwissLipidsEntryController(SwissLipidsEntryRepository repository,
            EntityLinks entityLinks) {
        super(repository, entityLinks);
    }

    @PageableAsQueryParam
    @Secured(ROLE_USER)
    @ResponseBody
    @GetMapping("/{id}/children")
    public ResponseEntity<PagedModel<EntityModel<SwissLipidsEntry>>> getChildren(@PathVariable String id, @ParameterObject Pageable p, Authentication authentication) {
        SwissLipidsEntry entry = repository.findById(id).get();
        Page<SwissLipidsEntry> page = new PageImpl<>(entry.getChildren().stream().filter((t) -> {
            return !id.equals(t.getId());
        }).collect(Collectors.toList()), p, 1);
        CollectionModel<SwissLipidsEntry> cm = CollectionModel.of(
                page,
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(SwissLipidsEntryController.class).getChildren(id, p, authentication)).
                        withSelfRel()
        );
        PageMetadata meta = new PagedModel.PageMetadata(page.getNumberOfElements(), page.getNumber(), page.getTotalElements(), page.getTotalPages());
        PagedModel<EntityModel<SwissLipidsEntry>> pagedResources = PagedModel.wrap(cm, meta);
        return ResponseEntity.ok(pagedResources);
    }

    @PageableAsQueryParam
    @ResponseBody
    @PostMapping("/query")
    public ResponseEntity<PagedModel<EntityModel<SwissLipidsEntry>>> querySwissLipidsEntries(@RequestParam String normalizedName, @ParameterObject Pageable p, Authentication authentication) throws Exception {
        Page<SwissLipidsEntry> page = this.repository.findAllByNormalizedName(normalizedName, p);
        return ResponseEntity.ok(assembler.toPagedModel(page, WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(SwissLipidsEntryController.class).querySwissLipidsEntries(normalizedName, p, authentication)).
                withSelfRel()));
    }

    @ResponseBody
    @GetMapping("/levels")
    public ResponseEntity<CollectionModel<SwissLipidsEntry.Level>> getSwissLipidsEntryLevels(Authentication authentication) throws Exception {
        CollectionModel<SwissLipidsEntry.Level> cm = CollectionModel.of(Arrays.asList(SwissLipidsEntry.Level.values()));
        cm.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(SwissLipidsEntryController.class).getSwissLipidsEntryLevels(authentication)).
                withSelfRel());
        return ResponseEntity.ok(cm);
    }

    @PageableAsQueryParam
    @ResponseBody
    @GetMapping("/levels/{level}")
    public ResponseEntity<PagedModel<EntityModel<SwissLipidsEntry>>> getSwissLipidsEntryCategoriesForLevel(@PathVariable String level, @ParameterObject @PageableDefault(value = 50) Pageable p) throws Exception {
        log.info("Querying by level {}", level);
        Page<SwissLipidsEntry> page = repository.findByLevel(level, p);
        log.info("Found {}", page.getTotalElements());

        return ResponseEntity.ok(assembler.toPagedModel(page));
    }

}
