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
package org.lipidcompass.data.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author nilshoffmann
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class PlottableLipidQuantity {

    private String dataset;
    private String normalizedShorthandNames;
    private String lipidClass;
    private String lipidCategory;
    private List<String> lipidIds;
    private String quantificationUnit;
    private String quantificationUnitAccession;
    private List<String> assayQuantityIds;
    private List<Double> assayQuantities;
    private Double minAssayQuantity;
    private Double averageAssayQuantity;
    private Double maxAssayQuantity;
    private Double stddevAssayQuantity;
    private Double countAssayQuantity;
    private Double perc25;
    private Double perc50;
    private Double perc75;
    private Double iqr;
    private Double lowerWhisker;
    private Double upperWhisker;
}
