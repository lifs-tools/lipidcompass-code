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
package org.lipidcompass.batch.config;

import com.arangodb.springframework.core.ArangoOperations;
import org.lipidcompass.data.model.Lipid;
import org.lipidcompass.data.model.LipidQuantity;
import org.lipidcompass.data.model.MzTabData;
import org.lipidcompass.data.model.MzTabResult;
import org.lipidcompass.data.model.lipidmaps.LipidMapsEntry;
import org.lipidcompass.data.model.swisslipids.SwissLipidsEntry;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author nilshoffmann
 */
@Configuration
public class ArangoDbInitializationConfiguration {
    @Autowired
    private ArangoOperations arangoOperations;
    
    @PostConstruct
    public void initializeCollections() {
        arangoOperations.collection(Lipid.class).count();
        arangoOperations.collection(LipidQuantity.class).count();
        arangoOperations.collection(LipidMapsEntry.class).count();
        arangoOperations.collection(SwissLipidsEntry.class).count();
        arangoOperations.collection(MzTabResult.class).count();
        arangoOperations.collection(MzTabData.class).count();
    }
}
