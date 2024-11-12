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

import org.lipidcompass.data.model.MzTabData;
import org.springframework.batch.item.ItemProcessor;
import org.lipidcompass.data.model.MzTabResult;
import org.lipidcompass.data.model.SubmissionStatus;
import org.lipidcompass.data.model.Visibility;
import org.lipidcompass.data.model.dto.MzTabSummary;
import de.isas.mztab2.model.MzTab;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;

/**
 *
 * @author nilshoffmann
 */
public class MzTabResultProcessor implements ItemProcessor<MzTab, MzTabResult> {

    private Long jobId;
    
    @BeforeStep
    public void getInterstepData(StepExecution stepExecution) {
        JobExecution jobExecution = stepExecution.getJobExecution();
        this.jobId = jobExecution.getJobId();
    }

    @Override
    public MzTabResult process(MzTab mzTab) throws Exception {
        if (mzTab == null) {
            throw new NullPointerException("MzTab object was null!");
        }
        Logger.getLogger(MzTabResultProcessor.class.getName()).
                log(Level.INFO, "Processing mztab {0}", mzTab.getMetadata().
                        getMzTabID());
        MzTabSummary summary = MzTabSummary.builder().
                description(mzTab.getMetadata().getDescription()).
                msRunCount(Long.valueOf(Optional.ofNullable(mzTab.getMetadata().getMsRun()).map(List::size).orElse(0))).
                assayCount(Long.valueOf(Optional.ofNullable(mzTab.getMetadata().getAssay()).map(List::size).orElse(0))).
                sampleCount(Long.valueOf(Optional.ofNullable(mzTab.getMetadata().getSample()).map(List::size).orElse(0))).
                studyVariableCount(Long.valueOf(Optional.ofNullable(mzTab.getMetadata().getStudyVariable()).map(List::size).orElse(0))).
                smlCount(Long.valueOf(Optional.ofNullable(mzTab.getSmallMoleculeSummary()).map(List::size).orElse(0))).
                smeCount(Long.valueOf(Optional.ofNullable(mzTab.getSmallMoleculeEvidence()).map(List::size).orElse(0))).
                smfCount(Long.valueOf(Optional.ofNullable(mzTab.getSmallMoleculeFeature()).map(List::size).orElse(0))).
                title(mzTab.getMetadata().getTitle()).
                id(mzTab.getMetadata().getMzTabID()).
                version(mzTab.getMetadata().getMzTabVersion()).
                contacts(mzTab.getMetadata().getContact()).
                publications(mzTab.getMetadata().getPublication()).
                quantificationUnit(mzTab.getMetadata().getSmallMoleculeQuantificationUnit()).
                build();
        MzTabData mzTabData = MzTabData.builder().
                transactionUuid(jobId+"").
                mzTab(mzTab).
                visibility(Visibility.PRIVATE).build();
        MzTabResult result = MzTabResult.builder().
                transactionUuid(jobId+"").
                mzTabSummary(summary).
                mzTabData(mzTabData).
                submissionStatus(SubmissionStatus.IN_PROGRESS).
                visibility(Visibility.PRIVATE).
                build();
        return result;
    }

}
