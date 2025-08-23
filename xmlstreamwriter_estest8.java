package org.apache.commons.io.output;

import org.junit.Test;
import java.io.OutputStream;

import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link XmlStreamWriter}.
 * This class focuses on scenarios involving the close() method.
 */
public class XmlStreamWriterTest {

    /**
     * Tests that calling close() on a writer initialized with a null OutputStream
     * throws a NullPointerException.
     * <p>
     * This behavior is expected because the underlying writer is lazily initialized
     * upon the first write or close operation, and its creation requires a
     * non-null OutputStream.
     */
    @Test
    public void close_whenConstructedWithNullOutputStream_shouldThrowNullPointerException() {
        // Arrange: Create a writer with a null underlying stream.
        final XmlStreamWriter writer = new XmlStreamWriter((OutputStream) null);

        // Act & Assert: Verify that closing the writer throws a NullPointerException.
        // The method reference writer::close represents the action that is expected to fail.
        assertThrows(NullPointerException.class, writer::close);
    }
}