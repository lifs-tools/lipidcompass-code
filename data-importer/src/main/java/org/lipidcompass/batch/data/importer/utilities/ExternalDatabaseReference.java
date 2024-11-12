/*
 * Copyright 2022 The LipidCompass Developers.
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
package org.lipidcompass.batch.data.importer.utilities;

/**
 * An external database reference for a lipid encodes the link to identify the
 * referenced lipid, contains its native abbreviation and name, the database's
 * element id and the name as normalized by Goslin.
 *
 * @author Nils Hoffmann
 */
public record ExternalDatabaseReference(
        String databaseUrl,
        String databaseElementId,
        String lipidLevel,
        String nativeAbbreviation,
        String nativeName,
        String normalizedName){};
