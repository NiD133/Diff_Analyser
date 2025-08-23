package org.apache.commons.io.input;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * This test class contains the refactored test case.
 * Note: The original class name and inheritance are preserved to match the provided context.
 */
public class WindowsLineEndingInputStream_ESTestTest6 extends WindowsLineEndingInputStream_ESTest_scaffolding {

    /**
     * Verifies that calling close() on a WindowsLineEndingInputStream
     * initialized with a null underlying stream throws a NullPointerException.
     * This is expected because the close() method attempts to delegate
     * the close operation to the wrapped input stream.
     */
    @Test(expected = NullPointerException.class, timeout = 4000)
    public void closeWithNullInputStreamThrowsNullPointerException() throws IOException {
        // Arrange: Create an instance with a null input stream.
        final WindowsLineEndingInputStream stream = new WindowsLineEndingInputStream(null, false);

        // Act & Assert: Calling close() should immediately throw a NullPointerException.
        stream.close();
    }
}