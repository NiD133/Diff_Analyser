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
 * Tests for RefinedSoundex phonetic encoding algorithm.
 * 
 * RefinedSoundex encodes words into phonetic codes to enable fuzzy matching
 * of similar-sounding words, particularly useful for spell checking.
 */
class RefinedSoundexTest extends AbstractStringEncoderTest<RefinedSoundex> {

    @Override
    protected RefinedSoundex createStringEncoder() {
        return new RefinedSoundex();
    }

    @Test
    void testDifference_EdgeCases() throws EncoderException {
        RefinedSoundex encoder = getStringEncoder();
        
        // Null inputs should return 0 difference
        assertEquals(0, encoder.difference(null, null));
        
        // Empty strings should return 0 difference
        assertEquals(0, encoder.difference("", ""));
        
        // Whitespace-only strings should return 0 difference
        assertEquals(0, encoder.difference(" ", " "));
    }

    @Test
    void testDifference_SimilarNames() throws EncoderException {
        RefinedSoundex encoder = getStringEncoder();
        
        // Similar surnames should have high similarity scores
        assertEquals(6, encoder.difference("Smith", "Smythe"));
        assertEquals(8, encoder.difference("Smithers", "Smythers"));
        assertEquals(5, encoder.difference("Green", "Greene"));
        
        // Moderately similar names should have medium similarity scores
        assertEquals(3, encoder.difference("Ann", "Andrew"));
        assertEquals(5, encoder.difference("Anothers", "Brothers"));
        
        // Dissimilar names should have low similarity scores
        assertEquals(1, encoder.difference("Margaret", "Andrew"));
        assertEquals(1, encoder.difference("Janet", "Margaret"));
        assertEquals(1, encoder.difference("Blotchet-Halls", "Greene"));
    }

    @Test
    void testEncode_BasicWords() {
        RefinedSoundex encoder = getStringEncoder();
        
        // Test case sensitivity - should produce identical codes
        assertEquals("T6036084", encoder.encode("testing"));
        assertEquals("T6036084", encoder.encode("TESTING"));
        
        // Test common English words from "The quick brown fox jumped over the lazy dogs"
        assertEquals("T60", encoder.encode("The"));
        assertEquals("Q503", encoder.encode("quick"));
        assertEquals("B1908", encoder.encode("brown"));
        assertEquals("F205", encoder.encode("fox"));
        assertEquals("J408106", encoder.encode("jumped"));
        assertEquals("O0209", encoder.encode("over"));
        assertEquals("T60", encoder.encode("the"));
        assertEquals("L7050", encoder.encode("lazy"));
        assertEquals("D6043", encoder.encode("dogs"));
    }

    @Test
    void testEncode_UsingStaticInstance() {
        // Verify that the static US_ENGLISH instance works correctly (CODEC-56 regression test)
        String expectedCode = "D6043";
        String actualCode = RefinedSoundex.US_ENGLISH.encode("dogs");
        
        assertEquals(expectedCode, actualCode, 
            "Static US_ENGLISH instance should encode 'dogs' correctly");
    }

    @Test
    void testGetMappingCode_NonLetterCharacters() {
        RefinedSoundex encoder = getStringEncoder();
        
        // Non-letter characters should map to code 0
        char mappingCode = encoder.getMappingCode('#');
        assertEquals(0, mappingCode, "Non-letter characters should map to code 0");
    }

    @Test
    void testEncode_InvalidCharacters() {
        // Test encoding with all possible byte values (0-255)
        char[] allByteValues = new char[256];
        for (int i = 0; i < allByteValues.length; i++) {
            allByteValues[i] = (char) i;
        }
        
        String inputWithAllBytes = new String(allByteValues);
        String expectedEncoding = "A0136024043780159360205050136024043780159360205053";
        String actualEncoding = new RefinedSoundex().encode(inputWithAllBytes);
        
        assertEquals(expectedEncoding, actualEncoding, 
            "Encoding should handle all byte values gracefully");
    }

    @Test
    void testConstructor_DefaultMapping() {
        RefinedSoundex encoder = new RefinedSoundex();
        String expectedCode = "D6043";
        String actualCode = encoder.soundex("dogs");
        
        assertEquals(expectedCode, actualCode, 
            "Default constructor should use US English mapping");
    }

    @Test
    void testConstructor_CharArrayMapping() {
        char[] usEnglishMapping = RefinedSoundex.US_ENGLISH_MAPPING_STRING.toCharArray();
        RefinedSoundex encoder = new RefinedSoundex(usEnglishMapping);
        
        String expectedCode = "D6043";
        String actualCode = encoder.soundex("dogs");
        
        assertEquals(expectedCode, actualCode, 
            "Constructor with char array mapping should work correctly");
    }

    @Test
    void testConstructor_StringMapping() {
        String usEnglishMapping = RefinedSoundex.US_ENGLISH_MAPPING_STRING;
        RefinedSoundex encoder = new RefinedSoundex(usEnglishMapping);
        
        String expectedCode = "D6043";
        String actualCode = encoder.soundex("dogs");
        
        assertEquals(expectedCode, actualCode, 
            "Constructor with string mapping should work correctly");
    }
}