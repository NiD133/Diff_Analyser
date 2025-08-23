package org.apache.commons.io.input;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Tests for {@link BoundedReader}.
 */
public class BoundedReaderTest {

    /**
     * Tests that calling reset() on a BoundedReader initialized with a null
     * underlying reader throws a NullPointerException. This occurs because the method
     * attempts to delegate the reset() call to the null reader instance.
     */
    @Test
    public void resetWithNullReaderThrowsNullPointerException() {
        // Arrange: Create a BoundedReader with a null target reader.
        // The max size is arbitrary as it does not affect this test's outcome.
        final BoundedReader boundedReader = new BoundedReader(null, 10);

        // Act & Assert: Verify that calling reset() on the bounded reader
        // throws a NullPointerException.
        assertThrows(NullPointerException.class, boundedReader::reset);
    }
}