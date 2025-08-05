/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.codec.language.bm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests PhoneticEngine.
 */
class PhoneticEngineTest {

    private static final int MAX_PHONEMES_DEFAULT = 10;

    public static Stream<Arguments> validInputsProvider() {
        // @formatter:off
        return Stream.of(
            // Test cases with valid inputs covering different configurations
            Arguments.of("Renault", "rinD|rinDlt|rina|rinalt|rino|rinolt|rinu|rinult", 
                         NameType.GENERIC, RuleType.APPROX, true, MAX_PHONEMES_DEFAULT),
            
            Arguments.of("Renault", "rYnDlt|rYnalt|rYnult|rinDlt|rinalt|rinolt|rinult", 
                         NameType.ASHKENAZI, RuleType.APPROX, true, MAX_PHONEMES_DEFAULT),
            
            Arguments.of("Renault", "rinDlt", 
                         NameType.ASHKENAZI, RuleType.APPROX, true, 1),
            
            Arguments.of("Renault", "rinDlt", 
                         NameType.SEPHARDIC, RuleType.APPROX, true, MAX_PHONEMES_DEFAULT),
            
            Arguments.of("SntJohn-Smith", "sntjonsmit", 
                         NameType.GENERIC, RuleType.EXACT, true, MAX_PHONEMES_DEFAULT),
            
            Arguments.of("d'ortley", "(ortlaj|ortlej)-(dortlaj|dortlej)", 
                         NameType.GENERIC, RuleType.EXACT, true, MAX_PHONEMES_DEFAULT),
            
            Arguments.of("van helsing", "(elSink|elsink|helSink|helsink|helzink|xelsink)-(banhelsink|fanhelsink|fanhelzink|vanhelsink|vanhelzink|vanjelsink)", 
                         NameType.GENERIC, RuleType.EXACT, false, MAX_PHONEMES_DEFAULT),
            
            Arguments.of("Judenburg", "iudnbYrk|iudnbirk|iudnburk|xudnbirk|xudnburk|zudnbirk|zudnburk", 
                         NameType.GENERIC, RuleType.APPROX, true, MAX_PHONEMES_DEFAULT),
            
            Arguments.of("Judenburg", "iudnbYrk|iudnbirk|iudnburk|xudnbirk|xudnburk|zudnbirk|zudnburk", 
                         NameType.GENERIC, RuleType.APPROX, true, Integer.MAX_VALUE)
        );
        // @formatter:on
    }

    public static Stream<Arguments> invalidInputsProvider() {
        // @formatter:off
        return Stream.of(
            // Test cases with edge-case/invalid inputs
            Arguments.of("bar", "bar|bor|var|vor", 
                         NameType.ASHKENAZI, RuleType.APPROX, false, MAX_PHONEMES_DEFAULT),
            
            Arguments.of("al", "|al", 
                         NameType.SEPHARDIC, RuleType.APPROX, false, MAX_PHONEMES_DEFAULT),
            
            Arguments.of("da", "da|di", 
                         NameType.GENERIC, RuleType.EXACT, false, MAX_PHONEMES_DEFAULT),
            
            Arguments.of("'''", "", 
                         NameType.SEPHARDIC, RuleType.APPROX, false, MAX_PHONEMES_DEFAULT),
            
            Arguments.of("'''", "", 
                         NameType.SEPHARDIC, RuleType.APPROX, false, Integer.MAX_VALUE)
        );
        // @formatter:on
    }

    @ParameterizedTest
    @MethodSource("validInputsProvider")
    void encode_ValidInputs_ProducesExpectedPhoneticRepresentation(
        final String inputName,
        final String expectedPhonetic,
        final NameType nameType,
        final RuleType ruleType,
        final boolean shouldConcat,
        final int maxPhonemes
    ) {
        final PhoneticEngine engine = new PhoneticEngine(nameType, ruleType, shouldConcat, maxPhonemes);
        final String actualPhonetic = engine.encode(inputName);

        assertEquals(expectedPhonetic, actualPhonetic, 
            "Phonetic representation mismatch for input: " + inputName);
        
        assertPhonemeCountWithinLimit(actualPhonetic, shouldConcat, maxPhonemes);
    }

    @ParameterizedTest
    @MethodSource("invalidInputsProvider")
    void encode_InvalidInputs_ProducesExpectedPhoneticRepresentation(
        final String inputName,
        final String expectedPhonetic,
        final NameType nameType,
        final RuleType ruleType,
        final boolean shouldConcat,
        final int maxPhonemes
    ) {
        final PhoneticEngine engine = new PhoneticEngine(nameType, ruleType, shouldConcat, maxPhonemes);
        final String actualPhonetic = engine.encode(inputName);

        assertEquals(expectedPhonetic, actualPhonetic,
            "Phonetic representation mismatch for invalid input: " + inputName);
    }

    /**
     * Helper method to verify phoneme count doesn't exceed max limit
     */
    private void assertPhonemeCountWithinLimit(
        final String phoneticResult, 
        final boolean shouldConcat, 
        final int maxPhonemes
    ) {
        if (shouldConcat) {
            final String[] phonemes = phoneticResult.split("\\|");
            assertTrue(phonemes.length <= maxPhonemes,
                "Phoneme count (" + phonemes.length + ") exceeds max (" + maxPhonemes + ")");
        } else {
            for (final String word : phoneticResult.split("-")) {
                final String[] phonemes = word.split("\\|");
                assertTrue(phonemes.length <= maxPhonemes,
                    "Word phoneme count (" + phonemes.length + ") exceeds max (" + maxPhonemes + ")");
            }
        }
    }
}