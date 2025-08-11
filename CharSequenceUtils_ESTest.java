package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import java.nio.CharBuffer;
import org.apache.commons.lang3.CharSequenceUtils;

/**
 * Test suite for CharSequenceUtils utility class.
 * Tests core functionality including indexOf, lastIndexOf, regionMatches, 
 * subSequence, and toCharArray methods.
 */
public class CharSequenceUtilsTest {

    // Test data constants
    private static final String SAMPLE_TEXT = "', is neither of type Map.Entry nor an Array";
    private static final String EMPTY_STRING = "";
    
    // ========== regionMatches Tests ==========
    
    @Test
    public void regionMatches_SameEmptyStringBuilder_ShouldReturnTrue() {
        StringBuilder sb = new StringBuilder();
        boolean result = CharSequenceUtils.regionMatches(sb, false, 0, sb, 0, 0);
        assertTrue("Empty StringBuilder should match itself", result);
    }

    @Test
    public void regionMatches_DifferentStartPositions_ShouldReturnFalse() {
        StringBuffer sb = new StringBuffer();
        boolean result = CharSequenceUtils.regionMatches(sb, false, 0, sb, 6, 0);
        assertFalse("Different start positions should not match", result);
    }

    @Test
    public void regionMatches_NegativeLength_ShouldReturnFalse() {
        boolean result = CharSequenceUtils.regionMatches(SAMPLE_TEXT, false, 117, EMPTY_STRING, -1469, -1231);
        assertFalse("Negative length should return false", result);
    }

    @Test
    public void regionMatches_IgnoreCase_DifferentRegions_ShouldReturnFalse() {
        StringBuilder sb = new StringBuilder(SAMPLE_TEXT);
        boolean result = CharSequenceUtils.regionMatches(sb, true, 0, sb, 7, 16);
        assertFalse("Different regions should not match", result);
    }

    @Test
    public void regionMatches_SameRegion_ShouldReturnTrue() {
        StringBuilder sb = new StringBuilder("', is eithr of typ Map.Entrynor an Array");
        boolean result = CharSequenceUtils.regionMatches("', is eithr of typ Map.Entrynor an Array", false, 1, sb, 1, 7);
        assertTrue("Same region should match", result);
    }

    // ========== lastIndexOf Tests ==========
    
    @Test
    public void lastIndexOf_CharNotFound_ShouldReturnMinusOne() {
        StringBuilder sb = new StringBuilder();
        int result = CharSequenceUtils.lastIndexOf(sb, 1114111, 1114111);
        assertEquals("Character not found should return -1", -1, result);
    }

    @Test
    public void lastIndexOf_CharFoundWithinRange_ShouldReturnCorrectIndex() {
        StringBuilder sb = new StringBuilder(SAMPLE_TEXT);
        int result = CharSequenceUtils.lastIndexOf(sb, 116, 13); // 't' character
        assertEquals("Should find 't' at position 9", 9, result);
    }

    @Test
    public void lastIndexOf_UnicodeCharacter_ShouldReturnCorrectIndex() {
        StringBuilder sb = new StringBuilder();
        sb.appendCodePoint(65536);
        int result = CharSequenceUtils.lastIndexOf(sb, 65536, 0);
        assertEquals("Should find Unicode character at position 0", 0, result);
    }

    @Test
    public void lastIndexOf_SubsequenceMatch_ShouldReturnCorrectIndex() {
        StringBuilder sb1 = new StringBuilder(SAMPLE_TEXT);
        sb1.insert(0, sb1); // Duplicate the string
        StringBuilder sb2 = new StringBuilder(SAMPLE_TEXT);
        int result = CharSequenceUtils.lastIndexOf(sb1, sb2, 98);
        assertEquals("Should find subsequence at position 44", 44, result);
    }

    @Test
    public void lastIndexOf_EmptySubsequence_ShouldReturnEndPosition() {
        CharBuffer buffer = CharBuffer.allocate(2);
        StringBuffer emptyBuffer = new StringBuffer(4);
        int result = CharSequenceUtils.lastIndexOf(buffer, emptyBuffer, 4);
        assertEquals("Empty subsequence should return end position", 2, result);
    }

    // ========== indexOf Tests ==========
    
    @Test
    public void indexOf_CharacterFound_ShouldReturnCorrectIndex() {
        StringBuilder sb = new StringBuilder(SAMPLE_TEXT);
        int result = CharSequenceUtils.indexOf(sb, 116, -2503); // 't' character
        assertEquals("Should find 't' at position 9", 9, result);
    }

    @Test
    public void indexOf_CharacterNotFound_ShouldReturnMinusOne() {
        StringBuilder sb = new StringBuilder();
        int result = CharSequenceUtils.indexOf(sb, -1952, -1952);
        assertEquals("Character not found should return -1", -1, result);
    }

    @Test
    public void indexOf_UnicodeCharacter_ShouldReturnCorrectIndex() {
        StringBuilder sb = new StringBuilder(SAMPLE_TEXT);
        sb.appendCodePoint(1114111);
        int result = CharSequenceUtils.indexOf(sb, 1114111, -897);
        assertEquals("Should find Unicode character at position 44", 44, result);
    }

    @Test
    public void indexOf_EmptySubsequence_ShouldReturnEndPosition() {
        StringBuilder sb = new StringBuilder(SAMPLE_TEXT);
        StringBuffer emptyBuffer = new StringBuffer(0);
        int result = CharSequenceUtils.indexOf(sb, emptyBuffer, Integer.MAX_VALUE);
        assertEquals("Empty subsequence should return end position", 44, result);
    }

    // ========== subSequence Tests ==========
    
    @Test
    public void subSequence_EmptyStringBuilder_ShouldReturnEmptySequence() {
        StringBuilder sb = new StringBuilder(0);
        CharSequence result = CharSequenceUtils.subSequence(sb, 0);
        assertEquals("Empty StringBuilder should return empty sequence", EMPTY_STRING, result);
    }

    @Test
    public void subSequence_ValidInput_ShouldReturnCorrectSubsequence() {
        StringBuilder sb = new StringBuilder(SAMPLE_TEXT);
        CharSequence result = CharSequenceUtils.subSequence(sb, 0);
        assertEquals("Should return the complete sequence", SAMPLE_TEXT, result);
    }

    @Test
    public void subSequence_NullInput_ShouldReturnNull() {
        CharSequence result = CharSequenceUtils.subSequence(null, 1114110);
        assertNull("Null input should return null", result);
    }

    // ========== toCharArray Tests ==========
    
    @Test
    public void toCharArray_StringInput_ShouldReturnCorrectArray() {
        char[] result = CharSequenceUtils.toCharArray(SAMPLE_TEXT);
        assertEquals("Array length should match string length", 44, result.length);
    }

    @Test
    public void toCharArray_EmptyStringBuilder_ShouldReturnEmptyArray() {
        StringBuilder sb = new StringBuilder(EMPTY_STRING);
        char[] result = CharSequenceUtils.toCharArray(sb);
        assertArrayEquals("Empty StringBuilder should return empty array", new char[]{}, result);
    }

    @Test
    public void toCharArray_StringBuilder_ShouldReturnCorrectArray() {
        StringBuilder sb = new StringBuilder(SAMPLE_TEXT);
        char[] result = CharSequenceUtils.toCharArray(sb);
        assertEquals("Array length should match StringBuilder length", 44, result.length);
    }

    // ========== Exception Tests ==========
    
    @Test(expected = StringIndexOutOfBoundsException.class)
    public void subSequence_InvalidIndex_ShouldThrowException() {
        StringBuffer sb = new StringBuffer();
        CharSequenceUtils.subSequence(sb, 407);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void subSequence_NegativeIndex_ShouldThrowException() {
        CharBuffer buffer = CharBuffer.allocate(84);
        CharSequenceUtils.subSequence(buffer, -233);
    }

    @Test(expected = NullPointerException.class)
    public void regionMatches_NullInput_ShouldThrowException() {
        CharSequenceUtils.regionMatches(null, false, 12, null, 12, 12);
    }

    @Test(expected = NullPointerException.class)
    public void indexOf_NullInput_ShouldThrowException() {
        CharSequenceUtils.indexOf(null, 113, 113);
    }

    @Test(expected = NullPointerException.class)
    public void lastIndexOf_NullInput_ShouldThrowException() {
        CharSequenceUtils.lastIndexOf(null, 1041, 0);
    }

    // ========== Edge Cases ==========
    
    @Test
    public void lastIndexOf_NullSequences_ShouldReturnMinusOne() {
        int result = CharSequenceUtils.lastIndexOf(null, null, -1525);
        assertEquals("Null sequences should return -1", -1, result);
    }

    @Test
    public void indexOf_NullSequences_ShouldReturnMinusOne() {
        int result = CharSequenceUtils.indexOf(null, null, 120);
        assertEquals("Null sequences should return -1", -1, result);
    }

    @Test
    public void lastIndexOf_NullTarget_ShouldReturnMinusOne() {
        StringBuilder sb = new StringBuilder(SAMPLE_TEXT);
        int result = CharSequenceUtils.lastIndexOf(null, sb, 1245);
        assertEquals("Null source should return -1", -1, result);
    }

    @Test
    public void indexOf_NullTarget_ShouldReturnMinusOne() {
        StringBuffer sb = new StringBuffer(0);
        int result = CharSequenceUtils.indexOf(sb, null, 0);
        assertEquals("Null target should return -1", -1, result);
    }

    // ========== Constructor Test ==========
    
    @Test
    public void constructor_ShouldCreateInstance() {
        CharSequenceUtils utils = new CharSequenceUtils();
        assertNotNull("Constructor should create instance", utils);
    }
}