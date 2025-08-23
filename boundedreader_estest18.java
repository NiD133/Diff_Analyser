package org.apache.commons.io.input;

import org.junit.Test;
import java.io.IOException;

/**
 * Unit tests for the {@link BoundedReader} class, focusing on error handling.
 */
public class BoundedReaderTest {

    /**
     * Tests that calling close() on a BoundedReader that was constructed with a null
     * underlying reader throws a NullPointerException. This happens because BoundedReader
     * delegates the close() call directly to the underlying reader.
     */
    @Test(expected = NullPointerException.class)
    public void closeShouldThrowNullPointerExceptionWhenReaderIsNull() throws IOException {
        // Arrange: Create a BoundedReader with a null underlying reader.
        // The max character limit is not relevant to this specific test.
        final BoundedReader boundedReader = new BoundedReader(null, 100);

        // Act & Assert: Attempting to close the reader should throw a NullPointerException
        // because it tries to call close() on the null target.
        boundedReader.close();
    }
}