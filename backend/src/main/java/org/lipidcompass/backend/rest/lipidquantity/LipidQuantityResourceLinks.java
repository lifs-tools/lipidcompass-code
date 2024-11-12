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
package org.lipidcompass.backend.rest.lipidquantity;

import org.lipidcompass.backend.rest.lipid.LipidController;
import org.lipidcompass.backend.rest.mztabresult.MzTabResultController;
import org.springframework.stereotype.Component;
import org.lipidcompass.data.model.LipidQuantity;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.lipidcompass.data.model.Lipid;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import org.springframework.security.core.Authentication;

/**
 *
 * @author nilshoffmann
 */
@Slf4j
@Component
public class LipidQuantityResourceLinks {

    public List<Link> getLinks(LipidQuantity lipidQuantity, Authentication authentication) {
        List<Link> l = new ArrayList<>();
        if (lipidQuantity.getLipids() != null) {
            for(Lipid lipid:lipidQuantity.getLipids()) {
              l.add(linkTo(
                    WebMvcLinkBuilder.methodOn(LipidController.class).getById(lipid.getId(), authentication)).withRel("lipids"));
            }
        }
        if (lipidQuantity.getMzTabResultId() != null) {
            try {
                l.add(linkTo(
                        WebMvcLinkBuilder.methodOn(MzTabResultController.class).getById(lipidQuantity.getMzTabResultId(), authentication)).withRel("mztab"));
            } catch (Exception ex) {
                log.error("Caught exception while trying to build resource link for LipidQuantity: ", ex);
            }
        }
//        if (lipidQuantity.getLipidMapsCategory() != null) {
//            l.add(linkTo(
//                    WebMvcLinkBuilder.methodOn(LipidMapsEntryController.class).getById(lipidQuantity.getLipidMapsCategory().getId(), authentication)).withRel("lipidMapsCategory"));
//        }
//        if (lipidQuantity.getLipidMapsMainClass() != null) {
//            l.add(linkTo(
//                    WebMvcLinkBuilder.methodOn(LipidMapsEntryController.class).getById(lipidQuantity.getLipidMapsMainClass().getId(), authentication)).withRel("lipidMapsMainClass"));
//        }
//        if (lipidQuantity.getLipidMapsSubClass() != null) {
//            l.add(linkTo(
//                    WebMvcLinkBuilder.methodOn(LipidMapsEntryController.class).getById(lipidQuantity.getLipidMapsSubClass().getId(), authentication)).withRel("lipidMapsSubClass"));
//        }
        return l;
    }
}
