package org.apache.commons.io;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

/**
 * Unit tests for {@link ByteOrderMark}.
 */
public class ByteOrderMarkTest {

    /**
     * Tests that the equals() method correctly identifies two different
     * ByteOrderMark instances as not being equal.
     */
    @Test
    public void equalsShouldReturnFalseForDifferentByteOrderMarks() {
        // Arrange: Define two different ByteOrderMark constants.
        final ByteOrderMark utf8Bom = ByteOrderMark.UTF_8;
        final ByteOrderMark utf16beBom = ByteOrderMark.UTF_16BE;

        // Act & Assert: Verify that the two instances are not equal.
        // The assertNotEquals method internally calls the equals() method.
        assertNotEquals(utf8Bom, utf16beBom);
    }
}