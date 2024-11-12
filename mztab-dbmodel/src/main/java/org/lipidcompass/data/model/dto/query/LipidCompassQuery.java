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
package org.lipidcompass.data.model.dto.query;

import org.lipidcompass.data.model.MzTabResult;
import org.lifstools.jgoslin.domain.LipidLevel;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Value;
import org.lipidcompass.data.model.dto.CvParameterFacet;

/**
 *
 * @author nilshoffmann
 */
@Value
public class LipidCompassQuery {

    @NotNull
    private LipidLevel lipidLevel = LipidLevel.CATEGORY;
    @NotNull
    private List<String> names;
    @NotNull
    private MatchMode matchMode = MatchMode.PREFIX;
    @NotNull
    private Boolean normalizeName = false;

    @NotNull
    private String unit = "";
    private Double minValue = 0.0d;
    private Double maxValue = Double.MAX_VALUE;
    
    private Double minMass = 0.0d;
    private Double maxMass = Double.MAX_VALUE;

    private String sumFormula;

    @NotNull
    private List<MzTabResult> mzTabResults;
    
    @NotNull
    private List<CvParameterFacet> facets;
    
    @NotNull
    private List<CvParameterFacet> selectedFacets;
}
