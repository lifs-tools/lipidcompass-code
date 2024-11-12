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
package org.lipidcompass.backend.rest.mztabresult;

import org.lipidcompass.backend.repository.MzTabResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
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
import org.lipidcompass.data.model.MzTabResult;
import de.isas.mztab2.model.Metadata;
import de.isas.mztab2.model.SmallMoleculeEvidence;
import de.isas.mztab2.model.SmallMoleculeFeature;
import de.isas.mztab2.model.SmallMoleculeSummary;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.lifstools.jgoslin.domain.LipidAdduct;
import static org.lipidcompass.backend.rest.MappingUtils.getLipidLevelAttributeName;
import static org.lipidcompass.data.model.Roles.ROLE_ADMIN;
import static org.lipidcompass.data.model.Roles.ROLE_USER;
import org.lipidcompass.data.model.dto.CvParameterFacet;
import org.lipidcompass.data.model.dto.query.LipidCompassQuery;
import org.lipidcompass.data.model.dto.query.MatchMode;
import org.lipidcompass.data.parser.GoslinAllGrammarsParser;
//import org.nd4j.linalg.api.ndarray.INDArray;
//import org.nd4j.linalg.dimensionalityreduction.PCA;
//import org.nd4j.linalg.factory.Nd4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * TODO implement authentication / checking support for owner and visibility.
 *
 * @author nilshoffmann
 */
@RestController
@ExposesResourceFor(MzTabResult.class)
@RequestMapping(value = "/mztab",
        produces = "application/hal+json")
public class MzTabResultController extends AbstractArangoController<MzTabResult, MzTabResultRepository> {

    private final static Logger log = LoggerFactory.getLogger(MzTabResultController.class);

    private final LipidMappingService lipidMappingService;

    private final GoslinAllGrammarsParser lipidNameParser = new GoslinAllGrammarsParser();

    @Autowired
    public MzTabResultController(MzTabResultRepository repository,
            EntityLinks entityLinks, LipidMappingService lipidMappingService) {
        super(repository, entityLinks);
        this.lipidMappingService = lipidMappingService;
    }

    @PageableAsQueryParam
    @Override
    @ResponseBody
    @GetMapping(path = {"", "/"})
    public ResponseEntity<PagedModel<EntityModel<MzTabResult>>> get(@ParameterObject Pageable p, Authentication authentication) {
        Page<MzTabResult> page = repository.findAll(p);
        CollectionModel<MzTabResult> cm = CollectionModel.of(
                page,
                WebMvcLinkBuilder.linkTo(this.getClass()).withSelfRel()
        );
        PagedModel.PageMetadata meta = new PagedModel.PageMetadata(page.getNumberOfElements(), page.getNumber(), page.getTotalElements(), page.getTotalPages());
        PagedModel<EntityModel<MzTabResult>> pagedResources = PagedModel.wrap(cm, meta);
        pagedResources.add(linkTo(methodOn(this.getClass()).
                deleteById(null, authentication)).
                withRel("delete"),
                linkTo(methodOn(this.getClass()).
                        count(authentication)).
                        withRel("count"),
                //                linkTo(methodOn(this.getClass()).
                //                        countByExample(null, authentication)).
                //                        withRel("countByExample"),
                linkTo(methodOn(this.getClass()).
                        saveSingle(null, authentication)).
                        withRel("saveSingle"),
                linkTo(methodOn(this.getClass()).
                        saveAll(null, authentication)).
                        withRel("saveAll"),
                linkTo(methodOn(this.getClass()).
                        getById(null, authentication)).
                        withRel("get"),
                linkTo(methodOn(this.getClass()).
                        getRandom(authentication)).
                        withRel("random")
        );
        return ResponseEntity.ok(pagedResources);
    }

    @Secured(ROLE_USER)
    @ResponseBody
    @GetMapping("/pid/{nativeId}")
    public ResponseEntity<EntityModel<? extends MzTabResult>> getByNativeId(@PathVariable String nativeId, Authentication authentication) throws Exception {
        Optional<MzTabResult> mzTabResult = repository.findByNativeId(nativeId);
        if (mzTabResult.isPresent()) {
            EntityModel<? extends MzTabResult> metadataResource = EntityModel.of(mzTabResult.get());
            metadataResource.add(WebMvcLinkBuilder.linkTo(
                    this.getClass()).withSelfRel());
            metadataResource.add(WebMvcLinkBuilder.linkTo(
                    this.getClass()).slash(mzTabResult.get().getId()).slash("metadata").withRel("metadata"));
            metadataResource.add(WebMvcLinkBuilder.linkTo(
                    this.getClass()).slash(mzTabResult.get().getId()).slash("summary").withRel("summary"));
            return ResponseEntity.ok(metadataResource);
        }
        return ResponseEntity.notFound().build();
    }
    
    @Override
    @ResponseBody
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<? extends MzTabResult>> getById(@PathVariable("id") String id, Authentication authentication) {
        Optional<MzTabResult> result = repository.findById(id);
        if (result.isPresent()) {
            EntityModel<? extends MzTabResult> metadataResource = EntityModel.of(result.get());
            metadataResource.add(WebMvcLinkBuilder.linkTo(
                    this.getClass()).withSelfRel());
            metadataResource.add(WebMvcLinkBuilder.linkTo(
                    this.getClass()).slash(result.get().getId()).slash("metadata").withRel("metadata"));
            metadataResource.add(WebMvcLinkBuilder.linkTo(
                    this.getClass()).slash(result.get().getId()).slash("summary").withRel("summary"));
            return ResponseEntity.ok(metadataResource);
        }
        return ResponseEntity.notFound().build();
    }

    @Secured(ROLE_USER)
    @ResponseBody
    @GetMapping("/{id}/metadata")
    public ResponseEntity<EntityModel<Metadata>> getMetadata(@PathVariable String id, Authentication authentication) throws Exception {
        MzTabResult result = repository.findById(id).get();
        EntityModel<Metadata> metadataResource = EntityModel.of(result.getMzTabData().getMzTab().getMetadata());
        metadataResource.add(WebMvcLinkBuilder.linkTo(
                this.getClass()).withSelfRel());
        metadataResource.add(WebMvcLinkBuilder.linkTo(
                this.getClass()).slash(result).withRel("mzTab"));
        return ResponseEntity.ok(metadataResource);
    }

    @PageableAsQueryParam
    @Secured(ROLE_USER)
    @ResponseBody
    @GetMapping("/{id}/summary")
    public ResponseEntity<CollectionModel<EntityModel<SmallMoleculeSummary>>> getSml(@PathVariable String id, @ParameterObject Pageable p, PagedResourcesAssembler<SmallMoleculeSummary> pagedResourceAssembler, Authentication authentication) throws Exception {
        MzTabResult result = repository.findById(id).get();
        List<EntityModel<SmallMoleculeSummary>> list = result.getMzTabData().getMzTab().getSmallMoleculeSummary().stream().map((sml) -> {
            EntityModel<SmallMoleculeSummary> res = EntityModel.of(sml);
            return res;
        }).collect(Collectors.toList());
        CollectionModel<EntityModel<SmallMoleculeSummary>> res = CollectionModel.of(list, WebMvcLinkBuilder.linkTo(
                this.getClass()).withSelfRel(), WebMvcLinkBuilder.linkTo(
                this.getClass()).slash(result).withRel("mzTab"), WebMvcLinkBuilder.linkTo(
                this.getClass()).slash(result).slash("metadata").withRel("metadata"));
        return ResponseEntity.ok(res);
    }

    @PageableAsQueryParam
    @Secured(ROLE_USER)
    @ResponseBody
    @GetMapping("/{id}/feature")
    public ResponseEntity<CollectionModel<EntityModel<SmallMoleculeFeature>>> getSmf(@PathVariable String id, @ParameterObject Pageable p, PagedResourcesAssembler<SmallMoleculeFeature> pagedResourceAssembler, Authentication authentication) throws Exception {
        MzTabResult result = repository.findById(id).get();
        List<EntityModel<SmallMoleculeFeature>> list = result.getMzTabData().getMzTab().getSmallMoleculeFeature().stream().map((sml) -> {
            EntityModel<SmallMoleculeFeature> res = EntityModel.of(sml);
            return res;
        }).collect(Collectors.toList());
        CollectionModel<EntityModel<SmallMoleculeFeature>> res = CollectionModel.of(list, WebMvcLinkBuilder.linkTo(
                this.getClass()).withSelfRel(), WebMvcLinkBuilder.linkTo(
                this.getClass()).slash(result).withRel("mzTab"), WebMvcLinkBuilder.linkTo(
                this.getClass()).slash(result).slash("metadata").withRel("metadata"));
        return ResponseEntity.ok(res);
    }

    @PageableAsQueryParam
    @Secured(ROLE_USER)
    @ResponseBody
    @GetMapping("/{id}/evidence")
    public ResponseEntity<CollectionModel<EntityModel<SmallMoleculeEvidence>>> getSme(@PathVariable String id, @ParameterObject Pageable p, PagedResourcesAssembler<SmallMoleculeFeature> pagedResourceAssembler, Authentication authentication) throws Exception {
        MzTabResult result = repository.findById(id).get();
        List<EntityModel<SmallMoleculeEvidence>> list = result.getMzTabData().getMzTab().getSmallMoleculeEvidence().stream().map((sml) -> {
            EntityModel<SmallMoleculeEvidence> res = EntityModel.of(sml);
            return res;
        }).collect(Collectors.toList());
        CollectionModel<EntityModel<SmallMoleculeEvidence>> res = CollectionModel.of(list, WebMvcLinkBuilder.linkTo(
                this.getClass()).withSelfRel(), WebMvcLinkBuilder.linkTo(
                this.getClass()).slash(result).withRel("mzTab"), WebMvcLinkBuilder.linkTo(
                this.getClass()).slash(result).slash("metadata").withRel("metadata"));
        return ResponseEntity.ok(res);
    }

    @Secured(ROLE_USER)
    @ResponseBody
    @GetMapping("/query")
    public ResponseEntity<EntityModel<LipidCompassQuery>> getQuery(Authentication authentication) {
        LipidCompassQuery query = new LipidCompassQuery(Arrays.asList("*"), null, Collections.emptyList(), repository.getFacets(Collections.emptyList()), Collections.emptyList());
        return ResponseEntity.ok(EntityModel.of(query));
    }

    @PageableAsQueryParam
    @Secured(ROLE_USER)
    @ResponseBody
    @PostMapping("/query")
    public ResponseEntity<PagedModel<EntityModel<MzTabResult>>> postQuery(@RequestBody LipidCompassQuery query, @ParameterObject Pageable p, Authentication authentication) {
//        LipidCompassQuery query = new LipidCompassQuery(Collections.emptyList(), Collections.emptyList(), repository.getFacets(), Collections.emptyList());
//        MzTabQueryResponse response = new MzTabQueryResponse();
//        response.setQuery(query);
        String attributeName = getLipidLevelAttributeName(query.getLipidLevel());
        Pageable pageable;
        if (p == null) {
            pageable = PageRequest.of(0, 50);
        } else {
            pageable = p;
        }
//        entityLinks.linkToSearchResource(LipidQuantityDocument.class, LinkRelation.of("search"), pageable).withHref("findByQuery");
        List<String> normalizedNames = query.getNames().stream().filter(name -> name != null && !name.isEmpty()).collect(Collectors.toList());
        if (normalizedNames.contains("*")) {
            log.info("Normalized names contains '*' wildcard, falling back to wildcard search");
            normalizedNames = Collections.emptyList();
        } else {
            if (query.getNormalizeName() && query.getMatchMode() == MatchMode.EXACT) {
                normalizedNames = query.getNames().stream().map((name) -> {
                    Optional<LipidAdduct> lipidAdduct = lipidNameParser.parse(name);
                    if (lipidAdduct.isPresent()) {
                        return lipidAdduct.get().getLipidString(query.getLipidLevel());
                    } else {
                        throw new RuntimeException("Could not normalize " + name + " please check for typing errors!");
                    }
                }).collect(Collectors.toList());
            } else {
                normalizedNames = query.getNames().stream().map((name) -> {
                    return name;
                }).collect(Collectors.toList());
            }
        }
        List<String> mzTabResultIds = Optional.ofNullable(query.getMzTabResults()).orElse(Collections.emptyList()).stream().map((t) -> {
            return t.getId();
        }).collect(Collectors.toList());
        log.info("Running query for level {} and names {} with facets {} and mzTabResultIds {}", attributeName, normalizedNames, query.getSelectedFacets(), mzTabResultIds);
        Page<MzTabResult> mzTabResults = repository.findByQuery(mzTabResultIds, attributeName, normalizedNames, query.getSelectedFacets(), p);
        CollectionModel<MzTabResult> cm = CollectionModel.of(
                mzTabResults,
                WebMvcLinkBuilder.linkTo(this.getClass()).withSelfRel()
        );
        PagedModel.PageMetadata meta = new PagedModel.PageMetadata(mzTabResults.getNumberOfElements(), mzTabResults.getNumber(), mzTabResults.getTotalElements(), mzTabResults.getTotalPages());
        PagedModel<EntityModel<MzTabResult>> pagedResources = PagedModel.wrap(cm, meta);
        return ResponseEntity.ok(pagedResources);
    }

    @Secured(ROLE_USER)
    @ResponseBody
    @PostMapping("/facets")
    public ResponseEntity<CollectionModel<EntityModel<CvParameterFacet>>> getFacets(@RequestBody List<CvParameterFacet> selectedFacets) {
        List<CvParameterFacet> facets = repository.getFacets(selectedFacets);
        List<EntityModel<CvParameterFacet>> list = facets.stream().map((sml) -> {
            EntityModel<CvParameterFacet> res = EntityModel.of(sml);
            return res;
        }).collect(Collectors.toList());
        CollectionModel<EntityModel<CvParameterFacet>> res = CollectionModel.of(list, WebMvcLinkBuilder.linkTo(
                this.getClass()).withSelfRel());
        return ResponseEntity.ok(res);
    }

    @Secured(ROLE_USER)
    @ResponseBody
    @GetMapping("/{id}/msrun/pca")
    public ResponseEntity<EntityModel<MsRunsPcaResultDto>> getMsRunsPca(@PathVariable String id) {
//        Optional<MzTabResult> mzTab = repository.findById(id);
//        if (mzTab.isPresent()) {
//            MzTabResult result = mzTab.get();
//            List<SmallMoleculeSummary> sms = result.getMzTabData().getMzTab().getSmallMoleculeSummary();
//            INDArray array = Nd4j.create(sms.stream().map((t) -> {
//                return Nd4j.create(t.getAbundanceAssay());
//            }).collect(Collectors.toList()), result.getMzTabSummary().getSmlCount(), result.getMzTabSummary().getAssayCount());
//            PCA pca = new PCA(array.transposei());
//            INDArray loadings = pca.convertToComponents(array);
//            MsRunsPcaResultDto dto = new MsRunsPcaResultDto();
//            dto.setComponentsMatrix(loadings.toFloatMatrix());
//            List<String> msRunNames = result.getMzTabData().getMzTab().getMetadata().getMsRun().stream().map(
//                    (msRun) -> msRun.getName()).collect(Collectors.toList());
//            dto.setMsRuns(msRunNames);
//            return ResponseEntity.of(Optional.of(EntityModel.of(dto)));
////            INDArray factor = PCA.pca_factor(array, 2, true);
//        }
        return ResponseEntity.noContent().build();
    }

//    @Secured("ROLE_USER")
//    @ResponseBody
//    @PostMapping("/findByLipidQuery")
//    public ResponseEntity<PagedModel<EntityModel<MzTabResult>>> findByLipidQuery(@RequestBody(required = false) @Valid LipidQuery lipidQuery, @PageableDefault(value = 50) Pageable p) {
//        if (lipidQuery == null || (lipidQuery.getLipidLevel() == null && lipidQuery.getShorthandLipidNames() == null)) {
//            return ResponseEntity.ok(assembler.toPagedModel(repository.findAll(p)));
//        }
//        log.info("Query: {}", lipidQuery);
//        if (lipidQuery.getLipidLevel() == null || lipidQuery.getLipidLevel() == LipidLevel.UNDEFINED_LEVEL) {
//            if (lipidQuery.isExactMatch()) {
//                return ResponseEntity.ok(assembler.toPagedModel(((MzTabResultRepository)repository).findByLipidNormalizedShorthandNameIn(lipidQuery.getShorthandLipidNames(), p)));
//            } else {
//                return ResponseEntity.ok(assembler.toPagedModel(repository.findByLipidNormalizedShorthandNameStartsWithIn(lipidQuery.getShorthandLipidNames(), p)));
//            }
//        } else {
//            if (lipidQuery.isExactMatch()) {
//                return ResponseEntity.ok(assembler.toPagedModel(repository.findByLipidLevelAndLipidNormalizedShorthandNameIn(lipidQuery.getLipidLevel(), lipidQuery.getShorthandLipidNames(), p)));
//            } else {
//                return ResponseEntity.ok(assembler.toPagedModel(repository.findByLipidLevelAndLipidNormalizedShorthandNameStartsWithIn(lipidQuery.getLipidLevel(), lipidQuery.getShorthandLipidNames(), p)));
//            }
//        }
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
