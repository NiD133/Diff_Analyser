package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link NullReader} class.
 */
public class NullReaderTest {

    /**
     * Tests that the constructor correctly initializes the reader's properties,
     * particularly when mark support is disabled.
     */
    @Test
    public void testConstructorSetsPropertiesCorrectlyWhenMarkIsUnsupported() {
        // Arrange: Define the properties for the NullReader.
        final long expectedSize = -1L; // A negative size is a valid, though unusual, case.
        final boolean shouldSupportMark = false;
        final boolean shouldThrowEofException = false;

        // Act: Create an instance of NullReader with the specified properties.
        final NullReader reader = new NullReader(expectedSize, shouldSupportMark, shouldThrowEofException);

        // Assert: Verify that the properties were set correctly.
        assertFalse("markSupported() should return false as configured in the constructor.",
                reader.markSupported());
        assertEquals("getSize() should return the size provided in the constructor.",
                expectedSize, reader.getSize());
    }
}