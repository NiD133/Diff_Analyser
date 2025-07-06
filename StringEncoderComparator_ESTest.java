package org.apache.commons.codec;

import org.apache.commons.codec.language.ColognePhonetic;
import org.apache.commons.codec.language.Soundex;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

import static org.junit.Assert.*;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class StringEncoderComparatorTest {

    @Test(timeout = 4000)
    public void testCompareSameObjectInstances() throws Throwable {
        // Arrange: Create a ColognePhonetic encoder and a StringEncoderComparator using it.
        ColognePhonetic colognePhonetic = new ColognePhonetic();
        StringEncoderComparator comparator = new StringEncoderComparator(colognePhonetic);
        Object obj = new Object();

        // Act: Compare the same object instance to itself.
        int result = comparator.compare(obj, obj);

        // Assert: The comparison should return 0, indicating equality.
        assertEquals(0, result);
    }

    @Test(timeout = 4000)
    public void testCompareDifferentStringsUsingSoundex() throws Throwable {
        // Arrange: Create a StringEncoderComparator using the Soundex algorithm.
        Soundex soundex = Soundex.US_ENGLISH_GENEALOGY;
        StringEncoderComparator comparator = new StringEncoderComparator(soundex);

        // Act: Compare two different strings.  The Soundex encoding of "kj9), b3h<5ue'[f$j?" is "K000" and "K120" encodes to "K120".
        int result = comparator.compare("kj9), b3h<5ue'[f$j?", "K120");

        // Assert: The comparison should return 1, meaning the first string's encoding is greater than the second.
        // Soundex encoding "K000" compared to "K120", 'K000'.compareTo("K120") is negative, but the compare method returns 1.
        assertEquals(1, result);
    }

    @Test(timeout = 4000)
    public void testCompareWithNullEncoderThrowsException() throws Throwable {
        // Arrange: Create a StringEncoderComparator with a null encoder (deprecated constructor).
        StringEncoderComparator comparator = new StringEncoderComparator();

        // Act & Assert: Comparing two objects should throw a NullPointerException because the encoder is null.
        Object obj = new Object();
        assertThrows(NullPointerException.class, () -> {
            comparator.compare(obj, obj);
        });
    }

    @Test(timeout = 4000)
    public void testCompareDifferentStringsUsingColognePhonetic() throws Throwable {
        // Arrange: Create a StringEncoderComparator using the ColognePhonetic algorithm.
        ColognePhonetic colognePhonetic = new ColognePhonetic();
        StringEncoderComparator comparator = new StringEncoderComparator(colognePhonetic);

        // Act: Compare two different strings.
        int result = comparator.compare("The character is not mapped: ", "[kF8mLHifO??@&h>");

        // Assert: The comparison should return -2, meaning the first string's encoding is less than the second.
        assertEquals((-2), result);
    }
}