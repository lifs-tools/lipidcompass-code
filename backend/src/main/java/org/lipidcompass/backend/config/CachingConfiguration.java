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
package org.lipidcompass.backend.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
//import org.springframework.boot.autoconfigure.cache.CacheProperties.Caffeine;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author nilshoffmann
 */
@Configuration
@EnableCaching
public class CachingConfiguration {

    public static final String SMILES_SVG = "SMILES_SVG";
    public static final String DBSTATS = "dbstats";

    @Bean
    public Caffeine caffeineConfig() {
        return Caffeine.newBuilder().expireAfterWrite(60, TimeUnit.SECONDS);
    }

//    @Component
//    public class SimpleCacheCustomizer implements CacheManagerCustomizer<ConcurrentMapCacheManager> {
//
//        @Override
//        public void customize(ConcurrentMapCacheManager cacheManager) {
//            cacheManager.setCacheNames(asList(SMILES_SVG, DBSTATS));
//        }
//    }
}
