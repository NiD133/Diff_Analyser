package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;

/**
 * Unit tests for the {@link SegmentUtils} class.
 */
public class SegmentUtilsTest {

    /**
     * Verifies that countArgs(String) throws a NullPointerException
     * when the input descriptor is null. This ensures the method correctly
     * handles invalid null input.
     */
    @Test(expected = NullPointerException.class)
    public void countArgsShouldThrowNullPointerExceptionForNullDescriptor() {
        // The method call is expected to throw a NullPointerException.
        // The @Test(expected=...) annotation handles the assertion.
        SegmentUtils.countArgs((String) null);
    }
}