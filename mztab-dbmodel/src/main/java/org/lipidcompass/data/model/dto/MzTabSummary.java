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

import de.isas.mztab2.model.Contact;
import de.isas.mztab2.model.Parameter;
import de.isas.mztab2.model.Publication;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author nilshoffmann
 */
@Data
@Builder
public class MzTabSummary {
    private String id;
    private String version;
    private String title;
    private String description;
    private List<Contact> contacts;
    private List<Publication> publications;
    private Long assayCount;
    private Long msRunCount;
    private Long studyVariableCount;
    private Long sampleCount;
    private Long smlCount;
    private Long smfCount;
    private Long smeCount;
    private Parameter quantificationUnit;
}
