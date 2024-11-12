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
package org.lipidcompass.batch.data.importer.utilities;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.lifstools.jgoslin.domain.LipidAdduct;
import org.lifstools.jgoslin.domain.LipidException;
import org.lifstools.jgoslin.domain.LipidParsingException;
import org.lifstools.jgoslin.parser.GoslinParser;
import org.lifstools.jgoslin.parser.LipidParser;
import org.lifstools.jgoslin.parser.SwissLipidsParser;
import org.lifstools.jgoslin.parser.SwissLipidsParserEventHandler;

/**
 *
 * @author nils.hoffmann
 */
@Slf4j
public class LipidNameNormalizer {

    private final static SwissLipidsParser slp = new SwissLipidsParser();
    private final static SwissLipidsParserEventHandler slpHandler = slp.newEventHandler();
    private final static LipidParser lipidParser = new LipidParser();
    private final static GoslinParser gparser = new GoslinParser();

//    public static Optional<Lipid> of(Optional<LipidAdduct> ola) {
//        if (ola.isEmpty()) {
//            return Optional.empty();
//        }
//        if (ola.get().getAdduct().getCharge() != 0 || !ola.get().getAdduct().getElements().isEmpty()) {
//            log.warn("Returning empty lipid for lipid adduct with adduct information: ", ola);
//            return Optional.empty();
//        }
//        if (!ola.get().getFragment().getName().isEmpty()) {
//            log.warn("Returning empty lipid for lipid adduct with fragment information: ", ola);
//            return Optional.empty();
//        }
//        LipidSpecies lipid = ola.get().getLipid();
//        String id = "LCLID:" + lipid.getNormalizedLipidString();
//        UUID uuid = UUID.nameUUIDFromBytes(id.getBytes(Charsets.UTF_8));
//        LipidBuilder lb = Lipid.builder().
//                chemicalFormula(lipid.getElements().getSumFormula()).
//                exactMass(lipid.getElements().getMass().floatValue()).
////                commonName(lipid.getLipidString()).
//                id(uuid.toString()).
//                nativeUrl("/lipids/"+"LCMP:" + lipid.getNormalizedLipidString()).
//                normalizedShorthandName(lipid.getNormalizedLipidString()).
////                systematicName(lipid.getNormalizedLipidString()).
//                synonyms(Arrays.asList(lipid.getNormalizedLipidString(), lipid.getLipidString()));
//        return Optional.of(lb.build());
//        
//    }
    public static Optional<LipidAdduct> normalizedSwissLipidsShortHandString(String abbreviation) {
        String result;
        if (abbreviation == null || abbreviation.isEmpty()) {
            return Optional.empty();
        }
        try {
            LipidAdduct la = lipidParser.parse(abbreviation);
            return Optional.ofNullable(la);
        } catch (LipidParsingException ex) {
            log.debug("Exception while parsing '" + abbreviation + "'", ex);
            return Optional.empty();
        }
    }

    public static Optional<LipidAdduct> normalizedLipidMapsShortHandString(String abbreviation) {
//        String result;
        if (abbreviation == null || abbreviation.isEmpty()) {
            return Optional.empty();
        }
        try {
            LipidAdduct la = lipidParser.parse(abbreviation);
            return Optional.ofNullable(la);
        } catch (LipidParsingException ex) {
            log.debug("Exception while parsing '" + abbreviation + "'", ex);
            return Optional.empty();
        }
    }

}
