/*
 * Copyright 2018 The LipidCompass Developers.
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
package org.lipidcompass.service.core.tracking;

import java.util.UUID;

/**
 *
 * @author nilshoffmann
 */
public interface AnalyticsTracker {
    void started(UUID uuid, String eventName, String type);
    void stopped(UUID uuid, String eventName, String type);
    void count(UUID uuid, String eventName, String type);
}
