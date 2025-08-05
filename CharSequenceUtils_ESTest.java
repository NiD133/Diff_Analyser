package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * Test suite for the CharSequenceUtils class.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class CharSequenceUtils_ESTest extends CharSequenceUtils_ESTest_scaffolding {

    /**
     * Test regionMatches with empty StringBuilder.
     */
    @Test(timeout = 4000)
    public void testRegionMatchesWithEmptyStringBuilder() {
        StringBuilder emptyBuilder = new StringBuilder();
        boolean result = CharSequenceUtils.regionMatches(emptyBuilder, false, 0, emptyBuilder, 0, 0);
        assertTrue(result);
    }

    /**
     * Test regionMatches with StringBuffer and mismatched regions.
     */
    @Test(timeout = 4000)
    public void testRegionMatchesWithStringBuffer() {
        StringBuffer buffer = new StringBuffer();
        boolean result = CharSequenceUtils.regionMatches(buffer, false, 0, buffer, 6, 0);
        assertFalse(result);
    }

    /**
     * Test regionMatches with a string and invalid indices.
     */
    @Test(timeout = 4000)
    public void testRegionMatchesWithInvalidIndices() {
        boolean result = CharSequenceUtils.regionMatches("Sample text", false, 117, "", -1469, -1231);
        assertFalse(result);
    }

    /**
     * Test lastIndexOf with empty StringBuilder.
     */
    @Test(timeout = 4000)
    public void testLastIndexOfWithEmptyStringBuilder() {
        StringBuilder emptyBuilder = new StringBuilder();
        int index = CharSequenceUtils.lastIndexOf(emptyBuilder, 1114111, 1114111);
        assertEquals(-1, index);
    }

    /**
     * Test lastIndexOf with a specific character in a StringBuilder.
     */
    @Test(timeout = 4000)
    public void testLastIndexOfWithSpecificCharacter() {
        StringBuilder builder = new StringBuilder("Sample text");
        int index = CharSequenceUtils.lastIndexOf(builder, 't', 13);
        assertEquals(9, index);
    }

    /**
     * Test lastIndexOf with inserted text in StringBuilder.
     */
    @Test(timeout = 4000)
    public void testLastIndexOfWithInsertedText() {
        StringBuffer buffer = new StringBuffer();
        StringBuilder builder = new StringBuilder(buffer);
        builder.insert(0, "Sample");
        int index = CharSequenceUtils.lastIndexOf(builder, 1403, 1403);
        assertEquals(-1, index);
    }

    /**
     * Test lastIndexOf with appended float value.
     */
    @Test(timeout = 4000)
    public void testLastIndexOfWithAppendedFloat() {
        StringBuilder builder = new StringBuilder();
        builder.append(-1.0F);
        int index = CharSequenceUtils.lastIndexOf(builder, -778, 4);
        assertEquals(-1, index);
    }

    /**
     * Test lastIndexOf with CharBuffer and StringBuilder.
     */
    @Test(timeout = 4000)
    public void testLastIndexOfWithCharBuffer() {
        StringBuffer buffer = new StringBuffer();
        StringBuilder builder = new StringBuilder(buffer);
        builder.insert(0, "Sample");
        char[] charArray = new char[20];
        CharBuffer charBuffer = CharBuffer.wrap(charArray);
        int index = CharSequenceUtils.lastIndexOf(charBuffer, builder, 2750);
        assertEquals(-1, index);
    }

    /**
     * Test lastIndexOf with zero-length StringBuilder.
     */
    @Test(timeout = 4000)
    public void testLastIndexOfWithZeroLengthBuilder() {
        StringBuilder builder = new StringBuilder(0);
        builder.insert(0, 0.0);
        int index = CharSequenceUtils.lastIndexOf("Sample text", builder, 0);
        assertEquals(-1, index);
    }

    /**
     * Test lastIndexOf with appended integer and boolean.
     */
    @Test(timeout = 4000)
    public void testLastIndexOfWithAppendedIntAndBoolean() {
        StringBuilder builder = new StringBuilder("");
        builder.append(1937);
        builder.append(true);
        CharBuffer charBuffer = CharBuffer.wrap((CharSequence) builder);
        builder.insert(0, (CharSequence) charBuffer);
        int index = CharSequenceUtils.lastIndexOf(builder, builder, 0);
        assertEquals(0, index);
    }

    /**
     * Test lastIndexOf with CharBuffer and StringBuffer.
     */
    @Test(timeout = 4000)
    public void testLastIndexOfWithCharBufferAndStringBuffer() {
        StringBuffer buffer = new StringBuffer(4);
        char[] charArray = new char[2];
        CharBuffer charBuffer = CharBuffer.wrap(charArray);
        int index = CharSequenceUtils.lastIndexOf(charBuffer, buffer, 4);
        assertEquals(2, index);
    }

    /**
     * Test lastIndexOf with empty StringBuilder and StringBuilder.
     */
    @Test(timeout = 4000)
    public void testLastIndexOfWithEmptyBuilders() {
        StringBuilder builder1 = new StringBuilder("");
        StringBuilder builder2 = new StringBuilder("");
        builder1.appendCodePoint(0);
        int index = CharSequenceUtils.lastIndexOf(builder1, builder2, 0);
        assertEquals(0, index);
    }

    /**
     * Test lastIndexOf with a string and non-existent substring.
     */
    @Test(timeout = 4000)
    public void testLastIndexOfWithNonExistentSubstring() {
        int index = CharSequenceUtils.lastIndexOf("Sample text", "Non-existent", -731);
        assertEquals(-1, index);
    }

    /**
     * Test indexOf with appended code point.
     */
    @Test(timeout = 4000)
    public void testIndexOfWithAppendedCodePoint() {
        StringBuilder builder = new StringBuilder();
        builder.appendCodePoint(65565);
        int index = CharSequenceUtils.indexOf(builder, 65536, -853);
        assertEquals(-1, index);
    }

    /**
     * Test indexOf with specific character in StringBuilder.
     */
    @Test(timeout = 4000)
    public void testIndexOfWithSpecificCharacter() {
        StringBuilder builder = new StringBuilder("Sample text");
        builder.appendCodePoint(1114111);
        int index = CharSequenceUtils.indexOf(builder, 65536, -897);
        assertEquals(-1, index);
    }

    /**
     * Test indexOf with a specific character in a StringBuilder.
     */
    @Test(timeout = 4000)
    public void testIndexOfWithSpecificCharacterInBuilder() {
        StringBuilder builder = new StringBuilder("Sample text");
        int index = CharSequenceUtils.indexOf(builder, 't', -2503);
        assertEquals(9, index);
    }

    /**
     * Test indexOf with empty StringBuilder.
     */
    @Test(timeout = 4000)
    public void testIndexOfWithEmptyStringBuilder() {
        StringBuilder builder = new StringBuilder();
        int index = CharSequenceUtils.indexOf(builder, -1952, -1952);
        assertEquals(-1, index);
    }

    /**
     * Test lastIndexOf with modified StringBuilder.
     */
    @Test(timeout = 4000)
    public void testLastIndexOfWithModifiedBuilder() {
        StringBuilder builder = new StringBuilder("Sample text");
        builder.insert(32, '?');
        builder.deleteCharAt(5);
        int index = CharSequenceUtils.lastIndexOf("Sample text", builder, 5);
        assertEquals(-1, index);
    }

    /**
     * Test subSequence with empty StringBuilder.
     */
    @Test(timeout = 4000)
    public void testSubSequenceWithEmptyStringBuilder() {
        StringBuilder builder = new StringBuilder(0);
        CharSequence subSequence = CharSequenceUtils.subSequence(builder, 0);
        assertEquals("", subSequence);
    }

    /**
     * Test lastIndexOf with duplicated StringBuilder.
     */
    @Test(timeout = 4000)
    public void testLastIndexOfWithDuplicatedBuilder() {
        StringBuilder builder = new StringBuilder("Sample text");
        builder.insert(0, (CharSequence) builder);
        StringBuilder anotherBuilder = new StringBuilder("Sample text");
        int index = CharSequenceUtils.lastIndexOf(builder, anotherBuilder, 98);
        assertEquals(44, index);
    }

    /**
     * Test lastIndexOf with a specific character in a string.
     */
    @Test(timeout = 4000)
    public void testLastIndexOfWithSpecificCharacterInString() {
        int index = CharSequenceUtils.lastIndexOf("Sample text", 't', 102);
        assertEquals(15, index);
    }

    /**
     * Test indexOf with StringBuilder and empty StringBuffer.
     */
    @Test(timeout = 4000)
    public void testIndexOfWithBuilderAndEmptyBuffer() {
        StringBuilder builder = new StringBuilder("Sample text");
        StringBuffer buffer = new StringBuffer(0);
        int index = CharSequenceUtils.indexOf(builder, buffer, Integer.MAX_VALUE);
        assertEquals(44, index);
    }

    /**
     * Test indexOf with appended code point.
     */
    @Test(timeout = 4000)
    public void testIndexOfWithAppendedCodePointInBuilder() {
        StringBuilder builder = new StringBuilder("Sample text");
        builder.appendCodePoint(1114111);
        int index = CharSequenceUtils.indexOf(builder, 1114111, -897);
        assertEquals(44, index);
    }

    /**
     * Test toCharArray with a string.
     */
    @Test(timeout = 4000)
    public void testToCharArrayWithString() {
        char[] charArray = CharSequenceUtils.toCharArray("Sample text");
        assertEquals(11, charArray.length);
    }

    /**
     * Test toCharArray with empty StringBuilder.
     */
    @Test(timeout = 4000)
    public void testToCharArrayWithEmptyStringBuilder() {
        StringBuilder builder = new StringBuilder("");
        char[] charArray = CharSequenceUtils.toCharArray(builder);
        assertArrayEquals(new char[] {}, charArray);
    }

    /**
     * Test toCharArray with StringBuilder.
     */
    @Test(timeout = 4000)
    public void testToCharArrayWithStringBuilder() {
        StringBuilder builder = new StringBuilder("Sample text");
        char[] charArray = CharSequenceUtils.toCharArray(builder);
        assertEquals(11, charArray.length);
    }

    /**
     * Test subSequence with null CharSequence.
     */
    @Test(timeout = 4000)
    public void testSubSequenceWithNullCharSequence() {
        CharSequence subSequence = CharSequenceUtils.subSequence(null, 1114110);
        assertNull(subSequence);
    }

    /**
     * Test subSequence with StringBuilder.
     */
    @Test(timeout = 4000)
    public void testSubSequenceWithStringBuilder() {
        StringBuilder builder = new StringBuilder("Sample text");
        CharSequence subSequence = CharSequenceUtils.subSequence(builder, 0);
        assertNotNull(subSequence);
        assertEquals("Sample text", subSequence);
    }

    /**
     * Test regionMatches with case-insensitive matching.
     */
    @Test(timeout = 4000)
    public void testRegionMatchesCaseInsensitive() {
        StringBuilder builder = new StringBuilder("Sample text");
        boolean result = CharSequenceUtils.regionMatches(builder, true, 0, builder, 7, 16);
        assertFalse(result);
    }

    /**
     * Test regionMatches with matching regions.
     */
    @Test(timeout = 4000)
    public void testRegionMatchesWithMatchingRegions() {
        StringBuilder builder = new StringBuilder("Sample text");
        boolean result = CharSequenceUtils.regionMatches("Sample text", false, 1, builder, 1, 7);
        assertTrue(result);
    }

    /**
     * Test regionMatches with mismatched regions.
     */
    @Test(timeout = 4000)
    public void testRegionMatchesWithMismatchedRegions() {
        StringBuilder builder = new StringBuilder("Sample text");
        boolean result = CharSequenceUtils.regionMatches(builder, false, 1, "Sample text", 0, 1);
        assertFalse(result);
    }

    /**
     * Test regionMatches with invalid indices.
     */
    @Test(timeout = 4000)
    public void testRegionMatchesWithInvalidIndices() {
        StringBuilder builder = new StringBuilder("Sample text");
        boolean result = CharSequenceUtils.regionMatches(builder, false, 84, builder, 48, 48);
        assertFalse(result);
    }

    /**
     * Test regionMatches with negative length.
     */
    @Test(timeout = 4000)
    public void testRegionMatchesWithNegativeLength() {
        StringBuilder builder = new StringBuilder();
        boolean result = CharSequenceUtils.regionMatches(builder, true, 1096, builder, 1096, -1627);
        assertFalse(result);
    }

    /**
     * Test regionMatches with negative start index.
     */
    @Test(timeout = 4000)
    public void testRegionMatchesWithNegativeStartIndex() {
        StringBuilder builder = new StringBuilder();
        boolean result = CharSequenceUtils.regionMatches(builder, true, 16, builder, -2908, 16);
        assertFalse(result);
    }

    /**
     * Test regionMatches with negative indices.
     */
    @Test(timeout = 4000)
    public void testRegionMatchesWithNegativeIndices() {
        StringBuilder builder = new StringBuilder("Sample text");
        boolean result = CharSequenceUtils.regionMatches("Sample text", false, -3004, builder, 65, -93);
        assertFalse(result);
    }

    /**
     * Test lastIndexOf with appended code point.
     */
    @Test(timeout = 4000)
    public void testLastIndexOfWithAppendedCodePoint() {
        StringBuilder builder = new StringBuilder();
        builder.appendCodePoint(65536);
        int index = CharSequenceUtils.lastIndexOf(builder, 65536, 0);
        assertEquals(0, index);
    }

    /**
     * Test lastIndexOf with non-existent character.
     */
    @Test(timeout = 4000)
    public void testLastIndexOfWithNonExistentCharacter() {
        StringBuilder builder = new StringBuilder("Sample text");
        int index = CharSequenceUtils.lastIndexOf(builder, 1114110, 25);
        assertEquals(-1, index);
    }

    /**
     * Test lastIndexOf with non-existent code point.
     */
    @Test(timeout = 4000)
    public void testLastIndexOfWithNonExistentCodePoint() {
        StringBuilder builder = new StringBuilder();
        builder.appendCodePoint(65571);
        int index = CharSequenceUtils.lastIndexOf(builder, 65541, 0);
        assertEquals(-1, index);
    }

    /**
     * Test lastIndexOf with empty StringBuilder and non-existent character.
     */
    @Test(timeout = 4000)
    public void testLastIndexOfWithEmptyBuilderAndNonExistentCharacter() {
        StringBuffer buffer = new StringBuffer();
        StringBuilder builder = new StringBuilder(buffer);
        int index = CharSequenceUtils.lastIndexOf(builder, 1114122, 0);
        assertEquals(-1, index);
    }

    /**
     * Test lastIndexOf with negative indices.
     */
    @Test(timeout = 4000)
    public void testLastIndexOfWithNegativeIndices() {
        StringBuilder builder = new StringBuilder();
        int index = CharSequenceUtils.lastIndexOf(builder, -76, -76);
        assertEquals(-1, index);
    }

    /**
     * Test lastIndexOf with non-existent character in a string.
     */
    @Test(timeout = 4000)
    public void testLastIndexOfWithNonExistentCharacterInString() {
        int index = CharSequenceUtils.lastIndexOf("Sample text", 1203, 36);
        assertEquals(-1, index);
    }

    /**
     * Test lastIndexOf with null CharSequence.
     */
    @Test(timeout = 4000)
    public void testLastIndexOfWithNullCharSequence() {
        try {
            CharSequenceUtils.lastIndexOf(null, 1041, 0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    /**
     * Test lastIndexOf with modified StringBuilder.
     */
    @Test(timeout = 4000)
    public void testLastIndexOfWithModifiedStringBuilder() {
        StringBuilder builder = new StringBuilder("Sample text");
        builder.delete(22, 789);
        int index = CharSequenceUtils.lastIndexOf("Sample text", builder, 22);
        assertEquals(0, index);
    }

    /**
     * Test lastIndexOf with deleted character in StringBuilder.
     */
    @Test(timeout = 4000)
    public void testLastIndexOfWithDeletedCharacter() {
        StringBuilder builder = new StringBuilder("Sample text");
        builder.deleteCharAt(1);
        int index = CharSequenceUtils.lastIndexOf("Sample text", builder, 1);
        assertEquals(-1, index);
    }

    /**
     * Test lastIndexOf with StringBuffer containing a character.
     */
    @Test(timeout = 4000)
    public void testLastIndexOfWithStringBufferContainingCharacter() {
        StringBuffer buffer = new StringBuffer();
        buffer.append('2');
        int index = CharSequenceUtils.lastIndexOf(buffer, buffer, '2');
        assertEquals(0, index);
    }

    /**
     * Test lastIndexOf with CharBuffer.
     */
    @Test(timeout = 4000)
    public void testLastIndexOfWithCharBuffer() {
        CharBuffer charBuffer = CharBuffer.allocate(3);
        int index = CharSequenceUtils.lastIndexOf(charBuffer, charBuffer, 3);
        assertEquals(0, index);
    }

    /**
     * Test lastIndexOf with empty StringBuilder and CharBuffer.
     */
    @Test(timeout = 4000)
    public void testLastIndexOfWithEmptyBuilderAndCharBuffer() {
        StringBuilder builder = new StringBuilder();
        CharBuffer charBuffer = CharBuffer.allocate(30);
        int index = CharSequenceUtils.lastIndexOf(builder, charBuffer, 30);
        assertEquals(-1, index);
    }

    /**
     * Test lastIndexOf with empty StringBuffer and negative index.
     */
    @Test(timeout = 4000)
    public void testLastIndexOfWithEmptyBufferAndNegativeIndex() {
        StringBuffer buffer = new StringBuffer();
        int index = CharSequenceUtils.lastIndexOf(buffer, buffer, -459);
        assertEquals(-1, index);
    }

    /**
     * Test lastIndexOf with CharBuffer and non-existent string.
     */
    @Test(timeout = 4000)
    public void testLastIndexOfWithCharBufferAndNonExistentString() {
        CharBuffer charBuffer = CharBuffer.allocate(65536);
        try {
            CharSequenceUtils.lastIndexOf(charBuffer, "Non-existent string", 65536);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    /**
     * Test lastIndexOf with empty StringBuffer and non-existent character.
     */
    @Test(timeout = 4000)
    public void testLastIndexOfWithEmptyBufferAndNonExistentCharacter() {
        StringBuffer buffer = new StringBuffer(40);
        int index = CharSequenceUtils.lastIndexOf(buffer, "'", 40);
        assertEquals(-1, index);
    }

    /**
     * Test lastIndexOf with StringBuilder and identical string.
     */
    @Test(timeout = 4000)
    public void testLastIndexOfWithBuilderAndIdenticalString() {
        StringBuilder builder = new StringBuilder("Sample text");
        int index = CharSequenceUtils.lastIndexOf(builder, "Sample text", 1041);
        assertEquals(0, index);
    }

    /**
     * Test lastIndexOf with null CharSequence and StringBuilder.
     */
    @Test(timeout = 4000)
    public void testLastIndexOfWithNullCharSequenceAndBuilder() {
        StringBuilder builder = new StringBuilder("Sample text");
        int index = CharSequenceUtils.lastIndexOf(null, builder, 1245);
        assertEquals(-1, index);
    }

    /**
     * Test lastIndexOf with null CharSequences.
     */
    @Test(timeout = 4000)
    public void testLastIndexOfWithNullCharSequences() {
        int index = CharSequenceUtils.lastIndexOf(null, null, -1525);
        assertEquals(-1, index);
    }

    /**
     * Test indexOf with appended code point and inserted long value.
     */
    @Test(timeout = 4000)
    public void testIndexOfWithAppendedCodePointAndInsertedLong() {
        StringBuilder builder = new StringBuilder();
        builder.appendCodePoint(65536);
        builder.insert(1, 1973L);
        int index = CharSequenceUtils.indexOf(builder, 65536, -897);
        assertEquals(-1, index);
    }

    /**
     * Test indexOf with appended code point.
     */
    @Test(timeout = 4000)
    public void testIndexOfWithAppendedCodePoint() {
        StringBuilder builder = new StringBuilder();
        builder.appendCodePoint(65571);
        int index = CharSequenceUtils.indexOf(builder, 65571, -2194);
        assertEquals(0, index);
    }

    /**
     * Test indexOf with empty StringBuilder and maximum integer values.
     */
    @Test(timeout = 4000)
    public void testIndexOfWithEmptyBuilderAndMaxIntValues() {
        StringBuilder builder = new StringBuilder();
        int index = CharSequenceUtils.indexOf(builder, Integer.MAX_VALUE, Integer.MAX_VALUE);
        assertEquals(-1, index);
    }

    /**
     * Test indexOf with appended code point and zero start index.
     */
    @Test(timeout = 4000)
    public void testIndexOfWithAppendedCodePointAndZeroStartIndex() {
        StringBuilder builder = new StringBuilder();
        builder.appendCodePoint(0);
        int index = CharSequenceUtils.indexOf(builder, 0, 0);
        assertEquals(0, index);
    }

    /**
     * Test indexOf with inserted CharBuffer.
     */
    @Test(timeout = 4000)
    public void testIndexOfWithInsertedCharBuffer() {
        StringBuilder builder = new StringBuilder();
        CharBuffer charBuffer = CharBuffer.allocate(65536);
        builder.insert(0, (CharSequence) charBuffer);
        try {
            CharSequenceUtils.indexOf(builder, -1790, 3507);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    /**
     * Test indexOf with empty StringBuilder and negative indices.
     */
    @Test(timeout = 4000)
    public void testIndexOfWithEmptyBuilderAndNegativeIndices() {
        StringBuilder builder = new StringBuilder();
        int index = CharSequenceUtils.indexOf(builder, -1790, 3507);
        assertEquals(-1, index);
    }

    /**
     * Test indexOf with non-existent character in a string.
     */
    @Test(timeout = 4000)
    public void testIndexOfWithNonExistentCharacterInString() {
        int index = CharSequenceUtils.indexOf("Sample text", 84, 1041);
        assertEquals(-1, index);
    }

    /**
     * Test indexOf with empty StringBuilder and non-existent code point.
     */
    @Test(timeout = 4000)
    public void testIndexOfWithEmptyBuilderAndNonExistentCodePoint() {
        StringBuilder builder = new StringBuilder();
        int index = CharSequenceUtils.indexOf(builder, 65571, -2194);
        assertEquals(-1, index);
    }

    /**
     * Test indexOf with empty StringBuffer and StringBuilder.
     */
    @Test(timeout = 4000)
    public void testIndexOfWithEmptyBufferAndBuilder() {
        StringBuffer buffer = new StringBuffer();
        StringBuilder builder = new StringBuilder(buffer);
        int index = CharSequenceUtils.indexOf(buffer, builder, -1231);
        assertEquals(0, index);
    }

    /**
     * Test indexOf with CharBuffer and StringBuilder.
     */
    @Test(timeout = 4000)
    public void testIndexOfWithCharBufferAndBuilder() {
        StringBuilder builder = new StringBuilder("Sample text");
        char[] charArray = new char[5];
        CharBuffer charBuffer = CharBuffer.wrap(charArray);
        int index = CharSequenceUtils.indexOf(charBuffer, builder, -1231);
        assertEquals(-1, index);
    }

    /**
     * Test indexOf with empty StringBuilder and empty string.
     */
    @Test(timeout = 4000)
    public void testIndexOfWithEmptyBuilderAndEmptyString() {
        StringBuilder builder = new StringBuilder("");
        int index = CharSequenceUtils.indexOf("", builder, 8);
        assertEquals(0, index);
    }

    /**
     * Test indexOf with empty StringBuffer and null CharSequence.
     */
    @Test(timeout = 4000)
    public void testIndexOfWithEmptyBufferAndNullCharSequence() {
        StringBuffer buffer = new StringBuffer(0);
        int index = CharSequenceUtils.indexOf(buffer, null, 0);
        assertEquals(-1, index);
    }

    /**
     * Test indexOf with null CharSequences.
     */
    @Test(timeout = 4000)
    public void testIndexOfWithNullCharSequences() {
        int index = CharSequenceUtils.indexOf(null, null, 120);
        assertEquals(-1, index);
    }

    /**
     * Test lastIndexOf with modified StringBuilder.
     */
    @Test(timeout = 4000)
    public void testLastIndexOfWithModifiedStringBuilder() {
        StringBuilder builder = new StringBuilder("Sample text");
        builder.deleteCharAt(22);
        int index = CharSequenceUtils.lastIndexOf("Sample text", builder, 22);
        assertEquals(-1, index);
    }

    /**
     * Test instantiation of CharSequenceUtils.
     */
    @Test(timeout = 4000)
    public void testCharSequenceUtilsInstantiation() {
        CharSequenceUtils charSequenceUtils = new CharSequenceUtils();
    }
}