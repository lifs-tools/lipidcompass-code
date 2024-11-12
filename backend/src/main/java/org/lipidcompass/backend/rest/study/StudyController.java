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
package org.lipidcompass.backend.rest.study;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.lipidcompass.backend.rest.AbstractArangoController;
import org.lipidcompass.data.model.Study;
import org.lipidcompass.data.model.Visibility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.lipidcompass.backend.repository.StudyRepository;
import static org.lipidcompass.data.model.Roles.ROLE_CURATOR;
import static org.lipidcompass.data.model.Roles.ROLE_USER;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;

/**
 *
 * @author nilshoffmann
 */
@RestController
@ExposesResourceFor(Study.class)
@RequestMapping(value = "/study",
        produces = "application/hal+json")
@Slf4j
public class StudyController extends AbstractArangoController<Study, StudyRepository> {

    @Autowired
    public StudyController(StudyRepository repository,
            EntityLinks entityLinks) {
        super(repository, entityLinks);
    }

    @PageableAsQueryParam
    @Secured({ROLE_CURATOR,ROLE_USER})
    @ResponseBody
    @PostMapping("/findByUserName")
    public ResponseEntity<PagedModel<EntityModel<Study>>> findByUserName(@RequestBody(required = false) String userName, @ParameterObject @PageableDefault(value = 50) Pageable p, Authentication authentication) {
        Page<Study> page = repository.findByVisibilityAndOwnerUserName(Visibility.PUBLIC, userName, Pageable.unpaged());
        log.info("Found {}", page.getTotalElements());
        return ResponseEntity.ok(assembler.toPagedModel(page));
    }

    @PreAuthorize("permitAll()")
    @ResponseBody
    @GetMapping("/random")
    @Override
    public ResponseEntity<EntityModel<Study>> getRandom(Authentication authentication) {
        return super.getRandom(authentication);
    }

}
