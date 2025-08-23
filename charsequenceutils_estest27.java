package org.apache.commons.lang3;

import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that calling regionMatches with a null for the first CharSequence argument
     * results in a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void testRegionMatchesWithNullFirstArgumentThrowsNullPointerException() {
        // Define arguments for the method call.
        // The second CharSequence is non-null to isolate the test's focus
        // on the first argument being the cause of the exception.
        final CharSequence other = "some string";
        final int offset = 0;
        final int len = 5;
        final boolean ignoreCase = false;

        // This call is expected to throw a NullPointerException because the first argument is null.
        CharSequenceUtils.regionMatches(null, ignoreCase, offset, other, offset, len);
    }
}