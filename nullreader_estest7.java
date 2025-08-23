package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Contains improved, more understandable tests for the {@link NullReader} class.
 */
public class NullReaderTest {

    /**
     * Tests that a NullReader initialized with a negative size correctly stores that size
     * and maintains the default behaviors for mark support and character processing.
     */
    @Test
    public void readerCreatedWithNegativeSizeShouldReportCorrectSizeAndDefaults() {
        // Arrange: Create a NullReader with a negative size.
        final long negativeSize = -476L;
        final NullReader reader = new NullReader(negativeSize);

        // Act: Call the protected processChar() method to check its default return value.
        // This action is independent of the reader's configured size.
        final int processedChar = reader.processChar();

        // Assert: Verify the reader's state and behavior.
        // 1. The size, even if negative, should be stored as provided.
        assertEquals("The reader's size should match the negative value from the constructor.",
                negativeSize, reader.getSize());

        // 2. The NullReader(long) constructor should enable mark support by default.
        assertTrue("Mark support should be enabled by default.", reader.markSupported());

        // 3. The processChar() method should return 0 by default, as per its documentation.
        assertEquals("processChar() should return 0 by default.", 0, processedChar);
    }
}