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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author nilshoffmann
 */
@Slf4j
public class MzTabDataSerializer extends StdSerializer<MzTabData> {
    public MzTabDataSerializer() {
        this(null);
    }
   
    public MzTabDataSerializer(Class<MzTabData> t) {
        super(t);
    }
 
    @Override
    public void serialize(
      MzTabData value, JsonGenerator jgen, SerializerProvider provider) 
      throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        jgen.writeStringField("id", value.getMzTab().getMetadata().getMzTabID());
        jgen.writeStringField("version", value.getMzTab().getMetadata().getMzTabVersion());
        jgen.writeStringField("title", value.getMzTab().getMetadata().getTitle());
        jgen.writeStringField("description", value.getMzTab().getMetadata().getDescription());
        jgen.writeArrayFieldStart("contact");
        if(value.getMzTab().getMetadata().getContact()!=null) {
            value.getMzTab().getMetadata().getContact().forEach((t) -> {
                try {
                    jgen.writeStartObject();
                    jgen.writeStringField("name", t.getName());
                    jgen.writeStringField("email", t.getEmail());
                    jgen.writeStringField("affiliation", t.getAffiliation());
                    jgen.writeEndObject();
                } catch (IOException ex) {
                    log.warn("Caught exception while trying to serialize contacts!", ex);
                }
            });
        }
        jgen.writeEndArray();
        jgen.writeNumberField("numberAssays", value.getMzTab().getMetadata().getAssay().size());
        jgen.writeNumberField("numberMsRuns", value.getMzTab().getMetadata().getMsRun().size());
        jgen.writeNumberField("numberStudyVariable", value.getMzTab().getMetadata().getStudyVariable().size());
        jgen.writeNumberField("numberSample", value.getMzTab().getMetadata().getSample().size());
        jgen.writeNumberField("numberSml", value.getMzTab().getSmallMoleculeSummary().size());
        jgen.writeNumberField("numberSmf", value.getMzTab().getSmallMoleculeFeature().size());
        jgen.writeNumberField("numberSme", value.getMzTab().getSmallMoleculeEvidence().size());
        if(value.getMzTab().getMetadata().getContact() == null || value.getMzTab().getMetadata().getContact().isEmpty()) {
            jgen.writeStringField("contact", null);
        } else {
            jgen.writeStringField("contact", String.join(" | ", value.getMzTab().getMetadata().getContact().stream().map((contact) -> {
                              return contact.getName()+"<"+contact.getEmail()+">"+contact.getAffiliation();
                          }).collect(Collectors.toList())));
        }
        jgen.writeEndObject();
    }
}
