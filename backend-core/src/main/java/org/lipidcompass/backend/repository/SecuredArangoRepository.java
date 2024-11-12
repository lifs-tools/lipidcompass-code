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

import com.arangodb.model.AqlQueryOptions;
import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;
import org.lipidcompass.data.model.ArangoBaseEntity;
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
public interface SecuredArangoRepository<T extends ArangoBaseEntity> extends ArangoRepository<T, String> {

//    @Override
//    public void deleteAll();
//
//    @Override
//    public void deleteAll(
//            Iterable<? extends T> entities);
//
//    @Override
//    public void delete(T entity);
//
//    @Override
//    public void deleteById(String id);
//
//    @Override
//    public <S extends T> Iterable<S> saveAll(Iterable<S> entities);
//
//    @Override
//    public <S extends T> Iterable<S> updateAll(Iterable<S> entities);
//
//    @Override
//    public <S extends T> S save(S entity);

    @Query(
            """
        FOR doc IN #collection
          FILTER doc.visibility == "PUBLIC"
          SORT RAND() 
          LIMIT 1 
          RETURN doc
        """
    )
    public <S extends T> S random(AqlQueryOptions options);

    @Query(
            """
        FOR doc IN #collection
          FILTER doc.visibility == "PUBLIC"
          COLLECT WITH COUNT INTO length
        RETURN length
        """
    )
    public Long countPublic();

    @Query(
            """
        WITH 
          #collection
        FOR doc IN #collection
          FILTER doc.visibility == "PUBLIC"
        #pageable
        RETURN doc
        """
    )
    public <S extends T> Page<S> findAllPublic(Pageable pageable);

    public <S extends T> Page<S> findAllByCreatedBy(User user, Pageable pageable);

    public <S extends T> Page<S> findAllByUpdatedBy(User user, Pageable pageable);
}
