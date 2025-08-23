package org.jsoup.helper;

import org.junit.Test;

/**
 * Tests for {@link Validate}.
 */
public class ValidateTest {

    /**
     * Tests that calling {@link Validate#noNullElements(Object[])} with a null array
     * correctly throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void noNullElementsThrowsNullPointerExceptionForNullArray() {
        // Act: Call the method with a null array, which is expected to throw.
        Validate.noNullElements(null);
    }
}