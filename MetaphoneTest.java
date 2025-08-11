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
import org.junit.jupiter.api.Test;

/**
 * Test suite for the Metaphone phonetic encoding algorithm.
 * 
 * The Metaphone algorithm converts words to a phonetic representation,
 * allowing comparison of words that sound similar but are spelled differently.
 */
class MetaphoneTest extends AbstractStringEncoderTest<Metaphone> {

    // Constants for better readability
    private static final String EXPECTED_SCIENCE_CODE = "SNS";
    private static final String EXPECTED_SCENE_CODE = "SN";
    private static final String EXPECTED_SCY_CODE = "S";
    private static final String EXPECTED_GNU_CODE = "N";
    private static final String EXPECTED_SIGNED_CODE = "SNT";
    private static final String EXPECTED_GHENT_CODE = "KNT";
    private static final String EXPECTED_BAUGH_CODE = "B";
    private static final String EXPECTED_AXEAXE_CODE = "AKSK"; // Truncated by max length
    private static final String EXPECTED_WHY_CODE = ""; // Special case - returns empty string

    @Override
    protected Metaphone createStringEncoder() {
        return new Metaphone();
    }

    // ========== Basic Metaphone Encoding Tests ==========

    @Test
    void testBasicMetaphoneEncoding() {
        // Test common English words to verify basic Metaphone functionality
        assertEquals("HL", getStringEncoder().metaphone("howl"));
        assertEquals("TSTN", getStringEncoder().metaphone("testing"));
        assertEquals("0", getStringEncoder().metaphone("The"));
        assertEquals("KK", getStringEncoder().metaphone("quick"));
        assertEquals("BRN", getStringEncoder().metaphone("brown"));
        assertEquals("FKS", getStringEncoder().metaphone("fox"));
        assertEquals("JMPT", getStringEncoder().metaphone("jumped"));
        assertEquals("OFR", getStringEncoder().metaphone("over"));
        assertEquals("0", getStringEncoder().metaphone("the"));
        assertEquals("LS", getStringEncoder().metaphone("lazy"));
        assertEquals("TKS", getStringEncoder().metaphone("dogs"));
    }

    // ========== Special Character Combination Tests ==========

    @Test
    void testScienceRelatedWords_DiscardSCEOrSCIOrSCY() {
        // Test that SC followed by E, I, or Y is handled correctly
        assertEquals(EXPECTED_SCIENCE_CODE, getStringEncoder().metaphone("SCIENCE"));
        assertEquals(EXPECTED_SCENE_CODE, getStringEncoder().metaphone("SCENE"));
        assertEquals(EXPECTED_SCY_CODE, getStringEncoder().metaphone("SCY"));
    }

    @Test
    void testSilentGNHandling() {
        // Test handling of silent GN at word beginning
        assertEquals(EXPECTED_GNU_CODE, getStringEncoder().metaphone("GNU"));
        
        // Test GN in middle of word (not silent)
        assertEquals(EXPECTED_SIGNED_CODE, getStringEncoder().metaphone("SIGNED"));
    }

    @Test
    void testSilentHAfterG() {
        // Test that H after G is handled as silent
        assertEquals(EXPECTED_GHENT_CODE, getStringEncoder().metaphone("GHENT"));
        assertEquals(EXPECTED_BAUGH_CODE, getStringEncoder().metaphone("BAUGH"));
    }

    @Test
    void testPHTransformation() {
        // Test that PH is converted to F sound
        assertEquals("FX", getStringEncoder().metaphone("PHISH"));
    }

    @Test
    void testTCHTransformation() {
        // Test that TCH is converted to X sound
        assertEquals("RX", getStringEncoder().metaphone("RETCH"));
        assertEquals("WX", getStringEncoder().metaphone("WATCH"));
    }

    @Test
    void testSHAndSIOAndSIATransformations() {
        // Test various S combinations that become X sound
        assertEquals("XT", getStringEncoder().metaphone("SHOT"));
        assertEquals("OTXN", getStringEncoder().metaphone("ODSIAN"));
        assertEquals("PLXN", getStringEncoder().metaphone("PULSION"));
    }

    @Test
    void testTIOAndTIATransformations() {
        // Test TI combinations that become X sound
        assertEquals("OX", getStringEncoder().metaphone("OTIA"));
        assertEquals("PRXN", getStringEncoder().metaphone("PORTION"));
    }

    @Test
    void testSCHAndCHTransformations() {
        // Test SCH and CH combinations
        assertEquals("SKTL", getStringEncoder().metaphone("SCHEDULE"));
        assertEquals("SKMT", getStringEncoder().metaphone("SCHEMATIC"));
        assertEquals("KRKT", getStringEncoder().metaphone("CHARACTER"));
        assertEquals("TX", getStringEncoder().metaphone("TEACH"));
    }

    @Test
    void testDGEOrDGIOrDGYTransformations() {
        // Test DG combinations that become J sound
        assertEquals("TJ", getStringEncoder().metaphone("DODGY"));
        assertEquals("TJ", getStringEncoder().metaphone("DODGE"));
        assertEquals("AJMT", getStringEncoder().metaphone("ADGIEMTI"));
    }

    @Test
    void testWordEndingInMB() {
        // Test that final B in MB combination is silent
        assertEquals("KM", getStringEncoder().metaphone("COMB"));
        assertEquals("TM", getStringEncoder().metaphone("TOMB"));
        assertEquals("WM", getStringEncoder().metaphone("WOMB"));
    }

    @Test
    void testWordsWithCIA() {
        // Test CIA combination handling
        assertEquals("XP", getStringEncoder().metaphone("CIAPO"));
    }

    // ========== Edge Cases and Special Behaviors ==========

    @Test
    void testWhyReturnsEmptyString() {
        // Special case: "WHY" returns empty string (CODEC-57)
        // This differs from PHP implementation which returns "H"
        assertEquals(EXPECTED_WHY_CODE, getStringEncoder().metaphone("WHY"));
    }

    @Test
    void testMaxLengthTruncation() {
        // Test that codes are truncated to maximum length (default 4)
        assertEquals(EXPECTED_AXEAXE_CODE, getStringEncoder().metaphone("AXEAXE"));
    }

    @Test
    void testCustomMaxLengthWithTruncation() {
        // Test setting custom max length
        getStringEncoder().setMaxCodeLen(6);
        assertEquals("AKSKSK", getStringEncoder().metaphone("AXEAXEAXE"));
    }

    // ========== Metaphone Equality Tests ==========

    @Test
    void testBasicMetaphoneEquality() {
        String[][] testPairs = {
            { "Case", "case" },     // Case insensitive
            { "CASE", "Case" },     // Mixed case
            { "caSe", "cAsE" },     // Random case
            { "quick", "cookie" }   // Phonetically similar
        };
        
        assertAllPairsAreMetaphoneEqual(testPairs);
    }

    @Test
    void testComputedMetaphoneMatches() {
        // Test data computed from http://www.lanw.com/java/phonetic/default.htm
        String[][] testPairs = {
            { "Lawrence", "Lorenza" },
            { "Gary", "Cahra" }
        };
        
        assertAllPairsAreMetaphoneEqual(testPairs);
    }

    // ========== Name-Based Metaphone Equality Tests ==========
    // These tests verify that names with similar pronunciations are correctly identified as equivalent

    @Test
    void testAeroEquivalents() {
        // Initial AE case
        assertWordMatchesAllVariants("Aero", new String[] { "Eure" });
    }

    @Test
    void testAlbertEquivalents() {
        // Initial A, not followed by E
        assertWordMatchesAllVariants("Albert", 
            new String[] { "Ailbert", "Alberik", "Albert", "Alberto", "Albrecht" });
    }

    @Test
    void testGaryEquivalents() {
        assertWordMatchesAllVariants("Gary",
            new String[] { "Cahra", "Cara", "Carey", "Cari", "Caria", "Carie", "Caro", "Carree", 
                          "Carri", "Carrie", "Carry", "Cary", "Cora", "Corey", "Cori", "Corie", 
                          "Correy", "Corri", "Corrie", "Corry", "Cory", "Gray", "Kara", "Kare", 
                          "Karee", "Kari", "Karia", "Karie", "Karrah", "Karrie", "Karry", "Kary", 
                          "Keri", "Kerri", "Kerrie", "Kerry", "Kira", "Kiri", "Kora", "Kore", 
                          "Kori", "Korie", "Korrie", "Korry" });
    }

    @Test
    void testJohnEquivalents() {
        assertWordMatchesAllVariants("John",
            new String[] { "Gena", "Gene", "Genia", "Genna", "Genni", "Gennie", "Genny", "Giana", 
                          "Gianna", "Gina", "Ginni", "Ginnie", "Ginny", "Jaine", "Jan", "Jana", 
                          "Jane", "Janey", "Jania", "Janie", "Janna", "Jany", "Jayne", "Jean", 
                          "Jeana", "Jeane", "Jeanie", "Jeanna", "Jeanne", "Jeannie", "Jen", "Jena", 
                          "Jeni", "Jenn", "Jenna", "Jennee", "Jenni", "Jennie", "Jenny", "Jinny", 
                          "Jo Ann", "Jo-Ann", "Jo-Anne", "Joan", "Joana", "Joane", "Joanie", "Joann", 
                          "Joanna", "Joanne", "Joeann", "Johna", "Johnna", "Joni", "Jonie", "Juana", 
                          "June", "Junia", "Junie" });
    }

    @Test
    void testKnightEquivalents() {
        // Initial KN case
        assertWordMatchesAllVariants("Knight", 
            new String[] { "Hynda", "Nada", "Nadia", "Nady", "Nat", "Nata", "Natty", "Neda", 
                          "Nedda", "Nedi", "Netta", "Netti", "Nettie", "Netty", "Nita", "Nydia" });
    }

    @Test
    void testMaryEquivalents() {
        assertWordMatchesAllVariants("Mary", 
            new String[] { "Mair", "Maire", "Mara", "Mareah", "Mari", "Maria", "Marie", "Mary", 
                          "Maura", "Maure", "Meara", "Merrie", "Merry", "Mira", "Moira", "Mora", 
                          "Moria", "Moyra", "Muire", "Myra", "Myrah" });
    }

    @Test
    void testParisEquivalents() {
        assertWordMatchesAllVariants("Paris", 
            new String[] { "Pearcy", "Perris", "Piercy", "Pierz", "Pryse" });
    }

    @Test
    void testPeterEquivalents() {
        assertWordMatchesAllVariants("Peter", 
            new String[] { "Peadar", "Peder", "Pedro", "Peter", "Petr", "Peyter", "Pieter", 
                          "Pietro", "Piotr" });
    }

    @Test
    void testRayEquivalents() {
        assertWordMatchesAllVariants("Ray", 
            new String[] { "Ray", "Rey", "Roi", "Roy", "Ruy" });
    }

    @Test
    void testSusanEquivalents() {
        assertWordMatchesAllVariants("Susan",
            new String[] { "Siusan", "Sosanna", "Susan", "Susana", "Susann", "Susanna", 
                          "Susannah", "Susanne", "Suzann", "Suzanna", "Suzanne", "Zuzana" });
    }

    @Test
    void testWhiteEquivalents() {
        // Initial WH case
        assertWordMatchesAllVariants("White",
            new String[] { "Wade", "Wait", "Waite", "Wat", "Whit", "Wiatt", "Wit", "Wittie", 
                          "Witty", "Wood", "Woodie", "Woody" });
    }

    @Test
    void testWrightEquivalents() {
        // Initial WR case
        assertWordMatchesAllVariants("Wright", 
            new String[] { "Rota", "Rudd", "Ryde" });
    }

    @Test
    void testXalanEquivalents() {
        assertWordMatchesAllVariants("Xalan", 
            new String[] { "Celene", "Celina", "Celine", "Selena", "Selene", "Selina", 
                          "Seline", "Suellen", "Xylina" });
    }

    // ========== Helper Methods ==========

    /**
     * Asserts that a source word has the same Metaphone code as all provided variants.
     * Also verifies that all variants have the same Metaphone code as each other.
     * 
     * @param sourceWord the primary word to test
     * @param variants array of words that should have the same Metaphone code
     */
    private void assertWordMatchesAllVariants(final String sourceWord, final String[] variants) {
        // Verify source matches all variants
        for (final String variant : variants) {
            assertTrue(getStringEncoder().isMetaphoneEqual(sourceWord, variant), 
                String.format("'%s' should have same Metaphone code as '%s'", sourceWord, variant));
        }
        
        // Verify all variants match each other
        for (final String variant1 : variants) {
            for (final String variant2 : variants) {
                assertTrue(getStringEncoder().isMetaphoneEqual(variant1, variant2),
                    String.format("Variants '%s' and '%s' should have same Metaphone code", variant1, variant2));
            }
        }
    }

    /**
     * Asserts that all word pairs in the provided array have equal Metaphone codes.
     * Tests both directions of equality (A equals B and B equals A).
     * 
     * @param wordPairs 2D array where each inner array contains exactly 2 words to compare
     */
    private void assertAllPairsAreMetaphoneEqual(final String[][] wordPairs) {
        validateTestData(wordPairs);
        
        for (final String[] pair : wordPairs) {
            final String word1 = pair[0];
            final String word2 = pair[1];
            final String errorMessage = String.format("Expected Metaphone match between '%s' and '%s'", word1, word2);
            
            assertTrue(getStringEncoder().isMetaphoneEqual(word1, word2), errorMessage);
            assertTrue(getStringEncoder().isMetaphoneEqual(word2, word1), errorMessage);
        }
    }

    /**
     * Validates that test data is properly formatted.
     * 
     * @param pairs 2D array that should contain pairs of strings
     * @throws AssertionError if data is invalid
     */
    private void validateTestData(final String[][] pairs) {
        if (pairs.length == 0) {
            fail("Test data array cannot be empty");
        }
        
        for (int i = 0; i < pairs.length; i++) {
            if (pairs[i].length != 2) {
                fail(String.format("Test data error: pair at index %d must contain exactly 2 elements, but contains %d", 
                    i, pairs[i].length));
            }
        }
    }
}