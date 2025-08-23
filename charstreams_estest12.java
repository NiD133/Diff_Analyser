package com.google.common.io;

import org.junit.Test;
import static org.junit.Assert.fail;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

/**
 * This test class has been improved to verify the behavior of the CharStreams.skipFully method,
 * specifically when the underlying Reader throws an unchecked exception.
 */
public class CharStreams_ESTestTest12 {

    /**
     * Tests that CharStreams.skipFully propagates an ArrayIndexOutOfBoundsException
     * thrown by the underlying Reader.
     */
    @Test(timeout = 4000)
    public void skipFully_whenReaderThrowsUncheckedException_propagatesException() {
        // Arrange: Create a Reader that is backed by a misconfigured ByteArrayInputStream.
        // The stream is set up with a negative offset, which will cause its read() method
        // to throw an ArrayIndexOutOfBoundsException.
        byte[] dummyData = new byte[10];
        int invalidOffset = -1;
        int length = 20; // This length is also out of bounds, but the negative offset is checked first.
        ByteArrayInputStream faultyInputStream = new ByteArrayInputStream(dummyData, invalidOffset, length);
        Reader faultyReader = new InputStreamReader(faultyInputStream, Charset.defaultCharset());

        // Act & Assert: Call skipFully and assert that it propagates the expected exception from the underlying stream.
        try {
            CharStreams.skipFully(faultyReader, 1L);
            fail("Expected an ArrayIndexOutOfBoundsException to be thrown due to the invalid stream configuration.");
        } catch (ArrayIndexOutOfBoundsException expected) {
            // This is the expected behavior. The exception from the underlying stream is correctly propagated.
        } catch (IOException e) {
            fail("An unexpected IOException was thrown, but an ArrayIndexOutOfBoundsException was expected. " + e.getMessage());
        }
    }
}