package org.apache.commons.lang3;

import org.junit.Test;

import java.nio.CharBuffer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Understandable and maintainable tests for {@link CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    private static final String TEST_STRING = "Apache Commons Lang";
    private static final String EMPTY_STRING = "";

    // ==========================================================================
    // regionMatches() Tests
    // ==========================================================================

    @Test
    public void regionMatches_whenRegionsAreEqual_shouldReturnTrue() {
        // Arrange
        CharSequence cs = new StringBuilder("abcdef");

        // Act
        boolean result = CharSequenceUtils.regionMatches(cs, false, 1, "XbcdeY", 1, 4);

        // Assert
        assertTrue("Expected 'bcde' to match", result);
    }

    @Test
    public void regionMatches_whenRegionsAreNotEqual_shouldReturnFalse() {
        // Arrange
        CharSequence cs = new StringBuilder("abcdef");

        // Act
        boolean result = CharSequenceUtils.regionMatches(cs, false, 1, "XbddeY", 1, 4);

        // Assert
        assertFalse("Expected 'bcde' not to match 'bdde'", result);
    }

    @Test
    public void regionMatches_whenRegionsAreEqualIgnoringCase_shouldReturnTrue() {
        // Arrange
        CharSequence cs = new StringBuilder("abcdef");

        // Act
        boolean result = CharSequenceUtils.regionMatches(cs, true, 1, "XBCDEY", 1, 4);

        // Assert
        assertTrue("Expected 'bcde' to match 'BCDE' ignoring case", result);
    }

    @Test
    public void regionMatches_withZeroLength_shouldReturnTrue() {
        // Arrange
        CharSequence cs = "abc";

        // Act
        // A zero-length region is always considered to match.
        boolean result = CharSequenceUtils.regionMatches(cs, false, 0, "xyz", 0, 0);

        // Assert
        assertTrue(result);
    }

    @Test
    public void regionMatches_withNegativeLength_shouldReturnFalse() {
        // Arrange
        CharSequence cs = "abc";

        // Act
        boolean result = CharSequenceUtils.regionMatches(cs, false, 0, "xyz", 0, -1);

        // Assert
        assertFalse(result);
    }

    @Test
    public void regionMatches_withInvalidOffsets_shouldReturnFalse() {
        // Arrange
        CharSequence cs = "abc";

        // Act & Assert
        assertFalse("Negative thisStart should return false", CharSequenceUtils.regionMatches(cs, false, -1, "xyz", 0, 1));
        assertFalse("Negative otherStart should return false", CharSequenceUtils.regionMatches(cs, false, 0, "xyz", -1, 1));
        assertFalse("thisStart too large should return false", CharSequenceUtils.regionMatches(cs, false, 4, "xyz", 0, 1));
        assertFalse("otherStart too large should return false", CharSequenceUtils.regionMatches(cs, false, 0, "xyz", 4, 1));
    }

    @Test(expected = NullPointerException.class)
    public void regionMatches_withNullCharSequence_shouldThrowNPE() {
        // Act
        CharSequenceUtils.regionMatches(null, false, 0, TEST_STRING, 0, 1);
    }

    // ==========================================================================
    // lastIndexOf(CharSequence, int, int) Tests
    // ==========================================================================

    @Test
    public void lastIndexOf_charFound_shouldReturnCorrectIndex() {
        // Arrange
        CharSequence cs = new StringBuilder(TEST_STRING); // "Apache Commons Lang"

        // Act
        int result = CharSequenceUtils.lastIndexOf(cs, 'o', 12);

        // Assert
        assertEquals(8, result);
    }

    @Test
    public void lastIndexOf_charNotFound_shouldReturnMinusOne() {
        // Arrange
        CharSequence cs = TEST_STRING;

        // Act
        int result = CharSequenceUtils.lastIndexOf(cs, 'z', 10);

        // Assert
        assertEquals(-1, result);
    }

    @Test
    public void lastIndexOf_onEmptyString_shouldReturnMinusOne() {
        // Arrange
        CharSequence cs = new StringBuilder();

        // Act
        int result = CharSequenceUtils.lastIndexOf(cs, 'a', 0);

        // Assert
        assertEquals(-1, result);
    }

    @Test
    public void lastIndexOf_withNegativeStartIndex_shouldReturnMinusOne() {
        // Arrange
        CharSequence cs = TEST_STRING;

        // Act
        int result = CharSequenceUtils.lastIndexOf(cs, 'a', -1);

        // Assert
        assertEquals(-1, result);
    }

    @Test
    public void lastIndexOf_withSurrogatePair_shouldFindCodePoint() {
        // Arrange
        // U+1F600 is a grinning face emoji, represented by surrogate pair \uD83D\uDE00
        String textWithSurrogate = "Hello \uD83D\uDE00 World";
        int grinningFaceCodePoint = 0x1F600;

        // Act
        int result = CharSequenceUtils.lastIndexOf(textWithSurrogate, grinningFaceCodePoint, 10);

        // Assert
        assertEquals("Should find the start of the surrogate pair", 6, result);
    }

    // ==========================================================================
    // lastIndexOf(CharSequence, CharSequence, int) Tests
    // ==========================================================================

    @Test
    public void lastIndexOf_searchStringFound_shouldReturnCorrectIndex() {
        // Arrange
        CharSequence cs = "abab";

        // Act
        int result = CharSequenceUtils.lastIndexOf(cs, "ab", 3);

        // Assert
        assertEquals(2, result);
    }

    @Test
    public void lastIndexOf_searchStringNotFound_shouldReturnMinusOne() {
        // Arrange
        CharSequence cs = "abab";

        // Act
        int result = CharSequenceUtils.lastIndexOf(cs, "ac", 3);

        // Assert
        assertEquals(-1, result);
    }

    @Test
    public void lastIndexOf_searchForEmptyString_shouldReturnClampedStartIndex() {
        // Arrange
        CharSequence cs = "abc";

        // Act & Assert
        assertEquals("Search for empty string at index 2 should return 2", 2, CharSequenceUtils.lastIndexOf(cs, EMPTY_STRING, 2));
        assertEquals("Search for empty string at out-of-bounds index should return length", 3, CharSequenceUtils.lastIndexOf(cs, EMPTY_STRING, 5));
    }

    @Test
    public void lastIndexOf_withNullSource_shouldReturnMinusOne() {
        // Act
        int result = CharSequenceUtils.lastIndexOf(null, TEST_STRING, 0);
        // Assert
        assertEquals(-1, result);
    }

    @Test
    public void lastIndexOf_withNullSearchString_shouldReturnMinusOne() {
        // Act
        int result = CharSequenceUtils.lastIndexOf(TEST_STRING, null, 0);
        // Assert
        assertEquals(-1, result);
    }

    // ==========================================================================
    // indexOf(CharSequence, int, int) Tests
    // ==========================================================================

    @Test
    public void indexOf_charFound_shouldReturnCorrectIndex() {
        // Arrange
        CharSequence cs = new StringBuffer(TEST_STRING); // "Apache Commons Lang"

        // Act
        int result = CharSequenceUtils.indexOf(cs, 'C', 0);

        // Assert
        assertEquals(7, result);
    }

    @Test
    public void indexOf_charNotFound_shouldReturnMinusOne() {
        // Arrange
        CharSequence cs = TEST_STRING;

        // Act
        int result = CharSequenceUtils.indexOf(cs, 'z', 0);

        // Assert
        assertEquals(-1, result);
    }

    @Test
    public void indexOf_withNegativeStartIndex_shouldStartFromZero() {
        // Arrange
        CharSequence cs = "abac";

        // Act
        int result = CharSequenceUtils.indexOf(cs, 'a', -5);

        // Assert
        assertEquals(0, result);
    }

    @Test
    public void indexOf_withSurrogatePair_shouldFindCodePoint() {
        // Arrange
        // U+1F600 is a grinning face emoji, represented by surrogate pair \uD83D\uDE00
        String textWithSurrogate = "Hello \uD83D\uDE00 World";
        int grinningFaceCodePoint = 0x1F600;

        // Act
        int result = CharSequenceUtils.indexOf(textWithSurrogate, grinningFaceCodePoint, 0);

        // Assert
        assertEquals("Should find the start of the surrogate pair", 6, result);
    }

    // ==========================================================================
    // indexOf(CharSequence, CharSequence, int) Tests
    // ==========================================================================

    @Test
    public void indexOf_searchStringFound_shouldReturnFirstOccurrence() {
        // Arrange
        CharSequence cs = "abab";

        // Act
        int result = CharSequenceUtils.indexOf(cs, "ab", 0);

        // Assert
        assertEquals(0, result);
    }

    @Test
    public void indexOf_searchForEmptyString_shouldReturnStartIndex() {
        // Arrange
        CharSequence cs = "abc";

        // Act & Assert
        assertEquals("Searching for empty string with negative start returns 0", 0, CharSequenceUtils.indexOf(cs, EMPTY_STRING, -1));
        assertEquals("Searching for empty string at index 1 returns 1", 1, CharSequenceUtils.indexOf(cs, EMPTY_STRING, 1));
        assertEquals("Searching for empty string at out-of-bounds index returns length", 3, CharSequenceUtils.indexOf(cs, EMPTY_STRING, 5));
    }

    // ==========================================================================
    // toCharArray() Tests
    // ==========================================================================

    @Test
    public void toCharArray_withString_shouldReturnCorrectArray() {
        // Act
        char[] result = CharSequenceUtils.toCharArray("abc");

        // Assert
        assertArrayEquals(new char[]{'a', 'b', 'c'}, result);
    }

    @Test
    public void toCharArray_withStringBuilder_shouldReturnCorrectArray() {
        // Arrange
        CharSequence cs = new StringBuilder("abc");

        // Act
        char[] result = CharSequenceUtils.toCharArray(cs);

        // Assert
        assertArrayEquals(new char[]{'a', 'b', 'c'}, result);
    }

    @Test
    public void toCharArray_withEmptyCharSequence_shouldReturnEmptyArray() {
        // Arrange
        CharSequence cs = new StringBuilder();

        // Act
        char[] result = CharSequenceUtils.toCharArray(cs);

        // Assert
        assertEquals(0, result.length);
    }

    // ==========================================================================
    // subSequence() Tests
    // ==========================================================================

    @Test
    public void subSequence_withValidStart_shouldReturnSubSequence() {
        // Arrange
        CharSequence cs = new StringBuffer(TEST_STRING); // "Apache Commons Lang"

        // Act
        CharSequence result = CharSequenceUtils.subSequence(cs, 7);

        // Assert
        assertEquals("Commons Lang", result.toString());
    }

    @Test
    public void subSequence_withStartAtEnd_shouldReturnEmptyString() {
        // Arrange
        CharSequence cs = TEST_STRING;

        // Act
        CharSequence result = CharSequenceUtils.subSequence(cs, cs.length());

        // Assert
        assertEquals(EMPTY_STRING, result.toString());
    }

    @Test
    public void subSequence_withNull_shouldReturnNull() {
        // Act
        CharSequence result = CharSequenceUtils.subSequence(null, 0);

        // Assert
        assertNull(result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void subSequence_withNegativeStart_shouldThrowException() {
        // Arrange
        CharSequence cs = CharBuffer.wrap(new char[]{'a'});

        // Act
        CharSequenceUtils.subSequence(cs, -1);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void subSequence_withStartGreaterThanLength_shouldThrowException() {
        // Arrange
        CharSequence cs = new StringBuffer();

        // Act
        // This effectively calls cs.subSequence(10, 0), which is invalid.
        CharSequenceUtils.subSequence(cs, 10);
    }
}