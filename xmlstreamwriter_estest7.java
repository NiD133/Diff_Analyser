package org.apache.commons.io.output;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Contains tests for the {@link XmlStreamWriter} class.
 * This class focuses on behavior related to stream state (e.g., closed).
 */
// Note: The original class name and inheritance are preserved.
public class XmlStreamWriter_ESTestTest7 extends XmlStreamWriter_ESTest_scaffolding {

    /**
     * Verifies that attempting to flush a closed XmlStreamWriter throws an IOException.
     * A writer must not be usable after it has been closed.
     */
    @Test(expected = IOException.class)
    public void flushOnClosedWriterShouldThrowIOException() throws IOException {
        // Arrange: Create a writer and immediately close it to set up the test state.
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final XmlStreamWriter writer = new XmlStreamWriter(outputStream);
        writer.close();

        // Act: Attempt to flush the closed writer.
        // This action is expected to throw an IOException.
        writer.flush();

        // Assert: The test succeeds if an IOException is thrown, as declared
        // by the 'expected' attribute of the @Test annotation.
    }
}