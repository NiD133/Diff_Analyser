package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link IOUtils}.
 *
 * <p>This test class was refactored for clarity. It was originally named
 * {@code IOCaseTestTest17}, but its content tests functionality in {@code IOUtils},
 * not {@code IOCase}.</p>
 */
public class IOUtilsTest {

    /**
     * Tests that {@code IOUtils.getScratchCharArray()} provides a clean, zero-filled
     * buffer on each call.
     * <p>
     * This is verified by:
     * <ol>
     *   <li>Requesting a buffer and asserting it is zero-filled.</li>
     *   <li>Modifying the retrieved buffer to make it "dirty".</li>
     *   <li>Requesting another buffer and asserting it is also zero-filled,
     *       proving that it is not affected by modifications to the previous one.</li>
     * </ol>
     */
    @Test
    @DisplayName("getScratchCharArray() should return a clean, zero-filled array on each call")
    void getScratchCharArray_returnsCleanBufferOnEachCall() {
        // Arrange: Get a scratch buffer for the first time.
        final char[] firstBuffer = IOUtils.getScratchCharArray();
        // The expected buffer is a new char array of the same size, which is guaranteed to be zero-filled by Java.
        final char[] expectedZeroedBuffer = new char[firstBuffer.length];

        // Assert: The initial buffer should be zero-filled.
        assertArrayEquals(expectedZeroedBuffer, firstBuffer, "The initial scratch buffer should be zero-filled.");

        // Act: Modify the contents of the first buffer to ensure it's "dirty".
        Arrays.fill(firstBuffer, 'X');

        // Act & Assert: Get another scratch buffer and verify it is also zero-filled,
        // proving that the method provides a clean buffer on each call.
        final char[] secondBuffer = IOUtils.getScratchCharArray();
        assertArrayEquals(expectedZeroedBuffer, secondBuffer, "A subsequent scratch buffer should also be zero-filled.");
    }
}