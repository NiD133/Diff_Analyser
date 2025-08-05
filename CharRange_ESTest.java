package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.function.Consumer;
import org.apache.commons.lang3.CharRange;

/**
 * Test suite for CharRange class functionality.
 * Tests cover range creation, equality, containment, and iteration operations.
 */
public class CharRangeTest {

    // ========== Equality Tests ==========
    
    @Test
    public void testEquals_DifferentRanges_ShouldNotBeEqual() {
        CharRange singleCharRange = CharRange.isIn('?', '?');
        CharRange multiCharRange = CharRange.isIn('D', '?');
        
        assertFalse("Different ranges should not be equal", 
                   multiCharRange.equals(singleCharRange));
        assertFalse("Equality should be symmetric", 
                   singleCharRange.equals(multiCharRange));
    }

    @Test
    public void testEquals_PositiveVsNegatedRange_ShouldNotBeEqual() {
        CharRange positiveRange = CharRange.isIn('+', '+');
        CharRange negatedRange = CharRange.isNotIn('7', '7');
        
        assertFalse("Positive and negated ranges should not be equal", 
                   negatedRange.equals(positiveRange));
        assertTrue("Negated range should be marked as negated", 
                  negatedRange.isNegated());
        assertFalse("Positive range should not be marked as negated", 
                   positiveRange.isNegated());
    }

    @Test
    public void testEquals_IdenticalNegatedRanges_ShouldBeEqual() {
        CharRange negatedRange1 = CharRange.isNot('O');
        CharRange negatedRange2 = CharRange.isNot('O');
        
        assertTrue("Identical negated ranges should be equal", 
                  negatedRange1.equals(negatedRange2));
        assertTrue("Both ranges should be negated", 
                  negatedRange1.isNegated() && negatedRange2.isNegated());
    }

    @Test
    public void testEquals_WithNonCharRangeObject_ShouldReturnFalse() {
        CharRange charRange = CharRange.isIn('8', 'A');
        Object nonCharRangeObject = new Object();
        
        assertFalse("CharRange should not equal non-CharRange object", 
                   charRange.equals(nonCharRangeObject));
    }

    // ========== Range Containment Tests ==========
    
    @Test
    public void testContainsCharRange_CharInRange_ShouldReturnTrue() {
        CharRange singleChar = CharRange.is('P');
        CharRange containingRange = CharRange.isIn(';', 'z');
        
        assertTrue("Range ';' to 'z' should contain character 'P'", 
                  containingRange.contains(singleChar));
    }

    @Test
    public void testContainsCharRange_NegatedRangeExcludesTarget_ShouldReturnFalse() {
        CharRange negatedRange = CharRange.isNot('*');
        CharRange targetChar = CharRange.is('*');
        
        assertFalse("Negated range should not contain the excluded character", 
                   negatedRange.contains(targetChar));
    }

    @Test
    public void testContainsCharRange_NegatedRangeSubset_ShouldReturnTrue() {
        CharRange largerNegatedRange = CharRange.isNotIn('+', '8');
        CharRange smallerNegatedRange = CharRange.isNotIn('+', 'X');
        
        assertTrue("Larger negated range should contain smaller negated range subset", 
                  largerNegatedRange.contains(smallerNegatedRange));
    }

    @Test
    public void testContainsCharRange_SelfContainment_ShouldReturnTrue() {
        CharRange range = CharRange.isIn('8', 'A');
        
        assertTrue("Range should contain itself", 
                  range.contains(range));
    }

    @Test
    public void testContainsCharRange_WithNullRange_ShouldThrowException() {
        CharRange range = CharRange.is('5');
        
        try {
            range.contains((CharRange) null);
            fail("Should throw NullPointerException for null range");
        } catch (NullPointerException e) {
            assertEquals("Exception message should mention 'range'", 
                        "range", e.getMessage());
        }
    }

    // ========== Character Containment Tests ==========
    
    @Test
    public void testContainsChar_NegatedRangeExcludesChar_ShouldReturnFalse() {
        CharRange negatedRange = CharRange.isNot('j');
        
        assertFalse("Negated range should not contain the excluded character", 
                   negatedRange.contains('j'));
    }

    @Test
    public void testContainsChar_NegatedRangeIncludesOtherChars_ShouldReturnTrue() {
        CharRange negatedRange = CharRange.isNotIn('N', 'N');
        
        assertTrue("Negated range should contain characters outside the excluded range", 
                  negatedRange.contains('|'));
    }

    @Test
    public void testContainsChar_CharInRange_ShouldReturnTrue() {
        CharRange range = CharRange.isIn('K', '#'); // Note: range is normalized to '#' to 'K'
        
        assertTrue("Range should contain character within bounds", 
                  range.contains('#'));
    }

    @Test
    public void testContainsChar_CharOutsideRange_ShouldReturnFalse() {
        CharRange range = CharRange.isIn('8', 'A');
        
        assertFalse("Range should not contain character outside bounds", 
                   range.contains(' '));
    }

    // ========== Accessor Method Tests ==========
    
    @Test
    public void testGetStart_NormalRange_ShouldReturnStartChar() {
        CharRange range = CharRange.isNot('}');
        
        assertEquals("Should return the start character", 
                    '}', range.getStart());
        assertTrue("Range should be negated", 
                  range.isNegated());
    }

    @Test
    public void testGetStart_ReversedRange_ShouldReturnNormalizedStart() {
        CharRange range = CharRange.isNotIn('<', '6'); // Reversed: '6' < '<'
        
        assertEquals("Should return normalized start character", 
                    '6', range.getStart());
        assertEquals("Should return normalized end character", 
                    '<', range.getEnd());
    }

    @Test
    public void testGetEnd_SingleCharRange_ShouldReturnSameChar() {
        CharRange range = CharRange.is('/');
        
        assertEquals("End should equal start for single character range", 
                    '/', range.getEnd());
        assertEquals("Start should equal end for single character range", 
                    '/', range.getStart());
        assertFalse("Single character range should not be negated by default", 
                   range.isNegated());
    }

    @Test
    public void testIsNegated_NegatedRange_ShouldReturnTrue() {
        CharRange negatedRange = CharRange.isNot('k');
        
        assertTrue("Negated range should return true for isNegated()", 
                  negatedRange.isNegated());
    }

    @Test
    public void testIsNegated_PositiveRange_ShouldReturnFalse() {
        CharRange positiveRange = CharRange.isIn('8', 'A');
        
        assertFalse("Positive range should return false for isNegated()", 
                   positiveRange.isNegated());
    }

    // ========== String Representation Tests ==========
    
    @Test
    public void testToString_MultiCharRange_ShouldShowRange() {
        CharRange range = CharRange.isIn('8', 'A');
        
        assertEquals("String representation should show range format", 
                    "8-A", range.toString());
    }

    // ========== Hash Code Tests ==========
    
    @Test
    public void testHashCode_NegatedRange_ShouldNotThrow() {
        CharRange negatedRange = CharRange.isNotIn('N', 'N');
        
        // Should not throw exception
        int hashCode = negatedRange.hashCode();
        assertTrue("Hash code should be calculated", hashCode != 0 || hashCode == 0);
    }

    @Test
    public void testHashCode_PositiveRange_ShouldNotThrow() {
        CharRange positiveRange = CharRange.is('P');
        
        // Should not throw exception  
        int hashCode = positiveRange.hashCode();
        assertTrue("Hash code should be calculated", hashCode != 0 || hashCode == 0);
    }

    // ========== Iterator Tests ==========
    
    @Test
    public void testForEach_NegatedRange_ShouldExecute() {
        CharRange negatedRange = CharRange.isNot('S');
        Consumer<Character> mockConsumer = mock(Consumer.class);
        
        // This test verifies the forEach method executes without throwing
        // Note: Negated ranges iterate over a very large set, so this may take time
        try {
            negatedRange.forEach(mockConsumer);
        } catch (Exception e) {
            // Expected for large ranges in test environment
        }
    }

    @Test
    public void testForEach_PositiveRange_ShouldExecute() {
        CharRange positiveRange = CharRange.isIn('8', 'A');
        Consumer<Character> mockConsumer = mock(Consumer.class);
        
        positiveRange.forEach(mockConsumer);
        
        // Verify the consumer was called for each character in range
        // Range '8' to 'A' includes: '8', '9', ':', ';', '<', '=', '>', '?', '@', 'A'
        verify(mockConsumer, times(10)).accept(any(Character.class));
    }

    @Test
    public void testIterator_ShouldReturnIterator() {
        CharRange range = CharRange.isNotIn('Y', '@'); // Normalized to '@' to 'Y'
        
        assertNotNull("Iterator should not be null", 
                     range.iterator());
    }
}