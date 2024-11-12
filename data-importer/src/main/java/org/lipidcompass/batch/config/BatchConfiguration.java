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
package org.lipidcompass.batch.config;

import org.lipidcompass.batch.config.jobs.StudyImportConfiguration;
import org.lipidcompass.batch.config.jobs.LipidMapsImportConfiguration;
import org.lipidcompass.batch.config.jobs.SwissLipidsImportConfiguration;
import org.lipidcompass.batch.config.jobs.MzTabImportConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.ApplicationContextFactory;
import org.springframework.batch.core.configuration.support.GenericApplicationContextFactory;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

/**
 * General configuration for batch jobs.
 *
 * @author nilshoffmann
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private static final Logger log = LoggerFactory.getLogger(
            BatchConfiguration.class);

//    @Bean
//    public ApplicationContextFactory swissLipidsJobs() {
//        return new GenericApplicationContextFactory(SwissLipidsImportConfiguration.class);
//    }
//
//    @Bean
//    public ApplicationContextFactory lipidMapsJobs() {
//        return new GenericApplicationContextFactory(LipidMapsImportConfiguration.class);
//    }
//
//    @Bean
//    public ApplicationContextFactory mzTabJobs() {
//        return new GenericApplicationContextFactory(MzTabImportConfiguration.class);
//    }
//
//    @Bean
//    public ApplicationContextFactory studyJobs() {
//        return new GenericApplicationContextFactory(StudyImportConfiguration.class);
//    }

    @Autowired
    private MailSender mailSender;

    @Bean
    public JobExecutionListener listener() {
        return new JobExecutionListenerSupport() {

            @Override
            public void beforeJob(JobExecution jobExecution) {
                long jobId = jobExecution.getJobId();
                jobExecution.getExecutionContext().put("jobId", jobId);
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
                    log.info("!!! JOB FINISHED! Time to verify the results");
                }
                try {
                    SimpleMailMessage mailMessage = new SimpleMailMessage(
                            templateMessage());
                    mailMessage.setText(jobExecution.getJobInstance().getJobName() + "\n" + "With parameters: \n" + jobExecution.getJobParameters().toProperties() + "\n" + "Started on: " + jobExecution.getCreateTime() + "\n" + "Terminated on: " + jobExecution.getEndTime() + "\n" + "Status: " + jobExecution.getStatus().name());
                    mailSender.send(mailMessage);
                } catch (MailException me) {
                    log.warn("Sending mail failed!", me);
                }
            }
        };
    }

    // required to access security context from batch jobs
    @Bean
    public MethodInvokingFactoryBean methodInvokingFactoryBean() {
        MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
        methodInvokingFactoryBean.setTargetClass(SecurityContextHolder.class);
        methodInvokingFactoryBean.setTargetMethod("setStrategyName");
        methodInvokingFactoryBean.setArguments(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        return methodInvokingFactoryBean;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor exec = new SimpleAsyncTaskExecutor("batch");
        DelegatingSecurityContextAsyncTaskExecutor dexec = new DelegatingSecurityContextAsyncTaskExecutor(exec);
        return dexec;
    }

    @Bean
    public SimpleMailMessage templateMessage() {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo("admin@lifs-tools.org");
        mailMessage.setSubject("Batch Job Status");
        mailMessage.setFrom("no-reply@lifs-tools.org");
        return mailMessage;
    }
}
