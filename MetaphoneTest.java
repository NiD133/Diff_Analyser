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
import static org.junit.jupiter.api.Assertions.fail;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for Metaphone.
 *
 * The test names and groupings aim to make the intent of each rule-focused test clear,
 * reduce noise by centralizing common assertions, and keep the original behavior.
 */
class MetaphoneTest extends AbstractStringEncoderTest<Metaphone> {

    // ---------------------------------------------------------------------
    // Test helpers
    // ---------------------------------------------------------------------

    private String code(final String input) {
        return getStringEncoder().metaphone(input);
    }

    private void assertEncodesTo(final String input, final String expected) {
        assertEquals(expected, code(input), "metaphone(\"" + input + "\")");
    }

    /**
     * Asserts that:
     * - source is metaphone-equal to every candidate
     * - and every candidate is metaphone-equal to every other candidate
     */
    private void assertAllMetaphoneEqual(final String source, final String... candidates) {
        // source vs. each candidate
        for (final String candidate : candidates) {
            assertTrue(getStringEncoder().isMetaphoneEqual(source, candidate),
                "Expected same metaphone: source=" + source + ", candidate=" + candidate);
        }
        // candidates vs. each other
        for (final String a : candidates) {
            for (final String b : candidates) {
                assertTrue(getStringEncoder().isMetaphoneEqual(a, b),
                    "Expected same metaphone: a=" + a + ", b=" + b);
            }
        }
    }

    /**
     * Asserts symmetry of metaphone equality for the given pairs.
     * Each pair is [left, right].
     */
    private void assertPairsAreMetaphoneEqual(final String[][] pairs) {
        validatePairsFixture(pairs);
        for (final String[] pair : pairs) {
            final String left = pair[0];
            final String right = pair[1];
            final String msg = "Expected metaphone match between \"" + left + "\" and \"" + right + '"';
            assertTrue(getStringEncoder().isMetaphoneEqual(left, right), msg);
            assertTrue(getStringEncoder().isMetaphoneEqual(right, left), msg);
        }
    }

    private void validatePairsFixture(final String[][] pairs) {
        if (pairs.length == 0) {
            fail("Test fixture is empty");
        }
        for (int i = 0; i < pairs.length; i++) {
            if (pairs[i].length != 2) {
                fail("Each fixture row must have 2 elements [left, right]; bad row index: " + i);
            }
        }
    }

    // ---------------------------------------------------------------------
    // Boilerplate
    // ---------------------------------------------------------------------

    @Override
    protected Metaphone createStringEncoder() {
        return new Metaphone();
    }

    // ---------------------------------------------------------------------
    // Rule-focused tests
    // ---------------------------------------------------------------------

    @Nested
    @DisplayName("Discard/translation rules")
    class DiscardAndTranslateRules {

        @Test
        @DisplayName("Discard of SCE/SCI/SCY")
        void discardOfSCEOrSCIOrSCY() {
            assertEncodesTo("SCIENCE", "SNS");
            assertEncodesTo("SCENE", "SN");
            assertEncodesTo("SCY", "S");
        }

        @Test
        @DisplayName("Discard of silent GN (start of word) and handling of GNED")
        void discardOfSilentGN() {
            // NOTE: This does not test for silent GN, but for starting with GN
            assertEncodesTo("GNU", "N");

            // NOTE: Trying to test for GNED, but expected code path in original impl does not appear to execute
            assertEncodesTo("SIGNED", "SNT");
        }

        @Test
        @DisplayName("Discard of silent H after G")
        void discardOfSilentHAfterG() {
            assertEncodesTo("GHENT", "KNT");
            assertEncodesTo("BAUGH", "B");
        }

        @Test
        @DisplayName("PH -> F")
        void phToF() {
            assertEncodesTo("PHISH", "FX");
        }

        @Test
        @DisplayName("SH / SIO / SIA -> X")
        void shAndSioAndSiaToX() {
            assertEncodesTo("SHOT", "XT");
            assertEncodesTo("ODSIAN", "OTXN");
            assertEncodesTo("PULSION", "PLXN");
        }

        @Test
        @DisplayName("TCH handling")
        void tch() {
            assertEncodesTo("RETCH", "RX");
            assertEncodesTo("WATCH", "WX");
        }

        @Test
        @DisplayName("TIO / TIA -> X")
        void tioAndTiaToX() {
            assertEncodesTo("OTIA", "OX");
            assertEncodesTo("PORTION", "PRXN");
        }

        @Test
        @DisplayName("SCH/CH translations")
        void translateOfSCHAndCH() {
            assertEncodesTo("SCHEDULE", "SKTL");
            assertEncodesTo("SCHEMATIC", "SKMT");

            assertEncodesTo("CHARACTER", "KRKT");
            assertEncodesTo("TEACH", "TX");
        }

        @Test
        @DisplayName("DGE / DGI / DGY -> J")
        void translateToJOfDGEOrDGIOrDGY() {
            assertEncodesTo("DODGY", "TJ");
            assertEncodesTo("DODGE", "TJ");
            assertEncodesTo("ADGIEMTI", "AJMT");
        }

        @Test
        @DisplayName("Words ending in MB")
        void wordEndingInMB() {
            assertEncodesTo("COMB", "KM");
            assertEncodesTo("TOMB", "TM");
            assertEncodesTo("WOMB", "WM");
        }

        @Test
        @DisplayName("Words with CIA")
        void wordsWithCIA() {
            assertEncodesTo("CIAPO", "XP");
        }
    }

    @Nested
    @DisplayName("Length behavior")
    class LengthBehavior {

        @Test
        @DisplayName("Code truncated by default max length")
        void exceedLength() {
            // The raw code would be "AKSKS", but is truncated by the default max code length (4)
            assertEncodesTo("AXEAXE", "AKSK");
        }

        @Test
        @DisplayName("Custom max length with truncation")
        void setMaxLengthWithTruncation() {
            // The raw code would extend further; with max length 6 we expect "AKSKSK"
            getStringEncoder().setMaxCodeLen(6);
            assertEncodesTo("AXEAXEAXE", "AKSKSK");
        }
    }

    @Nested
    @DisplayName("Sample sentence")
    class SampleSentence {

        @Test
        void testMetaphone() {
            assertEncodesTo("howl", "HL");
            assertEncodesTo("testing", "TSTN");
            assertEncodesTo("The", "0");
            assertEncodesTo("quick", "KK");
            assertEncodesTo("brown", "BRN");
            assertEncodesTo("fox", "FKS");
            assertEncodesTo("jumped", "JMPT");
            assertEncodesTo("over", "OFR");
            assertEncodesTo("the", "0");
            assertEncodesTo("lazy", "LS");
            assertEncodesTo("dogs", "TKS");
        }
    }

    @Nested
    @DisplayName("Equality checks")
    class EqualityChecks {

        @Test
        @DisplayName("Case-insensitivity and a simple near-match")
        void isMetaphoneEqual_basic() {
            assertPairsAreMetaphoneEqual(new String[][] {
                { "Case", "case" },
                { "CASE", "Case" },
                { "caSe", "cAsE" },
                { "quick", "cookie" }
            });
        }

        @Test
        @DisplayName("Pairs from reference fixture")
        void isMetaphoneEqual_referencePairs() {
            // Matches computed from http://www.lanw.com/java/phonetic/default.htm
            assertPairsAreMetaphoneEqual(new String[][] {
                { "Lawrence", "Lorenza" },
                { "Gary", "Cahra" }
            });
        }

        @Test
        @DisplayName("Initial AE case: Aero")
        void isMetaphoneEqual_aero() {
            // Match data from http://www.lanw.com/java/phonetic/default.htm
            assertAllMetaphoneEqual("Aero", "Eure");
        }

        @Test
        @DisplayName("Initial A not followed by E: Albert and variants")
        void isMetaphoneEqual_albert() {
            // Match data from http://www.lanw.com/java/phonetic/default.htm
            assertAllMetaphoneEqual("Albert", "Ailbert", "Alberik", "Albert", "Alberto", "Albrecht");
        }

        @Test
        @DisplayName("Gary and many variants")
        void isMetaphoneEqual_gary() {
            // Match data from http://www.lanw.com/java/phonetic/default.htm
            assertAllMetaphoneEqual("Gary",
                "Cahra", "Cara", "Carey", "Cari", "Caria", "Carie", "Caro", "Carree", "Carri", "Carrie", "Carry", "Cary",
                "Cora", "Corey", "Cori", "Corie", "Correy", "Corri", "Corrie", "Corry", "Cory",
                "Gray",
                "Kara", "Kare", "Karee", "Kari", "Karia", "Karie", "Karrah", "Karrie", "Karry", "Kary",
                "Keri", "Kerri", "Kerrie", "Kerry",
                "Kira", "Kiri",
                "Kora", "Kore", "Kori", "Korie", "Korrie", "Korry"
            );
        }

        @Test
        @DisplayName("John and many variants")
        void isMetaphoneEqual_john() {
            // Match data from http://www.lanw.com/java/phonetic/default.htm
            assertAllMetaphoneEqual("John",
                "Gena", "Gene", "Genia", "Genna", "Genni", "Gennie", "Genny",
                "Giana", "Gianna", "Gina",
                "Ginni", "Ginnie", "Ginny",
                "Jaine",
                "Jan", "Jana", "Jane", "Janey", "Jania", "Janie", "Janna", "Jany", "Jayne",
                "Jean", "Jeana", "Jeane", "Jeanie", "Jeanna", "Jeanne", "Jeannie",
                "Jen", "Jena", "Jeni", "Jenn", "Jenna", "Jennee", "Jenni", "Jennie", "Jenny",
                "Jinny",
                "Jo Ann", "Jo-Ann", "Jo-Anne",
                "Joan", "Joana", "Joane", "Joanie", "Joann", "Joanna", "Joanne", "Joeann",
                "Johna", "Johnna",
                "Joni", "Jonie",
                "Juana",
                "June", "Junia", "Junie"
            );
        }

        @Test
        @DisplayName("Initial KN case: Knight and variants")
        void isMetaphoneEqual_knight() {
            // Match data from http://www.lanw.com/java/phonetic/default.htm
            assertAllMetaphoneEqual("Knight",
                "Hynda", "Nada", "Nadia", "Nady", "Nat", "Nata", "Natty",
                "Neda", "Nedda", "Nedi",
                "Netta", "Netti", "Nettie", "Netty",
                "Nita", "Nydia"
            );
        }

        @Test
        @DisplayName("Mary and variants")
        void isMetaphoneEqual_mary() {
            // Match data from http://www.lanw.com/java/phonetic/default.htm
            assertAllMetaphoneEqual("Mary",
                "Mair", "Maire", "Mara", "Mareah",
                "Mari", "Maria", "Marie", "Mary",
                "Maura", "Maure",
                "Meara",
                "Merrie", "Merry",
                "Mira",
                "Moira",
                "Mora", "Moria",
                "Moyra",
                "Muire",
                "Myra", "Myrah"
            );
        }

        @Test
        @DisplayName("Paris and variants")
        void isMetaphoneEqual_paris() {
            // Match data from http://www.lanw.com/java/phonetic/default.htm
            assertAllMetaphoneEqual("Paris", "Pearcy", "Perris", "Piercy", "Pierz", "Pryse");
        }

        @Test
        @DisplayName("Peter and variants")
        void isMetaphoneEqual_peter() {
            // Match data from http://www.lanw.com/java/phonetic/default.htm
            assertAllMetaphoneEqual("Peter", "Peadar", "Peder", "Pedro", "Peter", "Petr", "Peyter", "Pieter", "Pietro", "Piotr");
        }

        @Test
        @DisplayName("Ray and variants")
        void isMetaphoneEqual_ray() {
            // Match data from http://www.lanw.com/java/phonetic/default.htm
            assertAllMetaphoneEqual("Ray", "Ray", "Rey", "Roi", "Roy", "Ruy");
        }

        @Test
        @DisplayName("Susan and variants")
        void isMetaphoneEqual_susan() {
            // Match data from http://www.lanw.com/java/phonetic/default.htm
            assertAllMetaphoneEqual("Susan",
                "Siusan", "Sosanna", "Susan", "Susana", "Susann", "Susanna", "Susannah", "Susanne",
                "Suzann", "Suzanna", "Suzanne", "Zuzana"
            );
        }

        @Test
        @DisplayName("Initial WH case: White and variants")
        void isMetaphoneEqual_white() {
            // Match data from http://www.lanw.com/java/phonetic/default.htm
            assertAllMetaphoneEqual("White",
                "Wade", "Wait", "Waite", "Wat", "Whit", "Wiatt", "Wit", "Wittie", "Witty",
                "Wood", "Woodie", "Woody"
            );
        }

        @Test
        @DisplayName("Initial WR case: Wright and variants")
        void isMetaphoneEqual_wright() {
            // Match data from http://www.lanw.com/java/phonetic/default.htm
            assertAllMetaphoneEqual("Wright", "Rota", "Rudd", "Ryde");
        }

        @Test
        @DisplayName("Xalan and variants")
        void isMetaphoneEqual_xalan() {
            // Match data from http://www.lanw.com/java/phonetic/default.htm
            assertAllMetaphoneEqual("Xalan",
                "Celene", "Celina", "Celine", "Selena", "Selene", "Selina", "Seline", "Suellen", "Xylina"
            );
        }
    }

    @Nested
    @DisplayName("Edge cases")
    class EdgeCases {

        /**
         * Tests (CODEC-57) Metaphone.Metaphone(String) returns an empty string when passed the word "why".
         * PHP returns "H". The original Metaphone returns an empty string.
         */
        @Test
        void whyReturnsEmptyString() {
            assertEncodesTo("WHY", "");
        }
    }
}