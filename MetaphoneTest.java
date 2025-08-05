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

import java.util.stream.Stream;
import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Metaphone}.
 */
class MetaphoneTest extends AbstractStringEncoderTest<Metaphone> {

    @Override
    protected Metaphone createStringEncoder() {
        return new Metaphone();
    }

    // ==================== TEST METAPHONE EQUALITY FOR WORD PAIRS ====================

    @ParameterizedTest
    @MethodSource("provideBasicEqualityPairs")
    void testMetaphoneEquality_BasicPairs(String word1, String word2) {
        assertTrue(getStringEncoder().isMetaphoneEqual(word1, word2),
            () -> String.format("Words should be metaphone-equal: '%s' and '%s'", word1, word2));
    }

    private static Stream<Arguments> provideBasicEqualityPairs() {
        return Stream.of(
            Arguments.of("Case", "case"),
            Arguments.of("CASE", "Case"),
            Arguments.of("caSe", "cAsE"),
            Arguments.of("quick", "cookie")
        );
    }

    @ParameterizedTest
    @MethodSource("provideNameEqualityPairs")
    void testMetaphoneEquality_NamePairs(String word1, String word2) {
        assertTrue(getStringEncoder().isMetaphoneEqual(word1, word2),
            () -> String.format("Names should be metaphone-equal: '%s' and '%s'", word1, word2));
    }

    private static Stream<Arguments> provideNameEqualityPairs() {
        return Stream.of(
            Arguments.of("Lawrence", "Lorenza"),
            Arguments.of("Gary", "Cahra")
        );
    }

    // ==================== TEST METAPHONE EQUALITY FOR WORD GROUPS ====================

    @ParameterizedTest
    @ValueSource(strings = {"Eure"})
    void testMetaphoneEquality_Aero(String match) {
        assertTrue(getStringEncoder().isMetaphoneEqual("Aero", match),
            () -> String.format("'Aero' should match '%s'", match));
    }

    @ParameterizedTest
    @MethodSource("provideAlbertMatches")
    void testMetaphoneEquality_Albert(String match) {
        assertTrue(getStringEncoder().isMetaphoneEqual("Albert", match),
            () -> String.format("'Albert' should match '%s'", match));
    }

    private static Stream<String> provideAlbertMatches() {
        return Stream.of("Ailbert", "Alberik", "Albert", "Alberto", "Albrecht");
    }

    @ParameterizedTest
    @MethodSource("provideGaryMatches")
    void testMetaphoneEquality_Gary(String match) {
        assertTrue(getStringEncoder().isMetaphoneEqual("Gary", match),
            () -> String.format("'Gary' should match '%s'", match));
    }

    private static Stream<String> provideGaryMatches() {
        return Stream.of(
            "Cahra", "Cara", "Carey", "Cari", "Caria", "Carie", "Caro", "Carree", "Carri", "Carrie", "Carry", "Cary", 
            "Cora", "Corey", "Cori", "Corie", "Correy", "Corri", "Corrie", "Corry", "Cory", "Gray", "Kara", "Kare", 
            "Karee", "Kari", "Karia", "Karie", "Karrah", "Karrie", "Karry", "Kary", "Keri", "Kerri", "Kerrie", "Kerry", 
            "Kira", "Kiri", "Kora", "Kore", "Kori", "Korie", "Korrie", "Korry"
        );
    }

    @ParameterizedTest
    @MethodSource("provideJohnMatches")
    void testMetaphoneEquality_John(String match) {
        assertTrue(getStringEncoder().isMetaphoneEqual("John", match),
            () -> String.format("'John' should match '%s'", match));
    }

    private static Stream<String> provideJohnMatches() {
        return Stream.of(
            "Gena", "Gene", "Genia", "Genna", "Genni", "Gennie", "Genny", "Giana", "Gianna", "Gina", "Ginni", "Ginnie", 
            "Ginny", "Jaine", "Jan", "Jana", "Jane", "Janey", "Jania", "Janie", "Janna", "Jany", "Jayne", "Jean", 
            "Jeana", "Jeane", "Jeanie", "Jeanna", "Jeanne", "Jeannie", "Jen", "Jena", "Jeni", "Jenn", "Jenna", "Jennee", 
            "Jenni", "Jennie", "Jenny", "Jinny", "Jo Ann", "Jo-Ann", "Jo-Anne", "Joan", "Joana", "Joane", "Joanie", 
            "Joann", "Joanna", "Joanne", "Joeann", "Johna", "Johnna", "Joni", "Jonie", "Juana", "June", "Junia", "Junie"
        );
    }

    @ParameterizedTest
    @MethodSource("provideKnightMatches")
    void testMetaphoneEquality_Knight(String match) {
        assertTrue(getStringEncoder().isMetaphoneEqual("Knight", match),
            () -> String.format("'Knight' should match '%s'", match));
    }

    private static Stream<String> provideKnightMatches() {
        return Stream.of(
            "Hynda", "Nada", "Nadia", "Nady", "Nat", "Nata", "Natty", "Neda", "Nedda", "Nedi", "Netta", "Netti", 
            "Nettie", "Netty", "Nita", "Nydia"
        );
    }

    @ParameterizedTest
    @MethodSource("provideMaryMatches")
    void testMetaphoneEquality_Mary(String match) {
        assertTrue(getStringEncoder().isMetaphoneEqual("Mary", match),
            () -> String.format("'Mary' should match '%s'", match));
    }

    private static Stream<String> provideMaryMatches() {
        return Stream.of(
            "Mair", "Maire", "Mara", "Mareah", "Mari", "Maria", "Marie", "Mary", "Maura", "Maure", "Meara", "Merrie", 
            "Merry", "Mira", "Moira", "Mora", "Moria", "Moyra", "Muire", "Myra", "Myrah"
        );
    }

    @ParameterizedTest
    @MethodSource("provideParisMatches")
    void testMetaphoneEquality_Paris(String match) {
        assertTrue(getStringEncoder().isMetaphoneEqual("Paris", match),
            () -> String.format("'Paris' should match '%s'", match));
    }

    private static Stream<String> provideParisMatches() {
        return Stream.of("Pearcy", "Perris", "Piercy", "Pierz", "Pryse");
    }

    @ParameterizedTest
    @MethodSource("providePeterMatches")
    void testMetaphoneEquality_Peter(String match) {
        assertTrue(getStringEncoder().isMetaphoneEqual("Peter", match),
            () -> String.format("'Peter' should match '%s'", match));
    }

    private static Stream<String> providePeterMatches() {
        return Stream.of("Peadar", "Peder", "Pedro", "Peter", "Petr", "Peyter", "Pieter", "Pietro", "Piotr");
    }

    @ParameterizedTest
    @MethodSource("provideRayMatches")
    void testMetaphoneEquality_Ray(String match) {
        assertTrue(getStringEncoder().isMetaphoneEqual("Ray", match),
            () -> String.format("'Ray' should match '%s'", match));
    }

    private static Stream<String> provideRayMatches() {
        return Stream.of("Ray", "Rey", "Roi", "Roy", "Ruy");
    }

    @ParameterizedTest
    @MethodSource("provideSusanMatches")
    void testMetaphoneEquality_Susan(String match) {
        assertTrue(getStringEncoder().isMetaphoneEqual("Susan", match),
            () -> String.format("'Susan' should match '%s'", match));
    }

    private static Stream<String> provideSusanMatches() {
        return Stream.of(
            "Siusan", "Sosanna", "Susan", "Susana", "Susann", "Susanna", "Susannah", "Susanne", "Suzann", "Suzanna", 
            "Suzanne", "Zuzana"
        );
    }

    @ParameterizedTest
    @MethodSource("provideWhiteMatches")
    void testMetaphoneEquality_White(String match) {
        assertTrue(getStringEncoder().isMetaphoneEqual("White", match),
            () -> String.format("'White' should match '%s'", match));
    }

    private static Stream<String> provideWhiteMatches() {
        return Stream.of(
            "Wade", "Wait", "Waite", "Wat", "Whit", "Wiatt", "Wit", "Wittie", "Witty", "Wood", "Woodie", "Woody"
        );
    }

    @ParameterizedTest
    @MethodSource("provideWrightMatches")
    void testMetaphoneEquality_Wright(String match) {
        assertTrue(getStringEncoder().isMetaphoneEqual("Wright", match),
            () -> String.format("'Wright' should match '%s'", match));
    }

    private static Stream<String> provideWrightMatches() {
        return Stream.of("Rota", "Rudd", "Ryde");
    }

    @ParameterizedTest
    @MethodSource("provideXalanMatches")
    void testMetaphoneEquality_Xalan(String match) {
        assertTrue(getStringEncoder().isMetaphoneEqual("Xalan", match),
            () -> String.format("'Xalan' should match '%s'", match));
    }

    private static Stream<String> provideXalanMatches() {
        return Stream.of(
            "Celene", "Celina", "Celine", "Selena", "Selene", "Selina", "Seline", "Suellen", "Xylina"
        );
    }

    // ==================== TEST SPECIFIC METAPHONE RULES ====================

    @ParameterizedTest
    @MethodSource
    void testDiscardOfSCEOrSCIOrSCY(String word, String expected) {
        assertEquals(expected, getStringEncoder().metaphone(word));
    }

    private static Stream<Arguments> testDiscardOfSCEOrSCIOrSCY() {
        return Stream.of(
            Arguments.of("SCIENCE", "SNS"),
            Arguments.of("SCENE", "SN"),
            Arguments.of("SCY", "S")
        );
    }

    @ParameterizedTest
    @MethodSource
    void testDiscardOfSilentGN(String word, String expected) {
        assertEquals(expected, getStringEncoder().metaphone(word));
    }

    private static Stream<Arguments> testDiscardOfSilentGN() {
        return Stream.of(
            Arguments.of("GNU", "N"),        // GN at start
            Arguments.of("SIGNED", "SNT")    // GN in middle
        );
    }

    @ParameterizedTest
    @MethodSource
    void testDiscardOfSilentHAfterG(String word, String expected) {
        assertEquals(expected, getStringEncoder().metaphone(word));
    }

    private static Stream<Arguments> testDiscardOfSilentHAfterG() {
        return Stream.of(
            Arguments.of("GHENT", "KNT"),
            Arguments.of("BAUGH", "B")
        );
    }

    @Test
    void testExceedLength() {
        // Should be AKSKS but gets truncated by max code length (default=4)
        assertEquals("AKSK", getStringEncoder().metaphone("AXEAXE"));
    }

    @ParameterizedTest
    @MethodSource
    void testSHAndSIOAndSIAToX(String word, String expected) {
        assertEquals(expected, getStringEncoder().metaphone(word));
    }

    private static Stream<Arguments> testSHAndSIOAndSIAToX() {
        return Stream.of(
            Arguments.of("SHOT", "XT"),
            Arguments.of("ODSIAN", "OTXN"),
            Arguments.of("PULSION", "PLXN")
        );
    }

    @ParameterizedTest
    @MethodSource
    void testTCH(String word, String expected) {
        assertEquals(expected, getStringEncoder().metaphone(word));
    }

    private static Stream<Arguments> testTCH() {
        return Stream.of(
            Arguments.of("RETCH", "RX"),
            Arguments.of("WATCH", "WX")
        );
    }

    @ParameterizedTest
    @MethodSource
    void testTIOAndTIAToX(String word, String expected) {
        assertEquals(expected, getStringEncoder().metaphone(word));
    }

    private static Stream<Arguments> testTIOAndTIAToX() {
        return Stream.of(
            Arguments.of("OTIA", "OX"),
            Arguments.of("PORTION", "PRXN")
        );
    }

    @ParameterizedTest
    @MethodSource
    void testTranslateOfSCHAndCH(String word, String expected) {
        assertEquals(expected, getStringEncoder().metaphone(word));
    }

    private static Stream<Arguments> testTranslateOfSCHAndCH() {
        return Stream.of(
            Arguments.of("SCHEDULE", "SKTL"),
            Arguments.of("SCHEMATIC", "SKMT"),
            Arguments.of("CHARACTER", "KRKT"),
            Arguments.of("TEACH", "TX")
        );
    }

    @ParameterizedTest
    @MethodSource
    void testTranslateToJOfDGEOrDGIOrDGY(String word, String expected) {
        assertEquals(expected, getStringEncoder().metaphone(word));
    }

    private static Stream<Arguments> testTranslateToJOfDGEOrDGIOrDGY() {
        return Stream.of(
            Arguments.of("DODGY", "TJ"),
            Arguments.of("DODGE", "TJ"),
            Arguments.of("ADGIEMTI", "AJMT")
        );
    }

    @ParameterizedTest
    @MethodSource
    void testWordEndingInMB(String word, String expected) {
        assertEquals(expected, getStringEncoder().metaphone(word));
    }

    private static Stream<Arguments> testWordEndingInMB() {
        return Stream.of(
            Arguments.of("COMB", "KM"),
            Arguments.of("TOMB", "TM"),
            Arguments.of("WOMB", "WM")
        );
    }

    @Test
    void testWordsWithCIA() {
        assertEquals("XP", getStringEncoder().metaphone("CIAPO"));
    }

    @Test
    void testPHTOF() {
        assertEquals("FX", getStringEncoder().metaphone("PHISH"));
    }

    @Test
    void testSetMaxLengthWithTruncation() {
        getStringEncoder().setMaxCodeLen(6);
        // Should be AKSKSK but gets truncated to max length (6)
        assertEquals("AKSKSK", getStringEncoder().metaphone("AXEAXEAXE"));
    }

    @ParameterizedTest
    @MethodSource
    void testMetaphoneBasicWords(String word, String expected) {
        assertEquals(expected, getStringEncoder().metaphone(word));
    }

    private static Stream<Arguments> testMetaphoneBasicWords() {
        return Stream.of(
            Arguments.of("howl", "HL"),
            Arguments.of("testing", "TSTN"),
            Arguments.of("The", "0"),
            Arguments.of("quick", "KK"),
            Arguments.of("brown", "BRN"),
            Arguments.of("fox", "FKS"),
            Arguments.of("jumped", "JMPT"),
            Arguments.of("over", "OFR"),
            Arguments.of("the", "0"),
            Arguments.of("lazy", "LS"),
            Arguments.of("dogs", "TKS")
        );
    }

    @Test
    void testWhy() {
        /*
         * Original Metaphone returns empty string for WHY.
         * Note: PHP version returns "H" but that's a different implementation.
         */
        assertEquals("", getStringEncoder().metaphone("WHY"));
    }
}