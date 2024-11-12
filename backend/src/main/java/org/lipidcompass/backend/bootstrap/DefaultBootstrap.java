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
package org.lipidcompass.backend.bootstrap;

import com.arangodb.entity.ViewEntity;
import com.arangodb.entity.arangosearch.CollectionLink;
import com.arangodb.entity.arangosearch.FieldLink;
import com.arangodb.entity.arangosearch.StoreValuesType;
import com.arangodb.entity.arangosearch.analyzer.SearchAnalyzer;
import com.arangodb.entity.arangosearch.analyzer.TextAnalyzer;
import com.arangodb.entity.arangosearch.analyzer.TextAnalyzerProperties;
import com.arangodb.model.arangosearch.ArangoSearchCreateOptions;
import com.arangodb.springframework.core.ArangoOperations;
import java.util.List;
import org.lipidcompass.config.LipidCompassProperties;
import org.lipidcompass.data.model.Authority;
import org.lipidcompass.data.model.CrossReference;
import org.lipidcompass.data.model.Study;
import org.lipidcompass.data.model.HasAuthority;
import org.lipidcompass.data.model.Lipid;
import org.lipidcompass.data.model.LipidQuantity;
import org.lipidcompass.data.model.MzTabResult;
import org.lipidcompass.data.model.User;
import org.lipidcompass.data.model.lipidmaps.HasLipidMapsChild;
import org.lipidcompass.data.model.lipidmaps.LipidMapsEntry;
import org.lipidcompass.data.model.relations.HasCrossReference;
import org.lipidcompass.data.model.relations.HasMzTabResult;
import org.lipidcompass.data.model.relations.HasCvParameterReference;
import org.lipidcompass.data.model.swisslipids.HasSwissLipidsChild;
import org.lipidcompass.data.model.swisslipids.SwissLipidsEntry;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.lipidcompass.backend.config.ArangodbConfiguration.ArangoSearchAnalyzer;
import org.lipidcompass.data.model.DatabaseInfo;
import org.lipidcompass.data.model.MzTabAssay;
import org.lipidcompass.data.model.MzTabMsRun;
import org.lipidcompass.data.model.MzTabStudyVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 *
 * @author nils.hoffmann
 */
@Slf4j
@Profile({"!dev", "!integ"})
@Component
public class DefaultBootstrap {

    @Autowired
    private ArangoOperations arangoOperations;

    @Autowired
    private AuthorityAndUserBootstrap authorityAndUserBootstrap;

    @Value("${arangodb.database}")
    private String database;

    @Value("${arangodb.search.global.stopwords}")
    private List<String> stopWords;

    @Autowired
    private LipidCompassProperties lipidCompassProperties;

    @PostConstruct
    public void init() {
        log.info("Starting initialization of vertex and edge collections for database {}", database);
        arangoOperations.collection(Authority.class).count();
        arangoOperations.collection(CrossReference.class).count();
//            arangoOperations.collection(Evidence.class).count();
        arangoOperations.collection(Study.class).count();
        arangoOperations.collection(HasAuthority.class).count();
        arangoOperations.collection(HasCrossReference.class).count();
        arangoOperations.collection(HasCvParameterReference.class).count();
        arangoOperations.collection(HasLipidMapsChild.class).count();
        arangoOperations.collection(HasMzTabResult.class).count();
        arangoOperations.collection(HasSwissLipidsChild.class).count();
        arangoOperations.collection(LipidMapsEntry.class).count();
        arangoOperations.collection(Lipid.class).count();
        arangoOperations.collection(LipidQuantity.class).count();
//            arangoOperations.collection(LipidGroupedQuantity.class).count();
        arangoOperations.collection(MzTabResult.class).count();
        arangoOperations.collection(MzTabStudyVariable.class).count();
        arangoOperations.collection(MzTabAssay.class).count();
        arangoOperations.collection(MzTabMsRun.class).count();
        arangoOperations.collection(SwissLipidsEntry.class).count();
        arangoOperations.collection(User.class).count();
        arangoOperations.collection(DatabaseInfo.class).count();
        log.info("Finished initialization of vertex and edge collections for database {}", database);
        authorityAndUserBootstrap.init();
        TextAnalyzer ta = new TextAnalyzer();
        TextAnalyzerProperties tap = new TextAnalyzerProperties();
        tap.setAccent(true);
        tap.setStemming(false);
        tap.setStopwords(stopWords);
        tap.setLocale("en.UTF8");
        ta.setProperties(tap);
        ta.setName(ArangoSearchAnalyzer.GLOBAL_SEARCH.getIdentifier());
        SearchAnalyzer sa = arangoOperations.driver().db(database).createSearchAnalyzer(ta);
        arangoOperations.driver().db(database).createSearchAnalyzer(sa);
        ArangoSearchCreateOptions asco = new ArangoSearchCreateOptions();
        CollectionLink studyLink = CollectionLink.on("studies");
        studyLink.fields(FieldLink.on("name").analyzers(ArangoSearchAnalyzer.GLOBAL_SEARCH.getIdentifier()).storeValues(StoreValuesType.ID), FieldLink.on("description").analyzers(ArangoSearchAnalyzer.GLOBAL_SEARCH.getIdentifier()).storeValues(StoreValuesType.ID));
        CollectionLink mzTabResultLink = CollectionLink.on("mzTabResult");
        mzTabResultLink.fields(FieldLink.on("mzTabSummary").analyzers(ArangoSearchAnalyzer.GLOBAL_SEARCH.getIdentifier()).storeValues(StoreValuesType.ID).includeAllFields(Boolean.TRUE));
        asco.link(studyLink, mzTabResultLink);
        if (!arangoOperations.driver().db(database).view("global_search_view").exists()) {
            ViewEntity globalView = arangoOperations.driver().db(database).createArangoSearch("global_search_view", asco);
        }

    }
}
