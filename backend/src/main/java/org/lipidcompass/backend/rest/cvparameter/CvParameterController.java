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

import org.lipidcompass.backend.repository.CvParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.lipidcompass.backend.rest.AbstractArangoController;
import org.lipidcompass.backend.services.LipidMappingService;
import org.lipidcompass.data.model.CvParameter;
import org.lipidcompass.data.model.dto.GroupedCvParameters;
import org.lipidcompass.data.model.relations.HasCvParameterReference;
import de.isas.mztab2.model.Parameter;
import static org.lipidcompass.data.model.Roles.ROLE_USER;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.hateoas.CollectionModel;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;

/**
 * TODO implement authentication / checking support for owner and visibility.
 *
 * @author nilshoffmann
 */
@RestController
@ExposesResourceFor(Parameter.class)
@RequestMapping(value = "/cvparameter",
        produces = "application/hal+json")
public class CvParameterController extends AbstractArangoController<CvParameter, CvParameterRepository> {

    private final LipidMappingService lipidMappingService;

    @Autowired
    public CvParameterController(CvParameterRepository repository,
            EntityLinks entityLinks, LipidMappingService lipidMappingService) {
        super(repository, entityLinks);
        this.lipidMappingService = lipidMappingService;
    }

    @PageableAsQueryParam
    @Secured(ROLE_USER)
    @ResponseBody
    @GetMapping("/findByAccession/{accession}")
    public ResponseEntity<PagedModel<EntityModel<CvParameter>>> findByAccession(@PathVariable String accession, @ParameterObject Pageable p, Authentication authentication) throws Exception {
        Page<CvParameter> page = repository.findAllByAccession(accession, p);
        return ResponseEntity.ok(assembler.toPagedModel(page, WebMvcLinkBuilder.linkTo(
                this.getClass()).
                withSelfRel()));
    }

    @PageableAsQueryParam
    @Secured(ROLE_USER)
    @ResponseBody
    @GetMapping("/findByName/{name}")
    public ResponseEntity<PagedModel<EntityModel<CvParameter>>> findByName(@PathVariable String name, @ParameterObject Pageable p, Authentication authentication) throws Exception {
        Page<CvParameter> page = repository.findAllByName(name, p);
        return ResponseEntity.ok(assembler.toPagedModel(page, WebMvcLinkBuilder.linkTo(
                this.getClass()).
                withSelfRel()));
    }

    @PageableAsQueryParam
    @Secured(ROLE_USER)
    @ResponseBody
    @GetMapping("/findByReferenceType/{referenceType}")
    public ResponseEntity<PagedModel<EntityModel<CvParameter>>> findByName(@PathVariable HasCvParameterReference.ReferenceType referenceType, @ParameterObject Pageable p, Authentication authentication) throws Exception {
        Page<CvParameter> page = repository.findAllByReferenceType(referenceType, p);
        return ResponseEntity.ok(assembler.toPagedModel(page, WebMvcLinkBuilder.linkTo(
                this.getClass()).
                withSelfRel()));
    }

    @Secured(ROLE_USER)
    @ResponseBody
    @GetMapping("/grouped")
    public ResponseEntity<CollectionModel<GroupedCvParameters>> getGrouped(Authentication authentication) throws Exception {
        Page<GroupedCvParameters> page = repository.findAllGroupedByReferenceType(Pageable.unpaged());
        CollectionModel<GroupedCvParameters> cm = CollectionModel.of(page.getContent());
        cm.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(CvParameterController.class).getGrouped(authentication)).
                withSelfRel());
        return ResponseEntity.ok(cm);
    }
//    
//    @ResponseBody
//    @GetMapping("/findByCv/{name}")
//    public ResponseEntity<PagedModel<EntityModel<CvParameter>>> findAllByCv(@PathVariable String name, Pageable p, Authentication authentica) throws Exception {
//        Page<CvParameter> page = repository.findAllByCv(name, p);
//        return ResponseEntity.ok(assembler.toPagedModel(page, WebMvcLinkBuilder.linkTo(
//                this.getClass()).
//                withSelfRel()));
//    }

//
//    @ResponseBody
//    @GetMapping("/{id}/quantities")
//    public ResponseEntity<EntityModel<LipidQuantity>> getQuantities(@PathVariable String id) throws Exception {
//        Optional<MzTabResult> optResult = repository.findById(id);
//        MzTabResult result = optResult.get();
//        MzTab mzTab = result.getMzTab();
//        for (SmallMoleculeSummary sms : mzTab.getSmallMoleculeSummary()) {
//            LipidQuantity q = LipidQuantity.builder().
//                    identificationReliability(mzTab.getMetadata().getSmallMoleculeIdentificationReliability()).
//                    quantitationUnit(mzTab.getMetadata().getSmallMoleculeQuantificationUnit()).
//                    source(result).tissue(tissue);
//            build();
//            q.setLipid(lipidMappingService.findByNames(sms.getChemicalName()));
//            //q.setOrganism(sms.get);
//            //q.setTissue(tissue);
//            q.setQuantitationUnit(result.get().getMzTab().getMetadata().getSmallMoleculeQuantificationUnit());
//            q.setIdentificationReliability(result.get().getMzTab().getMetadata().getSmallMoleculeIdentificationReliability());
//            for (StudyVariable sv : result.get().getMzTab().getMetadata().getStudyVariable()) {
//                Parameter svAverageFunction = sv.getAverageFunction();
//                Parameter svVariationFunction = sv.getVariationFunction();
////                q.setsv.getName()
//                q.setStudyVariableQuantities(studyVariableQuantities);
//            }
//        }
//    }
}
