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

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link RefinedSoundex} encoder with a focus on clarity and maintainability.
 */
@DisplayName("Tests for RefinedSoundex encoder")
class RefinedSoundexTest extends AbstractStringEncoderTest<RefinedSoundex> {

    @Override
    protected RefinedSoundex createStringEncoder() {
        return new RefinedSoundex();
    }

    @Nested
    @DisplayName("Encoding logic")
    class EncodingTests {

        @Test
        @DisplayName("should produce correct codes for a sample of English words")
        void shouldEncodeKnownWords() {
            assertEquals("T6036084", getStringEncoder().encode("testing"));
            assertEquals("Q503", getStringEncoder().encode("quick"));
            assertEquals("B1908", getStringEncoder().encode("brown"));
            assertEquals("F205", getStringEncoder().encode("fox"));
            assertEquals("J408106", getStringEncoder().encode("jumped"));
            assertEquals("O0209", getStringEncoder().encode("over"));
            assertEquals("T60", getStringEncoder().encode("the"));
            assertEquals("L7050", getStringEncoder().encode("lazy"));
            assertEquals("D6043", getStringEncoder().encode("dogs"));
        }

        @Test
        @DisplayName("should be case-insensitive")
        void shouldBeCaseInsensitive() {
            // The implementation cleans the string, which includes uppercasing.
            assertEquals(getStringEncoder().encode("TESTING"), getStringEncoder().encode("testing"),
                "Encoding should be the same for different cases.");
        }

        @Test
        @DisplayName("should ignore non-alphabetic characters")
        void shouldIgnoreNonAlphabeticCharacters() {
            // The implementation cleans the string, which removes non-letters.
            assertEquals(getStringEncoder().encode("Ashcraft"), getStringEncoder().encode("A$hcraft"),
                "Special characters should be ignored.");
            assertEquals(getStringEncoder().encode("Tymczak"), getStringEncoder().encode("T-y-m-c-z-a-k"),
                "Hyphens should be ignored.");
        }

        @Test
        @DisplayName("should return an empty string for an input containing no letters")
        void shouldReturnEmptyStringForInputWithNoLetters() {
            assertEquals("", getStringEncoder().encode("!@#$%^&*()"));
            assertEquals("", getStringEncoder().encode("1234567890"));
        }
    }

    @Nested
    @DisplayName("Difference calculation")
    class DifferenceTests {

        @Test
        @DisplayName("should return 0 for null or empty inputs")
        void shouldReturnZeroForNullOrEmptyInputs() throws EncoderException {
            assertEquals(0, getStringEncoder().difference(null, null));
            assertEquals(0, getStringEncoder().difference("", ""));
            assertEquals(0, getStringEncoder().difference(" ", " ")); // " " is cleaned to ""
        }

        @Test
        @DisplayName("should calculate similarity between common names")
        void shouldCalculateSimilarityForCommonNames() throws EncoderException {
            assertEquals(6, getStringEncoder().difference("Smith", "Smythe"));
            assertEquals(3, getStringEncoder().difference("Ann", "Andrew"));
            assertEquals(1, getStringEncoder().difference("Margaret", "Andrew"));
            assertEquals(1, getStringEncoder().difference("Janet", "Margaret"));
        }

        @Test
        @DisplayName("should match known examples from Microsoft T-SQL documentation")
        void shouldMatchKnownMicrosoftExamples() throws EncoderException {
            // Examples from https://msdn.microsoft.com/library/default.asp?url=/library/en-us/tsqlref/ts_de-dz_8co5.asp
            // and https://msdn.microsoft.com/library/default.asp?url=/library/en-us/tsqlref/ts_setu-sus_3o6w.asp
            assertEquals(5, getStringEncoder().difference("Green", "Greene"));
            assertEquals(1, getStringEncoder().difference("Blotchet-Halls", "Greene"));
            assertEquals(8, getStringEncoder().difference("Smithers", "Smythers"));
            assertEquals(5, getStringEncoder().difference("Anothers", "Brothers"));
        }
    }

    @Nested
    @DisplayName("Instantiation and configuration")
    class InstanceTests {

        @Test
        @DisplayName("should be creatable with a custom mapping string")
        void shouldWorkWithStringConstructor() {
            final RefinedSoundex customInstance = new RefinedSoundex(RefinedSoundex.US_ENGLISH_MAPPING_STRING);
            assertEquals("D6043", customInstance.soundex("dogs"));
        }

        @Test
        @DisplayName("should be creatable with a custom mapping char array")
        void shouldWorkWithCharArrayConstructor() {
            final char[] mapping = RefinedSoundex.US_ENGLISH_MAPPING_STRING.toCharArray();
            final RefinedSoundex customInstance = new RefinedSoundex(mapping);
            assertEquals("D6043", customInstance.soundex("dogs"));
        }

        @Test
        @DisplayName("should provide a pre-configured static instance for US English (verifies CODEC-56)")
        void shouldWorkWithStaticInstance() {
            assertEquals("D6043", RefinedSoundex.US_ENGLISH.encode("dogs"));
        }
    }

    @Test
    @DisplayName("getMappingCode() should return 0 for non-letter characters")
    void getMappingCodeShouldReturnZeroForNonLetter() {
        final char code = getStringEncoder().getMappingCode('#');
        assertEquals(0, code, "The mapping code for a non-letter character should be 0.");
    }
}