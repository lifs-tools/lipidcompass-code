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
import lombok.Data;
import org.springframework.batch.core.JobInstance;

/**
 *
 * @author nilshoffmann
 */
@Data
public class JobInstanceDto {
    private String name;
    private Long id;
    private Long instanceId;
    private Integer version;
    private List<JobExecutionDto> jobExecutions;
    
    public static JobInstanceDto of(JobInstance jobInstance, List<JobExecutionDto> jobExecutions) {
        JobInstanceDto dto = new JobInstanceDto();
        dto.name = jobInstance.getJobName();
        dto.id = jobInstance.getId();
        dto.instanceId = jobInstance.getInstanceId();
        dto.version = jobInstance.getVersion();
        dto.jobExecutions = jobExecutions;
        return dto;
    }
}
