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
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests PhoneticEngine.
 * 
 * Notes for readers:
 * - "concat" controls whether multiple encodings are concatenated across words (true) or kept per-word (false).
 * - When concat is false, the engine separates per-word encodings with '-' and per-word alternatives with '|'.
 * - When concat is true, only alternatives are returned, separated with '|'.
 */
class PhoneticEngineTest {

    private static final int TEN = 10;

    // Mirrors the old JUnit 4 @Test(timeout=10000L) semantics.
    private static final Duration ENCODE_TIMEOUT = Duration.ofSeconds(10);

    static Stream<Arguments> provideValidData() {
        // @formatter:off
        return Stream.of(
                Arguments.of("Renault", "rinD|rinDlt|rina|rinalt|rino|rinolt|rinu|rinult", NameType.GENERIC,   RuleType.APPROX, Boolean.TRUE,  TEN),
                Arguments.of("Renault", "rYnDlt|rYnalt|rYnult|rinDlt|rinalt|rinolt|rinult",                   NameType.ASHKENAZI, RuleType.APPROX, Boolean.TRUE,  TEN),
                Arguments.of("Renault", "rinDlt",                                                             NameType.ASHKENAZI, RuleType.APPROX, Boolean.TRUE,  1),
                Arguments.of("Renault", "rinDlt",                                                             NameType.SEPHARDIC, RuleType.APPROX, Boolean.TRUE,  TEN),
                Arguments.of("SntJohn-Smith", "sntjonsmit",                                                   NameType.GENERIC,   RuleType.EXACT,  Boolean.TRUE,  TEN),
                Arguments.of("d'ortley", "(ortlaj|ortlej)-(dortlaj|dortlej)",                                 NameType.GENERIC,   RuleType.EXACT,  Boolean.TRUE,  TEN),
                Arguments.of("van helsing", "(elSink|elsink|helSink|helsink|helzink|xelsink)-(banhelsink|fanhelsink|fanhelzink|vanhelsink|vanhelzink|vanjelsink)",
                                                                                                              NameType.GENERIC,   RuleType.EXACT,  Boolean.FALSE, TEN),
                Arguments.of("Judenburg", "iudnbYrk|iudnbirk|iudnburk|xudnbirk|xudnburk|zudnbirk|zudnburk",   NameType.GENERIC,   RuleType.APPROX, Boolean.TRUE,  TEN),
                Arguments.of("Judenburg", "iudnbYrk|iudnbirk|iudnburk|xudnbirk|xudnburk|zudnbirk|zudnburk",   NameType.GENERIC,   RuleType.APPROX, Boolean.TRUE,  Integer.MAX_VALUE)
        );
        // @formatter:on
    }

    static Stream<Arguments> provideInvalidData() {
        // @formatter:off
        return Stream.of(
                Arguments.of("bar", "bar|bor|var|vor", NameType.ASHKENAZI, RuleType.APPROX, Boolean.FALSE, TEN),
                Arguments.of("al",  "|al",             NameType.SEPHARDIC, RuleType.APPROX, Boolean.FALSE, TEN),
                Arguments.of("da",  "da|di",           NameType.GENERIC,   RuleType.EXACT,  Boolean.FALSE, TEN),
                Arguments.of("'''","",                 NameType.SEPHARDIC, RuleType.APPROX, Boolean.FALSE, TEN),
                Arguments.of("'''","",                 NameType.SEPHARDIC, RuleType.APPROX, Boolean.FALSE, Integer.MAX_VALUE)
        );
        // @formatter:on
    }

    @ParameterizedTest(name = "encode(''{0}'', {2}/{3}, concat={4}, max={5}) => ''{1}''")
    @MethodSource("provideValidData")
    @DisplayName("Encodes representative examples and respects max phoneme limits")
    void encodesExamplesAndRespectsLimit(final String input,
                                         final String expectedEncoding,
                                         final NameType nameType,
                                         final RuleType ruleType,
                                         final boolean concat,
                                         final int maxPhonemes) {
        final PhoneticEngine engine = new PhoneticEngine(nameType, ruleType, concat, maxPhonemes);

        final String actualEncoding = encodeWithinTimeout(engine, input);

        assertEquals(expectedEncoding, actualEncoding, "Unexpected phonetic encoding");
        assertAlternativeCountNotExceedLimit(actualEncoding, concat, maxPhonemes);
    }

    @ParameterizedTest(name = "encode(''{0}'', {2}/{3}, concat={4}, max={5}) => ''{1}'' (edge cases)")
    @MethodSource("provideInvalidData")
    @DisplayName("Encodes edge cases (exact string match)")
    void encodesEdgeCases(final String input,
                          final String expectedEncoding,
                          final NameType nameType,
                          final RuleType ruleType,
                          final boolean concat,
                          final int maxPhonemes) {
        final PhoneticEngine engine = new PhoneticEngine(nameType, ruleType, concat, maxPhonemes);

        final String actual = encodeWithinTimeout(engine, input);

        // Keep expected on the left for clear failure messages.
        assertEquals(expectedEncoding, actual);
    }

    private static String encodeWithinTimeout(final PhoneticEngine engine, final String input) {
        return assertTimeoutPreemptively(
                ENCODE_TIMEOUT,
                () -> engine.encode(input),
                () -> "encode() did not complete within " + ENCODE_TIMEOUT.toMillis() + " ms for input: " + input
        );
    }

    /**
     * Verifies that the number of alternatives returned by the engine does not exceed maxPhonemes.
     * - When concat is true: the output is a single group of alternatives separated by '|'.
     * - When concat is false: the output is word groups separated by '-', each containing alternatives separated by '|'.
     */
    private static void assertAlternativeCountNotExceedLimit(final String encoded, final boolean concat, final int maxPhonemes) {
        if (concat) {
            final String[] alts = encoded.split("\\|");
            assertTrue(alts.length <= maxPhonemes,
                    () -> "Expected at most " + maxPhonemes + " alternatives but found " + alts.length +
                          " in encoded: " + encoded);
        } else {
            final String[] words = encoded.split("-");
            for (final String word : words) {
                final String[] alts = word.split("\\|");
                assertTrue(alts.length <= maxPhonemes,
                        () -> "Expected at most " + maxPhonemes + " alternatives per word but found " + alts.length +
                              " in segment: '" + word + "', full encoded: " + encoded);
            }
        }
    }
}