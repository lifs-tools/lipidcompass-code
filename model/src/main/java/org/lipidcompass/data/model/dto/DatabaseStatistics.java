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

import java.util.Date;
import lombok.Data;

/**
 *
 * @author nilshoffmann
 */
@Data
public class DatabaseStatistics {

    private String releaseVersion;

    private Date releaseDate;

    private Long categories;

    private Long classes;

    private Long species;

    private Long molecularSpecies;

    private Long snPosition;

    private Long structureDefined;

    private Long completeStructure;

    private Long fullStructure;

    private Long noLevel;

    private Long undefinedLevel;

    private Long experiments;

    private Long results;

    private Long crossReferences;

    private Long taxonomicSpecies;

    private Long tissues;

    private Long diseases;

    private Long cellTypes;

    private Long lipidQuantities;

    private Long studyVariableFactors;
}
