package org.apache.commons.io;

import org.junit.Test;

/**
 * Tests for {@link ByteOrderMark}.
 */
public class ByteOrderMarkTest {

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void getShouldThrowExceptionForNegativeIndex() {
        // Arrange
        final ByteOrderMark utf8Bom = ByteOrderMark.UTF_8;
        final int invalidIndex = -1;

        // Act
        // This call is expected to throw the exception.
        utf8Bom.get(invalidIndex);

        // Assert: The exception type is verified by the @Test(expected=...) annotation.
    }
}