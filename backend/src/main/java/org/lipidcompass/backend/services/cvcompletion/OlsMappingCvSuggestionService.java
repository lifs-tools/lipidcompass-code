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
package org.lipidcompass.backend.services.cvcompletion;

import de.isas.mztab2.cvmapping.CvParameterLookupService;
import info.psidev.cvmapping.CvMappingRule;
import info.psidev.cvmapping.CvReference;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.bind.JAXBException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import uk.ac.ebi.pride.utilities.ols.web.service.client.OLSClient;
import uk.ac.ebi.pride.utilities.ols.web.service.model.Term;

/**
 *
 * @author nils.hoffmann
 */
@Slf4j
@Service
public class OlsMappingCvSuggestionService {

    private final Map<String, CvMappingHolder> mappingNameToHolder;

    @Autowired
    public OlsMappingCvSuggestionService(OLSClient client, CvParameterLookupService lookupService) throws JAXBException, IOException {
        this.mappingNameToHolder = new TreeMap<>();
        // this.mappingNameToHolder.put("DEFAULT", new CvMappingHolder(client, lookupService, "mappings/mzTab-M-mapping.xml"));
        // TODO add more mappings
        //this.mappingNameToHolder.put("DEFAULT", new CvMappingHolder(client, lookupService, "mappings/mzTab-M-mapping.xml"));
    }

    public List<String> getCvMappings() {
        return new ArrayList<>(this.mappingNameToHolder.keySet());
    }

    public List<CvReference> getCvReferences() {
        return this.mappingNameToHolder.get("DEFAULT").getCvReferences();
    }

    public List<CvReference> getCvReferences(String mappingName) {
        return this.mappingNameToHolder.get(mappingName).getCvReferences();
    }

    public List<CvMappingRule> getRules() {
        return this.mappingNameToHolder.get("DEFAULT").getRules();
    }
    
    public List<CvMappingRule> getRules(String mappingName) {
        return this.mappingNameToHolder.get(mappingName).getRules();
    }

    public List<Term> suggestParameters(String partialParamName, String parentRuleId, int levels) {
        return this.mappingNameToHolder.get("DEFAULT").suggestParameters(partialParamName, parentRuleId, levels);
    }

    @Cacheable("suggestParameters")
    public List<Term> suggestParameters(String mappingName, String partialParamName, String parentRuleId, int levels) {
        return this.mappingNameToHolder.get(mappingName).suggestParameters(partialParamName, parentRuleId, levels);
    }
}
