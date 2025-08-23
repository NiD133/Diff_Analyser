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

// (FYI: Formatted and sorted with Eclipse)

package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests {@link Soundex}.
 *
 * Keep this file in UTF-8 encoding for proper Javadoc processing.
 *
 * The tests are grouped by behavior and use small helpers/parameterized inputs
 * to make intent and expectations obvious.
 */
class SoundexTest extends AbstractStringEncoderTest<Soundex> {

    // ---------------------------------------------------------------------
    // Test harness
    // ---------------------------------------------------------------------

    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }

    /** Shorthand for default encoder. */
    private String enc(final String input) {
        return getStringEncoder().encode(input);
    }

    // ---------------------------------------------------------------------
    // Core encoding examples (single-word checks)
    // ---------------------------------------------------------------------

    @ParameterizedTest(name = "enc(\"{0}\") = {1}")
    @CsvSource({
        // A classic pangram-like sentence split into words
        "testing, T235",
        "The,     T000",
        "quick,   Q200",
        "brown,   B650",
        "fox,     F200",
        "jumped,  J513",
        "over,    O160",
        "the,     T000",
        "lazy,    L200",
        "dogs,    D200"
    })
    void encodesBasicExamples(final String input, final String expected) {
        assertEquals(expected, enc(input));
    }

    @Test
    @DisplayName("Non-letters are ignored (e.g., '>')")
    void ignoresNonLetters() {
        assertEquals("H452", enc("HOL>MES"));
    }

    // ---------------------------------------------------------------------
    // Known datasets/examples from external references
    // ---------------------------------------------------------------------

    /**
     * Examples from http://www.bradandkathy.com/genealogy/overviewofsoundex.html
     */
    @ParameterizedTest(name = "enc(\"{0}\") = {1}")
    @CsvSource({
        "Allricht,      A462",
        "Eberhard,      E166",
        "Engebrethson,  E521",
        "Heimbach,      H512",
        "Hanselmann,    H524",
        "Hildebrand,    H431",
        "Kavanagh,      K152",
        "Lind,          L530",
        "Lukaschowsky,  L222",
        "McDonnell,     M235",
        "McGee,         M200",
        "Opnian,        O155",
        "Oppenheimer,   O155",
        "Riedemanas,    R355",
        "Zita,          Z300",
        "Zitzmeinn,     Z325"
    })
    void encodesBatchFromBradAndKathy(final String input, final String expected) {
        assertEquals(expected, enc(input));
    }

    /**
     * Examples from http://www.archives.gov/research_room/genealogy/census/soundex.html
     */
    @ParameterizedTest(name = "enc(\"{0}\") = {1}")
    @CsvSource({
        "Washington, W252",
        "Lee,        L000",
        "Gutierrez,  G362",
        "Pfister,    P236",
        "Jackson,    J250",
        "Tymczak,    T522",
        // For VanDeusen: D-250 is also possible,
        // but the algorithm here yields V532
        "VanDeusen,  V532"
    })
    void encodesBatchFromUSArchives(final String input, final String expected) {
        assertEquals(expected, enc(input));
    }

    /**
     * Examples from: http://www.myatt.demon.co.uk/sxalg.htm
     */
    @ParameterizedTest(name = "enc(\"{0}\") = {1}")
    @CsvSource({
        "HOLMES,     H452",
        "ADOMOMI,    A355",
        "VONDERLEHR, V536",
        "BALL,       B400",
        "SHAW,       S000",
        "JACKSON,    J250",
        "SCANLON,    S545",
        "SAINTJOHN,  S532"
    })
    void encodesBatchFromMyatt(final String input, final String expected) {
        assertEquals(expected, enc(input));
    }

    /**
     * Tests example from https://en.wikipedia.org/wiki/Soundex#American_Soundex
     */
    @ParameterizedTest(name = "enc(\"{0}\") = {1}")
    @CsvSource({
        "Robert,   R163",
        "Rupert,   R163",
        "Ashcraft, A261",
        "Ashcroft, A261",
        "Tymczak,  T522",
        "Pfister,  P236"
    })
    void encodesWikipediaAmericanSoundexExamples(final String input, final String expected) {
        assertEquals(expected, enc(input));
    }

    // ---------------------------------------------------------------------
    // Variations and normalization
    // ---------------------------------------------------------------------

    @Test
    void encodesManyNamesToB650() throws EncoderException {
        // All of these are known to map to B650
        checkEncodingVariations("B650", new String[] {
            "BARHAM","BARONE","BARRON","BERNA","BIRNEY","BIRNIE","BOOROM","BOREN","BORN","BOURN","BOURNE","BOWRON",
            "BRAIN","BRAME","BRANN","BRAUN","BREEN","BRIEN","BRIM","BRIMM","BRINN","BRION","BROOM","BROOME","BROWN",
            "BROWNE","BRUEN","BRUHN","BRUIN","BRUMM","BRUN","BRUNO","BRYAN","BURIAN","BURN","BURNEY","BYRAM","BYRNE",
            "BYRON","BYRUM"
        });
    }

    @Test
    void ignoresApostrophes() throws EncoderException {
        checkEncodingVariations("O165", new String[] {
            "OBrien",
            "'OBrien",
            "O'Brien",
            "OB'rien",
            "OBr'ien",
            "OBri'en",
            "OBrie'n",
            "OBrien'"
        });
    }

    /**
     * Test data from http://www.myatt.demon.co.uk/sxalg.htm
     */
    @Test
    void ignoresHyphens() throws EncoderException {
        checkEncodingVariations("K525", new String[] {
            "KINGSMITH", "-KINGSMITH", "K-INGSMITH", "KI-NGSMITH", "KIN-GSMITH",
            "KING-SMITH", "KINGS-MITH", "KINGSM-ITH", "KINGSMI-TH", "KINGSMIT-H", "KINGSMITH-"
        });
    }

    @Test
    void trimsSurroundingWhitespace() {
        assertEquals("W252", enc(" \t\n\r Washington \t\n\r "));
    }

    // ---------------------------------------------------------------------
    // Difference() similarity metric
    // ---------------------------------------------------------------------

    @Test
    void differenceCountsMatchingCodeCharacters() throws EncoderException {
        // Edge cases
        assertEquals(0, getStringEncoder().difference(null, null));
        assertEquals(0, getStringEncoder().difference("", ""));
        assertEquals(0, getStringEncoder().difference(" ", " "));

        // Normal cases
        assertEquals(4, getStringEncoder().difference("Smith", "Smythe"));
        assertEquals(2, getStringEncoder().difference("Ann", "Andrew"));
        assertEquals(1, getStringEncoder().difference("Margaret", "Andrew"));
        assertEquals(0, getStringEncoder().difference("Janet", "Margaret"));

        // Examples from MS T-SQL docs
        assertEquals(4, getStringEncoder().difference("Green", "Greene"));
        assertEquals(0, getStringEncoder().difference("Blotchet-Halls", "Greene"));

        // More MS SQL examples
        assertEquals(4, getStringEncoder().difference("Smith", "Smythe"));
        assertEquals(4, getStringEncoder().difference("Smithers", "Smythers"));
        assertEquals(2, getStringEncoder().difference("Anothers", "Brothers"));
    }

    // ---------------------------------------------------------------------
    // HW rule behavior (treatment of H and W)
    // ---------------------------------------------------------------------

    /**
     * Consonants from the same code group separated by W or H are treated as one.
     * From US Archives examples.
     */
    @Test
    void hwRule_Ashcraft_Ashcroft() {
        assertEquals("A261", enc("Ashcraft"));
        assertEquals("A261", enc("Ashcroft"));
        assertEquals("Y330", enc("yehudit"));
        assertEquals("Y330", enc("yhwdyt"));
    }

    /**
     * Consonants from the same code group separated by W or H are treated as one.
     * Test data from http://www.myatt.demon.co.uk/sxalg.htm
     */
    @Test
    void hwRule_BoothDavis() {
        assertEquals("B312", enc("BOOTHDAVIS"));
        assertEquals("B312", enc("BOOTH-DAVIS"));
    }

    /**
     * Consonants from the same code group separated by W or H are treated as one.
     */
    @Test
    void hwRule_MultipleVariants() throws EncoderException {
        assertEquals("S460", enc("Sgler"));
        assertEquals("S460", enc("Swhgler"));
        checkEncodingVariations("S460", new String[] {
            "SAILOR","SALYER","SAYLOR","SCHALLER","SCHELLER","SCHILLER","SCHOOLER","SCHULER","SCHUYLER",
            "SEILER","SEYLER","SHOLAR","SHULER","SILAR","SILER","SILLER"
        });
    }

    // ---------------------------------------------------------------------
    // Alternative mappings (simplified, genealogy)
    // ---------------------------------------------------------------------

    @Test
    @DisplayName("Genealogy mapping: treat vowels and HW as silent")
    void genealogyMapping() {
        final Soundex s = Soundex.US_ENGLISH_GENEALOGY;
        assertEquals("H251", s.encode("Heggenburger"));
        assertEquals("B425", s.encode("Blackman"));
        assertEquals("S530", s.encode("Schmidt"));
        assertEquals("L150", s.encode("Lippmann"));

        // Local additions
        assertEquals("D200", s.encode("Dodds")); // 'o' silent here (not a separator)
        assertEquals("D200", s.encode("Dhdds")); // 'h' silent
        assertEquals("D200", s.encode("Dwdds")); // 'w' silent
    }

    @Test
    @DisplayName("Simplified mapping: treat vowels and HW as separators")
    void simplifiedMapping() {
        final Soundex s = Soundex.US_ENGLISH_SIMPLIFIED;
        assertEquals("W452", s.encode("WILLIAMS"));
        assertEquals("B625", s.encode("BARAGWANATH"));
        assertEquals("D540", s.encode("DONNELL"));
        assertEquals("L300", s.encode("LLOYD"));
        assertEquals("W422", s.encode("WOOLCOCK"));

        // Local additions
        assertEquals("D320", s.encode("Dodds"));
        assertEquals("D320", s.encode("Dwdds")); // 'w' is a separator
        assertEquals("D320", s.encode("Dhdds")); // 'h' is a separator
    }

    // ---------------------------------------------------------------------
    // SQL Server documentation examples
    // ---------------------------------------------------------------------

    @Test
    void msSqlServer_Examples1() {
        assertEquals("S530", enc("Smith"));
        assertEquals("S530", enc("Smythe"));
    }

    @Test
    void msSqlServer_Examples2() throws EncoderException {
        checkEncodingVariations("E625", new String[] {
            "Erickson", "Erickson", "Erikson", "Ericson", "Ericksen", "Ericsen"
        });
    }

    @ParameterizedTest(name = "enc(\"{0}\") = {1}")
    @CsvSource({
        "Ann,      A500",
        "Andrew,   A536",
        "Janet,    J530",
        "Margaret, M626",
        "Steven,   S315",
        "Michael,  M240",
        "Robert,   R163",
        "Laura,    L600",
        "Anne,     A500"
    })
    void msSqlServer_Examples3(final String input, final String expected) {
        assertEquals(expected, enc(input));
    }

    // ---------------------------------------------------------------------
    // Constructors and static instances
    // ---------------------------------------------------------------------

    @Test
    void newInstance_usesDefaultMapping() {
        assertEquals("W452", new Soundex().soundex("Williams"));
    }

    @Test
    void newInstance_charArrayMapping() {
        assertEquals("W452", new Soundex(Soundex.US_ENGLISH_MAPPING_STRING.toCharArray()).soundex("Williams"));
    }

    @Test
    void newInstance_stringMapping() {
        assertEquals("W452", new Soundex(Soundex.US_ENGLISH_MAPPING_STRING).soundex("Williams"));
    }

    @Test
    void usEnglishStaticInstance() {
        assertEquals("W452", Soundex.US_ENGLISH.soundex("Williams"));
    }

    // ---------------------------------------------------------------------
    // SoundexUtils
    // ---------------------------------------------------------------------

    @Test
    void soundexUtilsConstructable() {
        new SoundexUtils();
    }

    @Test
    void soundexUtilsNullAndEmptyInput() {
        assertNull(SoundexUtils.clean(null));
        assertEquals("", SoundexUtils.clean(""));
        assertEquals(0, SoundexUtils.differenceEncoded(null, ""));
        assertEquals(0, SoundexUtils.differenceEncoded("", null));
    }

    // ---------------------------------------------------------------------
    // Non-ASCII letters behavior (default US mapping)
    // ---------------------------------------------------------------------

    /**
     * Fancy characters are not mapped by the default US mapping.
     * https://issues.apache.org/jira/browse/CODEC-30
     */
    @Test
    void usMapping_EWithAcute() {
        assertEquals("E000", enc("e"));
        if (Character.isLetter('\u00e9')) { // e-acute
            assertThrows(IllegalArgumentException.class, () -> enc("\u00e9"));
        } else {
            assertEquals("", enc("\u00e9"));
        }
    }

    /**
     * Fancy characters are not mapped by the default US mapping.
     * https://issues.apache.org/jira/browse/CODEC-30
     */
    @Test
    void usMapping_OWithDiaeresis() {
        assertEquals("O000", enc("o"));
        if (Character.isLetter('\u00f6')) { // o-umlaut
            assertThrows(IllegalArgumentException.class, () -> enc("\u00f6"));
        } else {
            assertEquals("", enc("\u00f6"));
        }
    }
}