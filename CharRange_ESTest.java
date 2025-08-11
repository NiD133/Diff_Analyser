package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import java.util.function.Consumer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class CharRangeTest extends CharRangeTestScaffolding {

    @Test(timeout = 4000)
    public void testSingleCharacterEquality() {
        CharRange range1 = CharRange.isIn('?', '?');
        CharRange range2 = CharRange.isIn('D', '?');
        
        assertFalse(range1.equals(range2));
        assertEquals('D', range2.getEnd());
        assertEquals('?', range2.getStart());
    }

    @Test(timeout = 4000)
    public void testNegatedRangeEquality() {
        CharRange range1 = CharRange.isIn('+', '+');
        CharRange range2 = CharRange.isNotIn('7', '7');
        
        assertFalse(range1.equals(range2));
        assertTrue(range2.isNegated());
    }

    @Test(timeout = 4000)
    public void testRangeContainment() {
        CharRange range1 = CharRange.is('P');
        CharRange range2 = CharRange.isIn(';', 'z');
        
        assertTrue(range2.contains(range1));
    }

    @Test(timeout = 4000)
    public void testNegatedSingleCharacterContainment() {
        CharRange range1 = CharRange.isNot('*');
        CharRange range2 = CharRange.is('*');
        
        assertFalse(range1.contains(range2));
    }

    @Test(timeout = 4000)
    public void testNegatedRangeContainment() {
        CharRange range1 = CharRange.isNotIn('+', '8');
        CharRange range2 = CharRange.isNotIn('+', 'X');
        
        assertTrue(range1.contains(range2));
    }

    @Test(timeout = 4000)
    public void testNegatedCharacterDoesNotContainItself() {
        CharRange range = CharRange.isNot('j');
        
        assertFalse(range.contains('j'));
    }

    @Test(timeout = 4000)
    public void testNegatedCharacterIsNegated() {
        CharRange range = CharRange.isNot('k');
        
        assertTrue(range.isNegated());
    }

    @Test(timeout = 4000)
    public void testNegatedCharacterStartAndEnd() {
        CharRange range = CharRange.isNot('}');
        
        assertEquals('}', range.getStart());
        assertEquals('}', range.getEnd());
    }

    @Test(timeout = 4000)
    public void testNonNegatedCharacterRange() {
        CharRange range = CharRange.is('/');
        
        assertFalse(range.isNegated());
        assertEquals('/', range.getStart());
        assertEquals('/', range.getEnd());
    }

    @Test(timeout = 4000)
    public void testNullPointerExceptionOnNullRange() {
        CharRange range = CharRange.is('5');
        
        try {
            range.contains((CharRange) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testRangeToString() {
        CharRange range = CharRange.isIn('8', 'A');
        
        assertEquals("8-A", range.toString());
    }

    @Test(timeout = 4000)
    public void testHashCodeForNegatedRange() {
        CharRange range = CharRange.isNotIn('N', 'N');
        
        range.hashCode();
        assertTrue(range.isNegated());
    }

    @Test(timeout = 4000)
    public void testHashCodeForNonNegatedRange() {
        CharRange range = CharRange.is('P');
        
        range.hashCode();
        assertFalse(range.isNegated());
    }

    @Test(timeout = 4000)
    public void testEqualNegatedRanges() {
        CharRange range1 = CharRange.isNot('O');
        CharRange range2 = CharRange.isNot('O');
        
        assertTrue(range1.equals(range2));
    }

    @Test(timeout = 4000)
    public void testNonEqualNegatedAndNonNegatedRanges() {
        CharRange range1 = CharRange.isNot('=');
        CharRange range2 = CharRange.is('=');
        
        assertFalse(range1.equals(range2));
    }

    @Test(timeout = 4000)
    public void testRangeContainsCharacter() {
        CharRange range = CharRange.isIn('K', '#');
        
        assertTrue(range.contains('#'));
    }

    @Test(timeout = 4000)
    public void testRangeDoesNotContainCharacter() {
        CharRange range = CharRange.isIn('8', 'A');
        
        assertFalse(range.contains(' '));
    }

    @Test(timeout = 4000)
    public void testNegatedRangeIterator() {
        CharRange range = CharRange.isNotIn('Y', '@');
        
        range.iterator();
        assertTrue(range.isNegated());
    }
}