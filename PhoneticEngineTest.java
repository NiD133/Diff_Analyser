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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests the {@link PhoneticEngine} for phonetic encoding under various configurations.
 */
class PhoneticEngineTest {

    private static Stream<Arguments> provideStandardEncodingCases() {
        // Each Arguments entry: inputName, expectedPhonetic, nameType, ruleType, concat, maxPhonemes
        // @formatter:off
        return Stream.of(
            // --- Test 'Renault' with different NameTypes and a standard maxPhonemes ---
            Arguments.of("Renault", "rinD|rinDlt|rina|rinalt|rino|rinolt|rinu|rinult", NameType.GENERIC, RuleType.APPROX, true, 10),
            Arguments.of("Renault", "rYnDlt|rYnalt|rYnult|rinDlt|rinalt|rinolt|rinult", NameType.ASHKENAZI, RuleType.APPROX, true, 10),
            Arguments.of("Renault", "rinDlt", NameType.SEPHARDIC, RuleType.APPROX, true, 10),

            // --- Test the maxPhonemes constraint ---
            Arguments.of("Renault", "rinDlt", NameType.ASHKENAZI, RuleType.APPROX, true, 1),

            // --- Test multi-word and complex names ---
            Arguments.of("SntJohn-Smith", "sntjonsmit", NameType.GENERIC, RuleType.EXACT, true, 10), // Hyphenated
            Arguments.of("d'ortley", "(ortlaj|ortlej)-(dortlaj|dortlej)", NameType.GENERIC, RuleType.EXACT, true, 10), // Apostrophe
            Arguments.of("van helsing", "(elSink|elsink|helSink|helsink|helzink|xelsink)-(banhelsink|fanhelsink|fanhelzink|vanhelsink|vanhelzink|vanjelsink)", NameType.GENERIC, RuleType.EXACT, false, 10), // Space-separated, not concatenated

            // --- Test with a large maxPhonemes value ---
            Arguments.of("Judenburg", "iudnbYrk|iudnbirk|iudnburk|xudnbirk|xudnburk|zudnbirk|zudnburk", NameType.GENERIC, RuleType.APPROX, true, 10),
            Arguments.of("Judenburg", "iudnbYrk|iudnbirk|iudnburk|xudnbirk|xudnburk|zudnbirk|zudnburk", NameType.GENERIC, RuleType.APPROX, true, Integer.MAX_VALUE)
        );
        // @formatter:on
    }

    private static Stream<Arguments> provideSpecialEncodingCases() {
        // Each Arguments entry: input, expectedPhonetic, nameType, ruleType, concat, maxPhonemes
        // @formatter:off
        return Stream.of(
            // --- Test name prefixes which are handled specially ---
            Arguments.of("bar", "bar|bor|var|vor", NameType.ASHKENAZI, RuleType.APPROX, false, 10),
            Arguments.of("al", "|al", NameType.SEPHARDIC, RuleType.APPROX, false, 10),
            Arguments.of("da", "da|di", NameType.GENERIC, RuleType.EXACT, false, 10),

            // --- Test input with only non-alphanumeric characters, which should result in an empty encoding ---
            Arguments.of("'''", "", NameType.SEPHARDIC, RuleType.APPROX, false, 10),
            Arguments.of("'''", "", NameType.SEPHARDIC, RuleType.APPROX, false, Integer.MAX_VALUE)
        );
        // @formatter:on
    }

    @DisplayName("Should encode names correctly with various configurations")
    @ParameterizedTest(name = "{index}: name={0}, type={2}, rule={3}, concat={4}, maxPhonemes={5}")
    @MethodSource("provideStandardEncodingCases")
    void testStandardEncoding(final String name, final String expectedPhonetic, final NameType nameType,
                              final RuleType ruleType, final boolean concat, final int maxPhonemes) {
        // Arrange
        final PhoneticEngine engine = new PhoneticEngine(nameType, ruleType, concat, maxPhonemes);

        // Act
        final String actualPhonetic = engine.encode(name);

        // Assert
        assertEquals(expectedPhonetic, actualPhonetic, "The phonetic encoding should match the expected value.");
        assertMaxPhonemesConstraint(actualPhonetic, concat, maxPhonemes);
    }

    @DisplayName("Should produce expected encodings for special cases like prefixes and non-alphabetic input")
    @ParameterizedTest(name = "{index}: input=\"{0}\", type={2}, rule={3}, concat={4}, maxPhonemes={5}")
    @MethodSource("provideSpecialEncodingCases")
    void testSpecialCaseEncoding(final String input, final String expectedPhonetic, final NameType nameType,
                                 final RuleType ruleType, final boolean concat, final int maxPhonemes) {
        // Arrange
        final PhoneticEngine engine = new PhoneticEngine(nameType, ruleType, concat, maxPhonemes);

        // Act
        final String actualPhonetic = engine.encode(input);

        // Assert
        assertEquals(expectedPhonetic, actualPhonetic, "The phonetic encoding for the special case should match.");
    }

    /**
     * Asserts that the generated phonetic encoding respects the {@code maxPhonemes} constraint.
     *
     * @param phoneticEncoding The full phonetic string generated by the engine.
     * @param isConcat         The 'concat' setting of the engine.
     * @param maxPhonemes      The maximum number of phonemes allowed.
     */
    private void assertMaxPhonemesConstraint(final String phoneticEncoding, final boolean isConcat, final int maxPhonemes) {
        if (isConcat) {
            // When concatenated, phonemes for the whole name are joined by "|"
            final String[] phonemes = phoneticEncoding.split("\\|");
            assertTrue(phonemes.length <= maxPhonemes,
                () -> "Number of phonemes (" + phonemes.length + ") should not exceed maxPhonemes (" + maxPhonemes + ")");
        } else {
            // When not concatenated, each word's phonemes are in groups separated by "-"
            final String[] wordEncodings = phoneticEncoding.split("-");
            for (final String wordEncoding : wordEncodings) {
                final String[] phonemes = wordEncoding.split("\\|");
                assertTrue(phonemes.length <= maxPhonemes,
                    () -> "Number of phonemes for a word (" + phonemes.length + ") should not exceed maxPhonemes (" + maxPhonemes + ")");
            }
        }
    }
}