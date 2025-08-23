package org.apache.commons.compress.utils;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Contains tests for the {@link ByteUtils} class.
 */
public class ByteUtilsTest {

    /**
     * Tests that {@code ByteUtils.fromLittleEndian(InputStream, int)} propagates an
     * {@code ArrayIndexOutOfBoundsException} thrown by the underlying stream's read() method.
     *
     * <p>This is a test for exception propagation. We create a faulty {@code ByteArrayInputStream}
     * with an invalid negative offset. The stream's constructor does not throw an exception, but any
     * subsequent call to {@code read()} will. The test ensures that our utility method does not
     * suppress or wrap this runtime exception.</p>
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void fromLittleEndianWithInputStreamShouldPropagateExceptionFromUnderlyingStream() throws IOException {
        // Arrange: Create an InputStream that is guaranteed to fail on read()
        // by providing an invalid negative offset to its constructor.
        byte[] buffer = new byte[1];
        InputStream faultyStream = new ByteArrayInputStream(buffer, -1, 6);

        // Act: Attempt to read from the faulty stream via the method under test.
        // This call is expected to throw an ArrayIndexOutOfBoundsException, which
        // will be caught and verified by the @Test(expected=...) annotation.
        ByteUtils.fromLittleEndian(faultyStream, 1);
    }
}