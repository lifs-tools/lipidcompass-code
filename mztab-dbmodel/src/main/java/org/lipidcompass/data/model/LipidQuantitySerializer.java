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

/**
 *
 * @author nils.hoffmann
 */
public class LipidQuantitySerializer extends StdSerializer<LipidQuantity> {
    public LipidQuantitySerializer() {
        this(null);
    }
   
    public LipidQuantitySerializer(Class<LipidQuantity> t) {
        super(t);
    }
 
    @Override
    public void serialize(
      LipidQuantity value, JsonGenerator jgen, SerializerProvider provider) 
      throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        jgen.writeStringField("id", value.getId());
        jgen.writeStringField("mzTabResultId", value.getMzTabResultId());
        jgen.writeStringField("visibility", value.getVisibility().name());
        jgen.writeEndObject();
    }
}
