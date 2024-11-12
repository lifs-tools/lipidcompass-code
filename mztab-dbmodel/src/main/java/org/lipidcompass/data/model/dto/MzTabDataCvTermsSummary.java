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
package org.lipidcompass.data.model.dto;

import de.isas.mztab2.model.CV;
import de.isas.mztab2.model.Database;
import de.isas.mztab2.model.Instrument;
import de.isas.mztab2.model.Parameter;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author nilshoffmann
 */
@Data
@Builder
public class MzTabDataCvTermsSummary {

    private String mzTabResultId;
    private List<Parameter> sampleSpecies;
    private List<Parameter> sampleTissue;
    private List<Parameter> sampleDisease;
    private List<Parameter> sampleCelltype;
    private List<Parameter> sampleCustom;
    private List<Parameter> msRunFormat;
    private List<Parameter> msRunIdFormat;
    private List<Parameter> msRunScanPolarity;
    private List<Parameter> msRunFragmentation;
    private List<Parameter> assayCustom;
    private List<Parameter> studyVariableFactors;
    private List<Parameter> custom;
    private List<CV> cvs;
    private List<Database> database;
    private List<Instrument> instrument;
    private Parameter smallMoleculeQuantificationUnit;
    private Parameter smallMoleculeFeatureQuantificationUnit;
    private Parameter smallMoleculeIdentificationReliability;
    private List<Parameter> idConfidenceMeasure;
}
