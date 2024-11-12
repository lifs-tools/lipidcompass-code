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
package org.lipidcompass.batch.rest.dto;

import java.util.Collections;
import java.util.List;
import lombok.Data;
import org.openscience.cdk.smiles.SmiFlavor;

/**
 *
 * @author nilshoffmann
 */
@Data
public class SmilesDto {
    
    private List<String> inputSmiles = Collections.emptyList();
    
    private List<String> outputSmiles = Collections.emptyList();
    
    private Integer smilesFlavor = SmiFlavor.UniversalSmiles;
    
}
