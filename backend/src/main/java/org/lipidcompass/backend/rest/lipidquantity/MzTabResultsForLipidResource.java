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

import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.lipidcompass.data.model.dto.MzTabResultsForLipid;
/**
 *
 * @author nilshoffmann
 */
@Getter
public class MzTabResultsForLipidResource extends RepresentationModel {
    
    private MzTabResultsForLipid mzTabResultsForLipid;
    public MzTabResultsForLipidResource(String lipidId, MzTabResultsForLipid mzTabResultsForLipid) {
        this.mzTabResultsForLipid = mzTabResultsForLipid;
        add(linkTo(methodOn(LipidQuantityController.class).findMzTabResultsForLipid(lipidId)).withSelfRel());
    }
}