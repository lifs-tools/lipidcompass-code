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

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.lipidcompass.config.TrackingConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Implementation to record basic application actions with Piwik / Matomo.
 *
 * @author nilshoffmann
 */
@Slf4j
@Service
public class MatomoTracker implements AnalyticsTracker {

    private final Integer gaId;

    private final boolean enabled;

    private final URL appUrl;

    private final URL piwikServerUrl;

    @Autowired
    public MatomoTracker(TrackingConfig trackingConfig) throws MalformedURLException {
        if (trackingConfig.getId() == null || trackingConfig.getId().isEmpty() || trackingConfig.getUrl() == null || trackingConfig.getUrl().
                isEmpty() || trackingConfig.getAppUrl() == null || trackingConfig.getAppUrl().
                isEmpty()) {
            this.gaId = null;
            enabled = false;
            this.piwikServerUrl = null;
            this.appUrl = null;
            log.info(
                    "Disabling piwik tracker. To enable, set 'ga.id' property to your site id, 'ga.url' to the full URL of your piwik.php script, and 'ga.appUrl' to the url of your application.");

        } else {
            this.gaId = Integer.parseInt(trackingConfig.getId());
            enabled = true;
            this.piwikServerUrl = new URL(trackingConfig.getUrl());
            this.appUrl = new URL(trackingConfig.getAppUrl());
            log.info(
                    "Enabling piwik tracker for site " + gaId + " and server " + piwikServerUrl + " and application url " + appUrl);
        }
    }

    @Override
    public void started(UUID uuid, String event, String type) {
        if (enabled) {
            try {
                callUri(uuid, event + "-started", type);
            } catch (URISyntaxException ex) {
                log.error("Exception in started() method of MatomoTracker", ex);
            }
        }
    }

    @Override
    public void stopped(UUID uuid, String event, String type) {
        if (enabled) {
            try {
                callUri(uuid, event + "-stopped", type);
            } catch (URISyntaxException ex) {
                log.error("Exception in started() method of MatomoTracker", ex);
            }
        }
    }

    @Override
    public void count(UUID uuid, String event, String type) {
        if (enabled) {
            try {
                callUri(uuid, event, type);
            } catch (URISyntaxException ex) {
                log.error("Exception in count() method of MatomoTracker", ex);
            }
        }
    }

    private void callUri(UUID uuid, String event, String type) throws URISyntaxException {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance().
                uri(this.piwikServerUrl.toURI());
        UriComponents uriComponents = builder.queryParam("rec", 1).
                queryParam("idsite", gaId).
                queryParam("url", this.appUrl.toURI().
                        toASCIIString()).
                queryParam("uid", uuid.toString()).
                queryParam("action_name", event).
                queryParam("e_a", type).
                queryParam("send_image", 0).
                build();
        RestTemplate template = new RestTemplate();
        ResponseEntity<Integer> response = template.getForEntity(uriComponents.
                toUri(), Integer.class);
        if (response.getStatusCode() == HttpStatusCode.valueOf(204) || response.
                getStatusCode() == HttpStatusCode.valueOf(200)) {
            log.debug("Piwik call succeeded!");
        } else {
            log.warn(
                    "Piwik call to " + uriComponents.toUri() + " did not succeed!");
        }

    }

}
