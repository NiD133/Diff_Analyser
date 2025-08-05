package com.google.common.escape;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Test suite for ArrayBasedUnicodeEscaper functionality.
 * 
 * This class tests the core escaping behavior including:
 * - Character replacement mapping
 * - Safe range handling
 * - Unicode code point escaping
 * - Edge cases and boundary conditions
 */
public class ArrayBasedUnicodeEscaper_ESTest {

    private TestableArrayBasedUnicodeEscaper htmlEscaper;
    private TestableArrayBasedUnicodeEscaper emptyEscaper;
    private TestableArrayBasedUnicodeEscaper noSafeRangeEscaper;

    /**
     * Concrete implementation of ArrayBasedUnicodeEscaper for testing purposes.
     * Simulates HTML-style escaping with numeric character references for unsafe characters.
     */
    private static class TestableArrayBasedUnicodeEscaper extends ArrayBasedUnicodeEscaper {
        
        public TestableArrayBasedUnicodeEscaper(Map<Character, String> replacements, 
                                              int safeMin, int safeMax, String unsafeReplacement) {
            super(replacements, safeMin, safeMax, unsafeReplacement);
        }

        @Override
        protected char[] escapeUnsafe(int codePoint) {
            // Return HTML-style numeric character reference: &#NNNN;
            String escaped = "&#" + codePoint + ";";
            return escaped.toCharArray();
        }
    }

    @Before
    public void setUp() {
        // Set up HTML-like escaper with common character replacements
        Map<Character, String> htmlReplacements = new HashMap<>();
        htmlReplacements.put('<', "&lt;");
        htmlReplacements.put('>', "&gt;");
        htmlReplacements.put('&', "&amp;");
        htmlReplacements.put('"', "&quot;");
        
        // Safe range: printable ASCII characters (32-126)
        htmlEscaper = new TestableArrayBasedUnicodeEscaper(htmlReplacements, 32, 126, null);
        
        // Empty escaper with no replacements
        emptyEscaper = new TestableArrayBasedUnicodeEscaper(new HashMap<>(), 32, 126, null);
        
        // Escaper with no safe range (all characters are potentially unsafe)
        noSafeRangeEscaper = new TestableArrayBasedUnicodeEscaper(htmlReplacements, 200, 100, null);
    }

    @Test
    public void testBasicCharacterReplacement() {
        // Test that mapped characters are properly replaced
        assertEquals("&lt;div&gt;", htmlEscaper.escape("<div>"));
        assertEquals("&amp;nbsp;", htmlEscaper.escape("&nbsp;"));
        assertEquals("Say &quot;hello&quot;", htmlEscaper.escape("Say \"hello\""));
    }

    @Test
    public void testSafeRangeCharactersNotEscaped() {
        // Test that characters within safe range are not escaped
        String safeText = "Hello World 123!";
        assertEquals(safeText, htmlEscaper.escape(safeText));
        
        // Test individual safe characters
        assertEquals("A", htmlEscaper.escape("A"));
        assertEquals("z", htmlEscaper.escape("z"));
        assertEquals("0", htmlEscaper.escape("0"));
        assertEquals(" ", htmlEscaper.escape(" ")); // space (ASCII 32)
        assertEquals("~", htmlEscaper.escape("~")); // tilde (ASCII 126)
    }

    @Test
    public void testUnsafeCharactersEscaped() {
        // Test characters outside safe range get escaped via escapeUnsafe
        assertEquals("&#31;", htmlEscaper.escape("\u001f")); // Below safe range
        assertEquals("&#127;", htmlEscaper.escape("\u007f")); // Above safe range
        assertEquals("&#8364;", htmlEscaper.escape("€")); // Euro symbol (U+20AC = 8364)
    }

    @Test
    public void testMixedContentEscaping() {
        // Test string with mix of safe, mapped, and unsafe characters
        String input = "Price: €50 <span>\"Sale\"</span>\u001f";
        String expected = "Price: &#8364;50 &lt;span&gt;&quot;Sale&quot;&lt;/span&gt;&#31;";
        assertEquals(expected, htmlEscaper.escape(input));
    }

    @Test
    public void testEmptyAndNullInputs() {
        // Test edge cases with empty/null inputs
        assertEquals("", htmlEscaper.escape(""));
        assertEquals(null, htmlEscaper.escape(null));
    }

    @Test
    public void testNoReplacementMap() {
        // Test escaper with no character replacements
        assertEquals("Hello World", emptyEscaper.escape("Hello World"));
        assertEquals("&#31;", emptyEscaper.escape("\u001f")); // Still escapes unsafe chars
    }

    @Test
    public void testNoSafeRange() {
        // Test escaper where safeMax < safeMin (no safe range)
        assertEquals("&#72;&#101;&#108;&#108;&#111;", noSafeRangeEscaper.escape("Hello"));
        assertEquals("&lt;&#100;&#105;&#118;&gt;", noSafeRangeEscaper.escape("<div>"));
    }

    @Test
    public void testSingleCharacterInputs() {
        // Test individual character escaping
        assertEquals("&lt;", htmlEscaper.escape("<"));
        assertEquals("&gt;", htmlEscaper.escape(">"));
        assertEquals("&amp;", htmlEscaper.escape("&"));
        assertEquals("&quot;", htmlEscaper.escape("\""));
        assertEquals("a", htmlEscaper.escape("a"));
    }

    @Test
    public void testUnicodeCodePoints() {
        // Test various Unicode code points
        assertEquals("&#9786;", htmlEscaper.escape("☺")); // Smiley face
        assertEquals("&#12354;", htmlEscaper.escape("あ")); // Hiragana A
        assertEquals("&#128512;", htmlEscaper.escape("😀")); // Grinning face emoji
    }

    @Test
    public void testBoundaryValues() {
        // Test characters at the boundaries of safe range
        assertEquals("&#31;", htmlEscaper.escape("\u001f")); // Just below safe min (32)
        assertEquals(" ", htmlEscaper.escape(" ")); // At safe min (32)
        assertEquals("~", htmlEscaper.escape("~")); // At safe max (126)
        assertEquals("&#127;", htmlEscaper.escape("\u007f")); // Just above safe max (126)
    }

    @Test
    public void testLongStringPerformance() {
        // Test with longer string to verify performance characteristics
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("Hello World ");
        }
        String longSafeString = sb.toString();
        
        // Should return same string since all characters are safe
        assertEquals(longSafeString, htmlEscaper.escape(longSafeString));
        
        // Test long string with escaping needed
        String longUnsafeString = longSafeString.replace("Hello", "<Hello>");
        assertTrue(htmlEscaper.escape(longUnsafeString).contains("&lt;Hello&gt;"));
    }
}