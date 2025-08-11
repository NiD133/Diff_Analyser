package org.jsoup.internal;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.jsoup.internal.StringUtil.normaliseWhitespace;
import static org.jsoup.internal.StringUtil.resolve;
import static org.junit.jupiter.api.Assertions.*;

public class StringUtilTest {

    @Test
    public void join_shouldConcatenateStringsWithSeparator() {
        // Empty string case
        assertEquals("", StringUtil.join(Collections.singletonList(""), " "));
        
        // Single element case
        assertEquals("one", StringUtil.join(Collections.singletonList("one"), " "));
        
        // Multiple elements case
        assertEquals("one two three", StringUtil.join(Arrays.asList("one", "two", "three"), " "));
    }

    @Test 
    public void padding_shouldReturnCorrectSpaceStrings() {
        // Basic padding tests
        assertEquals("", StringUtil.padding(0));
        assertEquals(" ", StringUtil.padding(1));
        assertEquals("  ", StringUtil.padding(2));
        assertEquals("               ", StringUtil.padding(15));
        
        // Default max padding is 30, so 45 should be capped at 30
        assertEquals("                              ", StringUtil.padding(45));
    }

    @Test
    public void padding_withCustomMaxWidth_shouldRespectLimits() {
        // Unlimited padding (-1 max width)
        assertEquals("", StringUtil.padding(0, -1));
        assertEquals("                    ", StringUtil.padding(20, -1)); // within memoization range
        assertEquals("                     ", StringUtil.padding(21, -1)); // beyond memoization
        assertEquals("                              ", StringUtil.padding(30, -1));
        assertEquals("                                             ", StringUtil.padding(45, -1));

        // Zero max width
        assertEquals("", StringUtil.padding(0, 0));
        assertEquals("", StringUtil.padding(21, 0)); // should return empty due to zero max

        // Custom max width of 30
        assertEquals("", StringUtil.padding(0, 30));
        assertEquals(" ", StringUtil.padding(1, 30));
        assertEquals("  ", StringUtil.padding(2, 30));
        assertEquals("               ", StringUtil.padding(15, 30));
        assertEquals("                              ", StringUtil.padding(45, 30)); // capped at 30

        // Verify max applies to memoized values too
        assertEquals(5, StringUtil.padding(20, 5).length());
    }

    @Test 
    public void paddingArray_shouldContainPrecomputedPaddingStrings() {
        String[] paddingArray = StringUtil.padding;
        
        assertEquals(21, paddingArray.length); // memoized up to 20 spaces (plus empty string at index 0)
        
        // Each index should contain a string with that many spaces
        for (int i = 0; i < paddingArray.length; i++) {
            assertEquals(i, paddingArray[i].length());
        }
    }

    @Test 
    public void isBlank_shouldIdentifyBlankStrings() {
        // Blank cases
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank("      "));
        assertTrue(StringUtil.isBlank("   \r\n  "));

        // Non-blank cases
        assertFalse(StringUtil.isBlank("hello"));
        assertFalse(StringUtil.isBlank("   hello   "));
    }

    @Test 
    public void isNumeric_shouldIdentifyNumericStrings() {
        // Non-numeric cases
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric(" "));
        assertFalse(StringUtil.isNumeric("123 546")); // contains space
        assertFalse(StringUtil.isNumeric("hello"));
        assertFalse(StringUtil.isNumeric("123.334")); // contains decimal point

        // Numeric cases
        assertTrue(StringUtil.isNumeric("1"));
        assertTrue(StringUtil.isNumeric("1234"));
    }

    @Test 
    public void isWhitespace_shouldIdentifyHtmlWhitespaceCharacters() {
        // HTML whitespace characters
        assertTrue(StringUtil.isWhitespace('\t')); // tab
        assertTrue(StringUtil.isWhitespace('\n')); // newline
        assertTrue(StringUtil.isWhitespace('\r')); // carriage return
        assertTrue(StringUtil.isWhitespace('\f')); // form feed
        assertTrue(StringUtil.isWhitespace(' '));  // space

        // Unicode whitespace characters that are NOT HTML whitespace
        assertFalse(StringUtil.isWhitespace('\u00a0')); // non-breaking space
        assertFalse(StringUtil.isWhitespace('\u2000')); // en quad
        assertFalse(StringUtil.isWhitespace('\u3000')); // ideographic space
    }

    @Test 
    public void normaliseWhitespace_shouldCollapseAndNormalizeWhitespace() {
        // Multiple whitespace characters should collapse to single space
        assertEquals(" ", normaliseWhitespace("    \r \n \r\n"));
        assertEquals(" hello there ", normaliseWhitespace("   hello   \r \n  there    \n"));
        
        // No change needed
        assertEquals("hello", normaliseWhitespace("hello"));
        
        // Newline between words becomes space
        assertEquals("hello there", normaliseWhitespace("hello\nthere"));
    }

    @Test 
    public void normaliseWhitespace_shouldHandleUnicodeSurrogates() {
        // Test with high surrogate pairs (Unicode characters outside BMP)
        String inputWithSurrogates = "\ud869\udeb2\u304b\u309a  1"; // contains surrogate pair + multiple spaces
        String expectedOutput = "\ud869\udeb2\u304b\u309a 1";       // multiple spaces collapsed to one

        assertEquals(expectedOutput, normaliseWhitespace(inputWithSurrogates));
        
        // Verify Jsoup parsing produces same result
        String extractedText = Jsoup.parse(inputWithSurrogates).text();
        assertEquals(expectedOutput, extractedText);
    }

    @Test 
    public void resolve_shouldResolveRelativeUrls() {
        // Basic relative URL resolution
        assertEquals("http://example.com/one/two?three", 
                    resolve("http://example.com", "./one/two?three"));
        assertEquals("http://example.com/one/two?three", 
                    resolve("http://example.com?one", "./one/two?three"));
        assertEquals("http://example.com/one/two?three#four", 
                    resolve("http://example.com", "./one/two?three#four"));
        
        // Absolute URLs should be returned as-is
        assertEquals("https://example.com/one", 
                    resolve("http://example.com/", "https://example.com/one"));
        
        // Parent directory navigation
        assertEquals("http://example.com/one/two.html", 
                    resolve("http://example.com/two/", "../one/two.html"));
        
        // Protocol-relative URLs
        assertEquals("https://example2.com/one", 
                    resolve("https://example.com/", "//example2.com/one"));
        
        // Port preservation
        assertEquals("https://example.com:8080/one", 
                    resolve("https://example.com:8080", "./one"));
        
        // Cross-domain absolute URLs
        assertEquals("https://example2.com/one", 
                    resolve("http://example.com/", "https://example2.com/one"));
        
        // Invalid base URL handling
        assertEquals("https://example.com/one", 
                    resolve("wrong", "https://example.com/one"));
        
        // Empty relative URL
        assertEquals("https://example.com/one", 
                    resolve("https://example.com/one", ""));
        
        // Both URLs invalid
        assertEquals("", resolve("wrong", "also wrong"));
    }

    @Test
    public void resolve_shouldHandleFtpProtocol() {
        assertEquals("ftp://example.com/one", 
                    resolve("ftp://example.com/two/", "../one"));
        assertEquals("ftp://example.com/one/two.c", 
                    resolve("ftp://example.com/one/", "./two.c"));
        assertEquals("ftp://example.com/one/two.c", 
                    resolve("ftp://example.com/one/", "two.c"));
    }

    @Test
    public void resolve_shouldHandleRfc3986Examples() {
        // Test cases from RFC 3986 section 5.4.2
        String baseUrl = "http://example.com/b/c/d;p?q";
        
        assertEquals("http://example.com/g", resolve(baseUrl, "../../../g"));
        assertEquals("http://example.com/g", resolve(baseUrl, "../../../../g"));
        assertEquals("http://example.com/g", resolve(baseUrl, "/./g"));
        assertEquals("http://example.com/g", resolve(baseUrl, "/../g"));
        assertEquals("http://example.com/b/c/g.", resolve(baseUrl, "g."));
        assertEquals("http://example.com/b/c/.g", resolve(baseUrl, ".g"));
        assertEquals("http://example.com/b/c/g..", resolve(baseUrl, "g.."));
        assertEquals("http://example.com/b/c/..g", resolve(baseUrl, "..g"));
        assertEquals("http://example.com/b/g", resolve(baseUrl, "./../g"));
        assertEquals("http://example.com/b/c/g/", resolve(baseUrl, "./g/."));
        assertEquals("http://example.com/b/c/g/h", resolve(baseUrl, "g/./h"));
        assertEquals("http://example.com/b/c/h", resolve(baseUrl, "g/../h"));
        assertEquals("http://example.com/b/c/g;x=1/y", resolve(baseUrl, "g;x=1/./y"));
        assertEquals("http://example.com/b/c/y", resolve(baseUrl, "g;x=1/../y"));
        assertEquals("http://example.com/b/c/g?y/./x", resolve(baseUrl, "g?y/./x"));
        assertEquals("http://example.com/b/c/g?y/../x", resolve(baseUrl, "g?y/../x"));
        assertEquals("http://example.com/b/c/g#s/./x", resolve(baseUrl, "g#s/./x"));
        assertEquals("http://example.com/b/c/g#s/../x", resolve(baseUrl, "g#s/../x"));
    }

    @Test 
    void resolve_shouldStripControlCharactersFromUrls() {
        // Control characters in URLs should be stripped during resolution
        assertEquals("foo:bar", resolve("\nhttps://\texample.com/", "\r\nfo\to:ba\br"));
    }

    @Test 
    void resolve_shouldAllowSpacesInUrls() {
        assertEquals("https://example.com/foo bar/", 
                    resolve("HTTPS://example.com/example/", "../foo bar/"));
    }

    @Test
    void isAscii_shouldIdentifyAsciiStrings() {
        // ASCII strings
        assertTrue(StringUtil.isAscii(""));
        assertTrue(StringUtil.isAscii("example.com"));
        assertTrue(StringUtil.isAscii("One Two"));
        
        // Non-ASCII strings
        assertFalse(StringUtil.isAscii("🧔"));        // emoji
        assertFalse(StringUtil.isAscii("测试"));       // Chinese characters
        assertFalse(StringUtil.isAscii("测试.com"));   // mixed ASCII and non-ASCII
    }

    @Test 
    void isAsciiLetter_shouldIdentifyAsciiLetters() {
        // Lowercase letters
        assertTrue(StringUtil.isAsciiLetter('a'));
        assertTrue(StringUtil.isAsciiLetter('n'));
        assertTrue(StringUtil.isAsciiLetter('z'));
        
        // Uppercase letters
        assertTrue(StringUtil.isAsciiLetter('A'));
        assertTrue(StringUtil.isAsciiLetter('N'));
        assertTrue(StringUtil.isAsciiLetter('Z'));

        // Non-letters
        assertFalse(StringUtil.isAsciiLetter(' '));  // space
        assertFalse(StringUtil.isAsciiLetter('-'));  // punctuation
        assertFalse(StringUtil.isAsciiLetter('0'));  // digit
        assertFalse(StringUtil.isAsciiLetter('ß'));  // non-ASCII letter
        assertFalse(StringUtil.isAsciiLetter('Ě'));  // non-ASCII letter
    }

    @Test 
    void isDigit_shouldIdentifyAsciiDigits() {
        // ASCII digits 0-9
        assertTrue(StringUtil.isDigit('0'));
        assertTrue(StringUtil.isDigit('1'));
        assertTrue(StringUtil.isDigit('2'));
        assertTrue(StringUtil.isDigit('3'));
        assertTrue(StringUtil.isDigit('4'));
        assertTrue(StringUtil.isDigit('5'));
        assertTrue(StringUtil.isDigit('6'));
        assertTrue(StringUtil.isDigit('7'));
        assertTrue(StringUtil.isDigit('8'));
        assertTrue(StringUtil.isDigit('9'));

        // Non-digits
        assertFalse(StringUtil.isDigit('a'));  // ASCII letter
        assertFalse(StringUtil.isDigit('A'));  // ASCII letter
        assertFalse(StringUtil.isDigit('ä'));  // non-ASCII letter
        assertFalse(StringUtil.isDigit('Ä'));  // non-ASCII letter
        assertFalse(StringUtil.isDigit('١')); // Arabic-Indic digit
        assertFalse(StringUtil.isDigit('୳')); // Odia digit
    }

    @Test 
    void isHexDigit_shouldIdentifyHexadecimalDigits() {
        // Decimal digits 0-9
        assertTrue(StringUtil.isHexDigit('0'));
        assertTrue(StringUtil.isHexDigit('1'));
        assertTrue(StringUtil.isHexDigit('2'));
        assertTrue(StringUtil.isHexDigit('3'));
        assertTrue(StringUtil.isHexDigit('4'));
        assertTrue(StringUtil.isHexDigit('5'));
        assertTrue(StringUtil.isHexDigit('6'));
        assertTrue(StringUtil.isHexDigit('7'));
        assertTrue(StringUtil.isHexDigit('8'));
        assertTrue(StringUtil.isHexDigit('9'));
        
        // Lowercase hex letters a-f
        assertTrue(StringUtil.isHexDigit('a'));
        assertTrue(StringUtil.isHexDigit('b'));
        assertTrue(StringUtil.isHexDigit('c'));
        assertTrue(StringUtil.isHexDigit('d'));
        assertTrue(StringUtil.isHexDigit('e'));
        assertTrue(StringUtil.isHexDigit('f'));
        
        // Uppercase hex letters A-F
        assertTrue(StringUtil.isHexDigit('A'));
        assertTrue(StringUtil.isHexDigit('B'));
        assertTrue(StringUtil.isHexDigit('C'));
        assertTrue(StringUtil.isHexDigit('D'));
        assertTrue(StringUtil.isHexDigit('E'));
        assertTrue(StringUtil.isHexDigit('F'));

        // Non-hex characters
        assertFalse(StringUtil.isHexDigit('g'));  // beyond hex range
        assertFalse(StringUtil.isHexDigit('G'));  // beyond hex range
        assertFalse(StringUtil.isHexDigit('ä'));  // non-ASCII
        assertFalse(StringUtil.isHexDigit('Ä'));  // non-ASCII
        assertFalse(StringUtil.isHexDigit('١')); // Arabic-Indic digit
        assertFalse(StringUtil.isHexDigit('୳')); // Odia digit
    }
}