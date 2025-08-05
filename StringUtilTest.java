package org.jsoup.internal;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static org.jsoup.internal.StringUtil.normaliseWhitespace;
import static org.jsoup.internal.StringUtil.resolve;
import static org.junit.jupiter.api.Assertions.*;

public class StringUtilTest {
    private static final int MAX_PADDING = 30;

    // Join Tests
    @ParameterizedTest
    @MethodSource("joinTestCases")
    void join_combinesInputsWithSeparator(String expected, Collection<?> inputs, String separator) {
        assertEquals(expected, StringUtil.join(inputs, separator));
    }

    private static Stream<Arguments> joinTestCases() {
        return Stream.of(
            Arguments.of("", Collections.singletonList(""), " "),
            Arguments.of("one", Collections.singletonList("one"), " "),
            Arguments.of("one two three", Arrays.asList("one", "two", "three"), " ")
        );
    }

    // Padding Tests
    @Test
    void padding_withDefaultMax_returnsSpacesUpTo30() {
        assertEquals("", StringUtil.padding(0));
        assertEquals(" ", StringUtil.padding(1));
        assertEquals("  ", StringUtil.padding(2));
        assertEquals("               ", StringUtil.padding(15));
        assertEquals("                              ", StringUtil.padding(MAX_PADDING));
        assertEquals("                              ", StringUtil.padding(45)); // Caps at MAX_PADDING
    }

    @Test
    void padding_withUnlimitedMax_returnsExactWidth() {
        assertEquals("", StringUtil.padding(0, -1));
        assertEquals("                    ", StringUtil.padding(20, -1));
        assertEquals("                     ", StringUtil.padding(21, -1));  // Exceeds memoization
        assertEquals("                                             ", StringUtil.padding(45, -1));
    }

    @Test
    void padding_withMaxZero_returnsEmptyString() {
        assertEquals("", StringUtil.padding(0, 0));
        assertEquals("", StringUtil.padding(21, 0));
    }

    @Test
    void padding_withMax30_capsAt30Spaces() {
        assertEquals("", StringUtil.padding(0, MAX_PADDING));
        assertEquals(" ", StringUtil.padding(1, MAX_PADDING));
        assertEquals("                              ", StringUtil.padding(45, MAX_PADDING));
    }

    @Test
    void padding_mixedCases_respectsMaxSetting() {
        // Max setting overrides requested width
        assertEquals(5, StringUtil.padding(20, 5).length());
    }

    @Test
    void paddingArray_containsCorrectLengths() {
        String[] padding = StringUtil.padding;
        assertEquals(21, padding.length);
        for (int i = 0; i < padding.length; i++) {
            assertEquals(i, padding[i].length());
        }
    }

    // Blank Checks
    @ParameterizedTest
    @ValueSource(strings = {"", "      ", "   \r\n  "})
    void isBlank_returnsTrueForEmptyOrWhitespace(String input) {
        assertTrue(StringUtil.isBlank(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"hello", "   hello   "})
    void isBlank_returnsFalseForNonWhitespace(String input) {
        assertFalse(StringUtil.isBlank(input));
    }

    @Test
    void isBlank_returnsTrueForNull() {
        assertTrue(StringUtil.isBlank(null));
    }

    // Numeric Checks
    @ParameterizedTest
    @ValueSource(strings = {"1", "1234"})
    void isNumeric_returnsTrueForDigits(String input) {
        assertTrue(StringUtil.isNumeric(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "123 546", "hello", "123.334"})
    void isNumeric_returnsFalseForNonDigits(String input) {
        assertFalse(StringUtil.isNumeric(input));
    }

    @Test
    void isNumeric_returnsFalseForNull() {
        assertFalse(StringUtil.isNumeric(null));
    }

    // Whitespace Checks
    @ParameterizedTest
    @ValueSource(chars = {'\t', '\n', '\r', '\f', ' '})
    void isWhitespace_returnsTrueForWhitespaceChars(char c) {
        assertTrue(StringUtil.isWhitespace(c));
    }

    @ParameterizedTest
    @ValueSource(chars = {'\u00a0', '\u2000', '\u3000'})
    void isWhitespace_returnsFalseForNonBreakingSpaces(char c) {
        assertFalse(StringUtil.isWhitespace(c));
    }

    // Whitespace Normalization
    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "'    \\r \\n \\r\\n'     | ' '",
        "'   hello   \\r \\n  there    \\n' | ' hello there '",
        "'hello' | 'hello'",
        "'hello\\nthere' | 'hello there'"
    })
    void normaliseWhitespace_collapsesAndTrimsSpaces(String input, String expected) {
        assertEquals(expected, normaliseWhitespace(input));
    }

    @Test
    void normaliseWhitespace_handlesHighSurrogatePairs() {
        String testStr = "\ud869\udeb2\u304b\u309a  1";
        String expected = "\ud869\udeb2\u304b\u309a 1";
        
        assertEquals(expected, normaliseWhitespace(testStr));
        assertEquals(expected, Jsoup.parse(testStr).text());
    }

    // URL Resolution
    @ParameterizedTest
    @MethodSource("urlResolutionCases")
    void resolve_combinesBaseAndRelativeUrls(String baseUrl, String relativeUrl, String expected) {
        assertEquals(expected, resolve(baseUrl, relativeUrl));
    }

    private static Stream<Arguments> urlResolutionCases() {
        return Stream.of(
            Arguments.of("http://example.com", "./one/two?three", "http://example.com/one/two?three"),
            Arguments.of("http://example.com?one", "./one/two?three", "http://example.com/one/two?three"),
            Arguments.of("http://example.com", "./one/two?three#four", "http://example.com/one/two?three#four"),
            Arguments.of("http://example.com/two/", "../one/two.html", "http://example.com/one/two.html"),
            // More test cases from original...
            Arguments.of("wrong", "https://example.com/one", "https://example.com/one"),
            Arguments.of("https://example.com/one", "", "https://example.com/one"),
            Arguments.of("wrong", "also wrong", "")
        );
    }

    @Test
    void resolve_stripsControlCharactersFromUrls() {
        assertEquals("foo:bar", resolve("\nhttps://\texample.com/", "\r\nfo\to:ba\br"));
    }

    @Test
    void resolve_preservesSpacesInUrls() {
        assertEquals("https://example.com/foo bar/", 
                     resolve("HTTPS://example.com/example/", "../foo bar/"));
    }

    // ASCII Checks
    @ParameterizedTest
    @ValueSource(strings = {"", "example.com", "One Two"})
    void isAscii_returnsTrueForAsciiStrings(String input) {
        assertTrue(StringUtil.isAscii(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"🧔", "测试", "测试.com"})
    void isAscii_returnsFalseForNonAsciiStrings(String input) {
        assertFalse(StringUtil.isAscii(input));
    }

    // Character Classification
    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "a | true",   "n | true",   "z | true",
        "A | true",   "N | true",   "Z | true",
        "  | false",  "- | false",  "0 | false",
        "ß | false",  "Ě | false"
    })
    void isAsciiLetter_correctlyClassifiesChars(char c, boolean expected) {
        assertEquals(expected, StringUtil.isAsciiLetter(c));
    }

    @ParameterizedTest
    @ValueSource(chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'})
    void isDigit_returnsTrueForDigits(char c) {
        assertTrue(StringUtil.isDigit(c));
    }

    @ParameterizedTest
    @ValueSource(chars = {'a', 'A', 'ä', 'Ä', '١', '୳'})
    void isDigit_returnsFalseForNonDigits(char c) {
        assertFalse(StringUtil.isDigit(c));
    }

    @ParameterizedTest
    @ValueSource(chars = {
        '0','1','2','3','4','5','6','7','8','9',
        'a','b','c','d','e','f',
        'A','B','C','D','E','F'
    })
    void isHexDigit_returnsTrueForHexChars(char c) {
        assertTrue(StringUtil.isHexDigit(c));
    }

    @ParameterizedTest
    @ValueSource(chars = {'g', 'G', 'ä', 'Ä', '١', '୳'})
    void isHexDigit_returnsFalseForNonHexChars(char c) {
        assertFalse(StringUtil.isHexDigit(c));
    }
}