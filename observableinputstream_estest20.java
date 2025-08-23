package org.apache.commons.io.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.PipedInputStream;
import java.util.List;

import org.apache.commons.io.IOExceptionList;
import org.junit.Test;

/**
 * Tests for {@link ObservableInputStream}.
 */
public class ObservableInputStreamTest {

    /**
     * Tests that an IOException from the underlying stream during a single-byte
     * read is caught, forwarded to observers, and then re-thrown wrapped in an
     * {@link IOExceptionList}.
     */
    @Test
    public void testReadOnFailingStreamPropagatesException() {
        // Arrange: Create an input stream that is guaranteed to fail on read.
        // An unconnected PipedInputStream serves this purpose.
        final PipedInputStream failingStream = new PipedInputStream();
        final ObservableInputStream.Observer observer = new ObservableInputStream.Observer() {
            // A simple, anonymous observer for this test case.
        };
        final ObservableInputStream observableStream = new ObservableInputStream(failingStream, observer);

        // Act & Assert
        try {
            observableStream.read();
            fail("Expected an IOException to be thrown.");
        } catch (final IOException e) {
            // The exception should be wrapped in an IOExceptionList.
            assertTrue("Exception should be an instance of IOExceptionList.", e instanceof IOExceptionList);

            // The list should contain the original exception from the failing stream.
            final IOExceptionList exceptionList = (IOExceptionList) e;
            final List<Throwable> causes = exceptionList.getCauseList();
            assertEquals("There should be exactly one cause.", 1, causes.size());
            assertTrue("The cause should be an IOException.", causes.get(0) instanceof IOException);
            assertEquals("The cause should have the expected message.", "Pipe not connected", causes.get(0).getMessage());
        }
    }
}