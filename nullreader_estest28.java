package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

/**
 * Contains tests for the {@link NullReader} class.
 */
public class NullReaderTest {

    /**
     * Verifies that reading from a NullReader initialized with a negative size
     * proceeds as if the reader has an infinite length. The read operation should
     * return the default character (0) and increment the internal position.
     * A negative size ensures the end-of-file condition (position == size) is never met
     * for a positive position counter.
     */
    @Test
    public void testReadWithNegativeSizeBehavesAsInfiniteReader() throws IOException {
        // Arrange: Create a NullReader with a negative size, which effectively
        // makes it an infinite stream since the position will never equal the size.
        final long negativeSize = -100L;
        final NullReader reader = new NullReader(negativeSize, false, false);
        assertEquals("Initial position should be zero.", 0L, reader.getPosition());

        // Act: Perform a single read operation.
        final int characterRead = reader.read();

        // Assert: The reader should return the default character and advance its position.
        final long expectedPosition = 1L;
        final int expectedCharacter = 0; // Default char returned by processChar()

        assertEquals("Position should increment after a read.", expectedPosition, reader.getPosition());
        assertEquals("Read should return the default character.", expectedCharacter, characterRead);
    }
}