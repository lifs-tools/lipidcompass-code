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
package org.lipidcompass.backend.rest.cvparameter;

import org.lipidcompass.backend.repository.HasCvParameterReferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.lipidcompass.backend.rest.AbstractArangoController;
import org.lipidcompass.data.model.relations.HasCvParameterReference;

/**
 *
 * @author nilshoffmann
 */
@RestController
@ExposesResourceFor(HasCvParameterReference.class)
@RequestMapping(value = "/hasCvParameterReference",
        produces = "application/hal+json")
public class HasCvParameterReferenceController extends AbstractArangoController<HasCvParameterReference, HasCvParameterReferenceRepository> {

    @Autowired
    public HasCvParameterReferenceController(HasCvParameterReferenceRepository repository,
            EntityLinks entityLinks) {
        super(repository, entityLinks);
    }
    
//    @ResponseBody
//    @GetMapping("/findByReferenceType/{referenceType}")
//    public ResponseEntity<PagedModel<EntityModel<HasCvParameterReference>>> findByReferenceType(@PathVariable String referenceType, Pageable p, Authentication authentication) throws Exception {
//        Page<HasCvParameterReference> page = repository.findAllByReferenceType(referenceType, p);
//        return ResponseEntity.ok(assembler.toPagedModel(page, WebMvcLinkBuilder.linkTo(
//                                 this.getClass()).
//                                 withSelfRel()));
//    }

}
