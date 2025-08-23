package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;

/**
 * Unit tests for the {@link SegmentUtils} class.
 */
public class SegmentUtilsTest {

    /**
     * Verifies that countArgs() throws a NullPointerException when the
     * descriptor string is null.
     */
    @Test(expected = NullPointerException.class)
    public void countArgsShouldThrowNullPointerExceptionForNullDescriptor() {
        // The second argument (widthOfLongsAndDoubles) is arbitrary, as the
        // method should throw the exception before using it.
        SegmentUtils.countArgs(null, 1);
    }
}