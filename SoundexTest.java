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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

/**
 * Tests {@link Soundex}.
 */
class SoundexTest extends AbstractStringEncoderTest<Soundex> {

    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }

    // ==================== Basic Encoding Tests ====================
    @Test
    void encodeBasic() {
        assertEquals("T235", getStringEncoder().encode("testing"), "Testing should encode to T235");
        assertEquals("T000", getStringEncoder().encode("The"), "The should encode to T000");
        assertEquals("Q200", getStringEncoder().encode("quick"), "Quick should encode to Q200");
        assertEquals("B650", getStringEncoder().encode("brown"), "Brown should encode to B650");
        assertEquals("F200", getStringEncoder().encode("fox"), "Fox should encode to F200");
        assertEquals("J513", getStringEncoder().encode("jumped"), "Jumped should encode to J513");
        assertEquals("O160", getStringEncoder().encode("over"), "Over should encode to O160");
        assertEquals("T000", getStringEncoder().encode("the"), "the should encode to T000");
        assertEquals("L200", getStringEncoder().encode("lazy"), "Lazy should encode to L200");
        assertEquals("D200", getStringEncoder().encode("dogs"), "Dogs should encode to D200");
    }

    @Test
    void encodeWithBadCharacters() {
        assertEquals("H452", getStringEncoder().encode("HOL>MES"), 
            "Non-alphabetic characters should be ignored");
    }

    @Test
    void encodeIgnoresTrimmableWhitespace() {
        assertEquals("W252", getStringEncoder().encode(" \t\n\r Washington \t\n\r "), 
            "Whitespace should be trimmed before encoding");
    }

    // ==================== Special Character Handling ====================
    @Nested
    class SpecialCharacterTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "OBrien", "'OBrien", "O'Brien", "OB'rien", "OBr'ien", "OBri'en", "OBrie'n", "OBrien'"
        })
        void encodeIgnoresApostrophes(String input) throws EncoderException {
            assertEquals("O165", getStringEncoder().encode(input), 
                "Apostrophes should be ignored: " + input);
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "KINGSMITH", "-KINGSMITH", "K-INGSMITH", "KI-NGSMITH", "KIN-GSMITH", 
            "KING-SMITH", "KINGS-MITH", "KINGSM-ITH", "KINGSMI-TH", "KINGSMIT-H", "KINGSMITH-"
        })
        void encodeIgnoresHyphens(String input) throws EncoderException {
            assertEquals("K525", getStringEncoder().encode(input), 
                "Hyphens should be ignored: " + input);
        }
    }

    // ==================== HW Rule Tests ====================
    @Nested
    class HWRuleTests {
        @Test
        void encodeWithHWSpecialHandling() {
            // From archives.gov example
            assertEquals("A261", getStringEncoder().encode("Ashcraft"), 
                "Ashcraft should encode to A261 (H acts as separator)");
            assertEquals("A261", getStringEncoder().encode("Ashcroft"), 
                "Ashcroft should encode to A261 (H acts as separator)");
            assertEquals("B312", getStringEncoder().encode("BOOTHDAVIS"), 
                "BOOTHDAVIS should encode to B312");
            assertEquals("B312", getStringEncoder().encode("BOOTH-DAVIS"), 
                "Hyphen in BOOTH-DAVIS should be ignored");
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "SAILOR", "SALYER", "SAYLOR", "SCHALLER", "SCHELLER", "SCHILLER", 
            "SCHOOLER", "SCHULER", "SCHUYLER", "SEILER", "SEYLER", "SHOLAR", 
            "SHULER", "SILAR", "SILER", "SILLER"
        })
        void encodeWithHWAsSeparator(String input) throws EncoderException {
            assertEquals("S460", getStringEncoder().encode(input), 
                "H/W should act as separators for: " + input);
        }
    }

    // ==================== Batch Encoding Tests ====================
    @Nested
    class BatchEncodingTests {
        @Test
        void encodeBatchFromBradAndKathy() {
            // From: http://www.bradandkathy.com/genealogy/overviewofsoundex.html
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
        void encodeBatchFromArchivesGov() {
            // From: http://www.archives.gov/research_room/genealogy/census/soundex.html
            assertEquals("W252", getStringEncoder().encode("Washington"));
            assertEquals("L000", getStringEncoder().encode("Lee"));
            assertEquals("G362", getStringEncoder().encode("Gutierrez"));
            assertEquals("P236", getStringEncoder().encode("Pfister"));
            assertEquals("J250", getStringEncoder().encode("Jackson"));
            assertEquals("T522", getStringEncoder().encode("Tymczak"));
            assertEquals("V532", getStringEncoder().encode("VanDeusen"));
        }

        @Test
        void encodeBatchFromSxalg() {
            // From: http://www.myatt.demon.co.uk/sxalg.htm
            assertEquals("H452", getStringEncoder().encode("HOLMES"));
            assertEquals("A355", getStringEncoder().encode("ADOMOMI"));
            assertEquals("V536", getStringEncoder().encode("VONDERLEHR"));
            assertEquals("B400", getStringEncoder().encode("BALL"));
            assertEquals("S000", getStringEncoder().encode("SHAW"));
            assertEquals("J250", getStringEncoder().encode("JACKSON"));
            assertEquals("S545", getStringEncoder().encode("SCANLON"));
            assertEquals("S532", getStringEncoder().encode("SAINTJOHN"));
        }
    }

    // ==================== Soundex Difference Tests ====================
    @Nested
    class DifferenceTests {
        @ParameterizedTest
        @MethodSource("differenceTestData")
        void testSoundexDifference(String s1, String s2, int expected) throws EncoderException {
            assertEquals(expected, getStringEncoder().difference(s1, s2),
                "Difference between '" + s1 + "' and '" + s2 + "'");
        }

        static Stream<Arguments> differenceTestData() {
            return Stream.of(
                // Edge cases
                Arguments.of(null, null, 0),
                Arguments.of("", "", 0),
                Arguments.of(" ", " ", 0),
                
                // Normal cases
                Arguments.of("Smith", "Smythe", 4),
                Arguments.of("Ann", "Andrew", 2),
                Arguments.of("Margaret", "Andrew", 1),
                Arguments.of("Janet", "Margaret", 0),
                
                // From MSDN examples
                Arguments.of("Green", "Greene", 4),
                Arguments.of("Blotchet-Halls", "Greene", 0),
                Arguments.of("Smithers", "Smythers", 4),
                Arguments.of("Anothers", "Brothers", 2)
            );
        }
    }

    // ==================== Instance Configuration Tests ====================
    @Nested
    class InstanceTests {
        @Test
        void usEnglishInstance() {
            assertEquals("W452", Soundex.US_ENGLISH.soundex("Williams"));
        }

        @Test
        void newInstanceWithMappingString() {
            assertEquals("W452", new Soundex(Soundex.US_ENGLISH_MAPPING_STRING).soundex("Williams"));
        }

        @Test
        void newInstanceWithMappingArray() {
            assertEquals("W452", new Soundex(Soundex.US_ENGLISH_MAPPING_STRING.toCharArray()).soundex("Williams"));
        }

        @Test
        void defaultInstance() {
            assertEquals("W452", new Soundex().soundex("Williams"));
        }
    }

    // ==================== Specialized Soundex Variants ====================
    @Nested
    class VariantTests {
        @Test
        void simplifiedSoundex() {
            // From: http://west-penwith.org.uk/misc/soundex.htm
            final Soundex s = Soundex.US_ENGLISH_SIMPLIFIED;
            assertEquals("W452", s.encode("WILLIAMS"));
            assertEquals("B625", s.encode("BARAGWANATH"));
            assertEquals("D540", s.encode("DONNELL"));
            assertEquals("L300", s.encode("LLOYD"));
            assertEquals("W422", s.encode("WOOLCOCK"));
            assertEquals("D320", s.encode("Dodds"));
            assertEquals("D320", s.encode("Dwdds"));
            assertEquals("D320", s.encode("Dhdds"));
        }

        @Test
        void genealogySoundex() {
            // From: http://www.genealogy.com/articles/research/00000060.html
            final Soundex s = Soundex.US_ENGLISH_GENEALOGY;
            assertEquals("H251", s.encode("Heggenburger"));
            assertEquals("B425", s.encode("Blackman"));
            assertEquals("S530", s.encode("Schmidt"));
            assertEquals("L150", s.encode("Lippmann"));
            assertEquals("D200", s.encode("Dodds"));
            assertEquals("D200", s.encode("Dhdds"));
            assertEquals("D200", s.encode("Dwdds"));
        }
    }

    // ==================== Wikipedia Examples ====================
    @Test
    void wikipediaExamples() {
        // From: https://en.wikipedia.org/wiki/Soundex
        assertEquals("R163", getStringEncoder().encode("Robert"));
        assertEquals("R163", getStringEncoder().encode("Rupert"));
        assertEquals("A261", getStringEncoder().encode("Ashcraft"));
        assertEquals("A261", getStringEncoder().encode("Ashcroft"));
        assertEquals("T522", getStringEncoder().encode("Tymczak"));
        assertEquals("P236", getStringEncoder().encode("Pfister"));
    }

    // ==================== Character Validation Tests ====================
    @Nested
    class CharacterValidationTests {
        @Test
        void usMappingRejectsEWithAcute() {
            assertEquals("E000", getStringEncoder().encode("e"));
            assertThrows(IllegalArgumentException.class, 
                () -> getStringEncoder().encode("\u00e9"), 
                "Non-ASCII character é should be rejected");
        }

        @Test
        void usMappingRejectsOWithDiaeresis() {
            assertEquals("O000", getStringEncoder().encode("o"));
            assertThrows(IllegalArgumentException.class, 
                () -> getStringEncoder().encode("\u00f6"), 
                "Non-ASCII character ö should be rejected");
        }
    }

    // ==================== SoundexUtils Tests ====================
    @Nested
    class SoundexUtilsTests {
        @Test
        void soundexUtilsHandlesNull() {
            assertNull(SoundexUtils.clean(null));
            assertEquals("", SoundexUtils.clean(""));
            assertEquals(0, SoundexUtils.differenceEncoded(null, ""));
            assertEquals(0, SoundexUtils.differenceEncoded("", null));
        }

        @Test
        void soundexUtilsIsConstructable() {
            new SoundexUtils();
        }
    }

    // ==================== Parameterized B650 Test ====================
    @ParameterizedTest
    @MethodSource("b650DataProvider")
    void testB650Encoding(String input) throws EncoderException {
        assertEquals("B650", getStringEncoder().encode(input), 
            "Input should encode to B650: " + input);
    }

    static Stream<String> b650DataProvider() {
        return Stream.of(
            "BARHAM", "BARONE", "BARRON", "BERNA", "BIRNEY", "BIRNIE", 
            "BOOROM", "BOREN", "BORN", "BOURN", "BOURNE", "BOWRON", 
            "BRAIN", "BRAME", "BRANN", "BRAUN", "BREEN", "BRIEN", 
            "BRIM", "BRIMM", "BRINN", "BRION", "BROOM", "BROOME", 
            "BROWN", "BROWNE", "BRUEN", "BRUHN", "BRUIN", "BRUMM", 
            "BRUN", "BRUNO", "BRYAN", "BURIAN", "BURN", "BURNEY", 
            "BYRAM", "BYRNE", "BYRON", "BYRUM"
        );
    }

    // ==================== SQL Server Compatibility Tests ====================
    @Nested
    class SqlServerCompatibilityTests {
        @Test
        void sqlServerExample1() {
            assertEquals("S530", getStringEncoder().encode("Smith"));
            assertEquals("S530", getStringEncoder().encode("Smythe"));
        }

        @Test
        void sqlServerExample2() {
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

        @ParameterizedTest
        @ValueSource(strings = {
            "Erickson", "Erickson", "Erikson", 
            "Ericson", "Ericksen", "Ericsen"
        })
        void sqlServerEricksonVariants(String input) throws EncoderException {
            assertEquals("E625", getStringEncoder().encode(input), 
                "Erickson variant should encode to E625: " + input);
        }
    }
}