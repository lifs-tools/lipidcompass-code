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
package org.lipidcompass.batch.rest.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;

/**
 *
 * @author nils.hoffmann
 */
@Data
public class JobExecutionDto {
    private Long executionId;
    private Long jobId;
    private String jobName;
    private BatchStatus status;
    private Map<String, JobParameter<?>> jobParameters;
    private LocalDateTime createTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime lastUpdateTime;
    private ExitStatus exitStatus;
    private List<Throwable> failureExceptions;
    
    public static JobExecutionDto of(JobExecution jobExecution) {
        JobExecutionDto dto = new JobExecutionDto();
        dto.createTime = jobExecution.getCreateTime();
        dto.endTime = jobExecution.getEndTime();
        dto.executionId = jobExecution.getId();
        dto.exitStatus = jobExecution.getExitStatus();
        dto.jobId = jobExecution.getJobId();
        dto.jobName = jobExecution.getJobInstance().getJobName();
        dto.jobParameters = jobExecution.getJobParameters().getParameters();
        dto.lastUpdateTime = jobExecution.getLastUpdated();
        dto.startTime = jobExecution.getStartTime();
        dto.status = jobExecution.getStatus();
        dto.failureExceptions = jobExecution.getAllFailureExceptions();
        return dto;
    }
}
