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

import com.arangodb.entity.KeyType;
import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.PersistentIndexed;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author nilshoffmann
 */
@Document(value = "fattyAcyls", keyType = KeyType.autoincrement, keyIncrement = 1)
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class FattyAcyl extends ArangoBaseEntity {

    public enum FaBondType {
        UNDEFINED, ESTER, ETHER_PLASMANYL, ETHER_PLASMENYL, ETHER_UNSPECIFIED, AMIDE, LCB_EXCEPTION, LCB_REGULAR, NO_FA;

        public static FaBondType of(org.lifstools.jgoslin.domain.LipidFaBondType sourceType) {
            switch (sourceType) {
                case ESTER -> {
                    return FaBondType.ESTER;
                }
                case ETHER_PLASMANYL -> {
                    return FaBondType.ETHER_PLASMANYL;
                }
                case ETHER_PLASMENYL -> {
                    return FaBondType.ETHER_PLASMENYL;
                }
                case ETHER_UNSPECIFIED -> {
                    return FaBondType.ETHER_UNSPECIFIED;
                }
                case AMIDE -> {
                    return FaBondType.AMIDE;
                }
                case LCB_EXCEPTION -> {
                    return FaBondType.LCB_EXCEPTION;
                }
                case LCB_REGULAR -> {
                    return FaBondType.LCB_REGULAR;
                }
                case NO_FA -> {
                    return FaBondType.NO_FA;
                }
                case UNDEFINED_FA -> {
                    return FaBondType.UNDEFINED;
                }
                default -> throw new IllegalArgumentException("Could not map source faBondType '" + sourceType + "'. Unknown type!");
            }
        }
    }
    
    public enum FaType {
        FA, LCB;
    }

    @PersistentIndexed
    private String name;
    @PersistentIndexed
    private int position;
    @PersistentIndexed
    private int nCarbon;
    @PersistentIndexed
    private int nHydroxy;
    @PersistentIndexed
    private int nDoubleBonds;
    @PersistentIndexed
    private FaBondType faBondType;
    @PersistentIndexed
    private FaType faType;
//    private ModificationsList modifications;
    private Map<Integer, String> doubleBondPositions;

    @Builder
    public FattyAcyl(String name, int position, int nCarbon, int nHydroxy, int nDoubleBonds, FaBondType faBondType, Map<Integer, String> doubleBondPositions, String transactionUuid, Visibility visibility) {
        super(transactionUuid, visibility);
        this.name = name;
        this.position = position;
        this.nCarbon = nCarbon;
        this.nHydroxy = nHydroxy;
        this.nDoubleBonds = nDoubleBonds;
        this.faBondType = faBondType;
        switch(faBondType) {
            case LCB_EXCEPTION:
            case LCB_REGULAR:
                this.faType = FaType.LCB;
                break;
            default:
                this.faType = FaType.FA;
        }
        this.doubleBondPositions = doubleBondPositions;
    }

}
