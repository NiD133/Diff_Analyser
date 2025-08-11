package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import org.apache.commons.lang3.CharSequenceUtils;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class CharSequenceUtils_ESTest extends CharSequenceUtils_ESTest_scaffolding {

    // regionMatches tests
    @Test(timeout = 4000)
    public void testRegionMatches_WithEmptyBuildersAndZeroLength_ReturnsTrue() {
        StringBuilder builder = new StringBuilder();
        assertTrue("Region matches with zero length should return true", 
            CharSequenceUtils.regionMatches(builder, false, 0, builder, 0, 0));
    }

    @Test(timeout = 4000)
    public void testRegionMatches_WithStartIndexExceedingLength_ReturnsFalse() {
        StringBuffer buffer = new StringBuffer();
        assertFalse("Start index exceeds length, should return false", 
            CharSequenceUtils.regionMatches(buffer, false, 0, buffer, 6, 0));
    }

    @Test(timeout = 4000)
    public void testRegionMatches_WithNegativeLength_ReturnsFalse() {
        assertFalse("Negative length should return false", 
            CharSequenceUtils.regionMatches("', is neither of type Map.Entry nor an Array", false, 117, "", -1469, -1231));
    }

    @Test(timeout = 4000)
    public void testRegionMatches_WithMismatchedStartPositions_ReturnsFalse() {
        StringBuilder builder = new StringBuilder("', is neither of type Map.Entry nor an Array");
        assertFalse("Mismatched start positions should return false", 
            CharSequenceUtils.regionMatches(builder, true, 0, builder, 7, 16));
    }

    @Test(timeout = 4000)
    public void testRegionMatches_WithMatchingSubstrings_ReturnsTrue() {
        StringBuilder builder = new StringBuilder("', is eithr of typ Map.Entrynor an Array");
        assertTrue("Matching substrings should return true", 
            CharSequenceUtils.regionMatches("', is eithr of typ Map.Entrynor an Array", false, 1, builder, 1, 7));
    }

    @Test(timeout = 4000)
    public void testRegionMatches_WithDifferentStartPositions_ReturnsFalse() {
        StringBuilder builder = new StringBuilder("', is either of typ Map.Entry nor anArray");
        assertFalse("Different start positions should return false", 
            CharSequenceUtils.regionMatches(builder, false, 1, "', is either of typ Map.Entry nor anArray", 0, 1));
    }

    @Test(timeout = 4000)
    public void testRegionMatches_WithStartIndexBeyondLength_ReturnsFalse() {
        StringBuilder builder = new StringBuilder("', is neither of type Map.Entry nor an Array");
        assertFalse("Start index beyond length should return false", 
            CharSequenceUtils.regionMatches(builder, false, 84, builder, 48, 48));
    }

    @Test(timeout = 4000)
    public void testRegionMatches_WithNegativeCount_ReturnsFalse() {
        StringBuilder builder = new StringBuilder();
        assertFalse("Negative count should return false", 
            CharSequenceUtils.regionMatches(builder, true, 1096, builder, 1096, -1627));
    }

    @Test(timeout = 4000)
    public void testRegionMatches_WithNegativeStartInSecondSequence_ReturnsFalse() {
        StringBuilder builder = new StringBuilder();
        assertFalse("Negative start in second sequence should return false", 
            CharSequenceUtils.regionMatches(builder, true, 16, builder, -2908, 16));
    }

    @Test(timeout = 4000)
    public void testRegionMatches_WithNegativeStartAndCount_ReturnsFalse() {
        StringBuilder builder = new StringBuilder("', is neither of type Map.Entry nor an Array");
        assertFalse("Negative start and count should return false", 
            CharSequenceUtils.regionMatches("', is neither of type Map.Entry nor an Array", false, -3004, builder, 65, -93));
    }

    // lastIndexOf tests (CharSequence, int, int)
    @Test(timeout = 4000)
    public void testLastIndexOf_CharInt_WithHighCodePointInEmptyBuilder_ReturnsMinusOne() {
        StringBuilder builder = new StringBuilder();
        assertEquals("High code point not in empty builder should return -1", 
            -1, CharSequenceUtils.lastIndexOf(builder, 1114111, 1114111));
    }

    @Test(timeout = 4000)
    public void testLastIndexOf_CharInt_WithCharFoundInBuilder_ReturnsPosition() {
        StringBuilder builder = new StringBuilder("', is neither of type Map.Entry nor an Array");
        assertEquals("'t' should be found at position 9", 
            9, CharSequenceUtils.lastIndexOf(builder, 116, 13));
    }

    @Test(timeout = 4000)
    public void testLastIndexOf_CharInt_WithNonExistentHighCodePoint_ReturnsMinusOne() {
        StringBuffer buffer = new StringBuffer();
        StringBuilder builder = new StringBuilder(buffer).insert(0, "6KY>-,V");
        assertEquals("Non-existent high code point should return -1", 
            -1, CharSequenceUtils.lastIndexOf(builder, 1403, 1403));
    }

    @Test(timeout = 4000)
    public void testLastIndexOf_CharInt_WithNegativeCharInBuilder_ReturnsMinusOne() {
        StringBuilder builder = new StringBuilder();
        builder.append(-1.0F);
        assertEquals("Negative char not in builder should return -1", 
            -1, CharSequenceUtils.lastIndexOf(builder, -778, 4));
    }

    @Test(timeout = 4000)
    public void testLastIndexOf_CharInt_WithSupplementaryCodePoint_ReturnsPosition() {
        StringBuilder builder = new StringBuilder();
        builder.appendCodePoint(65536);
        assertEquals("Supplementary code point should be at position 0", 
            0, CharSequenceUtils.lastIndexOf(builder, 65536, 0));
    }

    @Test(timeout = 4000)
    public void testLastIndexOf_CharInt_WithHighCodePointNotInBuilder_ReturnsMinusOne() {
        StringBuilder builder = new StringBuilder("', ss }either of typp Map.Entry nor an Array");
        assertEquals("High code point not in builder should return -1", 
            -1, CharSequenceUtils.lastIndexOf(builder, 1114110, 25));
    }

    @Test(timeout = 4000)
    public void testLastIndexOf_CharInt_WithAnotherHighCodePoint_ReturnsMinusOne() {
        StringBuilder builder = new StringBuilder();
        builder.appendCodePoint(65571);
        assertEquals("Specific high code point not in builder should return -1", 
            -1, CharSequenceUtils.lastIndexOf(builder, 65541, 0));
    }

    @Test(timeout = 4000)
    public void testLastIndexOf_CharInt_WithCharBeyondMax_ReturnsMinusOne() {
        StringBuffer buffer = new StringBuffer();
        StringBuilder builder = new StringBuilder(buffer);
        assertEquals("Code point beyond max should return -1", 
            -1, CharSequenceUtils.lastIndexOf(builder, 1114122, 0));
    }

    @Test(timeout = 4000)
    public void testLastIndexOf_CharInt_WithNegativeChar_ReturnsMinusOne() {
        StringBuilder builder = new StringBuilder();
        assertEquals("Negative char should return -1", 
            -1, CharSequenceUtils.lastIndexOf(builder, -76, -76));
    }

    @Test(timeout = 4000)
    public void testLastIndexOf_CharInt_WithCharNotFoundInString_ReturnsMinusOne() {
        assertEquals("Char not found in string should return -1", 
            -1, CharSequenceUtils.lastIndexOf("', is either of typ Map.Entry nor an Array", 1203, 36));
    }

    @Test(timeout = 4000)
    public void testLastIndexOf_CharInt_WithNullCharSequence_ThrowsNullPointerException() {
        try {
            CharSequenceUtils.lastIndexOf(null, 1041, 0);
            fail("Expected NullPointerException for null char sequence");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    // lastIndexOf tests (CharSequence, CharSequence, int)
    @Test(timeout = 4000)
    public void testLastIndexOf_CharSequence_WithNonMatchingSequence_ReturnsMinusOne() {
        char[] chars = new char[20];
        CharBuffer buffer = CharBuffer.wrap(chars);
        StringBuilder builder = new StringBuilder(new StringBuffer()).insert(0, "6KY>-,V");
        assertEquals("Sequence not found should return -1", 
            -1, CharSequenceUtils.lastIndexOf(buffer, builder, 2750));
    }

    @Test(timeout = 4000)
    public void testLastIndexOf_CharSequence_WithNonMatchingStringAndBuilder_ReturnsMinusOne() {
        StringBuilder builder = new StringBuilder(0).insert(0, 0.0);
        assertEquals("String not found in builder should return -1", 
            -1, CharSequenceUtils.lastIndexOf("@3AXK#eny?[;j'", builder, 0));
    }

    @Test(timeout = 4000)
    public void testLastIndexOf_CharSequence_WithMatchingSequenceAtStart_ReturnsZero() {
        StringBuilder builder = new StringBuilder("");
        builder.append(1937).append(true);
        CharBuffer buffer = CharBuffer.wrap(builder);
        StringBuilder builder2 = builder.insert(0, buffer);
        assertEquals("Sequence should be found at position 0", 
            0, CharSequenceUtils.lastIndexOf(builder, builder2, 0));
    }

    @Test(timeout = 4000)
    public void testLastIndexOf_CharSequence_WithEmptyBufferInBuilder_ReturnsLength() {
        StringBuffer buffer = new StringBuffer(4);
        char[] chars = new char[2];
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        assertEquals("Empty buffer in builder should return buffer length", 
            2, CharSequenceUtils.lastIndexOf(charBuffer, buffer, 4));
    }

    @Test(timeout = 4000)
    public void testLastIndexOf_CharSequence_WithEmptySequenceInNonEmptyBuilder_ReturnsMatchPosition() {
        StringBuilder builder = new StringBuilder("");
        StringBuilder emptyBuilder = new StringBuilder("");
        builder.appendCodePoint(0);
        assertEquals("Empty sequence in non-empty builder should return 0", 
            0, CharSequenceUtils.lastIndexOf(builder, emptyBuilder, 0));
    }

    @Test(timeout = 4000)
    public void testLastIndexOf_CharSequence_WithNegativeStartIndex_ReturnsMinusOne() {
        assertEquals("Negative start index should return -1", 
            -1, CharSequenceUtils.lastIndexOf("', is neither of type Map.Entry nor an Array", "/VQAuqQP6w", -731));
    }

    @Test(timeout = 4000)
    public void testLastIndexOf_CharSequence_WithSelfInLargeSequence_ReturnsStartPosition() {
        StringBuilder builder = new StringBuilder("', is neither of type Map.Entry nor an Array");
        builder.insert(0, builder);
        StringBuilder searchBuilder = new StringBuilder("', is neither of type Map.Entry nor an Array");
        assertEquals("Should find original sequence at position 44", 
            44, CharSequenceUtils.lastIndexOf(builder, searchBuilder, 98));
    }

    @Test(timeout = 4000)
    public void testLastIndexOf_CharSequence_WithMatchingSequence_ReturnsPosition() {
        StringBuilder builder = new StringBuilder("', is neither of type Map.Entry nor an Array");
        assertEquals("Should find sequence at start", 
            0, CharSequenceUtils.lastIndexOf(builder, builder, 98));
    }

    @Test(timeout = 4000)
    public void testLastIndexOf_CharSequence_WithSingleCharSequence_ReturnsPosition() {
        StringBuffer buffer = new StringBuffer();
        buffer.append('2');
        assertEquals("Char '2' should be at position 0", 
            0, CharSequenceUtils.lastIndexOf(buffer, buffer, '2'));
    }

    @Test(timeout = 4000)
    public void testLastIndexOf_CharSequence_WithEmptyBuilder_ReturnsMinusOne() {
        StringBuilder builder = new StringBuilder();
        CharBuffer buffer = CharBuffer.allocate(30);
        assertEquals("Empty builder should return -1", 
            -1, CharSequenceUtils.lastIndexOf(builder, buffer, 30));
    }

    @Test(timeout = 4000)
    public void testLastIndexOf_CharSequence_WithNegativeStartIndexForBuffer_ReturnsMinusOne() {
        StringBuffer buffer = new StringBuffer();
        assertEquals("Negative start index should return -1", 
            -1, CharSequenceUtils.lastIndexOf(buffer, buffer, -459));
    }

    @Test(timeout = 4000)
    public void testLastIndexOf_CharSequence_WithStringNotFound_ReturnsMinusOne() {
        StringBuffer buffer = new StringBuffer(40);
        assertEquals("String not in buffer should return -1", 
            -1, CharSequenceUtils.lastIndexOf(buffer, "'", 40));
    }

    @Test(timeout = 4000)
    public void testLastIndexOf_CharSequence_WithFullMatch_ReturnsZero() {
        StringBuilder builder = new StringBuilder("', is neither of type Map.Entry nor an Array");
        assertEquals("Full match should return 0", 
            0, CharSequenceUtils.lastIndexOf(builder, "', is neither of type Map.Entry nor an Array", 1041));
    }

    @Test(timeout = 4000)
    public void testLastIndexOf_CharSequence_WithNullTarget_ReturnsMinusOne() {
        StringBuilder builder = new StringBuilder("', is neither of type Map.Entry nor an Array");
        assertEquals("Null target should return -1", 
            -1, CharSequenceUtils.lastIndexOf(null, builder, 1245));
    }

    @Test(timeout = 4000)
    public void testLastIndexOf_CharSequence_WithNullSourceAndTarget_ReturnsMinusOne() {
        assertEquals("Null source and target should return -1", 
            -1, CharSequenceUtils.lastIndexOf(null, null, -1525));
    }

    // indexOf tests (CharSequence, int, int)
    @Test(timeout = 4000)
    public void testIndexOf_CharInt_WithHighCodePointInBuilder_ReturnsMinusOne() {
        StringBuilder builder = new StringBuilder();
        builder.appendCodePoint(65565);
        assertEquals("High code point not found should return -1", 
            -1, CharSequenceUtils.indexOf(builder, 65536, -853));
    }

    @Test(timeout = 4000)
    public void testIndexOf_CharInt_WithHighCodePointInNonEmptyBuilder_ReturnsMinusOne() {
        StringBuilder builder = new StringBuilder(";i]b}Z<[yv");
        builder.appendCodePoint(1114111);
        assertEquals("High code point not found should return -1", 
            -1, CharSequenceUtils.indexOf(builder, 65536, -897));
    }

    @Test(timeout = 4000)
    public void testIndexOf_CharInt_WithCharFoundInBuilder_ReturnsPosition() {
        StringBuilder builder = new StringBuilder("', is neither of type Map.Entry nor an Array");
        assertEquals("'t' should be found at position 9", 
            9, CharSequenceUtils.indexOf(builder, 116, -2503));
    }

    @Test(timeout = 4000)
    public void testIndexOf_CharInt_WithNegativeCharInEmptyBuilder_ReturnsMinusOne() {
        StringBuilder builder = new StringBuilder();
        assertEquals("Negative char in empty builder should return -1", 
            -1, CharSequenceUtils.indexOf(builder, -1952, -1952));
    }

    @Test(timeout = 4000)
    public void testIndexOf_CharInt_WithSupplementaryCodePoint_ReturnsPosition() {
        StringBuilder builder = new StringBuilder();
        builder.appendCodePoint(65571);
        assertEquals("Supplementary code point should be at position 0", 
            0, CharSequenceUtils.indexOf(builder, 65571, -2194));
    }

    @Test(timeout = 4000)
    public void testIndexOf_CharInt_WithCharBeyondMax_ReturnsMinusOne() {
        StringBuilder builder = new StringBuilder();
        assertEquals("Char beyond max should return -1", 
            -1, CharSequenceUtils.indexOf(builder, Integer.MAX_VALUE, Integer.MAX_VALUE));
    }

    @Test(timeout = 4000)
    public void testIndexOf_CharInt_WithNullChar_ReturnsPosition() {
        StringBuilder builder = new StringBuilder();
        builder.appendCodePoint(0);
        assertEquals("Null char should be at position 0", 
            0, CharSequenceUtils.indexOf(builder, 0, 0));
    }

    @Test(timeout = 4000)
    public void testIndexOf_CharInt_WithCharNotFoundBeyondEnd_ReturnsMinusOne() {
        assertEquals("Char not found beyond end should return -1", 
            -1, CharSequenceUtils.indexOf("', is neither of type Map.Entry nor an Array", 84, 1041));
    }

    @Test(timeout = 4000)
    public void testIndexOf_CharInt_WithHighCodePointNotInBuilder_ReturnsMinusOne() {
        StringBuilder builder = new StringBuilder();
        assertEquals("High code point not in builder should return -1", 
            -1, CharSequenceUtils.indexOf(builder, 65571, -2194));
    }

    // indexOf tests (CharSequence, CharSequence, int)
    @Test(timeout = 4000)
    public void testIndexOf_CharSequence_WithEmptyTargetInEmptySource_ReturnsZero() {
        StringBuffer buffer = new StringBuffer();
        StringBuilder builder = new StringBuilder(buffer);
        assertEquals("Empty target in empty source should return 0", 
            0, CharSequenceUtils.indexOf(buffer, builder, -1231));
    }

    @Test(timeout = 4000)
    public void testIndexOf_CharSequence_WithLargeSequenceInSmallBuffer_ReturnsMinusOne() {
        StringBuilder builder = new StringBuilder("', is neither of type Map.Entry nor an Array");
        char[] chars = new char[5];
        CharBuffer buffer = CharBuffer.wrap(chars);
        assertEquals("Large sequence not in small buffer should return -1", 
            -1, CharSequenceUtils.indexOf(buffer, builder, -1231));
    }

    @Test(timeout = 4000)
    public void testIndexOf_CharSequence_WithEmptyTargetInNonEmptySource_ReturnsZero() {
        StringBuilder builder = new StringBuilder("");
        assertEquals("Empty target in non-empty source should return 0", 
            0, CharSequenceUtils.indexOf("", builder, 8));
    }

    @Test(timeout = 4000)
    public void testIndexOf_CharSequence_WithNullTarget_ReturnsMinusOne() {
        StringBuffer buffer = new StringBuffer(0);
        assertEquals("Null target should return -1", 
            -1, CharSequenceUtils.indexOf(buffer, null, 0));
    }

    @Test(timeout = 4000)
    public void testIndexOf_CharSequence_WithNullSourceAndTarget_ReturnsMinusOne() {
        assertEquals("Null source and target should return -1", 
            -1, CharSequenceUtils.indexOf(null, null, 120));
    }

    // subSequence tests
    @Test(timeout = 4000)
    public void testSubSequence_WithValidStartIndex_ReturnsSubsequence() {
        StringBuilder builder = new StringBuilder("");
        CharSequence result = CharSequenceUtils.subSequence(builder, 0);
        assertEquals("Subsequence should be empty string", "", result);
    }

    @Test(timeout = 4000)
    public void testSubSequence_WithNull_ReturnsNull() {
        assertNull("Null input should return null", CharSequenceUtils.subSequence(null, 1114110));
    }

    @Test(timeout = 4000)
    public void testSubSequence_WithFullSequence_ReturnsFullSequence() {
        StringBuilder builder = new StringBuilder("', is neither of type Map.Entry nor an Array");
        CharSequence result = CharSequenceUtils.subSequence(builder, 0);
        assertEquals("Subsequence should be full sequence", 
            "', is neither of type Map.Entry nor an Array", result);
    }

    @Test(timeout = 4000)
    public void testSubSequence_WithIndexOutOfBounds_ThrowsException() {
        StringBuffer buffer = new StringBuffer();
        try {
            CharSequenceUtils.subSequence(buffer, 407);
            fail("Expected StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void testSubSequence_WithNegativeIndex_ThrowsException() {
        CharBuffer buffer = CharBuffer.allocate(84);
        try {
            CharSequenceUtils.subSequence(buffer, -233);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    // toCharArray tests
    @Test(timeout = 4000)
    public void testToCharArray_WithString_ReturnsCorrectArray() {
        char[] result = CharSequenceUtils.toCharArray("', is neither of type Map.Entry nor an Array");
        assertEquals("Array length should match string length", 44, result.length);
    }

    @Test(timeout = 4000)
    public void testToCharArray_WithEmptyBuilder_ReturnsEmptyArray() {
        StringBuilder builder = new StringBuilder("");
        char[] result = CharSequenceUtils.toCharArray(builder);
        assertArrayEquals("Empty builder should return empty array", new char[]{}, result);
    }

    @Test(timeout = 4000)
    public void testToCharArray_WithNonEmptyBuilder_ReturnsCorrectArray() {
        StringBuilder builder = new StringBuilder("', is neither of type Map.Entry nor an Array");
        char[] result = CharSequenceUtils.toCharArray(builder);
        assertEquals("Array length should match builder length", 44, result.length);
    }

    @Test(timeout = 4000)
    public void testToCharArray_WithLargeBuffer_ThrowsException() {
        CharBuffer buffer = CharBuffer.allocate(65536);
        try {
            CharSequenceUtils.toCharArray(buffer);
            fail("Expected exception for large buffer");
        } catch (Exception e) {
            // Expected (specific exception depends on implementation)
        }
    }

    // regionMatches exception test
    @Test(timeout = 4000)
    public void testRegionMatches_WithNullInput_ThrowsNullPointerException() {
        try {
            CharSequenceUtils.regionMatches(null, false, 12, null, 12, 12);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    // indexOf exception test
    @Test(timeout = 4000)
    public void testIndexOf_CharInt_WithNullInput_ThrowsNullPointerException() {
        try {
            CharSequenceUtils.indexOf(null, 113, 113);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    // Constructor test
    @Test(timeout = 4000)
    public void testCharSequenceUtils_Instantiation_Succeeds() {
        // Testing that the constructor can be called (deprecated but should work)
        new CharSequenceUtils();
    }
}