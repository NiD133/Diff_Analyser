package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collector;
import org.jsoup.internal.StringUtil;

/**
 * Test suite for StringUtil utility class.
 * Tests string manipulation, validation, padding, joining, and URL resolution functionality.
 */
public class StringUtilTest {

    // ========== Character Validation Tests ==========
    
    @Test
    public void testIsHexDigit_ValidHexCharacters() {
        assertTrue("'F' should be a valid hex digit", StringUtil.isHexDigit('F'));
        assertTrue("'A' should be a valid hex digit", StringUtil.isHexDigit('A'));
        assertTrue("'f' should be a valid hex digit", StringUtil.isHexDigit('f'));
        assertTrue("'a' should be a valid hex digit", StringUtil.isHexDigit('a'));
        assertTrue("'9' should be a valid hex digit", StringUtil.isHexDigit('9'));
        assertTrue("'0' should be a valid hex digit", StringUtil.isHexDigit('0'));
    }

    @Test
    public void testIsHexDigit_InvalidHexCharacters() {
        assertFalse("'\"' should not be a valid hex digit", StringUtil.isHexDigit('"'));
        assertFalse("Non-ASCII character should not be a valid hex digit", StringUtil.isHexDigit('\u008F'));
    }

    @Test
    public void testIsDigit_ValidDigits() {
        assertTrue("'0' should be a digit", StringUtil.isDigit('0'));
        assertTrue("'3' should be a digit", StringUtil.isDigit('3'));
    }

    @Test
    public void testIsDigit_InvalidDigits() {
        assertFalse("'Y' should not be a digit", StringUtil.isDigit('Y'));
        assertFalse("'%' should not be a digit", StringUtil.isDigit('%'));
    }

    @Test
    public void testIsAsciiLetter_ValidLetters() {
        assertTrue("'Z' should be an ASCII letter", StringUtil.isAsciiLetter('Z'));
        assertTrue("'z' should be an ASCII letter", StringUtil.isAsciiLetter('z'));
        assertTrue("'a' should be an ASCII letter", StringUtil.isAsciiLetter('a'));
        assertTrue("'A' should be an ASCII letter", StringUtil.isAsciiLetter('A'));
    }

    @Test
    public void testIsAsciiLetter_InvalidLetters() {
        assertFalse("'!' should not be an ASCII letter", StringUtil.isAsciiLetter('!'));
        assertFalse("'}' should not be an ASCII letter", StringUtil.isAsciiLetter('}'));
    }

    // ========== String Validation Tests ==========

    @Test
    public void testIsAscii_ValidAsciiString() {
        assertTrue("String with ASCII characters should return true", 
                   StringUtil.isAscii("{3f\"nUAQw7TH,Y-"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsAscii_NullString_ThrowsException() {
        StringUtil.isAscii(null);
    }

    @Test
    public void testIsNumeric_ValidNumericString() {
        assertTrue("String with only digits should be numeric", StringUtil.isNumeric("3"));
    }

    @Test
    public void testIsNumeric_InvalidStrings() {
        assertFalse("String with letters should not be numeric", 
                    StringUtil.isNumeric("Yn)+vHQao!UlQ0jsv(O"));
        assertFalse("Empty string should not be numeric", StringUtil.isNumeric(""));
        assertFalse("Null string should not be numeric", StringUtil.isNumeric(null));
    }

    @Test
    public void testIsBlank_BlankStrings() {
        assertTrue("String with only spaces should be blank", 
                   StringUtil.isBlank("                    "));
        assertTrue("Empty string should be blank", StringUtil.isBlank(""));
        assertTrue("Null string should be blank", StringUtil.isBlank(null));
    }

    @Test
    public void testIsBlank_NonBlankString() {
        assertFalse("String with content should not be blank", StringUtil.isBlank("_5s]9,"));
    }

    @Test
    public void testStartsWithNewline_ValidCases() {
        assertFalse("Empty string should not start with newline", 
                    StringUtil.startsWithNewline(""));
        assertFalse("Regular string should not start with newline", 
                    StringUtil.startsWithNewline("^[a-zA-Z][a-zA-Z0-9+-.]*:"));
        assertFalse("Null string should not start with newline", 
                    StringUtil.startsWithNewline(null));
    }

    // ========== Whitespace Tests ==========

    @Test
    public void testIsWhitespace_ValidWhitespaceCharacters() {
        assertTrue("Space (32) should be whitespace", StringUtil.isWhitespace(32));
        assertTrue("Tab (9) should be whitespace", StringUtil.isWhitespace(9));
        assertTrue("Line feed (10) should be whitespace", StringUtil.isWhitespace(10));
        assertTrue("Form feed (12) should be whitespace", StringUtil.isWhitespace(12));
        assertTrue("Carriage return (13) should be whitespace", StringUtil.isWhitespace(13));
    }

    @Test
    public void testIsWhitespace_NonWhitespaceCharacters() {
        assertFalse("Character 0 should not be whitespace", StringUtil.isWhitespace(0));
        assertFalse("Character 21 should not be whitespace", StringUtil.isWhitespace(21));
    }

    @Test
    public void testIsActuallyWhitespace_ValidWhitespaceCharacters() {
        assertTrue("Space (32) should be actually whitespace", StringUtil.isActuallyWhitespace(32));
        assertTrue("Tab (9) should be actually whitespace", StringUtil.isActuallyWhitespace(9));
        assertTrue("Line feed (10) should be actually whitespace", StringUtil.isActuallyWhitespace(10));
        assertTrue("Form feed (12) should be actually whitespace", StringUtil.isActuallyWhitespace(12));
        assertTrue("Carriage return (13) should be actually whitespace", StringUtil.isActuallyWhitespace(13));
        assertTrue("Non-breaking space (160) should be actually whitespace", StringUtil.isActuallyWhitespace(160));
    }

    @Test
    public void testIsActuallyWhitespace_NonWhitespaceCharacters() {
        assertFalse("Character 193 should not be actually whitespace", StringUtil.isActuallyWhitespace(193));
        assertFalse("Negative character should not be actually whitespace", StringUtil.isActuallyWhitespace(-5106));
    }

    @Test
    public void testIsInvisibleChar_ValidInvisibleCharacters() {
        assertTrue("Soft hyphen (173) should be invisible", StringUtil.isInvisibleChar(173));
        assertTrue("Zero width space (8203) should be invisible", StringUtil.isInvisibleChar(8203));
    }

    @Test
    public void testIsInvisibleChar_VisibleCharacter() {
        assertFalse("Character 8222 should not be invisible", StringUtil.isInvisibleChar(8222));
    }

    // ========== String Padding Tests ==========

    @Test
    public void testPadding_ValidWidth() {
        String result = StringUtil.padding(21);
        assertEquals("Should create 21 spaces", "                     ", result);
    }

    @Test
    public void testPadding_ZeroWidth() {
        String result = StringUtil.padding(0);
        assertEquals("Zero width should return empty string", "", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPadding_NegativeWidth_ThrowsException() {
        StringUtil.padding(-1);
    }

    @Test
    public void testPadding_WithMaxWidth_ValidInputs() {
        String result = StringUtil.padding(21, 21);
        assertEquals("Should create 21 spaces with max width", "                     ", result);
    }

    @Test
    public void testPadding_WithMaxWidth_ZeroWidth() {
        String result = StringUtil.padding(0, -1);
        assertEquals("Zero width should return empty string", "", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPadding_WithMaxWidth_NegativeWidth_ThrowsException() {
        StringUtil.padding(-766, -766);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPadding_WithMaxWidth_InvalidMaxWidth_ThrowsException() {
        StringUtil.padding(166, -37);
    }

    // ========== String Joining Tests ==========

    @Test
    public void testJoin_EmptyArray() {
        String[] emptyArray = new String[0];
        String result = StringUtil.join(emptyArray, "separator");
        assertEquals("Empty array should produce empty string", "", result);
    }

    @Test
    public void testJoin_ArrayWithContent() {
        String[] array = {"", "second", null};
        array[0] = "first";
        String result = StringUtil.join(array, ",");
        assertNotNull("Result should not be null", result);
    }

    @Test(expected = NullPointerException.class)
    public void testJoin_NullArray_ThrowsException() {
        StringUtil.join((String[]) null, "separator");
    }

    @Test
    public void testJoin_EmptyCollection() {
        List<String> emptyList = new LinkedList<>();
        String result = StringUtil.join(emptyList, "separator");
        assertEquals("Empty collection should produce empty string", "", result);
    }

    @Test
    public void testJoin_CollectionWithContent() {
        List<String> list = new LinkedList<>();
        list.add("item1");
        String result = StringUtil.join(list, "separator");
        assertNotNull("Result should not be null", result);
    }

    @Test(expected = NullPointerException.class)
    public void testJoin_NullCollection_ThrowsException() {
        StringUtil.join((Collection<?>) null, "separator");
    }

    @Test
    public void testJoin_EmptyIterator() {
        List<String> emptyList = new LinkedList<>();
        Iterator<String> emptyIterator = emptyList.iterator();
        String result = StringUtil.join(emptyIterator, "separator");
        assertEquals("Empty iterator should produce empty string", "", result);
    }

    @Test(expected = NullPointerException.class)
    public void testJoin_NullIterator_ThrowsException() {
        StringUtil.join((Iterator<?>) null, "separator");
    }

    // ========== String Array Search Tests ==========

    @Test
    public void testInSorted_StringFound() {
        String[] array = {"", "second", "third"};
        boolean result = StringUtil.inSorted("", array);
        assertTrue("Empty string should be found in array", result);
    }

    @Test
    public void testInSorted_StringNotFound() {
        String[] array = {"different"};
        boolean result = StringUtil.inSorted("target", array);
        assertFalse("Target string should not be found", result);
    }

    @Test(expected = NullPointerException.class)
    public void testInSorted_NullArray_ThrowsException() {
        StringUtil.inSorted("target", null);
    }

    @Test
    public void testIn_StringFound() {
        String[] array = new String[8];
        array[0] = "";
        boolean result = StringUtil.in("", array);
        assertTrue("Empty string should be found in array", result);
    }

    @Test
    public void testIn_StringNotFound() {
        String[] array = {"different"};
        boolean result = StringUtil.in("target", array);
        assertFalse("Target string should not be found", result);
    }

    // ========== StringBuilder Pool Tests ==========

    @Test
    public void testBorrowAndReleaseBuilder() {
        StringBuilder builder = StringUtil.borrowBuilder();
        assertNotNull("Borrowed builder should not be null", builder);
        
        String result = StringUtil.releaseBuilder(builder);
        assertEquals("Released builder should return empty string", "", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseBuilder_NullBuilder_ThrowsException() {
        StringUtil.releaseBuilder(null);
    }

    @Test(expected = NullPointerException.class)
    public void testReleaseBuilderVoid_NullBuilder_ThrowsException() {
        StringUtil.releaseBuilderVoid(null);
    }

    @Test
    public void testReleaseBuilderVoid_ValidBuilder() {
        StringBuilder builder = StringUtil.borrowBuilder();
        builder.append("test content");
        StringUtil.releaseBuilderVoid(builder);
        // Builder should be cleared after release
        assertEquals("Builder should be empty after release", "", builder.toString());
    }

    // ========== String Normalization Tests ==========

    @Test
    public void testNormaliseWhitespace_EmptyString() {
        String result = StringUtil.normaliseWhitespace("");
        assertEquals("Empty string should remain empty", "", result);
    }

    @Test(expected = NullPointerException.class)
    public void testNormaliseWhitespace_NullString_ThrowsException() {
        StringUtil.normaliseWhitespace(null);
    }

    @Test
    public void testAppendNormalisedWhitespace_WithStripLeading() {
        StringBuilder builder = StringUtil.borrowBuilder();
        StringUtil.appendNormalisedWhitespace(builder, "         ", true);
        assertEquals("Leading whitespace should be stripped", "", builder.toString());
        StringUtil.releaseBuilderVoid(builder);
    }

    @Test
    public void testAppendNormalisedWhitespace_WithoutStripLeading() {
        StringBuilder builder = StringUtil.borrowBuilder();
        String longWhitespace = "                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    ";
        StringUtil.appendNormalisedWhitespace(builder, longWhitespace, false);
        assertEquals("Long whitespace should be normalized to single space", " ", builder.toString());
        StringUtil.releaseBuilderVoid(builder);
    }

    @Test
    public void testAppendNormalisedWhitespace_WithContent() {
        StringBuilder builder = StringUtil.borrowBuilder();
        StringUtil.appendNormalisedWhitespace(builder, "width must be >= 0", true);
        assertEquals("Content should be preserved", "width must be >= 0", builder.toString());
        StringUtil.releaseBuilderVoid(builder);
    }

    @Test(expected = NullPointerException.class)
    public void testAppendNormalisedWhitespace_NullBuilder_ThrowsException() {
        StringUtil.appendNormalisedWhitespace(null, "test", true);
    }

    // ========== URL Resolution Tests ==========

    @Test
    public void testResolve_StringToString_SameUrl() {
        String result = StringUtil.resolve("l:h06g#USh|", "l:h06g#USh|");
        assertEquals("Same URL should be returned", "l:h06g#USh|", result);
    }

    @Test
    public void testResolve_StringToString_EmptyResult() {
        String result = StringUtil.resolve("org.jsoup.internal.SoftPool", "q/R|zQ`/Gq zKzj");
        assertEquals("Invalid URL should return empty string", "", result);
    }

    @Test(expected = NullPointerException.class)
    public void testResolve_StringToString_NullInputs_ThrowsException() {
        StringUtil.resolve((String) null, (String) null);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolve_URLToURL_InvalidRelativeUrl_ThrowsException() throws MalformedURLException {
        StringUtil.resolve((URL) null, "B1s2U[+");
    }

    @Test(expected = NullPointerException.class)
    public void testResolve_URLToURL_NullBase_ThrowsException() throws MalformedURLException {
        StringUtil.resolve((URL) null, "validUrl");
    }

    // ========== StringJoiner Tests ==========

    @Test
    public void testStringJoiner_EmptyJoiner() {
        StringUtil.StringJoiner joiner = new StringUtil.StringJoiner("separator");
        String result = joiner.complete();
        assertEquals("Empty joiner should return empty string", "", result);
    }

    @Test
    public void testStringJoiner_NullSeparator() {
        StringUtil.StringJoiner joiner = new StringUtil.StringJoiner(null);
        String result = joiner.complete();
        assertEquals("Joiner with null separator should return empty string", "", result);
    }

    @Test
    public void testStringJoiner_AddItems() {
        StringUtil.StringJoiner joiner = new StringUtil.StringJoiner("separator");
        StringUtil.StringJoiner result1 = joiner.add("item1");
        StringUtil.StringJoiner result2 = result1.add("item2");
        
        assertSame("add() should return same joiner instance", joiner, result1);
        assertSame("add() should return same joiner instance", result1, result2);
        
        joiner.complete();
    }

    @Test
    public void testStringJoiner_AppendToItem() {
        StringUtil.StringJoiner joiner = new StringUtil.StringJoiner("separator");
        StringUtil.StringJoiner result = joiner.append("content");
        
        assertSame("append() should return same joiner instance", joiner, result);
        joiner.complete();
    }

    // ========== Stream Collector Tests ==========

    @Test
    public void testJoining_CreatesCollector() {
        Collector<CharSequence, ?, String> collector = StringUtil.joining(" ");
        assertNotNull("Collector should not be null", collector);
    }

    // ========== Constructor Test ==========

    @Test
    public void testConstructor() {
        // Test that StringUtil can be instantiated (even though it's a utility class)
        StringUtil stringUtil = new StringUtil();
        assertNotNull("StringUtil instance should not be null", stringUtil);
    }
}