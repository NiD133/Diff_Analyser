package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link NullReader} class, focusing on its constructor and initial state.
 */
public class NullReaderTest {

    /**
     * Verifies that the NullReader constructor correctly initializes the reader's
     * properties, such as its size, mark support, and initial position.
     */
    @Test
    public void constructorShouldCorrectlyInitializeReaderState() {
        // Arrange: Define the properties for the NullReader.
        final long expectedSize = 959L;
        final boolean markIsSupported = true;
        final boolean throwEofOnRead = true;

        // Act: Create a new NullReader instance with the specified properties.
        final NullReader reader = new NullReader(expectedSize, markIsSupported, throwEofOnRead);

        // Assert: Check that the reader's state matches the constructor arguments.
        assertEquals("The size should match the value provided to the constructor.",
                expectedSize, reader.getSize());
        assertTrue("Mark support should be enabled as specified in the constructor.",
                reader.markSupported());
        assertEquals("The initial position should always be zero after construction.",
                0L, reader.getPosition());
    }
}