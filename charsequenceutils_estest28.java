package org.apache.commons.lang3;

import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that {@link CharSequenceUtils#indexOf(CharSequence, int, int)} throws
     * a NullPointerException when the input CharSequence is null.
     */
    @Test(expected = NullPointerException.class)
    public void indexOf_withNullCharSequence_shouldThrowNullPointerException() {
        // The specific character ('a') and start index (0) are arbitrary values,
        // as the method should throw the exception before using them.
        CharSequenceUtils.indexOf(null, 'a', 0);
    }
}