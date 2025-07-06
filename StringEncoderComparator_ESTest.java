/*
 * Test class for StringEncoderComparator.
 */
package org.apache.commons.codec;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.codec.language.ColognePhonetic;
import org.apache.commons.codec.language.Soundex;

/**
 * Test class for StringEncoderComparator.
 */
public class StringEncoderComparatorTest {

    /**
     * Test that two identical objects are considered equal.
     */
    @Test(timeout = 4000)
    public void testCompareIdenticalObjects()  throws Throwable  {
        ColognePhonetic colognePhonetic = new ColognePhonetic();
        StringEncoderComparator comparator = new StringEncoderComparator(colognePhonetic);
        Object object = new Object();
        int result = comparator.compare(object, object);
        assertEquals(0, result);
    }

    /**
     * Test comparison of two different strings using Soundex algorithm.
     */
    @Test(timeout = 4000)
    public void testCompareDifferentStringsUsingSoundex()  throws Throwable  {
        Soundex soundex = Soundex.US_ENGLISH_GENEALOGY;
        StringEncoderComparator comparator = new StringEncoderComparator(soundex);
        String str1 = "kj9), b3h<5ue'[f$j?";
        String str2 = "K120";
        int result = comparator.compare(str1, str2);
        assertEquals(1, result);
    }

    /**
     * Test that comparing two objects without a StringEncoder results in a NullPointerException.
     */
    @Test(timeout = 4000)
    public void testCompareWithoutStringEncoder()  throws Throwable  {
        StringEncoderComparator comparator = new StringEncoderComparator();
        Object object = new Object();
        try {
            comparator.compare(object, object);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    /**
     * Test comparison of two different strings using ColognePhonetic algorithm.
     */
    @Test(timeout = 4000)
    public void testCompareDifferentStringsUsingColognePhonetic()  throws Throwable  {
        ColognePhonetic colognePhonetic = new ColognePhonetic();
        StringEncoderComparator comparator = new StringEncoderComparator(colognePhonetic);
        String str1 = "The character is not mapped: ";
        String str2 = "[kF8mLHifO??@&h>";
        int result = comparator.compare(str1, str2);
        assertEquals(-2, result);
    }
}