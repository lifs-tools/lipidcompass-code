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
package org.lipidcompass.data.parser;

import org.lifstools.jgoslin.domain.LipidAdduct;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.lifstools.jgoslin.domain.KnownFunctionalGroups;
import org.lifstools.jgoslin.domain.LipidException;
import org.lifstools.jgoslin.parser.FattyAcidParser;
import org.lifstools.jgoslin.parser.GoslinParser;
import org.lifstools.jgoslin.parser.HmdbParser;
import org.lifstools.jgoslin.parser.LipidMapsParser;
import org.lifstools.jgoslin.parser.LipidParser;
import org.lifstools.jgoslin.parser.Parser;
import org.lifstools.jgoslin.parser.ShorthandParser;
import org.lifstools.jgoslin.parser.SwissLipidsParser;

/**
 *
 * @author nilshoffmann
 */
@Slf4j
public class GoslinAllGrammarsParser {

    public static enum Grammar {
        GOSLIN, SWISSLIPIDS, LIPIDMAPS, HMDB, SHORTHAND, FA
    };

    private final List<GoslinAllGrammarsParser.Grammar> grammars;
    private final KnownFunctionalGroups knownFunctionalGroups;
    private final LipidParser parser;

    public GoslinAllGrammarsParser() {
        this(Arrays.asList(GoslinAllGrammarsParser.Grammar.values()));
    }

    public GoslinAllGrammarsParser(List<Grammar> grammars) {
        this.grammars = Collections.unmodifiableList(grammars);
        this.knownFunctionalGroups = new KnownFunctionalGroups();
        this.parser = new LipidParser(knownFunctionalGroups);
    }

    public Optional<LipidAdduct> parse(String lipidName, Deque<GoslinAllGrammarsParser.Grammar> grammars) {
        return parseWith(lipidName.trim(), grammars);
    }

    public Optional<LipidAdduct> parse(String lipidName) {
        Optional<LipidAdduct> lipidAdduct = Optional.empty();
        try {
            lipidAdduct = Optional.ofNullable(parser.parse(lipidName));
        } catch (LipidException | NullPointerException ex) {
            log.debug("Failed to parse name: " + lipidName, ex);
        }
        return lipidAdduct;
    }

    private Parser<LipidAdduct> parserFor(GoslinAllGrammarsParser.Grammar grammar) {
        switch (grammar) {
            case GOSLIN -> {
                return new GoslinParser(knownFunctionalGroups);
            }
            case LIPIDMAPS -> {
                return new LipidMapsParser(knownFunctionalGroups);
            }
            case SWISSLIPIDS -> {
                return new SwissLipidsParser(knownFunctionalGroups);
            }
            case HMDB -> {
                return new HmdbParser(knownFunctionalGroups);
            }
            case SHORTHAND -> {
                return new ShorthandParser(knownFunctionalGroups);
            }
            case FA -> {
                return new FattyAcidParser(knownFunctionalGroups);
            }
            default ->
                throw new RuntimeException("No parser implementation available for grammar '" + grammar + "'!");
        }
//            case GOSLIN_FRAGMENTS:
//                return new GoslinFragmentsVisitorParser();
    }

    private Optional<LipidAdduct> parseWith(String lipidName, Deque<GoslinAllGrammarsParser.Grammar> grammars) {
        log.debug("Grammars left: " + grammars.size());
        GoslinAllGrammarsParser.Grammar grammar = grammars.pop();
        Parser<LipidAdduct> parser = parserFor(grammar);
        log.debug("Using grammar " + grammar + " with parser: " + parser.getClass().getSimpleName());
        LipidAdduct la = parser.parse(lipidName, parser.newEventHandler(), false);
        if (la == null) {
            log.debug("Could not parse " + lipidName + " with " + parser.getClass().getName() + " for grammar " + grammar);
            if (grammars.isEmpty()) {
                return Optional.empty();
            } else {
                return parseWith(lipidName.trim(), grammars);
            }
        }
        return Optional.of(la);
    }
}
