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
package org.lipidcompass.backend.rest.index;

import org.lipidcompass.backend.rest.crossreference.CrossReferenceController;
import org.lipidcompass.backend.rest.cvparameter.CvParameterController;
import org.lipidcompass.backend.rest.cvs.ControlledVocabularyController;
import org.lipidcompass.backend.rest.study.StudyController;
import org.lipidcompass.backend.rest.lipidquantity.LipidQuantityController;
import org.lipidcompass.backend.rest.lipidmaps.LipidMapsEntryController;
import org.lipidcompass.backend.rest.mztabresult.MzTabResultController;
import org.lipidcompass.backend.rest.stats.StatsController;
import org.lipidcompass.backend.rest.swisslipids.SwissLipidsEntryController;
import org.lipidcompass.backend.rest.user.UserController;
import org.lipidcompass.data.model.Lipid;
import org.springframework.hateoas.RepresentationModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nilshoffmann
 */
@RestController
@RequestMapping({"/", ""})
public class IndexController {

    @RequestMapping(method = RequestMethod.GET)
    public RepresentationModel index() {
        RepresentationModel index = new RepresentationModel();
        index.add(linkTo(StatsController.class).withRel("stats"));
        index.add(linkTo(Lipid.class).withRel("lipid"));
        index.add(linkTo(LipidQuantityController.class).withRel("lipidquantity"));
        index.add(linkTo(StudyController.class).withRel("study"));
        index.add(linkTo(MzTabResultController.class).withRel("mztab"));
        index.add(linkTo(ControlledVocabularyController.class).withRel("cv"));
        index.add(linkTo(CvParameterController.class).withRel("cvparameter"));
        index.add(linkTo(CrossReferenceController.class).withRel("crossreference"));
        index.add(linkTo(LipidMapsEntryController.class).withRel("lipidmaps"));
        index.add(linkTo(SwissLipidsEntryController.class).withRel("swisslipids"));
        index.add(linkTo(UserController.class).withRel("user"));
        return index;
    }
}
