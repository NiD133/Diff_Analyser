package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link SerializedString} class, focusing on its
 * UTF-8 appending functionality.
 */
public class SerializedStringTest {

    /**
     * Verifies that `appendQuotedUTF8` returns -1 when the provided offset
     * is so large that there is no space in the destination buffer to write the string.
     * A return value of -1 is the contract for indicating insufficient space.
     */
    @Test
    public void appendQuotedUTF8ShouldReturnNegativeOneWhenOffsetExceedsBufferCapacity() {
        // Arrange
        // The specific string content is not critical, as the out-of-bounds offset
        // is the primary condition being tested.
        SerializedString serializedString = new SerializedString("M:oX:\"bsoHuP");
        byte[] buffer = new byte[14];
        int outOfBoundsOffset = 77; // An offset well beyond the buffer's length.

        // Act
        int result = serializedString.appendQuotedUTF8(buffer, outOfBoundsOffset);

        // Assert
        // The method is expected to return -1 to signal that the append operation
        // failed due to insufficient space.
        final int INSUFFICIENT_SPACE_FLAG = -1;
        assertEquals("Should return -1 for an offset that leaves no room in the buffer",
                INSUFFICIENT_SPACE_FLAG, result);
    }
}