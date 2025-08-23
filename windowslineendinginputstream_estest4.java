package org.apache.commons.io.input;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;

/**
 * Tests for {@link WindowsLineEndingInputStream}.
 * This specific test class focuses on exception propagation.
 */
// The class name and inheritance are kept to match the original structure.
// In a real-world scenario, these might also be refactored for clarity.
public class WindowsLineEndingInputStream_ESTestTest4 extends WindowsLineEndingInputStream_ESTest_scaffolding {

    /**
     * Verifies that an exception from the underlying input stream is propagated
     * when read() is called on the WindowsLineEndingInputStream.
     *
     * @throws IOException if an I/O error occurs (not expected in this test).
     */
    @Test(timeout = 4000, expected = ArrayIndexOutOfBoundsException.class)
    public void readShouldPropagateExceptionFromUnderlyingStream() throws IOException {
        // Arrange: Create a faulty underlying stream that is guaranteed to throw
        // an ArrayIndexOutOfBoundsException on the first read(). We achieve this
        // by constructing a ByteArrayInputStream with an invalid negative offset.
        final byte[] anyData = { 0 };
        final int invalidOffset = -1;
        final int anyLength = 1;
        final InputStream faultyUnderlyingStream = new ByteArrayInputStream(anyData, invalidOffset, anyLength);

        final WindowsLineEndingInputStream streamUnderTest =
                new WindowsLineEndingInputStream(faultyUnderlyingStream, false);

        // Act: Attempt to read from the wrapper stream.
        // This should trigger the exception from the underlying stream.
        // The @Test(expected=...) annotation will automatically assert that the
        // correct exception was thrown, causing the test to pass.
        streamUnderTest.read();
    }
}