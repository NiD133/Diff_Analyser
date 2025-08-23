package com.itextpdf.text.pdf;

import org.junit.Test;

/**
 * Test suite for the {@link StringUtils} class, focusing on edge cases and invalid inputs.
 */
public class StringUtilsTest {

    /**
     * Verifies that {@code escapeString} correctly propagates an {@link ArrayIndexOutOfBoundsException}
     * when the provided output {@link ByteBuffer} is in an invalid state (i.e., has a negative count).
     * This ensures the method does not silently fail or handle unexpected states from its dependencies.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void escapeString_whenOutputBufferHasNegativeCount_throwsArrayIndexOutOfBoundsException() {
        // Arrange: Create an empty input and a ByteBuffer with an invalid negative count
        // to simulate a corrupt or improperly initialized buffer state.
        byte[] inputBytes = new byte[0];
        ByteBuffer outputBuffer = new ByteBuffer();
        outputBuffer.count = -27;

        // Act: Attempt to escape the string into the invalid buffer.
        // The exception is expected to be thrown from the underlying ByteBuffer's logic.
        StringUtils.escapeString(inputBytes, outputBuffer);

        // Assert: The test passes if an ArrayIndexOutOfBoundsException is thrown,
        // as specified by the @Test(expected=...) annotation.
    }
}