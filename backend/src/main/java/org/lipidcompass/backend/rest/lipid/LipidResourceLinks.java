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
package org.lipidcompass.backend.rest.lipid;

import org.lipidcompass.data.model.Lipid;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import org.springframework.hateoas.Link;
import org.springframework.security.core.Authentication;

/**
 *
 * @author nilshoffmann
 */
@Component
public class LipidResourceLinks {

    public List<Link> getLinks(Lipid lipid, Authentication authentication) {
        List<Link> l = new ArrayList<>();
//        if (lipid.getLipidMapsReference() != null) {
//            l.add(linkTo(
//                    WebMvcLinkBuilder.methodOn(LipidMapsEntryController.class).getById(lipid.getLipidMapsCategory().getId(), authentication)).withRel("lipidMapsCategory"));
//        }
//        if (lipid.getLipidMapsMainClass() != null) {
//            l.add(linkTo(
//                    WebMvcLinkBuilder.methodOn(LipidMapsEntryController.class).getById(lipid.getLipidMapsMainClass().getId(), authentication)).withRel("lipidMapsMainClass"));
//        }
//        if (lipid.getLipidMapsSubClass() != null) {
//            l.add(linkTo(
//                    WebMvcLinkBuilder.methodOn(LipidMapsEntryController.class).getById(lipid.getLipidMapsSubClass().getId(), authentication)).withRel("lipidMapsSubClass"));
//        }
        return l;
    }
}
