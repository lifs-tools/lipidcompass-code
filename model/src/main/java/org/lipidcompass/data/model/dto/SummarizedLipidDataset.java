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

import java.util.List;
import lombok.Data;

/**
 *
 * @author nilshoffmann
 */
@Data
public class SummarizedLipidDataset {
    private String mzTabResultId;
    private String quantificationUnitParamName;
    private String quantificationUnitParamAccession;
    private String lipidLevel;
    private List<String> x;
    private List<Double> y;
    private List<String> lipidCategory;
    private List<String> lipidClass;
    private List<Long> groupCount;
}
