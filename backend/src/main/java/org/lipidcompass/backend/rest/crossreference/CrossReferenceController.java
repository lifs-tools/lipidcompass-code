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
package org.lipidcompass.backend.rest.crossreference;

import org.lipidcompass.backend.repository.CrossReferenceRepository;
import org.lipidcompass.data.model.CrossReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.lipidcompass.backend.rest.AbstractArangoController;
import static org.lipidcompass.data.model.Roles.ROLE_USER;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author nilshoffmann
 */
@RestController
@ExposesResourceFor(CrossReference.class)
@RequestMapping(value = "/crossreference",
        produces = "application/hal+json")
public class CrossReferenceController extends AbstractArangoController<CrossReference, CrossReferenceRepository> {

    @Autowired
    public CrossReferenceController(CrossReferenceRepository repository,
            EntityLinks entityLinks) {
        super(repository, entityLinks);
    }

    @Secured({ROLE_USER})
    @ResponseBody
    @GetMapping("/findByNativeId/{nativeId}")
    public ResponseEntity<EntityModel<CrossReference>> findByNativeId(@PathVariable String nativeId, Authentication authentication) throws Exception {
        return ResponseEntity.ok(assembler.toModel(repository.findByNativeId(nativeId)));
    }

}
