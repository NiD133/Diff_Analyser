package org.apache.commons.io.input.buffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CircularByteBuffer}.
 */
// Renamed class to follow standard naming conventions (e.g., ClassNameTest).
class CircularByteBufferTest {

    @Test
    // Added @DisplayName for better readability in test reports.
    @DisplayName("hasSpace() should be true when buffer is not full and false when full")
    // Renamed method to clearly describe the behavior under test.
    void hasSpaceShouldCorrectlyReflectBufferStateAfterAddAndRead() {
        // Arrange: Create a buffer with a small, single-byte capacity to easily test state changes.
        final CircularByteBuffer buffer = new CircularByteBuffer(1);

        // Assert: A newly created buffer should have space.
        assertTrue(buffer.hasSpace(), "A new buffer should have space available.");

        // Act: Fill the buffer to its capacity.
        buffer.add((byte) 1);

        // Assert: A full buffer should report that it has no space.
        assertFalse(buffer.hasSpace(), "A full buffer should not have space available.");

        // Act: Read the byte, which should free up space.
        final byte firstReadByte = buffer.read();

        // Assert: The correct byte was read, and the buffer now has space again.
        assertEquals((byte) 1, firstReadByte, "The byte read should match the first byte added.");
        assertTrue(buffer.hasSpace(), "Buffer should have space after a byte is read.");

        // Act: Add another byte to test the cycle again. This ensures the buffer's internal
        // state management works correctly after wrapping around.
        buffer.add((byte) 2);

        // Assert: The buffer is full again.
        assertFalse(buffer.hasSpace(), "Buffer should not have space after being filled again.");

        // Act: Read the second byte.
        final byte secondReadByte = buffer.read();

        // Assert: The correct byte was read, and the buffer has space once more.
        assertEquals((byte) 2, secondReadByte, "The byte read should match the second byte added.");
        assertTrue(buffer.hasSpace(), "Buffer should have space again after the second read.");
    }
}