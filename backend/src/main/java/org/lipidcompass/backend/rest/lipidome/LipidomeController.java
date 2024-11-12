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
package org.lipidcompass.backend.rest.lipidome;

import org.lipidcompass.backend.repository.LipidomeRepository;
import org.lipidcompass.backend.rest.AbstractArangoController;
import org.lipidcompass.backend.services.LipidRenderService;
import org.lipidcompass.data.model.Lipidome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author nilshoffmann
 */
@Slf4j
@RestController
@ExposesResourceFor(Lipidome.class)
@RequestMapping(value = "/lipidome",
        produces = "application/hal+json")
public class LipidomeController extends AbstractArangoController<Lipidome, LipidomeRepository> {

    @Autowired
    public LipidomeController(LipidomeRepository repository,
            EntityLinks entityLinks) {
        super(repository, entityLinks);
    }

//     @PageableAsQueryParam
//     @Secured(ROLE_USER)
//     @ResponseBody
//     @PostMapping("/findByQuery")
//     public ResponseEntity<PagedModel<EntityModel<Lipidome>>> findByQuery(@RequestBody LipidCompassQuery lipidQuery, @ParameterObject @PageableDefault(value = 50) Pageable p) {
//         log.debug("Query: {}", lipidQuery);
//         if (lipidQuery == null || (lipidQuery.getLipidLevel() == null && lipidQuery.getNames() == null)) {
//             return ResponseEntity.ok(assembler.toPagedModel(repository.findAll(p)));
//         }

//         String attributeName = getLipidLevelAttributeName(lipidQuery.getLipidLevel());
//         GoslinAllGrammarsParser lipidNameParser = new GoslinAllGrammarsParser();
//         Pageable pageable;
//         if (p == null) {
//             pageable = PageRequest.of(0, 50);
//         } else {
//             pageable = p;
//         }
// //        entityLinks.linkToSearchResource(LipidQuantityDocument.class, LinkRelation.of("search"), pageable).withHref("findByQuery");
//         List<String> normalizedNames = lipidQuery.getNames().stream().filter(name -> name != null && !name.isEmpty()).collect(Collectors.toList());
//         if (normalizedNames.contains("*")) {
//             log.info("Normalized names contains '*' wildcard, falling back to wildcard search");
//             normalizedNames = Collections.emptyList();
//         } else {
//             if (lipidQuery.getNormalizeName() && lipidQuery.getMatchMode() == MatchMode.EXACT && (lipidQuery.getLipidLevel() != LipidLevel.CATEGORY || lipidQuery.getLipidLevel() != LipidLevel.CLASS)) {
//                 normalizedNames = lipidQuery.getNames().stream().map((name) -> {
//                     Optional<LipidAdduct> lipidAdduct = lipidNameParser.parse(name);
//                     if (lipidAdduct.isPresent()) {
//                         if (lipidQuery.getLipidLevel() == LipidLevel.UNDEFINED_LEVEL || lipidQuery.getLipidLevel() == LipidLevel.NO_LEVEL) {
//                             return lipidAdduct.get().getLipidString();
//                         }
//                         return lipidAdduct.get().getLipidString(lipidQuery.getLipidLevel());
//                     } else {
//                         log.warn("Could not normalize " + name + " please check for typing errors!");
//                         return name;
// //                        throw new RuntimeException("Could not normalize " + name + " please check for typing errors!");
//                     }
//                 }).collect(Collectors.toList());
//             } else {
//                 normalizedNames = lipidQuery.getNames().stream().map((name) -> {
//                     return name;
//                 }).collect(Collectors.toList());
//             }
//         }
//         log.debug("Querying ");
//         return ResponseEntity.ok(assembler.toPagedModel(
//                 repository.findByLipidLevelAndNormalizedShorthandNameMatches(
//                         lipidQuery.getLipidLevel(),
//                         lipidQuery.getSumFormula(),
//                         lipidQuery.getMinMass(),
//                         lipidQuery.getMaxMass(),
//                         normalizedNames,
//                         lipidQuery.getMatchMode(),
//                         pageable
//                 )
//         )
//         );
//     }

    // @Secured(ROLE_USER)
    // @ResponseBody
    // @GetMapping("/table")
    // public ResponseEntity<Resource> getLipidomeAsTable() throws Exception {
    //     ;
    //     StringBuilder sb = new StringBuilder();
    //     HashSet<String> keys = new LinkedHashSet<>();
    //     List<Map<String, String>> entries = Streams.stream(repository.findAll()).map((t) -> {
    //         Map<String, String> m = new LinkedHashMap<>();
    //         m.put("Species", "[NCBITaxon, NCBITaxon:9606, Homo sapiens, ]");
    //         m.put("Tissue", "[BTO, BTO:0000131, blood plasma, ]");
    //         m.put("Cell Type", null);
    //         m.put("Disease", null);
    //         m.put("Normalized Name", t.getNormalizedShorthandName());
    //         m.put("Common Name", t.getCommonName());
    //         m.put("Systematic Name", t.getSystematicName());
    //         m.put("Lipid Level", t.getLipidLevel().name());
    //         m.put("Lipid Category", t.getLipidCategory());
    //         m.put("Lipid Class", t.getLipidClass());
    //         m.put("Lipid Species", t.getLipidSpecies());
    //         m.put("Lipid Molecular Subspecies", t.getLipidMolecularSpecies());
    //         m.put("Lipid Structure Defined", t.getLipidStructureDefined());
    //         m.put("Lipid SN Position", t.getLipidSnPosition());
    //         m.put("Lipid Complete Structure", t.getLipidCompleteStructure());
    //         m.put("Lipid Full Structure", t.getLipidFullStructure());
    //         m.put("SMILES", t.getSmiles());
    //         m.put("Exact Mass", String.format("%.4f", t.getExactMass()));
    //         m.put("Formula", t.getChemicalFormula());
    //         m.put("Lipid Maps References", t.getLipidMapsEntry().stream().map((r) -> {
    //             return r.getNativeUrl();
    //         }).collect(Collectors.joining(" | ")));
    //         m.put("Swiss Lipids References", t.getSwissLipidsEntry().stream().map((r) -> {
    //             return r.getNativeUrl();
    //         }).collect(Collectors.joining(" | ")));
    //         keys.addAll(m.keySet());
    //         return m;
    //     }).collect(Collectors.toList());
    //     sb.append(keys.stream().collect(Collectors.joining("\t"))).append("\n");
    //     for (Map<String, String> m : entries) {
    //         List<String> l = new LinkedList();
    //         for (String key : keys) {
    //             l.add(m.getOrDefault(key, ""));
    //         }
    //         sb.append(l.stream().collect(Collectors.joining("\t"))).append("\n");
    //     }
    //     String tableContent = sb.toString();
    //     Resource file = new InMemoryResource(tableContent);
    //     return ResponseEntity.ok().
    //             header(HttpHeaders.CONTENT_DISPOSITION,
    //                     "attachment; filename=\"" + file.getFilename() + "\"").
    //             contentType(MediaType.TEXT_PLAIN).
    //             body(file);
    // }
//    @PreAuthorize("permitAll()")
//    @Secured(ROLE_USER)
    @ResponseBody
    @GetMapping("/random")
    @Override
    public ResponseEntity<EntityModel<Lipidome>> getRandom(Authentication authentication) {
        return super.getRandom(authentication);
    }

}
