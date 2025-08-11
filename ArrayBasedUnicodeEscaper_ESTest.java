package com.google.common.escape;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ArrayBasedUnicodeEscaperTest {

    // Concrete implementation for testing
    private static class TestEscaper extends ArrayBasedUnicodeEscaper {
        TestEscaper(Map<Character, String> replacements, 
                   int safeMin, 
                   int safeMax, 
                   String unsafeReplacement) {
            super(replacements, safeMin, safeMax, unsafeReplacement);
        }

        @Override
        protected char[] escapeUnsafe(int cp) {
            return ("[" + cp + "]").toCharArray();
        }
    }

    private TestEscaper escaper;

    @Before
    public void setUp() {
        Map<Character, String> replacements = new HashMap<>();
        replacements.put('a', "A_REPL");
        replacements.put('b', "B_REPL");
        replacements.put('c', "C_REPL");
        
        // Create escaper with:
        // - Safe range: 'A' (65) to 'Z' (90)
        // - Default unsafe replacement: null (use escapeUnsafe)
        escaper = new TestEscaper(replacements, 'A', 'Z', null);
    }

    @Test
    public void testNoEscapingRequired() {
        String input = "HELLO";
        assertEquals("All characters in safe range should remain unchanged",
                     input, escaper.escape(input));
    }

    @Test
    public void testReplacementOfMappedCharacters() {
        String input = "abc";
        assertEquals("Should replace mapped characters",
                     "A_REPLB_REPLC_REPL", escaper.escape(input));
    }

    @Test
    public void testUnsafeCharacters() {
        String input = "hello@";
        // 'h','e','l','l','o' are lowercase -> unsafe
        // '@' is below safe range -> unsafe
        String expected = "[104][101][108][108][111][64]";
        assertEquals("Should escape characters outside safe range",
                     expected, escaper.escape(input));
    }

    @Test
    public void testMixedContent() {
        String input = "Hello_World123";
        // Breakdown:
        //   H (safe) -> 'H'
        //   e (unsafe) -> '[101]'
        //   llo (unsafe) -> '[108][108][111]'
        //   _ (safe) -> '_'
        //   World (mixed case) -> 'W' (safe) + '[111][114][108][100]' (unsafe)
        //   123 (below safe range) -> '[49][50][51]'
        String expected = "H[101][108][108][111]_W[111][114][108][100][49][50][51]";
        assertEquals("Should handle mixed safe/unsafe/replaced chars",
                     expected, escaper.escape(input));
    }

    @Test
    public void testEmptyString() {
        assertEquals("Empty string should remain empty", 
                     "", escaper.escape(""));
    }

    @Test
    public void testSafeRangeBoundaries() {
        // Test boundaries of safe range (A-Z)
        char minSafe = 'A';  // 65
        char maxSafe = 'Z';  // 90
        char belowSafe = (char) (minSafe - 1);  // '@' (64)
        char aboveSafe = (char) (maxSafe + 1);  // '[' (91)

        assertEquals("Min safe boundary should be preserved", 
                     "A", escaper.escape(String.valueOf(minSafe)));
        assertEquals("Max safe boundary should be preserved", 
                     "Z", escaper.escape(String.valueOf(maxSafe)));
        assertEquals("Below safe should be escaped", 
                     "[64]", escaper.escape(String.valueOf(belowSafe)));
        assertEquals("Above safe should be escaped", 
                     "[91]", escaper.escape(String.valueOf(aboveSafe)));
    }

    @Test
    public void testSurrogateHandling() {
        // Contains surrogate pair (U+1F600 GRINNING FACE)
        String input = "A😀B";
        // Since our safe range is A-Z (65-90):
        //   'A' (65) -> safe
        //   '😀' (0x1F600) -> above safe range -> escaped
        //   'B' (66) -> safe
        String expected = "A[128512]B";
        assertEquals("Should escape non-BMP characters",
                     expected, escaper.escape(input));
    }
}