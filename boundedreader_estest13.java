package org.apache.commons.io.input;

import static org.junit.Assert.assertThrows;

import java.io.Reader;
import org.junit.Test;

/**
 * Tests for {@link BoundedReader}.
 */
public class BoundedReaderTest {

    /**
     * Tests that attempting to read from a BoundedReader initialized with a null
     * underlying reader throws a NullPointerException.
     */
    @Test
    public void readWithNullReaderShouldThrowNullPointerException() {
        // Arrange: Create a BoundedReader with a null underlying reader.
        // The max character limit is arbitrary as the read should fail immediately.
        final BoundedReader boundedReader = new BoundedReader(null, 10);

        // Act & Assert: Verify that a NullPointerException is thrown when read() is called.
        assertThrows(NullPointerException.class, boundedReader::read);
    }
}