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
import org.junit.jupiter.api.Test;

/**
 * Tests {@link RefinedSoundex}.
 */
class RefinedSoundexTest extends AbstractStringEncoderTest<RefinedSoundex> {

    @Override
    protected RefinedSoundex createStringEncoder() {
        return new RefinedSoundex();
    }

    @Test
    void testStringDifference_EdgeCasesAndCommonExamples() throws EncoderException {
        final RefinedSoundex encoder = getStringEncoder();

        // Edge cases with null/empty inputs
        assertEquals(0, encoder.difference(null, null));
        assertEquals(0, encoder.difference("", ""));
        assertEquals(0, encoder.difference(" ", " "));

        // Common names and examples
        assertEquals(6, encoder.difference("Smith", "Smythe"));
        assertEquals(3, encoder.difference("Ann", "Andrew"));
        assertEquals(1, encoder.difference("Margaret", "Andrew"));
        assertEquals(1, encoder.difference("Janet", "Margaret"));

        // Examples from external documentation:
        // https://msdn.microsoft.com/library/default.asp?url=/library/en-us/tsqlref/ts_de-dz_8co5.asp
        assertEquals(5, encoder.difference("Green", "Greene"));
        assertEquals(1, encoder.difference("Blotchet-Halls", "Greene"));

        // Additional examples from:
        // https://msdn.microsoft.com/library/default.asp?url=/library/en-us/tsqlref/ts_setu-sus_3o6w.asp
        assertEquals(6, encoder.difference("Smith", "Smythe"));
        assertEquals(8, encoder.difference("Smithers", "Smythers"));
        assertEquals(5, encoder.difference("Anothers", "Brothers"));
    }

    @Test
    void testEncode_BasicStrings() {
        final RefinedSoundex encoder = getStringEncoder();

        // Standard encoding cases
        assertEquals("T6036084", encoder.encode("testing"));
        assertEquals("T6036084", encoder.encode("TESTING")); // Case insensitivity
        assertEquals("T60", encoder.encode("The"));
        assertEquals("Q503", encoder.encode("quick"));
        assertEquals("B1908", encoder.encode("brown"));
        assertEquals("F205", encoder.encode("fox"));
        assertEquals("J408106", encoder.encode("jumped"));
        assertEquals("O0209", encoder.encode("over"));
        assertEquals("T60", encoder.encode("the")); // Same as "The"
        assertEquals("L7050", encoder.encode("lazy"));
        assertEquals("D6043", encoder.encode("dogs"));

        // Test CODEC-56: Verify static instance produces same result
        assertEquals("D6043", RefinedSoundex.US_ENGLISH.encode("dogs"));
    }

    @Test
    void testGetMappingCode_NonLetterCharacter_ReturnsZero() {
        final char code = getStringEncoder().getMappingCode('#');
        assertEquals(0, code, "Non-letter characters should map to 0");
    }

    @Test
    void testEncode_StringWithAllPossibleCharacters_ProducesExpectedCode() {
        // Create string containing all 256 ASCII characters
        final char[] allChars = new char[256];
        for (int i = 0; i < allChars.length; i++) {
            allChars[i] = (char) i;
        }
        final String input = new String(allChars);

        // Verify encoding handles all character values
        final String result = new RefinedSoundex().encode(input);
        assertEquals("A0136024043780159360205050136024043780159360205053", result);
    }

    @Test
    void testSoundex_DefaultConstructor_ProducesExpectedEncoding() {
        assertEquals("D6043", new RefinedSoundex().soundex("dogs"));
    }

    @Test
    void testSoundex_ConstructorWithMappingCharArray_ProducesExpectedEncoding() {
        final char[] mapping = RefinedSoundex.US_ENGLISH_MAPPING_STRING.toCharArray();
        assertEquals("D6043", new RefinedSoundex(mapping).soundex("dogs"));
    }

    @Test
    void testSoundex_ConstructorWithMappingString_ProducesExpectedEncoding() {
        final String mapping = RefinedSoundex.US_ENGLISH_MAPPING_STRING;
        assertEquals("D6043", new RefinedSoundex(mapping).soundex("dogs"));
    }
}