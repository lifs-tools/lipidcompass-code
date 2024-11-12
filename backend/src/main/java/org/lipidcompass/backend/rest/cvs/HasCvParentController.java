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
package org.lipidcompass.backend.rest.cvs;

import org.lipidcompass.backend.repository.HasCvParentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.lipidcompass.backend.rest.AbstractArangoController;
import org.lipidcompass.data.model.relations.HasCvParent;

/**
 *
 * @author nilshoffmann
 */
@RestController
@ExposesResourceFor(HasCvParent.class)
@RequestMapping(value = "/hasCvParent",
        produces = "application/hal+json")
public class HasCvParentController extends AbstractArangoController<HasCvParent, HasCvParentRepository> {

    @Autowired
    public HasCvParentController(HasCvParentRepository repository,
            EntityLinks entityLinks) {
        super(repository, entityLinks);
    }

}