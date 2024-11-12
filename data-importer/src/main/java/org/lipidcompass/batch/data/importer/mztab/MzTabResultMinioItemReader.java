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

import de.isas.mztab2.io.MzTabFileParser;
import de.isas.mztab2.model.MzTab;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.lipidcompass.backend.repository.SubmissionRepository;
import org.lipidcompass.data.model.submission.FileResource;
import org.lipidcompass.data.model.submission.Submission;
import org.lipidcompass.service.core.io.MinioStorageService;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.core.io.Resource;
import uk.ac.ebi.pride.jmztab2.utils.errors.MZTabErrorList;
import uk.ac.ebi.pride.jmztab2.utils.errors.MZTabErrorType;

/**
 *
 * @author nilshoffmann
 */
public class MzTabResultMinioItemReader implements ItemReader<MzTab> {

    private MzTabFileParser reader;
//    private final URI inputResource;
    private final MinioStorageService storageService;
    private final SubmissionRepository submissionRepository;
    private final String submissionId;
    private Optional<Submission> submission;
    private List<FileResource> mzTabFiles;
    private int mzTabFileIndex = 0;
    private int nFiles = 0;

    public MzTabResultMinioItemReader(String submissionId, SubmissionRepository submissionRepository, MinioStorageService storageService) throws IOException {
        Logger.getLogger(MzTabResultMinioItemReader.class.getName()).log(Level.INFO, "Reading submissionId {0}", submissionId);
        this.submissionId = submissionId;
        this.submissionRepository = submissionRepository;
        this.storageService = storageService;
    }

    @Override
    public MzTab read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (this.reader == null) {
            this.submission = this.submissionRepository.findById(this.submissionId);
            if (this.submission.isEmpty()) {
                throw new NullPointerException("Could not retrieve submission for id: " + this.submissionId);
            }
            this.mzTabFiles = this.submission.get().getSubmittedFiles().stream().filter((t) -> {
                return t.getFileType() == FileResource.FileType.MZTAB_M;
            }).collect(Collectors.toList());
            this.nFiles = this.mzTabFiles.size();
        }
        if (this.submission.isPresent()) {
            if (mzTabFileIndex < nFiles) {
                FileResource fr = this.mzTabFiles.get(mzTabFileIndex);
                Optional<Resource> resource = this.storageService.loadFile(fr.getFileName(), this.submission.get().getStorageBucket());
                if (resource.isPresent()) {
                    this.reader = new MzTabFileParser(resource.get().getURI());
                    MZTabErrorList errorList = this.reader.parse(System.out, MZTabErrorType.Level.Warn, 100);
                    if (!errorList.
                            isEmpty()) {
                        throw new ParseException(
                                "File from resource " + resource.get() + " contained errors: " + this.reader.
                                getErrorList().
                                toString());
                    }
                    this.mzTabFileIndex++;
                    return this.reader.getMZTabFile();
                }
            } else {
                // read past last element
                return null;
            }
        } 
        throw new IllegalStateException("Unexpected end of input!");
    }

}
