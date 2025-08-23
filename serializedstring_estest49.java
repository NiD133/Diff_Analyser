package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link SerializedString} class.
 */
public class SerializedStringTest {

    /**
     * Verifies that {@code appendQuoted(char[], int)} correctly handles an offset
     * that is outside the bounds of the destination buffer.
     * <p>
     * The method is expected to perform a bounds check and return -1 to indicate
     * that the append operation failed, rather than throwing an
     * {@link ArrayIndexOutOfBoundsException}.
     */
    @Test
    public void appendQuotedShouldReturnFailureCodeWhenOffsetIsOutOfBounds() {
        // Arrange
        final SerializedString contentToAppend = new SerializedString("value");
        final char[] destinationBuffer = new char[10];
        
        // Use an offset that is clearly larger than the buffer's length.
        final int outOfBoundsOffset = 20;

        // Act
        final int charsWritten = contentToAppend.appendQuoted(destinationBuffer, outOfBoundsOffset);

        // Assert
        // A return value of -1 signifies that the append operation failed due to lack of space.
        assertEquals("Should return -1 for an out-of-bounds offset", -1, charsWritten);
    }
}