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
package org.lipidcompass.backend.rest;

import org.lifstools.jgoslin.domain.LipidLevel;

/**
 *
 * @author nilshoffmann
 */
public class MappingUtils {

    public static String getLipidLevelAttributeName(LipidLevel lipidLevel) {
        String attributeName = switch (lipidLevel) {
            case CATEGORY ->
                "lipidCategory";
            case CLASS ->
                "lipidClass";
            case SPECIES ->
                "lipidSpecies";
            case MOLECULAR_SPECIES ->
                "lipidMolecularSpecies";
            case SN_POSITION ->
                "lipidSnPosition";
            case STRUCTURE_DEFINED ->
                "lipidStructureDefined";
            case COMPLETE_STRUCTURE ->
                "lipidCompleteStructure";
            case FULL_STRUCTURE ->
                "lipidFullStructure";
            case NO_LEVEL ->
                "lipidNoLevel";
            case UNDEFINED_LEVEL ->
                "lipidUndefinedLevel";
            default ->
                "lipidSpecies";
        };
        return attributeName;
    }
}
