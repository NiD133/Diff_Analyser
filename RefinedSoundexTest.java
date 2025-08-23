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

package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for RefinedSoundex.
 *
 * The tests are organized as small, focused parameterized assertions to improve readability
 * and maintenance. Magic values are replaced by named constants where it helps comprehension.
 */
class RefinedSoundexTest extends AbstractStringEncoderTest<RefinedSoundex> {

    private static final String DOGS = "dogs";
    private static final String DOGS_CODE = "D6043";

    /**
     * Expected encoding for the String made of the 256 8-bit characters (0..255).
     * This serves as a regression guard for how non-letters and unusual characters are handled.
     */
    private static final String EXPECTED_ASCII_0_255_CODE =
            "A0136024043780159360205050136024043780159360205053";

    @Override
    protected RefinedSoundex createStringEncoder() {
        return new RefinedSoundex();
    }

    private RefinedSoundex encoder() {
        return getStringEncoder();
    }

    // ----------------------------------------------------------------------
    // difference(..)
    // ----------------------------------------------------------------------

    @ParameterizedTest(name = "difference(\"{0}\", \"{1}\") = {2}")
    @MethodSource("differenceSamples")
    @DisplayName("Counts matching positions in encoded strings")
    void difference_shouldCountMatchingPositions(final String s1, final String s2, final int expected)
            throws EncoderException {
        assertEquals(expected, encoder().difference(s1, s2));
    }

    private static Stream<org.junit.jupiter.params.provider.Arguments> differenceSamples() {
        return Stream.of(
            // Edge cases
            arguments(null, null, 0),
            arguments("", "", 0),
            arguments(" ", " ", 0),

            // Typical cases
            arguments("Smith", "Smythe", 6),
            arguments("Ann", "Andrew", 3),
            arguments("Margaret", "Andrew", 1),
            arguments("Janet", "Margaret", 1),

            // Examples from MS T-SQL docs
            // https://msdn.microsoft.com/library/default.asp?url=/library/en-us/tsqlref/ts_de-dz_8co5.asp
            arguments("Green", "Greene", 5),
            arguments("Blotchet-Halls", "Greene", 1),

            // https://msdn.microsoft.com/library/default.asp?url=/library/en-us/tsqlref/ts_setu-sus_3o6w.asp
            arguments("Smith", "Smythe", 6),
            arguments("Smithers", "Smythers", 8),
            arguments("Anothers", "Brothers", 5)
        );
    }

    // ----------------------------------------------------------------------
    // encode(..)
    // ----------------------------------------------------------------------

    @ParameterizedTest(name = "encode(\"{0}\") = \"{1}\"")
    @MethodSource("encodeSamples")
    @DisplayName("Encodes words to expected Refined Soundex codes")
    void encode_shouldReturnExpectedCode(final String input, final String expected) {
        assertEquals(expected, encoder().encode(input));
    }

    private static Stream<org.junit.jupiter.params.provider.Arguments> encodeSamples() {
        return Stream.of(
            arguments("testing", "T6036084"),
            arguments("TESTING", "T6036084"), // case-insensitive
            arguments("The", "T60"),
            arguments("quick", "Q503"),
            arguments("brown", "B1908"),
            arguments("fox", "F205"),
            arguments("jumped", "J408106"),
            arguments("over", "O0209"),
            arguments("the", "T60"),
            arguments("lazy", "L7050"),
            arguments(DOGS, DOGS_CODE)
        );
    }

    @Test
    @DisplayName("US_ENGLISH static instance uses the default mapping")
    void encode_usingStaticUsEnglishInstance() {
        // Regression for CODEC-56
        assertEquals(DOGS_CODE, RefinedSoundex.US_ENGLISH.encode(DOGS));
    }

    // ----------------------------------------------------------------------
    // Mapping code
    // ----------------------------------------------------------------------

    @Test
    @DisplayName("Non-alphabetic characters map to code 0")
    void getMappingCode_returnsZeroForNonAlphabeticChar() {
        final char code = encoder().getMappingCode('#');
        assertEquals(0, code);
    }

    // ----------------------------------------------------------------------
    // Behavior with unusual input
    // ----------------------------------------------------------------------

    @Test
    @DisplayName("Encoding all 0..255 ASCII characters yields the expected code")
    void encode_allAsciiCharacters_producesExpectedCode() {
        final char[] allAscii = new char[256];
        for (int i = 0; i < allAscii.length; i++) {
            allAscii[i] = (char) i;
        }
        assertEquals(EXPECTED_ASCII_0_255_CODE, new RefinedSoundex().encode(new String(allAscii)));
    }

    // ----------------------------------------------------------------------
    // Constructors
    // ----------------------------------------------------------------------

    @ParameterizedTest(name = "Constructor #{index} encodes \"dogs\" as " + DOGS_CODE)
    @MethodSource("constructors")
    @DisplayName("All constructors produce the same default US English behavior")
    void constructors_produceEquivalentBehavior(final RefinedSoundex instance) {
        assertEquals(DOGS_CODE, instance.soundex(DOGS));
    }

    private static Stream<RefinedSoundex> constructors() {
        return Stream.of(
            new RefinedSoundex(),
            new RefinedSoundex(RefinedSoundex.US_ENGLISH_MAPPING_STRING.toCharArray()),
            new RefinedSoundex(RefinedSoundex.US_ENGLISH_MAPPING_STRING)
        );
    }
}