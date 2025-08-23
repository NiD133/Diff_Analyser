package org.apache.commons.io.output;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Contains tests for {@link XmlStreamWriter}.
 * This class focuses on a specific test case that was improved for clarity.
 */
public class XmlStreamWriter_ESTestTest19 extends XmlStreamWriter_ESTest_scaffolding {

    /**
     * Tests that calling write() with a null buffer throws a NullPointerException
     * after the internal writer has already been initialized. The first write call
     * triggers encoding detection and sets up an internal delegate writer. Subsequent
     * calls are passed to this delegate.
     */
    @Test(expected = NullPointerException.class)
    public void writeWithNullBufferAfterInitializationThrowsNPE() throws IOException {
        // Arrange: Create an XmlStreamWriter and ensure its internal writer is initialized.
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final XmlStreamWriter xmlStreamWriter = new XmlStreamWriter(outputStream);

        // Act 1: Perform an initial write to trigger encoding detection and
        // initialize the internal delegate writer.
        xmlStreamWriter.write(new char[]{'<', '?'});

        // Act 2: Attempt to write from a null buffer.
        // Assert: A NullPointerException is expected, as declared in the @Test annotation.
        xmlStreamWriter.write(null, 0, 1);
    }
}