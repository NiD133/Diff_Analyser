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
 * Tests for PhoneticEngine - verifies phonetic encoding of names using Beider-Morse algorithm.
 * 
 * The PhoneticEngine converts names into phonetic representations that can be compared
 * across different languages and name origins (Generic, Ashkenazi, Sephardic).
 */
class PhoneticEngineTest {

    private static final int DEFAULT_MAX_PHONEMES = 10;
    private static final int SINGLE_PHONEME = 1;

    /**
     * Test data for valid phonetic encoding scenarios.
     * Each test case includes:
     * - Input name
     * - Expected phonetic output (pipe-separated alternatives)
     * - Name type (Generic, Ashkenazi, Sephardic)
     * - Rule type (Exact or Approximate matching)
     * - Whether to concatenate multiple word encodings
     * - Maximum number of phonemes to generate
     */
    public static Stream<Arguments> validEncodingTestCases() {
        return Stream.of(
                // French surname "Renault" with different name type interpretations
                Arguments.of("Renault", "rinD|rinDlt|rina|rinalt|rino|rinolt|rinu|rinult", 
                           NameType.GENERIC, RuleType.APPROX, true, DEFAULT_MAX_PHONEMES),
                           
                Arguments.of("Renault", "rYnDlt|rYnalt|rYnult|rinDlt|rinalt|rinolt|rinult", 
                           NameType.ASHKENAZI, RuleType.APPROX, true, DEFAULT_MAX_PHONEMES),
                           
                // Test limiting phonemes to single result
                Arguments.of("Renault", "rinDlt", 
                           NameType.ASHKENAZI, RuleType.APPROX, true, SINGLE_PHONEME),
                           
                Arguments.of("Renault", "rinDlt", 
                           NameType.SEPHARDIC, RuleType.APPROX, true, DEFAULT_MAX_PHONEMES),

                // Hyphenated surname with exact matching
                Arguments.of("SntJohn-Smith", "sntjonsmit", 
                           NameType.GENERIC, RuleType.EXACT, true, DEFAULT_MAX_PHONEMES),

                // Name with apostrophe prefix
                Arguments.of("d'ortley", "(ortlaj|ortlej)-(dortlaj|dortlej)", 
                           NameType.GENERIC, RuleType.EXACT, true, DEFAULT_MAX_PHONEMES),

                // Multi-word name without concatenation (separate word encoding)
                Arguments.of("van helsing", "(elSink|elsink|helSink|helsink|helzink|xelsink)-(banhelsink|fanhelsink|fanhelzink|vanhelsink|vanhelzink|vanjelsink)", 
                           NameType.GENERIC, RuleType.EXACT, false, DEFAULT_MAX_PHONEMES),

                // German place name with approximate matching
                Arguments.of("Judenburg", "iudnbYrk|iudnbirk|iudnburk|xudnbirk|xudnburk|zudnbirk|zudnburk", 
                           NameType.GENERIC, RuleType.APPROX, true, DEFAULT_MAX_PHONEMES),
                           
                // Same test with unlimited phonemes
                Arguments.of("Judenburg", "iudnbYrk|iudnbirk|iudnburk|xudnbirk|xudnburk|zudnbirk|zudnburk", 
                           NameType.GENERIC, RuleType.APPROX, true, Integer.MAX_VALUE)
        );
    }

    /**
     * Test data for edge cases and special input scenarios.
     * These test cases verify proper handling of:
     * - Short names
     * - Names with prefixes
     * - Special characters
     * - Empty results
     */
    public static Stream<Arguments> edgeCaseTestData() {
        return Stream.of(
                // Short name with multiple phonetic possibilities
                Arguments.of("bar", "bar|bor|var|vor", 
                           NameType.ASHKENAZI, RuleType.APPROX, false, DEFAULT_MAX_PHONEMES),
                           
                // Name prefix that can be empty or preserved
                Arguments.of("al", "|al", 
                           NameType.SEPHARDIC, RuleType.APPROX, false, DEFAULT_MAX_PHONEMES),
                           
                // Short name with exact matching
                Arguments.of("da", "da|di", 
                           NameType.GENERIC, RuleType.EXACT, false, DEFAULT_MAX_PHONEMES),
                           
                // Special characters that result in empty encoding
                Arguments.of("'''", "", 
                           NameType.SEPHARDIC, RuleType.APPROX, false, DEFAULT_MAX_PHONEMES),
                           
                Arguments.of("'''", "", 
                           NameType.SEPHARDIC, RuleType.APPROX, false, Integer.MAX_VALUE)
        );
    }

    /**
     * Tests phonetic encoding with valid input names.
     * Verifies that:
     * 1. The encoded result matches the expected phonetic representation
     * 2. The number of phonemes doesn't exceed the specified maximum
     * 3. Concatenation behavior works correctly for multi-word names
     */
    @ParameterizedTest
    @MethodSource("validEncodingTestCases")
    void shouldEncodeNamesCorrectly(String inputName, String expectedPhoneticResult, 
                                   NameType nameType, RuleType ruleType, 
                                   boolean shouldConcatenateWords, int maxPhonemes) {
        // Given: A phonetic engine configured with specific parameters
        PhoneticEngine engine = new PhoneticEngine(nameType, ruleType, shouldConcatenateWords, maxPhonemes);

        // When: Encoding the input name
        String actualPhoneticResult = engine.encode(inputName);

        // Then: The result should match expected phonetic representation
        assertEquals(expectedPhoneticResult, actualPhoneticResult, 
                    String.format("Phonetic encoding failed for name '%s' with %s/%s rules", 
                                inputName, nameType, ruleType));

        // And: The number of phonemes should not exceed the maximum limit
        verifyPhonemeCountLimit(actualPhoneticResult, shouldConcatenateWords, maxPhonemes);
    }

    /**
     * Tests phonetic encoding with edge cases and special inputs.
     * Verifies proper handling of short names, prefixes, and special characters.
     */
    @ParameterizedTest
    @MethodSource("edgeCaseTestData")
    void shouldHandleEdgeCasesCorrectly(String inputName, String expectedPhoneticResult, 
                                       NameType nameType, RuleType ruleType, 
                                       boolean shouldConcatenateWords, int maxPhonemes) {
        // Given: A phonetic engine configured for edge case testing
        PhoneticEngine engine = new PhoneticEngine(nameType, ruleType, shouldConcatenateWords, maxPhonemes);

        // When: Encoding the edge case input
        String actualResult = engine.encode(inputName);

        // Then: The result should match the expected output
        assertEquals(expectedPhoneticResult, actualResult,
                    String.format("Edge case encoding failed for input '%s'", inputName));
    }

    /**
     * Verifies that the phonetic result doesn't exceed the maximum phoneme limit.
     * 
     * @param phoneticResult The encoded phonetic string
     * @param isConcatenated Whether words are concatenated or separated
     * @param maxPhonemes Maximum allowed phonemes per word
     */
    private void verifyPhonemeCountLimit(String phoneticResult, boolean isConcatenated, int maxPhonemes) {
        if (isConcatenated) {
            // For concatenated results, check total phoneme count
            String[] phonemes = phoneticResult.split("\\|");
            assertTrue(phonemes.length <= maxPhonemes, 
                      String.format("Total phonemes (%d) exceeded limit (%d) in concatenated result: %s", 
                                  phonemes.length, maxPhonemes, phoneticResult));
        } else {
            // For non-concatenated results, check each word separately
            String[] words = phoneticResult.split("-");
            for (String word : words) {
                String[] phonemes = word.split("\\|");
                assertTrue(phonemes.length <= maxPhonemes,
                          String.format("Phonemes (%d) exceeded limit (%d) in word '%s'", 
                                      phonemes.length, maxPhonemes, word));
            }
        }
    }
}