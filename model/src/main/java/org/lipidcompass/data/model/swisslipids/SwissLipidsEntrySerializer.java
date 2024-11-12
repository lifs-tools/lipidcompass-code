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
package org.lipidcompass.data.model.swisslipids;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author nils.hoffmann
 */
@Slf4j
public class SwissLipidsEntrySerializer extends StdSerializer<SwissLipidsEntry> {

    public SwissLipidsEntrySerializer() {
        this(null);
    }

    public SwissLipidsEntrySerializer(Class<SwissLipidsEntry> t) {
        super(t);
    }

    @Override
    public void serialize(
            SwissLipidsEntry value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        try {
            String id = value.getId();
            jgen.writeStartObject();
            jgen.writeStringField("id", id);
            jgen.writeStringField("abbreviation", value.getAbbreviation());
            jgen.writeStringField("nativeId", value.getNativeId());
            jgen.writeStringField("nativeUrl", value.getNativeUrl());
            jgen.writeStringField("description", value.getDescription());
            if (value.getLevel() == null) {
                jgen.writeStringField("level", null);
            } else {
                jgen.writeStringField("level", value.getLevel().name());
            }
            jgen.writeStringField("normalizedName", value.getNormalizedName());
            try {
                if (value.getParent() == null) {
                    jgen.writeStringField("parent", null);
                } else {
                    String parentId = Optional.ofNullable(value.getParent()).
                            map((entry) -> {
                                return entry.getId();
                            }
                            ).orElse("null");
                    jgen.writeStringField("parent", parentId);
                }
            } catch (NoSuchElementException nsee) {
                log.debug("Could not retrieve parent!");
                jgen.writeStringField("parent", null);
            }
            if (value.getChildren() == null || value.getChildren().isEmpty()) {
                jgen.writeArrayFieldStart("children");
                jgen.writeEndArray();
            } else {
                jgen.writeArrayFieldStart("children");
                jgen.writeStartArray(value.getChildren().size());
                value.getChildren().forEach((child) -> {
                    try {
                        jgen.writeString(child.getId());
                    } catch (IOException ex) {
                        log.error("Caught IOException while trying to write nativeId {} for child of {}: {}", child.getNativeId(), value.getNativeId(), ex.getLocalizedMessage());
                    }
                });
                jgen.writeEndArray();
            }
            if (value.getCrossReferences() == null || value.getCrossReferences().isEmpty()) {
                jgen.writeArrayFieldStart("crossReferences");
                jgen.writeEndArray();
            } else {
                jgen.writeArrayFieldStart("crossReferences");
                value.getCrossReferences().forEach((crossRef) -> {
                    try {
                        jgen.writeStartObject();
                        jgen.writeStringField("crossReferenceType", crossRef.getCrossReferenceType().name());
                        jgen.writeStringField("id", crossRef.getId());
                        jgen.writeStringField("url", crossRef.getUrl());
                        jgen.writeEndObject();
                    } catch (IOException ex) {
                        log.error("Caught IOException while trying to write nativeId {} for cross ref of {}: {}", crossRef.getNativeId(), value.getNativeId(), ex.getLocalizedMessage());
                    }
                });
                jgen.writeEndArray();
            }
            jgen.writeEndObject();
        } catch (NoSuchElementException nse) {
            log.debug("Could not retrieve id for element, returning 'null'!");
            jgen.writeNull();
        }

    }
}
