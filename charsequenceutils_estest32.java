package org.apache.commons.lang3;

import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * Unit tests for the null-safe behavior of {@link org.apache.commons.lang3.CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that CharSequenceUtils.subSequence() returns null when the input CharSequence is null.
     * This verifies the documented null-safe behavior of the method.
     */
    @Test
    public void subSequenceShouldReturnNullForNullInput() {
        // The subSequence method is designed to be null-safe.
        // When the input CharSequence is null, the method should return null,
        // regardless of the start index provided.
        final int arbitraryStartIndex = 1;
        assertNull("subSequence(null, *) should return null",
                   CharSequenceUtils.subSequence(null, arbitraryStartIndex));
    }
}