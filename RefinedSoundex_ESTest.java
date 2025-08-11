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

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link RefinedSoundex} class.
 */
public class RefinedSoundexTest {

    private final RefinedSoundex soundex = new RefinedSoundex();

    // --- Constructor Tests ---

    @Test
    public void constructorWithEmptyCharArrayMappingSucceeds() {
        // This test verifies that the constructor does not throw an exception for an empty mapping.
        new RefinedSoundex(new char[0]);
    }

    @Test(expected = NullPointerException.class)
    public void constructorWithNullStringMappingThrowsException() {
        new RefinedSoundex((String) null);
    }

    @Test(expected = NullPointerException.class)
    public void constructorWithNullCharArrayMappingThrowsException() {
        new RefinedSoundex((char[]) null);
    }

    // --- soundex() and encode(String) Tests ---

    @Test
    public void soundexOfNullStringReturnsNull() {
        assertNull(soundex.soundex(null));
    }

    @Test
    public void encodeNullStringReturnsNull() {
        assertNull(soundex.encode((String) null));
    }

    @Test
    public void soundexOfEmptyStringIsEmpty() {
        assertEquals("", soundex.soundex(""));
    }

    @Test
    public void soundexOfStringWithOnlyNonLettersIsEmpty() {
        assertEquals("", soundex.encode("!@#$%-"));
    }

    @Test
    public void soundexStripsNonLettersBeforeEncoding() {
        // "Robert" and "R-o-b-e-r-t" should have the same Refined Soundex code.
        String expectedCode = "R196";
        assertEquals(expectedCode, soundex.encode("R-o-b-e-r-t"));
    }

    @Test
    public void soundexIsCaseInsensitive() {
        assertEquals("T3926", soundex.encode("Ashcraft"));
        assertEquals("T3926", soundex.encode("ashcraft"));
        assertEquals("T3926", soundex.encode("ASHCRAFT"));
    }

    @Test
    public void soundexHandlesBasicNames() {
        assertEquals("R196", soundex.encode("Robert"));
        assertEquals("R196", soundex.encode("Rupert")); // Same code as Robert
        assertEquals("A3926", soundex.encode("Ashcraft"));
    }

    @Test
    public void soundexWorksWithCustomMapping() {
        // Arrange: Using the standard Soundex mapping to test custom mapping functionality.
        RefinedSoundex customSoundex = new RefinedSoundex("01230120022455012623010202");

        // Act & Assert
        assertEquals("A2613", customSoundex.encode("Ashcraft"));
    }

    // --- encode(Object) Tests ---

    @Test
    public void encodeObjectWithStringEncodesCorrectly() throws EncoderException {
        // Arrange
        Object input = "Robert";
        String expectedCode = "R196";

        // Act
        Object encodedObject = soundex.encode(input);

        // Assert
        assertTrue(encodedObject instanceof String);
        assertEquals(expectedCode, encodedObject);
    }

    @Test(expected = EncoderException.class)
    public void encodeInvalidObjectTypeThrowsException() throws EncoderException {
        // An object that is not a String should cause an exception.
        soundex.encode(12345);
    }

    @Test(expected = EncoderException.class)
    public void encodeNullObjectThrowsException() throws EncoderException {
        // A null object should cause an exception, as it's not an instance of String.
        soundex.encode(null);
    }

    // --- difference() Tests ---

    @Test
    public void differenceOfIdenticalStringsIsLengthOfEncoding() throws EncoderException {
        // The difference between two identical strings should be the length of their common encoding.
        String input = "Robert";
        String encoded = soundex.encode(input);

        int difference = soundex.difference(input, input);

        assertEquals(encoded.length(), difference);
        assertEquals(4, difference); // "R196"
    }

    @Test
    public void differenceOfSimilarStrings() throws EncoderException {
        // The difference is the number of matching characters in the Refined Soundex codes.
        // Tymczak -> T8353
        // Tuchinsky -> T3833
        // Match at positions 0 ('T') and 4 ('3'). Difference should be 2.
        int difference = soundex.difference("Tymczak", "Tuchinsky");
        assertEquals(2, difference);
    }

    @Test
    public void differenceOfNullStringsIsZero() throws EncoderException {
        assertEquals(0, soundex.difference(null, null));
        assertEquals(0, soundex.difference("test", null));
        assertEquals(0, soundex.difference(null, "test"));
    }

    // --- getMappingCode() Tests ---

    @Test
    public void getMappingCodeForUsEnglishVowelsAndConsonants() {
        // Vowels and similar letters map to '0'
        assertEquals('0', RefinedSoundex.US_ENGLISH.getMappingCode('A'));
        assertEquals('0', RefinedSoundex.US_ENGLISH.getMappingCode('E'));
        assertEquals('0', RefinedSoundex.US_ENGLISH.getMappingCode('Y'));

        // Consonants map to their group number
        assertEquals('1', RefinedSoundex.US_ENGLISH.getMappingCode('B'));
        assertEquals('1', RefinedSoundex.US_ENGLISH.getMappingCode('P'));
        assertEquals('3', RefinedSoundex.US_ENGLISH.getMappingCode('S'));
        assertEquals('6', RefinedSoundex.US_ENGLISH.getMappingCode('T'));
    }

    @Test
    public void getMappingCodeForNonLetterIsZero() {
        assertEquals((char) 0, soundex.getMappingCode('+'));
        assertEquals((char) 0, soundex.getMappingCode('5'));
    }

    @Test
    public void getMappingCodeWithCustomMapping() {
        // Arrange: A simple custom mapping for A, B, C.
        RefinedSoundex customSoundex = new RefinedSoundex("XYZ");

        // Act & Assert
        assertEquals('X', customSoundex.getMappingCode('A'));
        assertEquals('Y', customSoundex.getMappingCode('B'));
        assertEquals('Z', customSoundex.getMappingCode('C'));
        // 'D' is outside the mapping and should return 0.
        assertEquals((char) 0, customSoundex.getMappingCode('D'));
    }
}