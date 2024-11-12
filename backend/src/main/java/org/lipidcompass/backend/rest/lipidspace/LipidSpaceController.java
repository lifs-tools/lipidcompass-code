/*
 * Copyright 2022 The LipidCompass Developers.
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
package org.lipidcompass.backend.rest.lipidspace;

import java.io.StringWriter;
import java.util.Arrays;
import static org.lipidcompass.data.model.Roles.ROLE_USER;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.lifstools.lipidspace.client.api.DefaultApi;
import org.lifstools.lipidspace.client.model.LipidSpacePcaRequest;
import org.lifstools.lipidspace.client.model.LipidSpacePcaResponse;
import org.lifstools.lipidspace.client.model.TableType;
import org.lipidcompass.backend.repository.LipidQuantityRepository;
import org.lipidcompass.lipidspace.model.LipidSpaceQueryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import lombok.extern.slf4j.Slf4j;
import org.lifstools.lipidspace.client.model.TableColumnType;
import org.lipidcompass.data.model.LipidQuantityDatasetAssayTableRow;
import org.springframework.data.domain.Page;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

/**
 *
 * @author Nils Hoffmann
 */
@Slf4j
@RestController
@RequestMapping(value = "/lipidspace",
        produces = "application/hal+json")
public class LipidSpaceController {

    private final EntityLinks entityLinks;
    private final LipidQuantityRepository lipidQuantityRepository;
    private final DefaultApi lipidSpaceApi;
//    private final DefaultRepresentationModelAssembler<PlottableLipidQuantity> assembler;

    @Autowired
    public LipidSpaceController(LipidQuantityRepository lipidQuantityRepository,
            EntityLinks entityLinks,
            @Qualifier("lipidSpaceDefaultApi") DefaultApi lipidSpaceApi) {
        this.lipidQuantityRepository = lipidQuantityRepository;
        this.entityLinks = entityLinks;
        this.lipidSpaceApi = lipidSpaceApi;
//        this.assembler = new DefaultRepresentationModelAssembler<PlottableLipidQuantity>(entityLinks);
    }

    @Secured(ROLE_USER)
    @ResponseBody
    @PostMapping("/compare")
    public ResponseEntity<EntityModel<LipidSpacePcaResponse>> compare(@RequestBody(required = true) @Valid LipidSpaceQueryDto lipidSpaceDto) throws RestClientException {
        if (lipidSpaceDto == null) {
            return ResponseEntity.noContent().build();
        }
        log.info("Query: {}", lipidSpaceDto);
        /*
        TODO: update JSON Schema for LipidSpace service, update binding
        class_color_map = {
            lipid_class_name: color_vector[i % len(color_vector)]
            for i, lipid_class_name in enumerate(set(df["lipid_class_name"]))
        }
        class_color_map["rest"] = color_vector[len(class_color_map) % len(color_vector)]

        lipid_to_class = {
            row["lipid_name"]: row["lipid_class_name"] for i, row in df.iterrows()
        }
        requested_spaces = ["global", "LipidomeDistanceMatrix"]
         */
        LipidSpacePcaRequest rq = new LipidSpacePcaRequest();
        rq.setTableType(TableType.FLAT_TABLE);
        rq.setTableColumnTypes(
                Arrays.asList(
                        TableColumnType.LIPIDCOLUMN,
                        TableColumnType.SAMPLECOLUMN,
                        TableColumnType.QUANTCOLUMN
                )
        );
        List<String> lipidNames = lipidSpaceDto.lipidNames();
        List<String> sampleCvTerms = lipidSpaceDto.sampleCvTerms();
        List<String> mzTabIds = lipidSpaceDto.mzTabResultIds();
        log.info("mzTab IDs: {}", mzTabIds);
        log.info("Lipid names: {}", lipidNames);
        Page<LipidQuantityDatasetAssayTableRow> results = this.lipidQuantityRepository.findLipidQuantityTableForLipidSpace(lipidNames, mzTabIds, Pageable.unpaged());
        log.info("Response contained {} rows", results.getTotalElements());
        List<Column<?>> columns = new LinkedList<>();
        columns.add(StringColumn.create("LipidName"));
        columns.add(StringColumn.create("Sample"));
//        columns.add(StringColumn.create("Assay", results.getNumberOfElements()));
        columns.add(DoubleColumn.create("Quantity"));
//        columns.add(DoubleColumn.create("RelativeQuantity", results.getNumberOfElements()));
        Table table = Table.create("Dataset Comparison", columns);
        results.stream().forEachOrdered((t) -> {
            if (t.getNormalizedShorthandNames() != null && !t.getNormalizedShorthandNames().isBlank()) {
                Row row = table.appendRow();
                row.setString("LipidName", t.getNormalizedShorthandNames());
                row.setString("Sample", t.getDataset() + "-" + t.getAssay());
                //            row.setString("Assay", t.getAssay());
                row.setDouble("Quantity", t.getAssayQuantity());
                //            row.setDouble("RelativeQuantity", t.getAssayRelativeQuantity());
//                log.info("Adding row: {}", row);
                //            table.append(row);
            }
        });
        StringWriter sw = new StringWriter();
        table.write().csv(sw);
        rq.setTable(sw.toString());
        rq.setRequestedSpaces(Arrays.asList("global", "LipidomeDistanceMatrix"));
        Optional<EntityModel<LipidSpacePcaResponse>> resp = Optional.empty();
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            objectMapper.writeValue(new File("lipidSpaceRqBody.json"), rq);
//        } catch (IOException ex) {
//            Logger.getLogger(LipidSpaceController.class.getName()).log(Level.SEVERE, null, ex);
//        }
        LipidSpacePcaResponse response;
        response = lipidSpaceApi.postTablePca(rq);
        if (response != null) {
            resp = Optional.of(EntityModel.of(response));
        }
        return ResponseEntity.of(resp);
    }
}
