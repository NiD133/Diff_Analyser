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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link Soundex}.
 *
 * <p>
 * Keep this file in UTF-8 encoding for proper Javadoc processing.
 * </p>
 */
class SoundexTest extends AbstractStringEncoderTest<Soundex> {

    private Soundex soundex;

    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }

    @BeforeEach
    void setUp() {
        soundex = createStringEncoder();
    }

    /**
     * Helper method to avoid code duplication when checking multiple variations
     * that should produce the same Soundex code.
     *
     * @param expectedCode The expected Soundex code.
     * @param inputs       An array of input strings to encode.
     * @throws EncoderException If encoding fails.
     */
    private void assertEncodingEquals(final String expectedCode, final String... inputs) throws EncoderException {
        for (final String input : inputs) {
            assertEquals(expectedCode, soundex.encode(input), "Encoding mismatch for input: " + input);
        }
    }

    @Nested
    class EncodingTests {
        @Test
        void testB650() throws EncoderException {
            assertEncodingEquals("B650",
                    "BARHAM", "BARONE", "BARRON", "BERNA", "BIRNEY", "BIRNIE", "BOOROM", "BOREN", "BORN", "BOURN",
                    "BOURNE", "BOWRON", "BRAIN", "BRAME", "BRANN", "BRAUN", "BREEN", "BRIEN", "BRIM", "BRIMM", "BRINN",
                    "BRION", "BROOM", "BROOME", "BROWN", "BROWNE", "BRUEN", "BRUHN", "BRUIN", "BRUMM", "BRUN", "BRUNO",
                    "BRYAN", "BURIAN", "BURN", "BURNEY", "BYRAM", "BYRNE", "BYRON", "BYRUM");
        }

        @Test
        void testBadCharacters() {
            assertEquals("H452", soundex.encode("HOL>MES"));
        }

        @Test
        void testEncodeBasic() {
            assertEquals("T235", soundex.encode("testing"));
            assertEquals("T000", soundex.encode("The"));
            assertEquals("Q200", soundex.encode("quick"));
            assertEquals("B650", soundex.encode("brown"));
            assertEquals("F200", soundex.encode("fox"));
            assertEquals("J513", soundex.encode("jumped"));
            assertEquals("O160", soundex.encode("over"));
            assertEquals("T000", soundex.encode("the"));
            assertEquals("L200", soundex.encode("lazy"));
            assertEquals("D200", soundex.encode("dogs"));
        }

        /**
         * Examples from http://www.bradandkathy.com/genealogy/overviewofsoundex.html
         */
        @Test
        void testEncodeBatch2() {
            assertEquals("A462", soundex.encode("Allricht"));
            assertEquals("E166", soundex.encode("Eberhard"));
            assertEquals("E521", soundex.encode("Engebrethson"));
            assertEquals("H512", soundex.encode("Heimbach"));
            assertEquals("H524", soundex.encode("Hanselmann"));
            assertEquals("H431", soundex.encode("Hildebrand"));
            assertEquals("K152", soundex.encode("Kavanagh"));
            assertEquals("L530", soundex.encode("Lind"));
            assertEquals("L222", soundex.encode("Lukaschowsky"));
            assertEquals("M235", soundex.encode("McDonnell"));
            assertEquals("M200", soundex.encode("McGee"));
            assertEquals("O155", soundex.encode("Opnian"));
            assertEquals("O155", soundex.encode("Oppenheimer"));
            assertEquals("R355", soundex.encode("Riedemanas"));
            assertEquals("Z300", soundex.encode("Zita"));
            assertEquals("Z325", soundex.encode("Zitzmeinn"));
        }

        /**
         * Examples from http://www.archives.gov/research_room/genealogy/census/soundex.html
         */
        @Test
        void testEncodeBatch3() {
            assertEquals("W252", soundex.encode("Washington"));
            assertEquals("L000", soundex.encode("Lee"));
            assertEquals("G362", soundex.encode("Gutierrez"));
            assertEquals("P236", soundex.encode("Pfister"));
            assertEquals("J250", soundex.encode("Jackson"));
            assertEquals("T522", soundex.encode("Tymczak"));
            // For VanDeusen: D-250 (D, 2 for the S, 5 for the N, 0 added) is also
            // possible.
            assertEquals("V532", soundex.encode("VanDeusen"));
        }

        /**
         * Examples from: http://www.myatt.demon.co.uk/sxalg.htm
         */
        @Test
        void testEncodeBatch4() {
            assertEquals("H452", soundex.encode("HOLMES"));
            assertEquals("A355", soundex.encode("ADOMOMI"));
            assertEquals("V536", soundex.encode("VONDERLEHR"));
            assertEquals("B400", soundex.encode("BALL"));
            assertEquals("S000", soundex.encode("SHAW"));
            assertEquals("J250", soundex.encode("JACKSON"));
            assertEquals("S545", soundex.encode("SCANLON"));
            assertEquals("S532", soundex.encode("SAINTJOHN"));

        }

        @Test
        void testEncodeIgnoreApostrophes() throws EncoderException {
            assertEncodingEquals("O165", "'OBrien", "O'Brien", "OB'rien", "OBr'ien", "OBri'en", "OBrie'n", "OBrien'");
        }

        /**
         * Test data from http://www.myatt.demon.co.uk/sxalg.htm
         *
         * @throws EncoderException for some failure scenarios
         */
        @Test
        void testEncodeIgnoreHyphens() throws EncoderException {
            assertEncodingEquals("K525", "KINGSMITH", "-KINGSMITH", "K-INGSMITH", "KI-NGSMITH", "KIN-GSMITH", "KING-SMITH",
                    "KINGS-MITH", "KINGSM-ITH", "KINGSMI-TH", "KINGSMIT-H", "KINGSMITH-");
        }

        @Test
        void testEncodeIgnoreTrimmable() {
            assertEquals("W252", soundex.encode(" \t\n\r Washington \t\n\r "));
        }
    }

    @Nested
    class DifferenceTests {

        @Test
        void testDifference() throws EncoderException {
            // Edge cases
            assertEquals(0, soundex.difference(null, null));
            assertEquals(0, soundex.difference("", ""));
            assertEquals(0, soundex.difference(" ", " "));
            // Normal cases
            assertEquals(4, soundex.difference("Smith", "Smythe"));
            assertEquals(2, soundex.difference("Ann", "Andrew"));
            assertEquals(1, soundex.difference("Margaret", "Andrew"));
            assertEquals(0, soundex.difference("Janet", "Margaret"));
            // Examples from
            // https://msdn.microsoft.com/library/default.asp?url=/library/en-us/tsqlref/ts_de-dz_8co5.asp
            assertEquals(4, soundex.difference("Green", "Greene"));
            assertEquals(0, soundex.difference("Blotchet-Halls", "Greene"));
            // Examples from
            // https://msdn.microsoft.com/library/default.asp?url=/library/en-us/tsqlref/ts_setu-sus_3o6w.asp
            assertEquals(4, soundex.difference("Smith", "Smythe"));
            assertEquals(4, soundex.difference("Smithers", "Smythers"));
            assertEquals(2, soundex.difference("Anothers", "Brothers"));
        }
    }

    @Nested
    class GenealogySoundexTests {

        private Soundex genealogySoundex;

        @BeforeEach
        void setUp() {
            genealogySoundex = Soundex.US_ENGLISH_GENEALOGY;
        }

        @Test
        void testGenealogy() { // treat vowels and HW as silent
            assertEquals("H251", genealogySoundex.encode("Heggenburger"));
            assertEquals("B425", genealogySoundex.encode("Blackman"));
            assertEquals("S530", genealogySoundex.encode("Schmidt"));
            assertEquals("L150", genealogySoundex.encode("Lippmann"));
            // Additional local example
            assertEquals("D200", genealogySoundex.encode("Dodds")); // 'o' is not a separator here - it is silent
            assertEquals("D200", genealogySoundex.encode("Dhdds")); // 'h' is silent
            assertEquals("D200", genealogySoundex.encode("Dwdds")); // 'w' is silent
        }
    }

    @Nested
    class HWRuleTests {
        /**
         * Consonants from the same code group separated by W or H are treated as one.
         */
        @Test
        void testHWRuleEx1() {
            // From
            // http://www.archives.gov/research_room/genealogy/census/soundex.html:
            // Ashcraft is coded A-261 (A, 2 for the S, C ignored, 6 for the R, 1
            // for the F). It is not coded A-226.
            assertEquals("A261", soundex.encode("Ashcraft"));
            assertEquals("A261", soundex.encode("Ashcroft"));
            assertEquals("Y330", soundex.encode("yehudit"));
            assertEquals("Y330", soundex.encode("yhwdyt"));
        }

        /**
         * Consonants from the same code group separated by W or H are treated as one.
         *
         * Test data from http://www.myatt.demon.co.uk/sxalg.htm
         */
        @Test
        void testHWRuleEx2() {
            assertEquals("B312", soundex.encode("BOOTHDAVIS"));
            assertEquals("B312", soundex.encode("BOOTH-DAVIS"));
        }

        /**
         * Consonants from the same code group separated by W or H are treated as one.
         *
         * @throws EncoderException for some failure scenarios
         */
        @Test
        void testHWRuleEx3() throws EncoderException {
            assertEncodingEquals("S460", "Sgler", "Swhgler");
            // Also S460:
            assertEncodingEquals("S460", "SAILOR", "SALYER", "SAYLOR", "SCHALLER", "SCHELLER", "SCHILLER", "SCHOOLER",
                    "SCHULER", "SCHUYLER", "SEILER", "SEYLER", "SHOLAR", "SHULER", "SILAR", "SILER", "SILLER");
        }
    }

    @Nested
    class MsSqlServerTests {
        /**
         * Examples for MS SQLServer from
         * https://msdn.microsoft.com/library/default.asp?url=/library/en-us/tsqlref/ts_setu-sus_3o6w.asp
         */
        @Test
        void testMsSqlServer1() {
            assertEquals("S530", soundex.encode("Smith"));
            assertEquals("S530", soundex.encode("Smythe"));
        }

        /**
         * Examples for MS SQLServer from
         * https://support.microsoft.com/default.aspx?scid=https://support.microsoft.com:80/support
         * /kb/articles/Q100/3/65.asp&NoWebContent=1
         *
         * @throws EncoderException for some failure scenarios
         */
        @Test
        void testMsSqlServer2() throws EncoderException {
            assertEncodingEquals("E625", "Erickson", "Erickson", "Erikson", "Ericson", "Ericksen", "Ericsen");
        }

        /**
         * Examples for MS SQLServer from https://databases.about.com/library/weekly/aa042901a.htm
         */
        @Test
        void testMsSqlServer3() {
            assertEquals("A500", soundex.encode("Ann"));
            assertEquals("A536", soundex.encode("Andrew"));
            assertEquals("J530", soundex.encode("Janet"));
            assertEquals("M626", soundex.encode("Margaret"));
            assertEquals("S315", soundex.encode("Steven"));
            assertEquals("M240", soundex.encode("Michael"));
            assertEquals("R163", soundex.encode("Robert"));
            assertEquals("L600", soundex.encode("Laura"));
            assertEquals("A500", soundex.encode("Anne"));
        }
    }

    @Nested
    class NewInstanceTests {
        /**
         * https://issues.apache.org/jira/browse/CODEC-54
         * https://issues.apache.org/jira/browse/CODEC-56
         */
        @Test
        void testNewInstance() {
            assertEquals("W452", new Soundex().soundex("Williams"));
        }

        @Test
        void testNewInstance2() {
            assertEquals("W452", new Soundex(Soundex.US_ENGLISH_MAPPING_STRING.toCharArray()).soundex("Williams"));
        }

        @Test
        void testNewInstance3() {
            assertEquals("W452", new Soundex(Soundex.US_ENGLISH_MAPPING_STRING).soundex("Williams"));
        }
    }

    @Nested
    class SimplifiedSoundexTests {

        private Soundex simplifiedSoundex;

        @BeforeEach
        void setUp() {
            simplifiedSoundex = Soundex.US_ENGLISH_SIMPLIFIED;
        }

        @Test
        // examples and algorithm rules from: http://west-penwith.org.uk/misc/soundex.htm
        void testSimplifiedSoundex() { // treat vowels and HW as separators
            assertEquals("W452", simplifiedSoundex.encode("WILLIAMS"));
            assertEquals("B625", simplifiedSoundex.encode("BARAGWANATH"));
            assertEquals("D540", simplifiedSoundex.encode("DONNELL"));
            assertEquals("L300", simplifiedSoundex.encode("LLOYD"));
            assertEquals("W422", simplifiedSoundex.encode("WOOLCOCK"));
            // Additional local examples
            assertEquals("D320", simplifiedSoundex.encode("Dodds"));
            assertEquals("D320", simplifiedSoundex.encode("Dwdds")); // w is a separator
            assertEquals("D320", simplifiedSoundex.encode("Dhdds")); // h is a separator
        }
    }

    @Nested
    class SoundexUtilsTests {

        @Test
        void testSoundexUtilsConstructable() {
            new SoundexUtils();
        }

        @Test
        void testSoundexUtilsNullBehaviour() {
            assertNull(SoundexUtils.clean(null));
            assertEquals("", SoundexUtils.clean(""));
            assertEquals(0, SoundexUtils.differenceEncoded(null, ""));
            assertEquals(0, SoundexUtils.differenceEncoded("", null));
        }
    }

    @Nested
    class StaticUsEnglishTests {
        /**
         * https://issues.apache.org/jira/browse/CODEC-54
         * https://issues.apache.org/jira/browse/CODEC-56
         */
        @Test
        void testUsEnglishStatic() {
            assertEquals("W452", Soundex.US_ENGLISH.soundex("Williams"));
        }
    }

    @Nested
    class UsMappingTests {
        /**
         * Fancy characters are not mapped by the default US mapping.
         *
         * https://issues.apache.org/jira/browse/CODEC-30
         */
        @Test
        void testUsMappingEWithAcute() {
            assertEquals("E000", soundex.encode("e"));
            if (Character.isLetter('\u00e9')) { // e-acute
                // uppercase E-acute
                assertThrows(IllegalArgumentException.class, () -> soundex.encode("\u00e9"));
            } else {
                assertEquals("", soundex.encode("\u00e9"));
            }
        }

        /**
         * Fancy characters are not mapped by the default US mapping.
         *
         * https://issues.apache.org/jira/browse/CODEC-30
         */
        @Test
        void testUsMappingOWithDiaeresis() {
            assertEquals("O000", soundex.encode("o"));
            if (Character.isLetter('\u00f6')) { // o-umlaut
                // uppercase O-umlaut
                assertThrows(IllegalArgumentException.class, () -> soundex.encode("\u00f6"));
            } else {
                assertEquals("", soundex.encode("\u00f6"));
            }
        }
    }

    @Nested
    class WikipediaTests {

        @Test
        /**
         * Tests example from https://en.wikipedia.org/wiki/Soundex#American_Soundex as of
         * 2015-03-22.
         */
        void testWikipediaAmericanSoundex() {
            assertEquals("R163", soundex.encode("Robert"));
            assertEquals("R163", soundex.encode("Rupert"));
            assertEquals("A261", soundex.encode("Ashcraft"));
            assertEquals("A261", soundex.encode("Ashcroft"));
            assertEquals("T522", soundex.encode("Tymczak"));
            assertEquals("P236", soundex.encode("Pfister"));
        }
    }
}