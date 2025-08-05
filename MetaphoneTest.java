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
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests the Metaphone algorithm. The tests are structured into nested classes
 * to group them by functionality:
 * <ul>
 *     <li>{@link BasicEncodingTests}: General encoding examples.</li>
 *     <li>{@link MetaphoneRuleTests}: Tests for specific phonetic rules of the algorithm.</li>
 *     <li>{@link IsMetaphoneEqualTests}: Tests for the {@code isMetaphoneEqual} method.</li>
 *     <li>{@link ConfigurationTests}: Tests for encoder settings like max code length.</li>
 * </ul>
 */
class MetaphoneTest extends AbstractStringEncoderTest<Metaphone> {

    @Override
    protected Metaphone createStringEncoder() {
        return new Metaphone();
    }

    @Nested
    @DisplayName("Basic Encoding")
    class BasicEncodingTests {
        @ParameterizedTest(name = "Should encode ''{0}'' to ''{1}''")
        @CsvSource({
            "howl, HL",
            "testing, TSTN",
            "The, 0",       // Initial 'TH' is encoded as '0'
            "quick, KK",
            "brown, BRN",
            "fox, FKS",
            "jumped, JMPT",
            "over, OFR",
            "the, 0",
            "lazy, LS",
            "dogs, TKS"
        })
        void shouldEncodeCommonWords(final String input, final String expected) {
            assertEquals(expected, getStringEncoder().metaphone(input));
        }

        @Test
        @DisplayName("Should return an empty string for 'WHY' due to historical algorithm behavior")
        void shouldReturnEmptyStringForWhy() {
            // This behavior is noted in CODEC-57.
            // The original Metaphone algorithm returns an empty string, while other implementations (e.g., PHP) return "H".
            assertEquals("", getStringEncoder().metaphone("WHY"));
        }
    }

    @Nested
    @DisplayName("Algorithm Rule Verification")
    class MetaphoneRuleTests {

        @Test
        void shouldHandleInitialLetterRules() {
            assertEquals("N", getStringEncoder().metaphone("KNIGHT"), "Initial KN -> N");
            assertEquals("N", getStringEncoder().metaphone("GNU"), "Initial GN -> N");
            assertEquals("E", getStringEncoder().metaphone("AERO"), "Initial AE -> E");
            assertEquals("W", getStringEncoder().metaphone("WHITE"), "Initial WH -> W");
            assertEquals("R", getStringEncoder().metaphone("WRIGHT"), "Initial WR -> R");
            assertEquals("S", getStringEncoder().metaphone("XALAN"), "Initial X -> S");
        }

        @Test
        void shouldDropB_whenAtEndAfterM() {
            assertEquals("KM", getStringEncoder().metaphone("COMB"));
            assertEquals("TM", getStringEncoder().metaphone("TOMB"));
            assertEquals("WM", getStringEncoder().metaphone("WOMB"));
        }

        @Test
        void shouldEncodeCBasedRules() {
            assertEquals("XP", getStringEncoder().metaphone("CIAPO"), "CIA -> X");
            // Full code for "CHARACTER" is "KRKTR", truncated to "KRKT" by default max length of 4.
            assertEquals("KRKT", getStringEncoder().metaphone("CHARACTER"), "CH -> K");
            assertEquals("TX", getStringEncoder().metaphone("TEACH"), "CH -> X");
            assertEquals("SKTL", getStringEncoder().metaphone("SCHEDULE"), "SCH -> SK");
            assertEquals("SKMT", getStringEncoder().metaphone("SCHEMATIC"), "SCH -> SK");
            assertEquals("SNS", getStringEncoder().metaphone("SCIENCE"), "SC+I -> S");
            assertEquals("SN", getStringEncoder().metaphone("SCENE"), "SC+E -> S");
            assertEquals("S", getStringEncoder().metaphone("SCY"), "SC+Y -> S");
        }

        @Test
        void shouldEncodeDAsJ_whenFollowedByGE_GI_GY() {
            assertEquals("TJ", getStringEncoder().metaphone("DODGY"));
            assertEquals("TJ", getStringEncoder().metaphone("DODGE"));
            assertEquals("AJMT", getStringEncoder().metaphone("ADGIEMTI"));
        }

        @Test
        void shouldHandleGHRules() {
            assertEquals("KNT", getStringEncoder().metaphone("GHENT"), "GH -> K");
            assertEquals("B", getStringEncoder().metaphone("BAUGH"), "-GH becomes silent");
        }

        @Test
        void shouldDropG_whenInGNED() {
            assertEquals("SNT", getStringEncoder().metaphone("SIGNED"));
        }

        @Test
        void shouldEncodePHAsF() {
            assertEquals("FX", getStringEncoder().metaphone("PHISH"));
        }

        @Test
        void shouldEncodeTAndSRulesAsX() {
            assertEquals("XT", getStringEncoder().metaphone("SHOT"), "SH -> X");
            assertEquals("OTXN", getStringEncoder().metaphone("ODSIAN"), "SIA -> X");
            assertEquals("PLXN", getStringEncoder().metaphone("PULSION"), "SIO -> X");
            assertEquals("OX", getStringEncoder().metaphone("OTIA"), "TIA -> X");
            assertEquals("PRXN", getStringEncoder().metaphone("PORTION"), "TIO -> X");
            assertEquals("RX", getStringEncoder().metaphone("RETCH"), "TCH -> X");
            assertEquals("WX", getStringEncoder().metaphone("WATCH"), "TCH -> X");
        }
    }

    @Nested
    @DisplayName("isMetaphoneEqual Method")
    class IsMetaphoneEqualTests {

        @Test
        void shouldBeEqualForSameWordWithDifferentCasing() {
            assertTrue(getStringEncoder().isMetaphoneEqual("Case", "case"));
            assertTrue(getStringEncoder().isMetaphoneEqual("CASE", "Case"));
            assertTrue(getStringEncoder().isMetaphoneEqual("caSe", "cAsE"));
        }

        @Test
        void shouldBeEqualForPhoneticallySimilarWords() {
            assertTrue(getStringEncoder().isMetaphoneEqual("quick", "cookie"));
            assertTrue(getStringEncoder().isMetaphoneEqual("Lawrence", "Lorenza"));
            assertTrue(getStringEncoder().isMetaphoneEqual("Gary", "Cahra"));
        }

        /**
         * This group of tests verifies that a source name is phonetically equal to a list of other names.
         * The test data was computed from an external reference implementation.
         */
        @Nested
        @DisplayName("Reference Phonetic Equality")
        class ReferenceEqualityTests {

            private void assertSourceIsPhoneticallyEqualToAll(final String source, final String... matches) {
                final Metaphone encoder = getStringEncoder();
                for (final String match : matches) {
                    assertTrue(encoder.isMetaphoneEqual(source, match),
                        () -> "Source '" + source + "' should have same Metaphone as '" + match + "'");
                }
                // Also ensure all matches are equivalent to each other
                for (final String match1 : matches) {
                    for (final String match2 : matches) {
                        assertTrue(encoder.isMetaphoneEqual(match1, match2),
                            () -> "Match '" + match1 + "' should have same Metaphone as '" + match2 + "'");
                    }
                }
            }

            @Test
            @DisplayName("should match names for initial 'AE' (Aero)")
            void shouldMatchAeroAndEure() {
                assertSourceIsPhoneticallyEqualToAll("Aero", "Eure");
            }

            @Test
            @DisplayName("should match names for initial 'A' not followed by 'E' (Albert)")
            void shouldMatchAlbertAndVariants() {
                assertSourceIsPhoneticallyEqualToAll("Albert", "Ailbert", "Alberik", "Albert", "Alberto", "Albrecht");
            }

            @Test
            void shouldMatchGaryAndVariants() {
                assertSourceIsPhoneticallyEqualToAll("Gary",
                    "Cahra", "Cara", "Carey", "Cari", "Caria", "Carie", "Caro", "Carree", "Carri", "Carrie", "Carry", "Cary", "Cora", "Corey",
                    "Cori", "Corie", "Correy", "Corri", "Corrie", "Corry", "Cory", "Gray", "Kara", "Kare", "Karee", "Kari", "Karia", "Karie", "Karrah",
                    "Karrie", "Karry", "Kary", "Keri", "Kerri", "Kerrie", "Kerry", "Kira", "Kiri", "Kora", "Kore", "Kori", "Korie", "Korrie", "Korry");
            }

            @Test
            void shouldMatchJohnAndVariants() {
                assertSourceIsPhoneticallyEqualToAll("John",
                    "Gena", "Gene", "Genia", "Genna", "Genni", "Gennie", "Genny", "Giana", "Gianna", "Gina", "Ginni", "Ginnie", "Ginny", "Jaine",
                    "Jan", "Jana", "Jane", "Janey", "Jania", "Janie", "Janna", "Jany", "Jayne", "Jean", "Jeana", "Jeane", "Jeanie", "Jeanna", "Jeanne",
                    "Jeannie", "Jen", "Jena", "Jeni", "Jenn", "Jenna", "Jennee", "Jenni", "Jennie", "Jenny", "Jinny", "Jo Ann", "Jo-Ann", "Jo-Anne", "Joan",
                    "Joana", "Joane", "Joanie", "Joann", "Joanna", "Joanne", "Joeann", "Johna", "Johnna", "Joni", "Jonie", "Juana", "June", "Junia",
                    "Junie");
            }

            @Test
            @DisplayName("should match names for initial 'KN' (Knight)")
            void shouldMatchKnightAndVariants() {
                assertSourceIsPhoneticallyEqualToAll("Knight", "Hynda", "Nada", "Nadia", "Nady", "Nat", "Nata", "Natty", "Neda", "Nedda", "Nedi", "Netta",
                    "Netti", "Nettie", "Netty", "Nita", "Nydia");
            }

            @Test
            void shouldMatchMaryAndVariants() {
                assertSourceIsPhoneticallyEqualToAll("Mary", "Mair", "Maire", "Mara", "Mareah", "Mari", "Maria", "Marie", "Mary", "Maura", "Maure", "Meara",
                    "Merrie", "Merry", "Mira", "Moira", "Mora", "Moria", "Moyra", "Muire", "Myra", "Myrah");
            }

            @Test
            void shouldMatchParisAndVariants() {
                assertSourceIsPhoneticallyEqualToAll("Paris", "Pearcy", "Perris", "Piercy", "Pierz", "Pryse");
            }

            @Test
            void shouldMatchPeterAndVariants() {
                assertSourceIsPhoneticallyEqualToAll("Peter", "Peadar", "Peder", "Pedro", "Peter", "Petr", "Peyter", "Pieter", "Pietro", "Piotr");
            }

            @Test
            void shouldMatchRayAndVariants() {
                assertSourceIsPhoneticallyEqualToAll("Ray", "Ray", "Rey", "Roi", "Roy", "Ruy");
            }

            @Test
            void shouldMatchSusanAndVariants() {
                assertSourceIsPhoneticallyEqualToAll("Susan",
                    "Siusan", "Sosanna", "Susan", "Susana", "Susann", "Susanna", "Susannah", "Susanne", "Suzann", "Suzanna", "Suzanne", "Zuzana");
            }

            @Test
            @DisplayName("should match names for initial 'WH' (White)")
            void shouldMatchWhiteAndVariants() {
                assertSourceIsPhoneticallyEqualToAll("White",
                    "Wade", "Wait", "Waite", "Wat", "Whit", "Wiatt", "Wit", "Wittie", "Witty", "Wood", "Woodie", "Woody");
            }

            @Test
            @DisplayName("should match names for initial 'WR' (Wright)")
            void shouldMatchWrightAndVariants() {
                assertSourceIsPhoneticallyEqualToAll("Wright", "Rota", "Rudd", "Ryde");
            }

            @Test
            @DisplayName("should match names for initial 'X' (Xalan)")
            void shouldMatchXalanAndVariants() {
                assertSourceIsPhoneticallyEqualToAll("Xalan", "Celene", "Celina", "Celine", "Selena", "Selene", "Selina", "Seline", "Suellen", "Xylina");
            }
        }
    }

    @Nested
    @DisplayName("Configuration")
    class ConfigurationTests {
        @Test
        void shouldTruncateToDefaultMaxLengthOf4() {
            // The full Metaphone for "AXEAXE" is "AKSKS".
            // The default max length is 4, so it should be truncated.
            assertEquals("AKSK", getStringEncoder().metaphone("AXEAXE"));
        }

        @Test
        void shouldTruncateToCustomMaxLength() {
            // The full Metaphone for "AXEAXEAXE" is "AKSKSKS".
            final Metaphone metaphone = getStringEncoder();
            metaphone.setMaxCodeLen(6);
            assertEquals("AKSKSK", metaphone.metaphone("AXEAXEAXE"));
        }
    }
}