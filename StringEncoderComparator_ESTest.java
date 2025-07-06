package org.apache.commons.codec;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.codec.StringEncoderComparator;
import org.apache.commons.codec.language.ColognePhonetic;
import org.apache.commons.codec.language.Soundex;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * Test suite for the StringEncoderComparator class.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class StringEncoderComparator_ESTest extends StringEncoderComparator_ESTest_scaffolding {

    /**
     * Test comparing two identical objects using ColognePhonetic encoder.
     * Expect the comparison result to be 0 since both objects are the same.
     */
    @Test(timeout = 4000)
    public void testCompareIdenticalObjectsWithColognePhonetic() throws Throwable {
        ColognePhonetic colognePhonetic = new ColognePhonetic();
        StringEncoderComparator comparator = new StringEncoderComparator(colognePhonetic);
        Object object = new Object();
        
        int comparisonResult = comparator.compare(object, object);
        
        assertEquals("Expected comparison result to be 0 for identical objects", 0, comparisonResult);
    }

    /**
     * Test comparing two different strings using Soundex encoder.
     * Expect the comparison result to be 1 since the encoded forms are different.
     */
    @Test(timeout = 4000)
    public void testCompareDifferentStringsWithSoundex() throws Throwable {
        Soundex soundex = Soundex.US_ENGLISH_GENEALOGY;
        StringEncoderComparator comparator = new StringEncoderComparator(soundex);
        
        int comparisonResult = comparator.compare("kj9), b3h<5ue'[f$j?", "K120");
        
        assertEquals("Expected comparison result to be 1 for different encoded strings", 1, comparisonResult);
    }

    /**
     * Test comparing two objects without a StringEncoder.
     * Expect a NullPointerException since the encoder is not initialized.
     */
    @Test(timeout = 4000)
    public void testCompareWithoutEncoder() throws Throwable {
        StringEncoderComparator comparator = new StringEncoderComparator();
        Object object = new Object();
        
        try {
            comparator.compare(object, object);
            fail("Expected NullPointerException due to uninitialized encoder");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.codec.StringEncoderComparator", e);
        }
    }

    /**
     * Test comparing two different strings using ColognePhonetic encoder.
     * Expect a negative comparison result since the encoded forms are different.
     */
    @Test(timeout = 4000)
    public void testCompareDifferentStringsWithColognePhonetic() throws Throwable {
        ColognePhonetic colognePhonetic = new ColognePhonetic();
        StringEncoderComparator comparator = new StringEncoderComparator(colognePhonetic);
        
        int comparisonResult = comparator.compare("The character is not mapped: ", "[kF8mLHifO??@&h>");
        
        assertEquals("Expected comparison result to be negative for different encoded strings", -2, comparisonResult);
    }
}