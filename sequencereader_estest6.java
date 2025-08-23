package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.PipedReader;
import java.io.Reader;

/**
 * This test class contains the refactored test case.
 * While the original class name "SequenceReader_ESTestTest6" is kept for context,
 * a more appropriate name would be "SequenceReaderTest".
 */
public class SequenceReader_ESTestTest6 extends SequenceReader_ESTest_scaffolding {

    /**
     * Tests that if an underlying reader throws an IOException during a read operation,
     * the SequenceReader correctly propagates that exception.
     */
    @Test
    public void readShouldPropagateIOExceptionFromUnderlyingReader() {
        // Arrange: Create a reader that is guaranteed to throw an IOException on read.
        // An unconnected PipedReader serves this purpose perfectly.
        Reader faultyReader = new PipedReader();
        SequenceReader sequenceReader = new SequenceReader(faultyReader);

        // Act & Assert: Attempt to read and verify that the expected IOException is thrown.
        try {
            sequenceReader.read();
            fail("Expected an IOException because the underlying PipedReader is not connected.");
        } catch (final IOException e) {
            // Verify that the propagated exception is the one we expect.
            assertEquals("Pipe not connected", e.getMessage());
        }
    }
}