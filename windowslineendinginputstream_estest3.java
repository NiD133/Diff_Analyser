package org.apache.commons.io.input;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Tests for {@link WindowsLineEndingInputStream}.
 */
public class WindowsLineEndingInputStreamTest {

    /**
     * Tests that attempting to read from a stream constructed with a null input
     * immediately throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void readThrowsExceptionForNullInputStream() throws IOException {
        // Arrange: Create the stream with a null input, which is the invalid state under test.
        final WindowsLineEndingInputStream stream = new WindowsLineEndingInputStream(null, true);

        // Act: Attempting to read from the stream should trigger the exception.
        stream.read();

        // Assert: The test passes if a NullPointerException is thrown, as declared
        // in the @Test(expected=...) annotation.
    }
}