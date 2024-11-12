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
package org.lipidcompass.data.model;

/**
 * The publication status of a submission to LipidCompass. Initially, each
 * submission starts in IN_PROGRESS, transitioning to SUBMITTED, when the user
 * submits the data. IN_CURATION is available as soon as one of the curators
 * reviews the dataset. Upon completion and sign-off by the curator, the dataset
 * is switched to PUBLISHED.
 *
 * @author nils.hoffmann
 */
public enum SubmissionStatus {
    IN_PROGRESS, SUBMITTED, IN_CURATION, IN_REVIEW, PUBLISHED;
}
