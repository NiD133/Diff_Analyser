package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

/**
 * Contains tests for the {@link NullReader} class.
 */
public class NullReaderTest {

    /**
     * Tests that calling read() with a negative length updates the internal position.
     * <p>
     * While the general contract for {@link java.io.Reader#read(char[], int, int)}
     * expects a non-negative length, this test verifies the specific (and unusual)
     * behavior of NullReader, which directly adds the length to its internal position.
     * </p>
     */
    @Test
    public void testGetPositionAfterReadWithNegativeLength() throws IOException {
        // Arrange
        final long initialSize = 959L;
        final NullReader reader = new NullReader(initialSize);
        final char[] buffer = new char[10];
        final int negativeLength = -1_000; // A representative negative value
        final int offset = 0;

        // Act
        // Call read with a negative length, which is an unconventional use case.
        reader.read(buffer, offset, negativeLength);
        final long newPosition = reader.getPosition();

        // Assert
        // The position should now reflect the negative value passed as the length.
        assertEquals("The reader's position should be updated by the negative length.",
                     negativeLength, newPosition);
    }
}