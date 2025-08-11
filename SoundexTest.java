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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Soundex} phonetic encoding algorithm.
 * 
 * Soundex is a phonetic algorithm that encodes names by sound, as pronounced in English.
 * The goal is for homophones to be encoded to the same representation so that they can be matched
 * despite minor differences in spelling.
 *
 * <p>Keep this file in UTF-8 encoding for proper Javadoc processing.</p>
 */
class SoundexTest extends AbstractStringEncoderTest<Soundex> {

    // Common Soundex codes used in multiple tests
    private static final String SOUNDEX_B650 = "B650";
    private static final String SOUNDEX_S460 = "S460";
    private static final String SOUNDEX_E625 = "E625";
    private static final String SOUNDEX_K525 = "K525";
    private static final String SOUNDEX_O165 = "O165";

    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }

    // ========== Basic Encoding Tests ==========

    @Test
    void shouldEncodeBasicEnglishWords() {
        // Test common English words to verify basic Soundex functionality
        assertEquals("T235", getStringEncoder().encode("testing"));
        assertEquals("T000", getStringEncoder().encode("The"));
        assertEquals("Q200", getStringEncoder().encode("quick"));
        assertEquals("B650", getStringEncoder().encode("brown"));
        assertEquals("F200", getStringEncoder().encode("fox"));
        assertEquals("J513", getStringEncoder().encode("jumped"));
        assertEquals("O160", getStringEncoder().encode("over"));
        assertEquals("T000", getStringEncoder().encode("the"));
        assertEquals("L200", getStringEncoder().encode("lazy"));
        assertEquals("D200", getStringEncoder().encode("dogs"));
    }

    @Test
    void shouldHandleSpecialCharactersInInput() {
        // Soundex should ignore non-alphabetic characters
        assertEquals("H452", getStringEncoder().encode("HOL>MES"));
    }

    @Test
    void shouldIgnoreLeadingAndTrailingWhitespace() {
        // Whitespace should be trimmed before encoding
        assertEquals("W252", getStringEncoder().encode(" \t\n\r Washington \t\n\r "));
    }

    // ========== Soundex Difference Tests ==========

    @Test
    void shouldCalculateDifferenceBetweenSoundexCodes() throws EncoderException {
        Soundex encoder = getStringEncoder();
        
        // Edge cases with null and empty strings
        assertEquals(0, encoder.difference(null, null));
        assertEquals(0, encoder.difference("", ""));
        assertEquals(0, encoder.difference(" ", " "));
        
        // Similar sounding names should have high difference scores (max 4)
        assertEquals(4, encoder.difference("Smith", "Smythe"));
        assertEquals(4, encoder.difference("Green", "Greene"));
        assertEquals(4, encoder.difference("Smithers", "Smythers"));
        
        // Somewhat similar names should have medium scores
        assertEquals(2, encoder.difference("Ann", "Andrew"));
        assertEquals(2, encoder.difference("Anothers", "Brothers"));
        assertEquals(1, encoder.difference("Margaret", "Andrew"));
        
        // Dissimilar names should have low scores
        assertEquals(0, encoder.difference("Janet", "Margaret"));
        assertEquals(0, encoder.difference("Blotchet-Halls", "Greene"));
    }

    // ========== Special Character Handling Tests ==========

    @Test
    void shouldIgnoreApostrophesInAllPositions() throws EncoderException {
        // Apostrophes should be ignored regardless of position in the name
        String[] obrienVariations = {
            "OBrien", "'OBrien", "O'Brien", "OB'rien", 
            "OBr'ien", "OBri'en", "OBrie'n", "OBrien'"
        };
        checkEncodingVariations(SOUNDEX_O165, obrienVariations);
    }

    @Test
    void shouldIgnoreHyphensInAllPositions() throws EncoderException {
        // Hyphens should be ignored regardless of position in the name
        String[] kingsmithVariations = {
            "KINGSMITH", "-KINGSMITH", "K-INGSMITH", "KI-NGSMITH",
            "KIN-GSMITH", "KING-SMITH", "KINGS-MITH", "KINGSM-ITH",
            "KINGSMI-TH", "KINGSMIT-H", "KINGSMITH-"
        };
        checkEncodingVariations(SOUNDEX_K525, kingsmithVariations);
    }

    // ========== H and W Rule Tests ==========

    @Test
    void shouldTreatConsonantsFromSameCodeGroupSeparatedByHOrWAsOne() {
        // The H/W rule: consonants from the same code group separated by H or W are treated as one
        
        // Example: Ashcraft = A-261 (A, 2 for S, C ignored, 6 for R, 1 for F)
        // The S and C are both from code group 2, but H separates them, so C is ignored
        assertEquals("A261", getStringEncoder().encode("Ashcraft"));
        assertEquals("A261", getStringEncoder().encode("Ashcroft"));
        
        // Hebrew names demonstrating the rule
        assertEquals("Y330", getStringEncoder().encode("yehudit"));
        assertEquals("Y330", getStringEncoder().encode("yhwdyt"));
    }

    @Test
    void shouldApplyHAndWRuleToCompoundNames() {
        // Compound names with H/W separators
        assertEquals("B312", getStringEncoder().encode("BOOTHDAVIS"));
        assertEquals("B312", getStringEncoder().encode("BOOTH-DAVIS"));
    }

    @Test
    void shouldApplyHAndWRuleToVariousNameSpellings() throws EncoderException {
        // Multiple spellings that should all encode to the same Soundex due to H/W rule
        assertEquals("S460", getStringEncoder().encode("Sgler"));
        assertEquals("S460", getStringEncoder().encode("Swhgler"));
        
        String[] s460Names = {
            "SAILOR", "SALYER", "SAYLOR", "SCHALLER", "SCHELLER", "SCHILLER",
            "SCHOOLER", "SCHULER", "SCHUYLER", "SEILER", "SEYLER", "SHOLAR",
            "SHULER", "SILAR", "SILER", "SILLER"
        };
        checkEncodingVariations(SOUNDEX_S460, s460Names);
    }

    // ========== Soundex Variant Tests ==========

    @Test
    void shouldEncodeUsingGenealogySoundexVariant() {
        // Genealogy Soundex treats vowels, H, and W as silent (not separators)
        final Soundex genealogyEncoder = Soundex.US_ENGLISH_GENEALOGY;
        
        assertEquals("H251", genealogyEncoder.encode("Heggenburger"));
        assertEquals("B425", genealogyEncoder.encode("Blackman"));
        assertEquals("S530", genealogyEncoder.encode("Schmidt"));
        assertEquals("L150", genealogyEncoder.encode("Lippmann"));
        
        // In genealogy variant, vowels/H/W are completely silent
        assertEquals("D200", genealogyEncoder.encode("Dodds")); // 'o' is silent
        assertEquals("D200", genealogyEncoder.encode("Dhdds")); // 'h' is silent
        assertEquals("D200", genealogyEncoder.encode("Dwdds")); // 'w' is silent
    }

    @Test
    void shouldEncodeUsingSimplifiedSoundexVariant() {
        // Simplified Soundex treats H and W as separators (like vowels)
        final Soundex simplifiedEncoder = Soundex.US_ENGLISH_SIMPLIFIED;
        
        assertEquals("W452", simplifiedEncoder.encode("WILLIAMS"));
        assertEquals("B625", simplifiedEncoder.encode("BARAGWANATH"));
        assertEquals("D540", simplifiedEncoder.encode("DONNELL"));
        assertEquals("L300", simplifiedEncoder.encode("LLOYD"));
        assertEquals("W422", simplifiedEncoder.encode("WOOLCOCK"));
        
        // In simplified variant, H/W act as separators
        assertEquals("D320", simplifiedEncoder.encode("Dodds"));
        assertEquals("D320", simplifiedEncoder.encode("Dwdds")); // w separates duplicate codes
        assertEquals("D320", simplifiedEncoder.encode("Dhdds")); // h separates duplicate codes
    }

    // ========== Constructor and Instance Tests ==========

    @Test
    void shouldCreateNewInstancesWithDifferentConstructors() {
        // Test various constructor options
        assertEquals("W452", new Soundex().soundex("Williams"));
        assertEquals("W452", new Soundex(Soundex.US_ENGLISH_MAPPING_STRING.toCharArray()).soundex("Williams"));
        assertEquals("W452", new Soundex(Soundex.US_ENGLISH_MAPPING_STRING).soundex("Williams"));
        assertEquals("W452", Soundex.US_ENGLISH.soundex("Williams"));
    }

    // ========== Unicode and Special Character Tests ==========

    @Test
    void shouldHandleAccentedCharacters() {
        // Standard US mapping doesn't handle accented characters
        assertEquals("E000", getStringEncoder().encode("e"));
        
        if (Character.isLetter('\u00e9')) { // e-acute
            assertThrows(IllegalArgumentException.class, 
                () -> getStringEncoder().encode("\u00e9"),
                "Accented characters should throw IllegalArgumentException");
        } else {
            assertEquals("", getStringEncoder().encode("\u00e9"));
        }
    }

    @Test
    void shouldHandleUmlautCharacters() {
        // Standard US mapping doesn't handle umlaut characters
        assertEquals("O000", getStringEncoder().encode("o"));
        
        if (Character.isLetter('\u00f6')) { // o-umlaut
            assertThrows(IllegalArgumentException.class, 
                () -> getStringEncoder().encode("\u00f6"),
                "Umlaut characters should throw IllegalArgumentException");
        } else {
            assertEquals("", getStringEncoder().encode("\u00f6"));
        }
    }

    // ========== Reference Implementation Tests ==========

    @Test
    void shouldMatchWikipediaAmericanSoundexExamples() {
        // Examples from Wikipedia's American Soundex article (as of 2015-03-22)
        assertEquals("R163", getStringEncoder().encode("Robert"));
        assertEquals("R163", getStringEncoder().encode("Rupert"));
        assertEquals("A261", getStringEncoder().encode("Ashcraft"));
        assertEquals("A261", getStringEncoder().encode("Ashcroft"));
        assertEquals("T522", getStringEncoder().encode("Tymczak"));
        assertEquals("P236", getStringEncoder().encode("Pfister"));
    }

    @Test
    void shouldMatchMicrosoftSqlServerSoundexExamples() {
        // Examples from Microsoft SQL Server documentation
        assertEquals("S530", getStringEncoder().encode("Smith"));
        assertEquals("S530", getStringEncoder().encode("Smythe"));
        
        // Additional MS SQL Server examples
        assertEquals("A500", getStringEncoder().encode("Ann"));
        assertEquals("A536", getStringEncoder().encode("Andrew"));
        assertEquals("J530", getStringEncoder().encode("Janet"));
        assertEquals("M626", getStringEncoder().encode("Margaret"));
        assertEquals("S315", getStringEncoder().encode("Steven"));
        assertEquals("M240", getStringEncoder().encode("Michael"));
        assertEquals("R163", getStringEncoder().encode("Robert"));
        assertEquals("L600", getStringEncoder().encode("Laura"));
        assertEquals("A500", getStringEncoder().encode("Anne"));
    }

    // ========== Test Data from Various Sources ==========

    @Test
    void shouldEncodeNamesFromBradAndKathyGenealogySite() {
        // Examples from http://www.bradandkathy.com/genealogy/overviewofsoundex.html
        assertEquals("A462", getStringEncoder().encode("Allricht"));
        assertEquals("E166", getStringEncoder().encode("Eberhard"));
        assertEquals("E521", getStringEncoder().encode("Engebrethson"));
        assertEquals("H512", getStringEncoder().encode("Heimbach"));
        assertEquals("H524", getStringEncoder().encode("Hanselmann"));
        assertEquals("H431", getStringEncoder().encode("Hildebrand"));
        assertEquals("K152", getStringEncoder().encode("Kavanagh"));
        assertEquals("L530", getStringEncoder().encode("Lind"));
        assertEquals("L222", getStringEncoder().encode("Lukaschowsky"));
        assertEquals("M235", getStringEncoder().encode("McDonnell"));
        assertEquals("M200", getStringEncoder().encode("McGee"));
        assertEquals("O155", getStringEncoder().encode("Opnian"));
        assertEquals("O155", getStringEncoder().encode("Oppenheimer"));
        assertEquals("R355", getStringEncoder().encode("Riedemanas"));
        assertEquals("Z300", getStringEncoder().encode("Zita"));
        assertEquals("Z325", getStringEncoder().encode("Zitzmeinn"));
    }

    @Test
    void shouldEncodeNamesFromNationalArchives() {
        // Examples from http://www.archives.gov/research_room/genealogy/census/soundex.html
        assertEquals("W252", getStringEncoder().encode("Washington"));
        assertEquals("L000", getStringEncoder().encode("Lee"));
        assertEquals("G362", getStringEncoder().encode("Gutierrez"));
        assertEquals("P236", getStringEncoder().encode("Pfister"));
        assertEquals("J250", getStringEncoder().encode("Jackson"));
        assertEquals("T522", getStringEncoder().encode("Tymczak"));
        assertEquals("V532", getStringEncoder().encode("VanDeusen"));
    }

    @Test
    void shouldEncodeNamesFromMyattDemonCoUk() {
        // Examples from: http://www.myatt.demon.co.uk/sxalg.htm
        assertEquals("H452", getStringEncoder().encode("HOLMES"));
        assertEquals("A355", getStringEncoder().encode("ADOMOMI"));
        assertEquals("V536", getStringEncoder().encode("VONDERLEHR"));
        assertEquals("B400", getStringEncoder().encode("BALL"));
        assertEquals("S000", getStringEncoder().encode("SHAW"));
        assertEquals("J250", getStringEncoder().encode("JACKSON"));
        assertEquals("S545", getStringEncoder().encode("SCANLON"));
        assertEquals("S532", getStringEncoder().encode("SAINTJOHN"));
    }

    @Test
    void shouldHandleNamesWithSameSoundexCode() throws EncoderException {
        // Names that should all encode to B650
        String[] b650Names = createB650TestNames();
        checkEncodingVariations(SOUNDEX_B650, b650Names);
    }

    @Test
    void shouldHandleEricksonVariations() throws EncoderException {
        // Various spellings of "Erickson" should all encode the same
        String[] ericksonVariations = {
            "Erickson", "Erickson", "Erikson", "Ericson", "Ericksen", "Ericsen"
        };
        checkEncodingVariations(SOUNDEX_E625, ericksonVariations);
    }

    // ========== SoundexUtils Tests ==========

    @Test
    void shouldAllowSoundexUtilsConstruction() {
        // Verify SoundexUtils can be constructed (utility class test)
        new SoundexUtils();
    }

    @Test
    void shouldHandleNullInputsInSoundexUtils() {
        // SoundexUtils should handle null inputs gracefully
        assertNull(SoundexUtils.clean(null));
        assertEquals("", SoundexUtils.clean(""));
        assertEquals(0, SoundexUtils.differenceEncoded(null, ""));
        assertEquals(0, SoundexUtils.differenceEncoded("", null));
    }

    // ========== Helper Methods ==========

    /**
     * Verifies that all given input strings encode to the expected Soundex code.
     */
    private void checkEncodingVariations(String expectedSoundex, String[] inputs) throws EncoderException {
        for (String input : inputs) {
            assertEquals(expectedSoundex, getStringEncoder().encode(input), 
                String.format("Expected '%s' to encode to '%s'", input, expectedSoundex));
        }
    }

    /**
     * Creates the large array of names that should all encode to B650.
     * Extracted to improve readability of the main test method.
     */
    private String[] createB650TestNames() {
        return new String[]{
            "BARHAM", "BARONE", "BARRON", "BERNA", "BIRNEY", "BIRNIE", "BOOROM", "BOREN",
            "BORN", "BOURN", "BOURNE", "BOWRON", "BRAIN", "BRAME", "BRANN", "BRAUN",
            "BREEN", "BRIEN", "BRIM", "BRIMM", "BRINN", "BRION", "BROOM", "BROOME",
            "BROWN", "BROWNE", "BRUEN", "BRUHN", "BRUIN", "BRUMM", "BRUN", "BRUNO",
            "BRYAN", "BURIAN", "BURN", "BURNEY", "BYRAM", "BYRNE", "BYRON", "BYRUM"
        };
    }
}