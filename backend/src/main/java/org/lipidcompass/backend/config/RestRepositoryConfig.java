/*
 * Copyright 2022 The LipidCompass Developers.
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
package org.lipidcompass.backend.config;

import org.lipidcompass.data.model.ArangoBaseEdge;
import org.lipidcompass.data.model.ArangoBaseEntity;
import org.lipidcompass.data.model.Authority;
import org.lipidcompass.data.model.CrossReference;
import org.lipidcompass.data.model.CvParameter;
import org.lipidcompass.data.model.GroupedLipidQuantity;
import org.lipidcompass.data.model.HasAuthority;
import org.lipidcompass.data.model.HasOwner;
import org.lipidcompass.data.model.Lipid;
import org.lipidcompass.data.model.LipidQuantity;
import org.lipidcompass.data.model.MzTabAssay;
import org.lipidcompass.data.model.MzTabData;
import org.lipidcompass.data.model.MzTabResult;
import org.lipidcompass.data.model.MzTabStudyVariable;
import org.lipidcompass.data.model.Study;
import org.lipidcompass.data.model.User;
import org.lipidcompass.data.model.lipidmaps.LipidMapsEntry;
import org.lipidcompass.data.model.relations.HasGroupedAssayLipidQuantity;
import org.lipidcompass.data.model.relations.HasLipidQuantityLipidReference;
import org.lipidcompass.data.model.relations.HasMzTabResult;
import org.lipidcompass.data.model.relations.HasMzTabResultLipidQuantity;
import org.lipidcompass.data.model.submission.Submission;
import org.lipidcompass.data.model.swisslipids.SwissLipidsEntry;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

/**
 *
 * @author Nils Hoffmann
 */
@Configuration
public class RestRepositoryConfig implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        config.disableDefaultExposure();
        config.exposeIdsFor(ArangoBaseEntity.class, ArangoBaseEdge.class, Submission.class, Lipid.class, CrossReference.class,
                CvParameter.class, LipidMapsEntry.class, SwissLipidsEntry.class,
                LipidQuantity.class, MzTabResult.class, Study.class,
                User.class, HasOwner.class, HasAuthority.class, Authority.class, MzTabAssay.class, MzTabData.class,
                MzTabStudyVariable.class, HasMzTabResult.class, HasMzTabResultLipidQuantity.class,
                HasLipidQuantityLipidReference.class, HasGroupedAssayLipidQuantity.class,
                GroupedLipidQuantity.class);

    }
}
