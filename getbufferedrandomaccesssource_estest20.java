package com.itextpdf.text.io;

import org.junit.Test;

/**
 * Contains unit tests for the {@link GetBufferedRandomAccessSource} class.
 */
public class GetBufferedRandomAccessSourceTest {

    /**
     * Verifies that the constructor throws a NullPointerException when the provided
     * source is null. This is critical because the constructor immediately attempts
     * to access the source to determine its length.
     */
    @Test(expected = NullPointerException.class)
    public void constructorShouldThrowNullPointerExceptionForNullSource() {
        // Attempt to create an instance with a null source, which should fail.
        new GetBufferedRandomAccessSource(null);
    }
}