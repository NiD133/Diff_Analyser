package org.apache.commons.lang3;

import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that CharSequenceUtils.lastIndexOf() throws a NullPointerException
     * when the input CharSequence is null.
     */
    @Test(expected = NullPointerException.class)
    public void testLastIndexOfWithNullCharSequence() {
        // The specific character and start index are irrelevant for this test case,
        // as the NullPointerException should be thrown before they are used.
        final int irrelevantChar = 'a';
        final int irrelevantStartIndex = 0;

        CharSequenceUtils.lastIndexOf(null, irrelevantChar, irrelevantStartIndex);
    }
}