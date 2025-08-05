package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.lang3.CharRange;
import org.apache.commons.lang3.CharSet;

/**
 * Test suite for CharSet class functionality.
 * Tests cover construction, character containment, equality, and utility methods.
 */
public class CharSetTest {

    // ========== Constructor Tests ==========
    
    @Test
    public void testConstructorWithNullArray_ShouldThrowNullPointerException() {
        try {
            new CharSet((String[]) null);
            fail("Expected NullPointerException when constructing CharSet with null array");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test
    public void testConstructorWithEmptyArray_ShouldCreateEmptyCharSet() {
        String[] emptyArray = new String[3]; // All elements are null
        CharSet charSet = new CharSet(emptyArray);
        
        CharRange[] ranges = charSet.getCharRanges();
        assertEquals("CharSet created from empty array should have no ranges", 0, ranges.length);
        assertFalse("Empty CharSet should not contain any characters", charSet.contains('A'));
    }

    // ========== Factory Method Tests ==========
    
    @Test
    public void testGetInstanceWithNullArray_ShouldReturnNull() {
        CharSet result = CharSet.getInstance((String[]) null);
        assertNull("getInstance with null array should return null", result);
    }

    @Test
    public void testGetInstanceWithEmptyArray_ShouldReturnValidCharSet() {
        String[] emptyArray = new String[9]; // All elements are null
        CharSet charSet = CharSet.getInstance(emptyArray);
        
        assertNotNull("getInstance with empty array should return valid CharSet", charSet);
        assertEquals("Empty CharSet should have string representation '[]'", "[]", charSet.toString());
    }

    @Test
    public void testGetInstanceWithAlphabeticRange_ShouldCreateRangeCharSet() {
        String[] rangeArray = {"a-z"};
        CharSet charSet = CharSet.getInstance(rangeArray);
        
        assertNotNull("getInstance with 'a-z' should return valid CharSet", charSet);
    }

    @Test
    public void testGetInstanceWithNegatedPattern_ShouldCreateNegatedCharSet() {
        String[] negatedArray = {"^n%R"};
        CharSet charSet = CharSet.getInstance(negatedArray);
        
        assertNotNull("getInstance with negated pattern should return valid CharSet", charSet);
    }

    @Test
    public void testGetInstanceWithComplexPattern_ShouldCreateCharSet() {
        String[] complexArray = new String[9];
        complexArray[2] = "pDqg&f|G+l]-#pX9-?k";
        CharSet charSet = CharSet.getInstance(complexArray);
        
        assertNotNull("getInstance with complex pattern should return valid CharSet", charSet);
    }

    @Test
    public void testGetInstanceWithSpecialCharacters_ShouldCreateCharSet() {
        String[] specialArray = new String[9];
        specialArray[5] = "o|pC ~`^YDc:";
        CharSet charSet = CharSet.getInstance(specialArray);
        
        assertNotNull("getInstance with special characters should return valid CharSet", charSet);
    }

    @Test
    public void testGetInstanceWithBracketedPattern_ShouldCreateCharSet() {
        String[] bracketArray = {"]ZOr9"};
        CharSet charSet = CharSet.getInstance(bracketArray);
        
        assertNotNull("getInstance with bracketed pattern should return valid CharSet", charSet);
    }

    // ========== Character Containment Tests ==========
    
    @Test
    public void testContains_EmptyCharSet_ShouldReturnFalse() {
        String[] emptyArray = new String[9];
        CharSet charSet = CharSet.getInstance(emptyArray);
        
        assertFalse("Empty CharSet should not contain character 'T'", charSet.contains('T'));
    }

    @Test
    public void testContains_ComplexPattern_ShouldReturnTrueForMatchingChar() {
        String[] complexArray = new String[9];
        complexArray[2] = "pDqg&f|G+l]-#pX9-?k"; // Contains various characters including ranges
        CharSet charSet = CharSet.getInstance(complexArray);
        
        assertTrue("CharSet with complex pattern should contain character 'T'", charSet.contains('T'));
    }

    // ========== Equality Tests ==========
    
    @Test
    public void testEquals_SameCharSetInstance_ShouldReturnTrue() {
        CharSet charSet = CharSet.ASCII_ALPHA_UPPER;
        
        assertTrue("CharSet should be equal to itself", charSet.equals(charSet));
    }

    @Test
    public void testEquals_DifferentInstancesSameContent_ShouldReturnTrue() {
        String[] array = new String[9];
        CharSet charSet1 = CharSet.getInstance(array);
        CharSet charSet2 = CharSet.getInstance(array);
        
        assertTrue("CharSets with same content should be equal", charSet1.equals(charSet2));
        assertNotSame("Equal CharSets should be different instances", charSet1, charSet2);
    }

    @Test
    public void testEquals_DifferentObjectType_ShouldReturnFalse() {
        String[] array = new String[9];
        CharSet charSet = CharSet.getInstance(array);
        Object otherObject = new Object();
        
        assertFalse("CharSet should not be equal to different object type", charSet.equals(otherObject));
    }

    // ========== Utility Method Tests ==========
    
    @Test
    public void testGetCharRanges_EmptyCharSet_ShouldReturnEmptyArray() {
        String[] emptyArray = new String[3];
        CharSet charSet = new CharSet(emptyArray);
        
        CharRange[] ranges = charSet.getCharRanges();
        assertEquals("Empty CharSet should have no character ranges", 0, ranges.length);
    }

    @Test
    public void testGetCharRanges_EmptyConstant_ShouldReturnEmptyArray() {
        CharRange[] ranges = CharSet.EMPTY.getCharRanges();
        assertEquals("EMPTY constant should have no character ranges", 0, ranges.length);
    }

    @Test
    public void testHashCode_ShouldNotThrowException() {
        String[] array = new String[9];
        array[2] = "pDqg&f|G+l]-#pX9-?k";
        CharSet charSet = CharSet.getInstance(array);
        
        // Should not throw any exception
        int hashCode = charSet.hashCode();
        // Hash code can be any integer, so we just verify it completes successfully
    }

    @Test
    public void testToString_EmptyCharSet_ShouldReturnEmptyBrackets() {
        String[] emptyArray = new String[9];
        CharSet charSet = CharSet.getInstance(emptyArray);
        
        String result = charSet.toString();
        assertEquals("Empty CharSet should have string representation '[]'", "[]", result);
    }

    @Test
    public void testAdd_ShouldNotThrowException() {
        String[] array = new String[1];
        CharSet charSet = CharSet.getInstance(array);
        
        // Should not throw any exception when adding a pattern
        charSet.add("S4^V-hS3C g&F;t%");
    }
}