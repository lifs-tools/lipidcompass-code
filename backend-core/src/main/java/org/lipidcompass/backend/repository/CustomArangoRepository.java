/*
 * Copyright 2023 LIFS.
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
package org.lipidcompass.backend.repository;

import java.util.Collection;
import java.util.function.Function;
import org.lipidcompass.data.model.ArangoBaseEntity;

/**
 *
 * @author nilshoffmann
 * @param <T>
 * @param <ID>
 */
public interface CustomArangoRepository<T extends ArangoBaseEntity, ID> {
    
//    @Query("FOR c in #collection FILTER c.nativeId IN @nativeId RETURN c")
//    <S extends T> Iterable<S> findAllByNativeId(@Param("nativeId") Collection<String> nativeId);
    
    <S extends T> Iterable<S> createOrUpdateAll(Collection<S> entities, Class<S> domainClass, String attributeName, Function<T, String> attributeMapperFunction);
    
}
