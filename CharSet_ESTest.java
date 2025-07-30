package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.lang3.CharRange;
import org.apache.commons.lang3.CharSet;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * Test suite for the CharSet class.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class CharSet_ESTest extends CharSet_ESTest_scaffolding {

    /**
     * Test that hashCode can be called on a CharSet instance with a specific string array.
     */
    @Test(timeout = 4000)
    public void testHashCodeOnCharSetInstance() throws Throwable {
        String[] stringArray = new String[9];
        stringArray[2] = "pDqg&f|G+l]-#pX9-?k";
        CharSet charSet = CharSet.getInstance(stringArray);
        charSet.hashCode();
    }

    /**
     * Test that CharSet.getInstance returns a non-null instance when given a valid string array.
     */
    @Test(timeout = 4000)
    public void testGetInstanceReturnsNonNull() throws Throwable {
        String[] stringArray = new String[9];
        stringArray[1] = "^n%R";
        CharSet charSet = CharSet.getInstance(stringArray);
        assertNotNull(charSet);
    }

    /**
     * Test that CharSet.getInstance correctly handles a character range string.
     */
    @Test(timeout = 4000)
    public void testGetInstanceWithCharacterRange() throws Throwable {
        String[] stringArray = new String[1];
        stringArray[0] = "a-z";
        CharSet charSet = CharSet.getInstance(stringArray);
        assertNotNull(charSet);
    }

    /**
     * Test that a CharSet created with an empty string array has no character ranges.
     */
    @Test(timeout = 4000)
    public void testCharSetWithEmptyStringArray() throws Throwable {
        String[] stringArray = new String[3];
        CharSet charSet = new CharSet(stringArray);
        CharRange[] charRangeArray = charSet.getCharRanges();
        assertEquals(0, charRangeArray.length);
    }

    /**
     * Test that creating a CharSet with a null string array throws a NullPointerException.
     */
    @Test(timeout = 4000)
    public void testCharSetCreationWithNullArray() throws Throwable {
        CharSet charSet = null;
        try {
            charSet = new CharSet((String[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Arrays", e);
        }
    }

    /**
     * Test that two CharSet instances created with the same string array are equal but not the same instance.
     */
    @Test(timeout = 4000)
    public void testEqualityOfCharSetInstances() throws Throwable {
        String[] stringArray = new String[9];
        CharSet charSet1 = CharSet.getInstance(stringArray);
        CharSet charSet2 = CharSet.getInstance(stringArray);
        assertTrue(charSet1.equals(charSet2));
        assertNotSame(charSet1, charSet2);
    }

    /**
     * Test that a CharSet instance is equal to itself.
     */
    @Test(timeout = 4000)
    public void testCharSetEqualsItself() throws Throwable {
        CharSet charSet = CharSet.ASCII_ALPHA_UPPER;
        assertTrue(charSet.equals(charSet));
    }

    /**
     * Test that a CharSet instance is not equal to an arbitrary object.
     */
    @Test(timeout = 4000)
    public void testCharSetNotEqualToObject() throws Throwable {
        String[] stringArray = new String[9];
        CharSet charSet = CharSet.getInstance(stringArray);
        Object object = new Object();
        assertFalse(charSet.equals(object));
    }

    /**
     * Test that a CharSet contains a specific character.
     */
    @Test(timeout = 4000)
    public void testCharSetContainsCharacter() throws Throwable {
        String[] stringArray = new String[9];
        stringArray[2] = "pDqg&f|G+l]-#pX9-?k";
        CharSet charSet = CharSet.getInstance(stringArray);
        assertTrue(charSet.contains('T'));
    }

    /**
     * Test that CharSet.getInstance returns a non-null instance with a specific string array.
     */
    @Test(timeout = 4000)
    public void testGetInstanceWithSpecificStringArray() throws Throwable {
        String[] stringArray = new String[9];
        stringArray[5] = "o|pC ~`^YDc:";
        CharSet charSet = CharSet.getInstance(stringArray);
        assertNotNull(charSet);
    }

    /**
     * Test that CharSet.getInstance returns a non-null instance with a single-element string array.
     */
    @Test(timeout = 4000)
    public void testGetInstanceWithSingleElementArray() throws Throwable {
        String[] stringArray = new String[1];
        stringArray[0] = "]ZOr9";
        CharSet charSet = CharSet.getInstance(stringArray);
        assertNotNull(charSet);
    }

    /**
     * Test that adding a string to a CharSet instance does not throw an exception.
     */
    @Test(timeout = 4000)
    public void testAddStringToCharSet() throws Throwable {
        String[] stringArray = new String[1];
        CharSet charSet = CharSet.getInstance(stringArray);
        charSet.add("S4^V-hS3C g&F;t%");
    }

    /**
     * Test that CharSet.getInstance returns null when given a null string array.
     */
    @Test(timeout = 4000)
    public void testGetInstanceWithNullArray() throws Throwable {
        CharSet charSet = CharSet.getInstance((String[]) null);
        assertNull(charSet);
    }

    /**
     * Test that an empty CharSet has no character ranges.
     */
    @Test(timeout = 4000)
    public void testEmptyCharSetHasNoRanges() throws Throwable {
        String[] stringArray = new String[9];
        CharSet charSet = CharSet.getInstance(stringArray);
        CharRange[] charRangeArray = charSet.EMPTY.getCharRanges();
        assertEquals(0, charRangeArray.length);
    }

    /**
     * Test that a CharSet does not contain a specific character.
     */
    @Test(timeout = 4000)
    public void testCharSetDoesNotContainCharacter() throws Throwable {
        String[] stringArray = new String[9];
        CharSet charSet = CharSet.getInstance(stringArray);
        assertFalse(charSet.contains('T'));
    }

    /**
     * Test that the string representation of an empty CharSet is "[]".
     */
    @Test(timeout = 4000)
    public void testToStringOfEmptyCharSet() throws Throwable {
        String[] stringArray = new String[9];
        CharSet charSet = CharSet.getInstance(stringArray);
        assertEquals("[]", charSet.toString());
    }
}