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

import org.lifstools.jgoslin.domain.LipidLevel;
import org.lipidcompass.backend.repository.LipidQuantityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.lipidcompass.data.model.LipidQuantity;
import org.lipidcompass.data.model.PlottableLipidQuantity;
import org.lipidcompass.data.model.PlottableLipidSummaryStats;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import static org.lipidcompass.backend.rest.MappingUtils.getLipidLevelAttributeName;
import org.lipidcompass.data.model.LipidQuantityDatasetAssayTableRow;
import org.lipidcompass.data.model.dto.SummarizedLipidDataset;
import org.lipidcompass.data.model.dto.SummarizedLipidStatistics;
import org.lipidcompass.data.model.dto.query.LipidCompassQuery;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author nilshoffmann
 */
@RestController
@ExposesResourceFor(LipidQuantity.class)
@RequestMapping(value = "/lipidquantity/plot",
        produces = "application/hal+json")
@Slf4j
public class PlottableLipidQuantityController {

    private final LipidQuantityRepository repository;
    private final EntityLinks entityLinks;
//    private final DefaultRepresentationModelAssembler<PlottableLipidQuantity> assembler;

    @Autowired
    public PlottableLipidQuantityController(LipidQuantityRepository repository,
            EntityLinks entityLinks) {
        this.repository = repository;
        this.entityLinks = entityLinks;
//        this.assembler = new DefaultRepresentationModelAssembler<PlottableLipidQuantity>(entityLinks);
    }

    @Cacheable("plottableLipidQuantity")
    @PageableAsQueryParam
    @Secured("ROLE_USER")
    @ResponseBody
    @PostMapping("/findGroupedByLipid")
    public ResponseEntity<Page<PlottableLipidQuantity>> findLipidQuantitiesGroupedByLipid(@RequestBody @Valid LipidCompassQuery lipidQuery, @ParameterObject Pageable p, Authentication authentication) throws Exception {
//        if (p.getPageSize() == -1) {
        log.info("Running request unpaged!");
        return ResponseEntity.ok(repository.findLipidQuantitiesGroupedByLipid(lipidQuery.getNames(), lipidQuery.getMzTabResults().stream().map((mzTabResult) -> {
            return mzTabResult.getId();
        }).collect(Collectors.toList()), Pageable.unpaged()));
//        }
//        return ResponseEntity.ok(repository.findLipidQuantitiesGroupedByLipid(p, lipidQuery.getNames(), lipidQuery.getMzTabResults().stream().map((mzTabResult) -> {
//            return mzTabResult.getId();
//        }).collect(Collectors.toList())));
    }

    @Cacheable("plottableLipidSummaryStats")
    @PageableAsQueryParam
    @Secured("ROLE_USER")
    @ResponseBody
    @PostMapping("/findSummaryStatsGroupedByLipid")
    public ResponseEntity<Page<PlottableLipidSummaryStats>> findLipidSummaryStatsGroupedByLipid(@RequestBody @Valid LipidCompassQuery lipidQuery, @ParameterObject Pageable p, Authentication authentication) throws Exception {
        if (p.getPageSize() == -1) {
            log.info("Running request unpaged!");
            return ResponseEntity.ok(repository.findLipidSummaryStatsGroupedByLipids(lipidQuery.getNames(), lipidQuery.getMzTabResults().stream().map((mzTabResult) -> {
                return mzTabResult.getId();
            }).collect(Collectors.toList()), Pageable.unpaged()));
        }
        return ResponseEntity.ok(repository.findLipidSummaryStatsGroupedByLipids(lipidQuery.getNames(), lipidQuery.getMzTabResults().stream().map((mzTabResult) -> {
            return mzTabResult.getId();
        }).collect(Collectors.toList()), p));
    }

    @Cacheable("summarizedLipidStatistics")
    @Secured("ROLE_USER")
    @ResponseBody
    @GetMapping("/findyByLipidLevel/{lipidLevel}")
    public ResponseEntity<List<SummarizedLipidStatistics>> findByLipidLevel(@PathVariable LipidLevel lipidLevel, Authentication authentication) throws Exception {
        String attributeName = getLipidLevelAttributeName(lipidLevel);
        return ResponseEntity.ok(repository.findByLipidLevel(attributeName));
    }

    @Cacheable("summarizedLipidDataset")
    @Secured("ROLE_USER")
    @ResponseBody
    @GetMapping("/findyByMzTabResultIdAndByLipidLevel/{mzTabResultId}/{lipidLevel}")
    public ResponseEntity<SummarizedLipidDataset> findByMzTabResultIdAndByLipidLevel(@PathVariable String mzTabResultId, @PathVariable LipidLevel lipidLevel, Authentication authentication) throws Exception {
        String attributeName = getLipidLevelAttributeName(lipidLevel);
        return ResponseEntity.ok(repository.findyByMzTabResultIdAndByLipidLevel(mzTabResultId, attributeName));
    }

    @Cacheable("lipidQuantityDatasetAssayTableRow")
    @Secured("ROLE_USER")
    @ResponseBody
    @PostMapping("/findLipidQuantityTableByMzTabResultIdsAndNormalizedShorthandName")
    public ResponseEntity<Page<LipidQuantityDatasetAssayTableRow>> findLipidQuantityTableByMzTabResultIdsAndNormalizedShorthandName(@RequestBody @Valid LipidCompassQuery lipidQuery, @ParameterObject Pageable p, Authentication authentication) throws Exception {
        if (p.getPageSize() == -1) {
            log.info("Running request unpaged!");
            return ResponseEntity.ok(repository.findLipidQuantityTableByMzTabResultIdsAndNormalizedShorthandName(lipidQuery.getNames(), lipidQuery.getMzTabResults().stream().map((mzTabResult) -> {
                return mzTabResult.getId();
            }).collect(Collectors.toList()), Pageable.unpaged()));
        }
        return ResponseEntity.ok(repository.findLipidQuantityTableByMzTabResultIdsAndNormalizedShorthandName(lipidQuery.getNames(), lipidQuery.getMzTabResults().stream().map((mzTabResult) -> {
            return mzTabResult.getId();
        }).collect(Collectors.toList()), p));
    }

}
