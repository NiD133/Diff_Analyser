package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.stream.Collector;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.net.MockURL;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class StringUtil_ESTest extends StringUtil_ESTest_scaffolding {

    // Tests for isHexDigit
    @Test(timeout = 4000)
    public void testIsHexDigit_UpperCaseHexLetters_ReturnsTrue() {
        assertTrue(StringUtil.isHexDigit('F'));
        assertTrue(StringUtil.isHexDigit('A'));
    }

    @Test(timeout = 4000)
    public void testIsHexDigit_LowerCaseHexLetters_ReturnsTrue() {
        assertTrue(StringUtil.isHexDigit('f'));
        assertTrue(StringUtil.isHexDigit('a'));
    }

    @Test(timeout = 4000)
    public void testIsHexDigit_Digit_ReturnsTrue() {
        assertTrue(StringUtil.isHexDigit('9'));
    }

    @Test(timeout = 4000)
    public void testIsHexDigit_NonHexCharacter_ReturnsFalse() {
        assertFalse(StringUtil.isHexDigit('\"'));
        assertFalse(StringUtil.isHexDigit('\u008F'));
    }

    // Tests for isAsciiLetter
    @Test(timeout = 4000)
    public void testIsAsciiLetter_UpperCaseLetters_ReturnsTrue() {
        assertTrue(StringUtil.isAsciiLetter('Z'));
        assertTrue(StringUtil.isAsciiLetter('A'));
    }

    @Test(timeout = 4000)
    public void testIsAsciiLetter_LowerCaseLetters_ReturnsTrue() {
        assertTrue(StringUtil.isAsciiLetter('z'));
        assertTrue(StringUtil.isAsciiLetter('a'));
    }

    @Test(timeout = 4000)
    public void testIsAsciiLetter_NonLetterCharacters_ReturnsFalse() {
        assertFalse(StringUtil.isAsciiLetter('!'));
        assertFalse(StringUtil.isAsciiLetter('}'));
    }

    // Tests for padding
    @Test(timeout = 4000)
    public void testPadding_ValidWidth_ReturnsCorrectSpaces() {
        assertEquals("", StringUtil.padding(0));
        assertEquals("                     ", StringUtil.padding(21, 21));
        assertEquals("", StringUtil.padding(0, -1));
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testPadding_NegativeWidth_ThrowsException() {
        StringUtil.padding(-1);
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testPadding_InvalidWidthOrMax_ThrowsException() {
        StringUtil.padding(166, -37);
        StringUtil.padding(-766, -766);
    }

    // Tests for releaseBuilder
    @Test(timeout = 4000)
    public void testReleaseBuilder_ReleasesAndClearsBuilder() {
        StringBuilder builder = new StringBuilder("test");
        String result = StringUtil.releaseBuilder(builder);
        assertEquals("", builder.toString());
        assertEquals("test", result);
    }

    @Test(timeout = 4000)
    public void testReleaseBuilderVoid_ReleasesBuilder() {
        StringBuilder builder = new StringBuilder("content");
        StringUtil.releaseBuilderVoid(builder);
        assertEquals("", builder.toString());
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testReleaseBuilder_NullBuilder_ThrowsException() {
        StringUtil.releaseBuilder(null);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testReleaseBuilderVoid_NullBuilder_ThrowsException() {
        StringUtil.releaseBuilderVoid(null);
    }

    // Tests for resolve
    @Test(timeout = 4000)
    public void testResolve_WithBaseUrlAndRelativePath_ResolvesCorrectly() throws MalformedURLException {
        URL base = MockURL.getFtpExample();
        URL resolved = StringUtil.resolve(base, "path");
        assertEquals("ftp://ftp.someFakeButWellFormedURL.org/path", resolved.toString());
    }

    @Test(timeout = 4000, expected = MalformedURLException.class)
    public void testResolve_InvalidRelativeUrl_ThrowsException() throws MalformedURLException {
        StringUtil.resolve((URL) null, "B1s2U[+");
    }

    @Test(timeout = 4000)
    public void testResolve_WithBaseStringAndRelativePath_ReturnsResolvedUrl() {
        assertEquals("l:h06g#USh|", StringUtil.resolve("l:h06g#USh|", "l:h06g#USh|"));
        assertEquals("", StringUtil.resolve("org.jsoup.internal.SoftPool", "q/R|zQ`/Gq zKzj"));
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testResolve_NullBaseString_ThrowsException() {
        StringUtil.resolve((String) null, "relative");
    }

    // Tests for isAscii
    @Test(timeout = 4000)
    public void testIsAscii_AsciiString_ReturnsTrue() {
        assertTrue(StringUtil.isAscii("{3f\"nUAQw7TH,Y-"));
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testIsAscii_NullString_ThrowsException() {
        StringUtil.isAscii(null);
    }

    // Tests for inSorted
    @Test(timeout = 4000)
    public void testInSorted_ValueInArray_ReturnsTrue() {
        String[] array = {"value", "", "other"};
        assertTrue(StringUtil.inSorted("", array));
    }

    @Test(timeout = 4000)
    public void testInSorted_ValueNotInArray_ReturnsFalse() {
        String[] array = {"value", "other"};
        assertFalse(StringUtil.inSorted("missing", array));
    }

    // Tests for isInvisibleChar
    @Test(timeout = 4000)
    public void testIsInvisibleChar_ControlCharacters_ReturnsTrue() {
        assertTrue(StringUtil.isInvisibleChar(173)); // Soft hyphen
        assertTrue(StringUtil.isInvisibleChar(8203)); // Zero-width space
    }

    @Test(timeout = 4000)
    public void testIsInvisibleChar_VisibleCharacter_ReturnsFalse() {
        assertFalse(StringUtil.isInvisibleChar(8222)); // Double low-9 quotation mark
    }

    // Tests for isActuallyWhitespace
    @Test(timeout = 4000)
    public void testIsActuallyWhitespace_WhitespaceCharacters_ReturnsTrue() {
        assertTrue(StringUtil.isActuallyWhitespace(32));  // Space
        assertTrue(StringUtil.isActuallyWhitespace(9));   // Tab
        assertTrue(StringUtil.isActuallyWhitespace(10));  // Line feed
        assertTrue(StringUtil.isActuallyWhitespace(13));  // Carriage return
        assertTrue(StringUtil.isActuallyWhitespace(12));  // Form feed
        assertTrue(StringUtil.isActuallyWhitespace(160)); // Non-breaking space
    }

    @Test(timeout = 4000)
    public void testIsActuallyWhitespace_NonWhitespaceCharacters_ReturnsFalse() {
        assertFalse(StringUtil.isActuallyWhitespace(193));
        assertFalse(StringUtil.isActuallyWhitespace(-5106));
    }

    // Tests for isWhitespace
    @Test(timeout = 4000)
    public void testIsWhitespace_WhitespaceCharacters_ReturnsTrue() {
        assertTrue(StringUtil.isWhitespace(32)); // Space
        assertTrue(StringUtil.isWhitespace(9));  // Tab
        assertTrue(StringUtil.isWhitespace(10)); // Line feed
        assertTrue(StringUtil.isWhitespace(13)); // Carriage return
        assertTrue(StringUtil.isWhitespace(12)); // Form feed
    }

    @Test(timeout = 4000)
    public void testIsWhitespace_NonWhitespaceCharacters_ReturnsFalse() {
        assertFalse(StringUtil.isWhitespace(0));
        assertFalse(StringUtil.isWhitespace(21));
    }

    // Tests for normaliseWhitespace
    @Test(timeout = 4000)
    public void testNormaliseWhitespace_EmptyString_ReturnsEmpty() {
        assertEquals("", StringUtil.normaliseWhitespace(""));
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testNormaliseWhitespace_NullString_ThrowsException() {
        StringUtil.normaliseWhitespace(null);
    }

    // Tests for join
    @Test(timeout = 4000)
    public void testJoin_EmptyCollection_ReturnsEmptyString() {
        LinkedList<String> list = new LinkedList<>();
        assertEquals("", StringUtil.join(list, "delimiter"));
    }

    @Test(timeout = 4000)
    public void testJoin_NonEmptyCollection_ReturnsJoinedString() {
        LinkedList<String> list = new LinkedList<>();
        list.add("a");
        list.add("b");
        assertEquals("a,b", StringUtil.join(list, ","));
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testJoin_NullCollection_ThrowsException() {
        StringUtil.join((Collection<?>) null, "delimiter");
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testJoin_NullIterator_ThrowsException() {
        StringUtil.join((Iterator<?>) null, "delimiter");
    }

    // Tests for appendNormalisedWhitespace
    @Test(timeout = 4000)
    public void testAppendNormalisedWhitespace_WhitespaceString_AppendsSingleSpace() {
        StringBuilder sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "   ", false);
        assertEquals(" ", sb.toString());
    }

    @Test(timeout = 4000)
    public void testAppendNormalisedWhitespace_NonWhitespaceString_AppendsString() {
        StringBuilder sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "text", true);
        assertEquals("text", sb.toString());
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testAppendNormalisedWhitespace_NullBuilder_ThrowsException() {
        StringUtil.appendNormalisedWhitespace(null, "text", true);
    }

    // Tests for isDigit
    @Test(timeout = 4000)
    public void testIsDigit_DigitCharacter_ReturnsTrue() {
        assertTrue(StringUtil.isDigit('0'));
    }

    @Test(timeout = 4000)
    public void testIsDigit_NonDigitCharacter_ReturnsFalse() {
        assertFalse(StringUtil.isDigit('Y'));
        assertFalse(StringUtil.isDigit('%'));
    }

    // Tests for StringJoiner
    @Test(timeout = 4000)
    public void testStringJoiner_AddAndComplete_ReturnsJoinedString() {
        StringUtil.StringJoiner joiner = new StringUtil.StringJoiner(",");
        joiner.add("a").add("b");
        assertEquals("a,b", joiner.complete());
    }

    @Test(timeout = 4000)
    public void testStringJoiner_Complete_ReleasesBuilder() {
        StringUtil.StringJoiner joiner = new StringUtil.StringJoiner(",");
        joiner.complete();
        assertEquals("", joiner.sb.toString());
    }

    // Tests for joining (Collector)
    @Test(timeout = 4000)
    public void testJoining_ReturnsCollector() {
        Collector<CharSequence, ?, String> collector = StringUtil.joining(" ");
        assertNotNull(collector);
    }

    // Tests for isNumeric
    @Test(timeout = 4000)
    public void testIsNumeric_NumericString_ReturnsTrue() {
        assertTrue(StringUtil.isNumeric("3"));
    }

    @Test(timeout = 4000)
    public void testIsNumeric_NonNumericString_ReturnsFalse() {
        assertFalse(StringUtil.isNumeric("Yn)+vHQao!UlQ0jsv(O"));
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric(null));
    }

    // Tests for startsWithNewline
    @Test(timeout = 4000)
    public void testStartsWithNewline_StringWithNewline_ReturnsTrue() {
        assertTrue(StringUtil.startsWithNewline("\ntext"));
    }

    @Test(timeout = 4000)
    public void testStartsWithNewline_StringWithoutNewline_ReturnsFalse() {
        assertFalse(StringUtil.startsWithNewline("text"));
        assertFalse(StringUtil.startsWithNewline(""));
        assertFalse(StringUtil.startsWithNewline(null));
    }

    // Tests for isBlank
    @Test(timeout = 4000)
    public void testIsBlank_BlankStrings_ReturnsTrue() {
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank("   "));
        assertTrue(StringUtil.isBlank(null));
    }

    @Test(timeout = 4000)
    public void testIsBlank_NonBlankString_ReturnsFalse() {
        assertFalse(StringUtil.isBlank("text"));
    }

    // Tests for in
    @Test(timeout = 4000)
    public void testIn_ValueInArray_ReturnsTrue() {
        String[] array = {"", "value"};
        assertTrue(StringUtil.in("", array));
    }

    @Test(timeout = 4000)
    public void testIn_ValueNotInArray_ReturnsFalse() {
        String[] array = {"value"};
        assertFalse(StringUtil.in("missing", array));
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testIn_NullArrayElement_ThrowsException() {
        String[] array = new String[1]; // null element
        StringUtil.in("", array);
    }

    // Complex test covering multiple functionalities (original test28)
    @Test(timeout = 4000)
    public void testComplexScenario_JoinAndBuilderOperations() {
        StringBuilder builder = StringUtil.borrowBuilder();
        Iterator<String> mockIterator = mock(Iterator.class);
        when(mockIterator.hasNext()).thenReturn(true, false);
        when(mockIterator.next()).thenReturn(builder);

        String result = StringUtil.join(mockIterator, "");
        assertNotNull(result);

        builder.append("").append(true);
        assertTrue(builder.toString().contains("true"));
    }
}