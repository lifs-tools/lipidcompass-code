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
package org.lipidcompass.batch.data.importer.mztab;

import org.lipidcompass.backend.repository.LipidQuantityRepository;
import org.lipidcompass.data.model.LipidQuantity;
import java.util.Iterator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 *
 * @author nilshoffmann
 */
@Slf4j
public class LipidQuantityArangoItemReader implements ItemReader<LipidQuantity> {

    private Iterator<LipidQuantity> iterator;
    private LipidQuantityRepository repository;
    private Page<LipidQuantity> page;
    private PageRequest pageRequest;

    public LipidQuantityArangoItemReader(LipidQuantityRepository repository, int pageSize) {
        log.info("Initializing LipidQuantityArangoItemReader, processing {} entities!", repository.count());
        this.pageRequest = PageRequest.of(0, pageSize);
        this.repository = repository;
//        this.iterator = repository.findAll(this.p);
        log.info("Finished LipidQuantityArangoItemReader initialization!");
    }

    @Override
    public LipidQuantity read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (this.page == null) {
            this.page = repository.findAll(this.pageRequest);
            this.iterator = this.page.iterator();
            log.info("Initializing page {} with {} elements", this.page.getNumber(), this.page.getNumberOfElements());
        }
        if (this.iterator != null) {
            if (this.iterator.hasNext()) {
                log.debug("Reading next item");
                return this.iterator.next();
            } else {
                // advance page
                this.iterator = null;
                if (this.page.getNumber() < this.page.getTotalPages()) {
                    // updated page and iterator
                    this.page = this.repository.findAll(PageRequest.of(this.page.getNumber()+1, this.pageRequest.getPageSize()));
                    this.iterator = this.page.iterator();
                    log.info("Processing page {} with {} elements", this.page.getNumber(), this.page.getNumberOfElements());
                    if (this.iterator.hasNext()) {
                        log.debug("Reading next item");
                        return this.iterator.next();
                    }
                }
            }
        }
        this.iterator = null;
        return null;
    }
}
