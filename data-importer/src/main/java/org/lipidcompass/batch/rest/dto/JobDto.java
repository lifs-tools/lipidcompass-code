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

import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;

/**
 *
 * @author nilshoffmann
 */
@Data
public class JobDto {

    private String name;
    private List<JobInstanceDto> jobInstances;

    public static JobDto of(String name, List<JobInstance> jobInstances, JobExplorer jobExplorer) {
        JobDto jobDto = new JobDto();
        jobDto.name = name;
        jobDto.jobInstances = jobInstances.stream().map(
                jobInstance -> JobInstanceDto.of(jobInstance,
                        jobExplorer.
                                getJobExecutions(jobInstance).
                                stream().
                                map(
                                        jobExecution -> JobExecutionDto.of(jobExecution)
                                ).collect(Collectors.toList())
                )
        ).collect(Collectors.toList());
        return jobDto;
    }
}
