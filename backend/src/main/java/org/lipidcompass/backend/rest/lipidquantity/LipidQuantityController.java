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

import java.util.List;
import org.lipidcompass.backend.repository.LipidQuantityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.lipidcompass.backend.rest.AbstractArangoController;
import org.lipidcompass.backend.rest.DtoRepresentationModelAssembler;
import org.lipidcompass.data.model.LipidQuantity;
import org.lipidcompass.data.model.dto.MzTabResultsForLipid;
import org.lipidcompass.data.model.dto.query.LipidCompassQuery;
import org.lifstools.jgoslin.domain.LipidLevel;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import static org.lipidcompass.data.model.Roles.ROLE_USER;
import org.lipidcompass.data.model.dto.query.MatchMode;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author nilshoffmann
 */
@Slf4j
@RestController
@ExposesResourceFor(LipidQuantity.class)
@RequestMapping(value = "/lipidquantity",
        produces = "application/hal+json")
public class LipidQuantityController extends AbstractArangoController<LipidQuantity, LipidQuantityRepository> {

    @Autowired
    public LipidQuantityController(LipidQuantityRepository repository,
            EntityLinks entityLinks) {
        super(repository, entityLinks);
    }

//    @ResponseBody
//    @GetMapping("/findByNativeId/{nativeId}")
//    public ResponseEntity<EntityModel<LipidQuantity>> findByNativeId(@PathVariable String nativeId, Authentication authentication) throws Exception {
//        return ResponseEntity.ok(assembler.toModel(repository.findByNativeId(nativeId)));
//    }
    @PageableAsQueryParam
    @Secured(ROLE_USER)
    @ResponseBody
    @PostMapping("/findByLipidQuery")
    public ResponseEntity<PagedModel<EntityModel<LipidQuantity>>> findByLipidQuery(@RequestBody(required = false) @Valid LipidCompassQuery lipidQuery, @ParameterObject @PageableDefault(value = 50) Pageable p) {
        if (lipidQuery == null || (lipidQuery.getLipidLevel() == null && lipidQuery.getNames() == null)) {
            return ResponseEntity.ok(assembler.toPagedModel(repository.findAll(p)));
        }
        log.info("Query: {}", lipidQuery);
        if (lipidQuery.getLipidLevel() == null || lipidQuery.getLipidLevel() == LipidLevel.UNDEFINED_LEVEL) {
            if (lipidQuery.getMatchMode() == MatchMode.EXACT) {
                return ResponseEntity.ok(assembler.toPagedModel(repository.findByLipidNormalizedShorthandNameIn(lipidQuery.getNames(), p)));
            } else {
                return ResponseEntity.ok(assembler.toPagedModel(repository.findByLipidNormalizedShorthandNameStartsWithIn(lipidQuery.getNames(), p)));
            }
        } else {
            if (lipidQuery.getMatchMode() == MatchMode.EXACT) {
                return ResponseEntity.ok(assembler.toPagedModel(repository.findByLipidLevelAndLipidNormalizedShorthandNameIn(lipidQuery.getLipidLevel(), lipidQuery.getNames(), p)));
            } else {
                return ResponseEntity.ok(assembler.toPagedModel(repository.findByLipidLevelAndLipidNormalizedShorthandNameStartsWithIn(lipidQuery.getLipidLevel(), lipidQuery.getNames(), p)));
            }
        }
    }

    @PageableAsQueryParam
    @Secured(ROLE_USER)
    @ResponseBody
    @GetMapping("findMzTabResultsForLipid/{lipidId}")
    public ResponseEntity<CollectionModel<EntityModel<MzTabResultsForLipid>>> findMzTabResultsForLipid(@PathVariable String lipidId) {
        DtoRepresentationModelAssembler modelAssembler = new DtoRepresentationModelAssembler(entityLinks);
        List<MzTabResultsForLipid> p = repository.findMzTabResultsForLipid("lipids/" + lipidId);
        log.info("Retrieved {} for lipidId: {}", p, lipidId);
        return ResponseEntity.ok(modelAssembler.toCollectionModel(p));
    }

//    @ResponseBody
//    @PostMapping("findLipidsForMzTabResults")
//    public ResponseEntity<EntityModel<LipidsForMzTabResults>> findLipidsForMzTabResults(@RequestBody @Valid LipidsForMzTabResults lipidForMzTabResults, Pageable page) {        
//        DtoRepresentationModelAssembler modelAssembler = new DtoRepresentationModelAssembler(entityLinks);
//        LipidsForMzTabResults result = repository.findLipidsForMzTabResults(lipidForMzTabResults.getMzTabResultIds(), page);
//        result.setLipidIds(lipidForMzTabResults.getLipidIds());
//        log.info("Retrieved {} lipidIds for {} mzTabResults.", result.getLipidIds().size(), lipidForMzTabResults.getMzTabResultIds().size());
//        return ResponseEntity.ok(modelAssembler.toModel(result));
//    }
}
