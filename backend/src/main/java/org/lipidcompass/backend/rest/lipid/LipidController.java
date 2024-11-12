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

import com.google.common.collect.Streams;
import io.swagger.v3.oas.annotations.Parameter;
import org.lipidcompass.data.model.Lipid;
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
import org.lipidcompass.backend.repository.LipidRepository;
import org.lipidcompass.backend.rest.AbstractArangoController;
import org.lipidcompass.backend.services.LipidRenderService;
import org.lipidcompass.data.model.dto.query.LipidCompassQuery;
import org.lifstools.jgoslin.domain.LipidLevel;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.lifstools.jgoslin.domain.LipidAdduct;
import org.lifstools.jgoslin.domain.LipidCategory;
import org.lifstools.jgoslin.domain.LipidParsingException;
import org.lifstools.jgoslin.parser.LipidParser;
import static org.lipidcompass.backend.rest.MappingUtils.getLipidLevelAttributeName;
import static org.lipidcompass.data.model.Roles.ROLE_USER;
import org.lipidcompass.data.model.Visibility;
import org.lipidcompass.data.model.dto.query.MatchMode;
import org.lipidcompass.data.parser.GoslinAllGrammarsParser;
import org.openscience.cdk.exception.CDKException;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.util.InMemoryResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author nilshoffmann
 */
@Slf4j
@RestController
@ExposesResourceFor(Lipid.class)
@RequestMapping(value = "/lipid",
        produces = "application/hal+json")
public class LipidController extends AbstractArangoController<Lipid, LipidRepository> {

    private LipidRenderService lrs;

    @Autowired
    public LipidController(LipidRepository repository,
            EntityLinks entityLinks,
            LipidRenderService lrs) {
        super(repository, entityLinks);
        this.lrs = lrs;
    }

//    @ResponseBody
//    @GetMapping("/findByNativeId/{nativeId}")
//    public ResponseEntity<EntityModel<Lipid>> findByNativeId(@PathVariable String nativeId, Authentication authentication) throws Exception {
//        return ResponseEntity.ok(assembler.toModel(repository.findByNativeId(nativeId)));
//    }
    @PageableAsQueryParam
    @Secured(ROLE_USER)
    @ResponseBody
    @PostMapping("/findByLipidQuery")
    public ResponseEntity<PagedModel<EntityModel<Lipid>>> findByLipidQuery(@RequestBody LipidCompassQuery lipidQuery, @ParameterObject @PageableDefault(value = 50) Pageable p) {
        log.debug("Query: {}", lipidQuery);
        if (lipidQuery == null || (lipidQuery.getLipidLevel() == null && lipidQuery.getNames() == null)) {
            return ResponseEntity.ok(assembler.toPagedModel(repository.findAll(p)));
        }

        String attributeName = getLipidLevelAttributeName(lipidQuery.getLipidLevel());
        GoslinAllGrammarsParser lipidNameParser = new GoslinAllGrammarsParser();
        Pageable pageable;
        if (p == null) {
            pageable = PageRequest.of(0, 50);
        } else {
            pageable = p;
        }
//        entityLinks.linkToSearchResource(LipidQuantityDocument.class, LinkRelation.of("search"), pageable).withHref("findByQuery");
        List<String> normalizedNames = lipidQuery.getNames().stream().filter(name -> name != null && !name.isEmpty()).collect(Collectors.toList());
        if (normalizedNames.contains("*")) {
            log.info("Normalized names contains '*' wildcard, falling back to wildcard search");
            normalizedNames = Collections.emptyList();
        } else {
            if (lipidQuery.getNormalizeName() && lipidQuery.getMatchMode() == MatchMode.EXACT && (lipidQuery.getLipidLevel() != LipidLevel.CATEGORY || lipidQuery.getLipidLevel() != LipidLevel.CLASS)) {
                normalizedNames = lipidQuery.getNames().stream().map((name) -> {
                    Optional<LipidAdduct> lipidAdduct = lipidNameParser.parse(name);
                    if (lipidAdduct.isPresent()) {
                        if (lipidQuery.getLipidLevel() == LipidLevel.UNDEFINED_LEVEL || lipidQuery.getLipidLevel() == LipidLevel.NO_LEVEL) {
                            return lipidAdduct.get().getLipidString();
                        }
                        return lipidAdduct.get().getLipidString(lipidQuery.getLipidLevel());
                    } else {
                        log.warn("Could not normalize " + name + " please check for typing errors!");
                        return name;
//                        throw new RuntimeException("Could not normalize " + name + " please check for typing errors!");
                    }
                }).collect(Collectors.toList());
            } else {
                normalizedNames = lipidQuery.getNames().stream().map((name) -> {
                    return name;
                }).collect(Collectors.toList());
            }
        }
        log.debug("Querying ");
        return ResponseEntity.ok(assembler.toPagedModel(
                repository.findByLipidLevelAndNormalizedShorthandNameMatches(
                        lipidQuery.getLipidLevel(),
                        lipidQuery.getSumFormula(),
                        lipidQuery.getMinMass(),
                        lipidQuery.getMaxMass(),
                        normalizedNames,
                        lipidQuery.getMatchMode(),
                        pageable
                )
        )
        );
    }

    @Secured(ROLE_USER)
    @ResponseBody
    @GetMapping("/levels")
    public ResponseEntity<CollectionModel<LipidLevel>> getLipidLevels(Authentication authentication) throws Exception {
        CollectionModel<LipidLevel> cm = CollectionModel.of(Arrays.asList(LipidLevel.values()));
        cm.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(LipidController.class).getLipidLevels(authentication)).
                withSelfRel());
        return ResponseEntity.ok(cm);
    }

    @PageableAsQueryParam
    @Secured(ROLE_USER)
    @ResponseBody
    @PostMapping("/levels")
    public ResponseEntity<PagedModel<EntityModel<Lipid>>> getLipidCategoriesForLevel(@RequestBody String level, @ParameterObject @PageableDefault(value = 50) Pageable p) throws Exception {
        if (level == null || level.isEmpty()) {
            return ResponseEntity.ok(assembler.toPagedModel(repository.findAll(p)));
        }
        log.info("Querying by level {}", level);
        Page<Lipid> page = repository.findByLipidLevel(LipidLevel.valueOf(level), p);
        log.info("Found {}", page.getTotalElements());

        return ResponseEntity.ok(assembler.toPagedModel(page));
    }

    @Secured(ROLE_USER)
    @ResponseBody
    @PostMapping("/nameAtLevel")
    public ResponseEntity<CollectionModel<String>> getLipidNameAtLevel(@RequestBody LipidCompassQuery lipidQuery) {
        LipidParser parser = new LipidParser();
        List<String> results = lipidQuery.getNames().stream().map(
                (name)
                -> {
            try {
                LipidAdduct la = parser.parse(name);
                return la.getLipidString(lipidQuery.getLipidLevel());
            } catch (LipidParsingException lpe) {
                log.warn("Could not parse '" + name + "'!", lpe);
                return LipidCategory.UNDEFINED.name();
            }
        }
        ).collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(results));
    }

    @Secured(ROLE_USER)
    @ResponseBody
    @GetMapping("/table")
    public ResponseEntity<Resource> getLipidsAsTable() throws Exception {
        ;
        StringBuilder sb = new StringBuilder();
        HashSet<String> keys = new LinkedHashSet<>();
        List<Map<String, String>> entries = Streams.stream(repository.findAll()).map((t) -> {
            Map<String, String> m = new LinkedHashMap<>();
            m.put("Species", "[NCBITaxon, NCBITaxon:9606, Homo sapiens, ]");
            m.put("Tissue", "[BTO, BTO:0000131, blood plasma, ]");
            m.put("Cell Type", null);
            m.put("Disease", null);
            m.put("Normalized Name", t.getNormalizedShorthandName());
            m.put("Common Name", t.getCommonName());
            m.put("Systematic Name", t.getSystematicName());
            m.put("Lipid Level", t.getLipidLevel().name());
            m.put("Lipid Category", t.getLipidCategory());
            m.put("Lipid Class", t.getLipidClass());
            m.put("Lipid Species", t.getLipidSpecies());
            m.put("Lipid Molecular Subspecies", t.getLipidMolecularSpecies());
            m.put("Lipid Structure Defined", t.getLipidStructureDefined());
            m.put("Lipid SN Position", t.getLipidSnPosition());
            m.put("Lipid Complete Structure", t.getLipidCompleteStructure());
            m.put("Lipid Full Structure", t.getLipidFullStructure());
            m.put("SMILES", t.getSmiles());
            m.put("Exact Mass", String.format("%.4f", t.getExactMass()));
            m.put("Formula", t.getChemicalFormula());
            m.put("Lipid Maps References", t.getLipidMapsEntry().stream().map((r) -> {
                return r.getNativeUrl();
            }).collect(Collectors.joining(" | ")));
            m.put("Swiss Lipids References", t.getSwissLipidsEntry().stream().map((r) -> {
                return r.getNativeUrl();
            }).collect(Collectors.joining(" | ")));
            keys.addAll(m.keySet());
            return m;
        }).collect(Collectors.toList());
        sb.append(keys.stream().collect(Collectors.joining("\t"))).append("\n");
        for (Map<String, String> m : entries) {
            List<String> l = new LinkedList();
            for (String key : keys) {
                l.add(m.getOrDefault(key, ""));
            }
            sb.append(l.stream().collect(Collectors.joining("\t"))).append("\n");
        }
        String tableContent = sb.toString();
        Resource file = new InMemoryResource(tableContent);
        return ResponseEntity.ok().
                header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFilename() + "\"").
                contentType(MediaType.TEXT_PLAIN).
                body(file);
    }

//    @Secured("ROLE_USER")
    @ResponseBody
    @GetMapping("/{id}/svg")
    public ResponseEntity<EntityModel<LipidSvg>> getSmilesSvg(
            @PathVariable("id") String id, Authentication authentication) {
        Optional<Lipid> opt = repository.findById(id);
        if (opt.isPresent() && opt.get().getVisibility() == Visibility.PUBLIC) {
            String mockSmiles = "C[N+](C)(C)CCOP([O-])(=O)OC[C@H](NC([*])=O)[C@H](O)[*]";
            String smiles = opt.get().getSmiles();
            if (smiles == null || smiles.isBlank()) {
                log.warn("No SMILES string available for lipid id=" + id);
                return ResponseEntity.noContent().build();
            }
            try {
                return ResponseEntity.ok(
                        EntityModel.of(
                                LipidSvg.builder().
                                        lipidId(id).
                                        svg(lrs.render(smiles)).
                                        build()
                        )
                );
            } catch (CDKException ex) {
                log.warn("Could not create SMILES SVG for lipid id=" + id, ex);
                return ResponseEntity.noContent().build();
            }
        }
        return ResponseEntity.notFound().
                build();
    }

//    @PreAuthorize("permitAll()")
//    @Secured(ROLE_USER)
    @ResponseBody
    @GetMapping("/random")
    @Override
    public ResponseEntity<EntityModel<Lipid>> getRandom(Authentication authentication) {
        return super.getRandom(authentication);
    }

    @Secured("ROLE_ADMIN")
    @ResponseBody
    @PostMapping("/annotateWithSwissLipidsSmiles")
    public ResponseEntity annotateWithSwissLipidsSmiles() {
        this.repository.annotateWithSwissLipidsSmiles();
        return ResponseEntity.ok().build();
    }

}
