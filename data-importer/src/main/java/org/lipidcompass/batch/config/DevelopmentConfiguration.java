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
import org.lipidcompass.data.model.CrossReference;
import org.lipidcompass.data.model.Lipid;
import org.lipidcompass.data.model.lipidmaps.HasLipidMapsChild;
import org.lipidcompass.data.model.lipidmaps.LipidMapsEntry;
import org.lipidcompass.data.model.relations.HasCrossReference;
import org.lipidcompass.data.model.swisslipids.HasSwissLipidsChild;
import org.lipidcompass.data.model.swisslipids.SwissLipidsEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;

/**
 *
 * @author nils.hoffmann
 */
@Slf4j
@Profile("dev")
@Configuration
public class DevelopmentConfiguration {
    
    @Autowired
    private ArangoOperations arangoOperations;
    
    @Value("${arangodb.truncate:false}")
    public boolean truncate;
    
    @Value("${arangodb.database:lipidcompass}")
    private String database;
    
    @EventListener(ApplicationReadyEvent.class)
    public void truncateRepositories() {
        if (truncate) {
            if (database.toLowerCase().contains("dev")) {
                log.warn("arangodb.truncate is set and database name contains 'dev'! Clearing lipid collections!");
                arangoOperations.collection(Lipid.class).truncate();
                arangoOperations.collection(LipidMapsEntry.class).truncate();
                arangoOperations.collection(SwissLipidsEntry.class).truncate();
                arangoOperations.collection(CrossReference.class).truncate();
                arangoOperations.collection(HasCrossReference.class).truncate();
                arangoOperations.collection(HasSwissLipidsChild.class).truncate();
                arangoOperations.collection(HasLipidMapsChild.class).truncate();
//            arangoOperations.collection(MzTabData.class).truncate();
                
            } else {
                log.warn("arangodb.truncate is set but the database name DOES NOT contain 'dev'! Cowardly refusing to delete a potential production databases!");
            }
        }
    }
}
