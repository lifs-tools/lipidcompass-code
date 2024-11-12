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
package org.lipidcompass.backend.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import org.lipidcompass.data.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 *
 * @author nilshoffmann
 * @param <T>
 */
@NoRepositoryBean
public interface SecuredArangoUserRepository<T extends User> extends CustomArangoRepository<T, String>, ArangoRepository<T, String> {

//    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteAll();

//    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteAll(
            Iterable<? extends T> entities);

//    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void delete(T entity);

//    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteById(String id);

//    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> entities);

//    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public <S extends T> S save(S entity);

//    @Query("FOR doc IN #collection  SORT RAND() LIMIT 1 RETURN doc")
//    public <S extends T> S random(AqlQueryOptions options);
    public <S extends T> Page<S> findByCreatedBy(String user, Pageable pageable);

    public <S extends T> Page<S> findByUpdatedBy(String user, Pageable pageable);
    
    
}
