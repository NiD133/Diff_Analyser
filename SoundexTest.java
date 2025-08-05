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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link Soundex}.
 *
 * <p>Keep this file in UTF-8 encoding for proper Javadoc processing.</p>
 */
@DisplayName("Soundex Algorithm Tests")
class SoundexTest extends AbstractStringEncoderTest<Soundex> {

    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }

    @Nested
    @DisplayName("Standard US English Soundex Encoding")
    class StandardUsEnglishEncodingTests {

        @Test
        @DisplayName("should encode basic words")
        void shouldEncodeBasicWords() {
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
        @DisplayName("should encode a large batch of names to B650")
        void shouldEncodeNamesToB650() throws EncoderException {
            checkEncodingVariations("B650", new String[]{
                "BARHAM", "BARONE", "BARRON", "BERNA", "BIRNEY", "BIRNIE", "BOOROM",
                "BOREN", "BORN", "BOURN", "BOURNE", "BOWRON", "BRAIN", "BRAME",
                "BRANN", "BRAUN", "BREEN", "BRIEN", "BRIM", "BRIMM", "BRINN",
                "BRION", "BROOM", "BROOME", "BROWN", "BROWNE", "BRUEN", "BRUHN",
                "BRUIN", "BRUMM", "BRUN", "BRUNO", "BRYAN", "BURIAN", "BURN",
                "BURNEY", "BYRAM", "BYRNE", "BYRON", "BYRUM"});
        }

        /**
         * Tests the H-W rule where consonants from the same code group separated by H or W are treated as one.
         * From http://www.archives.gov/research_room/genealogy/census/soundex.html
         */
        @Test
        @DisplayName("should treat consonants separated by H or W as one (e.g., Ashcraft)")
        void shouldTreatConsonantsSeparatedByHOrWAsOne_Ashcraft() {
            // Ashcraft is coded A-261 (A, 2 for S, C ignored, 6 for R, 1 for F). It is not coded A-226.
            assertEquals("A261", getStringEncoder().encode("Ashcraft"));
            assertEquals("A261", getStringEncoder().encode("Ashcroft"));
            assertEquals("Y330", getStringEncoder().encode("yehudit"));
            assertEquals("Y330", getStringEncoder().encode("yhwdyt"));
        }

        /**
         * Tests the H-W rule.
         * Test data from http://www.myatt.demon.co.uk/sxalg.htm
         */
        @Test
        @DisplayName("should treat consonants separated by H or W as one (e.g., BOOTHDAVIS)")
        void shouldTreatConsonantsSeparatedByHOrWAsOne_BoothDavis() {
            assertEquals("B312", getStringEncoder().encode("BOOTHDAVIS"));
            assertEquals("B312", getStringEncoder().encode("BOOTH-DAVIS"));
        }

        @Test
        @DisplayName("should treat consonants separated by H or W as one (e.g., Sgler/Swhgler)")
        void shouldTreatConsonantsSeparatedByHOrWAsOne_Sgler() throws EncoderException {
            assertEquals("S460", getStringEncoder().encode("Sgler"));
            assertEquals("S460", getStringEncoder().encode("Swhgler"));
            // Also S460:
            checkEncodingVariations("S460", new String[]{
                "SAILOR", "SALYER", "SAYLOR", "SCHALLER", "SCHELLER", "SCHILLER",
                "SCHOOLER", "SCHULER", "SCHUYLER", "SEILER", "SEYLER", "SHOLAR",
                "SHULER", "SILAR", "SILER", "SILLER"});
        }

        /**
         * Tests example from https://en.wikipedia.org/wiki/Soundex#American_Soundex as of 2015-03-22.
         */
        @Test
        @DisplayName("should correctly encode examples from Wikipedia")
        void shouldEncodeWikipediaExamples() {
            assertEquals("R163", getStringEncoder().encode("Robert"));
            assertEquals("R163", getStringEncoder().encode("Rupert"));
            assertEquals("A261", getStringEncoder().encode("Ashcraft"));
            assertEquals("A261", getStringEncoder().encode("Ashcroft"));
            assertEquals("T522", getStringEncoder().encode("Tymczak"));
            assertEquals("P236", getStringEncoder().encode("Pfister"));
        }
    }

    @Nested
    @DisplayName("Encoding Based on External Source Examples")
    class ExternalSourceEncodingTests {

        /**
         * Examples from http://www.bradandkathy.com/genealogy/overviewofsoundex.html
         */
        @Test
        @DisplayName("should encode according to BradAndKathy.com genealogy examples")
        void shouldEncodeAccordingToBradAndKathyGenealogyExamples() {
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

        /**
         * Examples from http://www.archives.gov/research_room/genealogy/census/soundex.html
         */
        @Test
        @DisplayName("should encode according to National Archives examples")
        void shouldEncodeAccordingToNationalArchivesExamples() {
            assertEquals("W252", getStringEncoder().encode("Washington"));
            assertEquals("L000", getStringEncoder().encode("Lee"));
            assertEquals("G362", getStringEncoder().encode("Gutierrez"));
            assertEquals("P236", getStringEncoder().encode("Pfister"));
            assertEquals("J250", getStringEncoder().encode("Jackson"));
            assertEquals("T522", getStringEncoder().encode("Tymczak"));
            assertEquals("V532", getStringEncoder().encode("VanDeusen"));
        }

        /**
         * Examples from: http://www.myatt.demon.co.uk/sxalg.htm
         */
        @Test
        @DisplayName("should encode according to myatt.demon.co.uk examples")
        void shouldEncodeAccordingToMyattDemonExamples() {
            assertEquals("H452", getStringEncoder().encode("HOLMES"));
            assertEquals("A355", getStringEncoder().encode("ADOMOMI"));
            assertEquals("V536", getStringEncoder().encode("VONDERLEHR"));
            assertEquals("B400", getStringEncoder().encode("BALL"));
            assertEquals("S000", getStringEncoder().encode("SHAW"));
            assertEquals("J250", getStringEncoder().encode("JACKSON"));
            assertEquals("S545", getStringEncoder().encode("SCANLON"));
            assertEquals("S532", getStringEncoder().encode("SAINTJOHN"));
        }

        /**
         * Examples for MS SQLServer from
         * https://msdn.microsoft.com/library/default.asp?url=/library/en-us/tsqlref/ts_setu-sus_3o6w.asp
         */
        @Test
        @DisplayName("should produce same encoding for similar names (MS SQL Server examples)")
        void shouldProduceSameEncodingForSimilarNames_MsSqlExamples() {
            assertEquals("S530", getStringEncoder().encode("Smith"));
            assertEquals("S530", getStringEncoder().encode("Smythe"));
        }

        /**
         * Examples for MS SQLServer from
         * https://support.microsoft.com/default.aspx?scid=https://support.microsoft.com:80/support/kb/articles/Q100/3/65.asp&NoWebContent=1
         */
        @Test
        @DisplayName("should produce same encoding for name variations (MS SQL Server examples)")
        void shouldProduceSameEncodingForNameVariations_MsSqlExamples() throws EncoderException {
            checkEncodingVariations("E625", new String[]{"Erickson", "Erickson", "Erikson", "Ericson", "Ericksen", "Ericsen"});
        }

        /**
         * Examples for MS SQLServer from https://databases.about.com/library/weekly/aa042901a.htm
         */
        @Test
        @DisplayName("should encode various names (MS SQL Server examples)")
        void shouldEncodeVariousNames_MsSqlExamples() {
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
    }

    @Nested
    @DisplayName("Handling of Special Characters and Formatting")
    class SpecialCharacterHandlingTests {

        @Test
        @DisplayName("should ignore non-alphabetic characters")
        void shouldIgnoreNonAlphabeticCharacters() {
            assertEquals("H452", getStringEncoder().encode("HOL>MES"));
        }

        @Test
        @DisplayName("should ignore apostrophes")
        void shouldIgnoreApostrophes() throws EncoderException {
            checkEncodingVariations("O165", new String[]{
                "OBrien", "'OBrien", "O'Brien", "OB'rien", "OBr'ien", "OBri'en", "OBrie'n", "OBrien'"});
        }

        /**
         * Test data from http://www.myatt.demon.co.uk/sxalg.htm
         */
        @Test
        @DisplayName("should ignore hyphens")
        void shouldIgnoreHyphens() throws EncoderException {
            checkEncodingVariations("K525", new String[]{
                "KINGSMITH", "-KINGSMITH", "K-INGSMITH", "KI-NGSMITH", "KIN-GSMITH",
                "KING-SMITH", "KINGS-MITH", "KINGSM-ITH", "KINGSMI-TH", "KINGSMIT-H",
                "KINGSMITH-"});
        }

        @Test
        @DisplayName("should trim leading and trailing whitespace")
        void shouldTrimWhitespace() {
            assertEquals("W252", getStringEncoder().encode(" \t\n\r Washington \t\n\r "));
        }

        /**
         * Tests that characters not in the US English mapping are handled correctly.
         * The behavior depends on whether the JVM considers the character a "letter".
         * If it is a letter, it's passed to the mapping function which throws an exception.
         * If not, it's cleaned from the input string.
         * See https://issues.apache.org/jira/browse/CODEC-30
         */
        @Test
        @DisplayName("should handle unmapped letter 'é' (e-acute)")
        void shouldHandleUnmappedLetterEWithAcute() {
            assertEquals("E000", getStringEncoder().encode("e"));
            // This test is environment-dependent. On some JVMs, 'é' is a letter, on others it is not.
            if (Character.isLetter('\u00e9')) { // e-acute
                // If it's a letter, it should fail mapping.
                assertThrows(IllegalArgumentException.class, () -> getStringEncoder().encode("\u00e9"));
            } else {
                // If not a letter, it should be cleaned, resulting in an empty encoding.
                assertEquals("", getStringEncoder().encode("\u00e9"));
            }
        }

        /**
         * See {@link #shouldHandleUnmappedLetterEWithAcute()}
         */
        @Test
        @DisplayName("should handle unmapped letter 'ö' (o-diaeresis)")
        void shouldHandleUnmappedLetterOWithDiaeresis() {
            assertEquals("O000", getStringEncoder().encode("o"));
            // This test is environment-dependent.
            if (Character.isLetter('\u00f6')) { // o-umlaut
                assertThrows(IllegalArgumentException.class, () -> getStringEncoder().encode("\u00f6"));
            } else {
                assertEquals("", getStringEncoder().encode("\u00f6"));
            }
        }
    }

    @Nested
    @DisplayName("difference() Method")
    class DifferenceMethodTests {

        @Test
        @DisplayName("should return 0 for null or empty inputs")
        void shouldReturnZeroForEdgeCaseInputs() throws EncoderException {
            assertEquals(0, getStringEncoder().difference(null, null), "Difference between two nulls");
            assertEquals(0, getStringEncoder().difference("", ""), "Difference between two empty strings");
            assertEquals(0, getStringEncoder().difference(" ", " "), "Difference between two single spaces");
        }

        @Test
        @DisplayName("should return 4 for phonetically identical or very similar names")
        void shouldReturnFourForSimilarOrIdenticalNames() throws EncoderException {
            assertEquals(4, getStringEncoder().difference("Smith", "Smythe"));
            assertEquals(4, getStringEncoder().difference("Smithers", "Smythers"));
            assertEquals(4, getStringEncoder().difference("Green", "Greene"));
        }

        @Test
        @DisplayName("should return a score based on phonetic similarity")
        void shouldReturnScoreBasedOnSimilarity() throws EncoderException {
            assertEquals(2, getStringEncoder().difference("Ann", "Andrew"));
            assertEquals(1, getStringEncoder().difference("Margaret", "Andrew"));
            assertEquals(2, getStringEncoder().difference("Anothers", "Brothers"));
        }

        @Test
        @DisplayName("should return 0 for phonetically dissimilar names")
        void shouldReturnZeroForDissimilarNames() throws EncoderException {
            assertEquals(0, getStringEncoder().difference("Janet", "Margaret"));
            assertEquals(0, getStringEncoder().difference("Blotchet-Halls", "Greene"));
        }
    }

    @Nested
    @DisplayName("Soundex Variants (Genealogy and Simplified)")
    class SoundexVariantTests {

        /**
         * Tests the US_ENGLISH_GENEALOGY mapping which treats vowels and H/W as silent.
         * Rules from: http://www.genealogy.com/articles/research/00000060.html
         */
        @Test
        @DisplayName("should encode using the Genealogy mapping")
        void shouldEncodeUsingGenealogyMapping() {
            final Soundex soundex = Soundex.US_ENGLISH_GENEALOGY;
            assertEquals("H251", soundex.encode("Heggenburger"));
            assertEquals("B425", soundex.encode("Blackman"));
            assertEquals("S530", soundex.encode("Schmidt"));
            assertEquals("L150", soundex.encode("Lippmann"));
            // 'o' is silent, not a separator
            assertEquals("D200", soundex.encode("Dodds"));
            // 'h' is silent
            assertEquals("D200", soundex.encode("Dhdds"));
            // 'w' is silent
            assertEquals("D200", soundex.encode("Dwdds"));
        }

        /**
         * Tests the US_ENGLISH_SIMPLIFIED mapping which treats vowels and H/W as separators.
         * Rules from: http://west-penwith.org.uk/misc/soundex.htm
         */
        @Test
        @DisplayName("should encode using the Simplified mapping")
        void shouldEncodeUsingSimplifiedMapping() {
            final Soundex soundex = Soundex.US_ENGLISH_SIMPLIFIED;
            assertEquals("W452", soundex.encode("WILLIAMS"));
            assertEquals("B625", soundex.encode("BARAGWANATH"));
            assertEquals("D540", soundex.encode("DONNELL"));
            assertEquals("L300", soundex.encode("LLOYD"));
            assertEquals("W422", soundex.encode("WOOLCOCK"));
            // 'o' is a separator
            assertEquals("D320", soundex.encode("Dodds"));
            // 'w' is a separator
            assertEquals("D320", soundex.encode("Dwdds"));
            // 'h' is a separator
            assertEquals("D320", soundex.encode("Dhdds"));
        }
    }

    @Nested
    @DisplayName("Instantiation and Static Instances")
    class InstantiationTests {

        @Test
        @DisplayName("should encode correctly with default constructor")
        void shouldEncodeCorrectlyWithDefaultConstructor() {
            assertEquals("W452", new Soundex().soundex("Williams"));
        }

        @Test
        @DisplayName("should encode correctly with char array constructor")
        void shouldEncodeCorrectlyWithCharArrayConstructor() {
            assertEquals("W452", new Soundex(Soundex.US_ENGLISH_MAPPING_STRING.toCharArray()).soundex("Williams"));
        }

        @Test
        @DisplayName("should encode correctly with string constructor")
        void shouldEncodeCorrectlyWithStringConstructor() {
            assertEquals("W452", new Soundex(Soundex.US_ENGLISH_MAPPING_STRING).soundex("Williams"));
        }

        @Test
        @DisplayName("should encode correctly with the static US_ENGLISH instance")
        void shouldEncodeCorrectlyWithStaticUsEnglishInstance() {
            assertEquals("W452", Soundex.US_ENGLISH.soundex("Williams"));
        }
    }

    @Nested
    @DisplayName("Tests for SoundexUtils (for completeness)")
    // Note: These tests cover SoundexUtils, which is used by Soundex.
    // Ideally, these would be in a dedicated SoundexUtilsTest class.
    class SoundexUtilsAdjacencyTests {

        @Test
        @DisplayName("should handle null and empty inputs in utility methods")
        void shouldHandleNullAndEmptyInputsInCleanAndDifferenceEncoded() {
            assertNull(SoundexUtils.clean(null));
            assertEquals("", SoundexUtils.clean(""));
            assertEquals(0, SoundexUtils.differenceEncoded(null, ""));
            assertEquals(0, SoundexUtils.differenceEncoded("", null));
        }

        @Test
        @DisplayName("should have a constructable SoundexUtils class for code coverage")
        void shouldHaveConstructableSoundexUtilsClass() {
            // This test is primarily for code coverage of the private constructor.
            new SoundexUtils();
        }
    }
}