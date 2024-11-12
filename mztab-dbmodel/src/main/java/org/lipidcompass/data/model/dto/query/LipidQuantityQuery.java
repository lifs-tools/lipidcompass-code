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

import org.lifstools.jgoslin.domain.LipidLevel;
import java.util.Collections;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author nilshoffmann
 */
@Data
public class LipidQuantityQuery {

    @NotNull
    private LipidLevel lipidLevel = LipidLevel.CATEGORY;
    @NotNull
    private List<String> names;
    @NotNull
    private MatchMode matchMode = MatchMode.PREFIX;
    @NotNull
    private boolean normalizeName = false;
    @NotNull
    private boolean addDefaultFacets = false;
    @NotNull
    private String unit = "";
    private Double minValue = Double.MIN_VALUE;
    private Double maxValue = Double.MAX_VALUE;

    @NotNull
    private List<String> facetFields = Collections.emptyList();
    @NotNull
    private List<String> facetPivotFields = Collections.emptyList();
    
    @NotNull
    private List<FilterOn> filterQueries = Collections.emptyList();
}
