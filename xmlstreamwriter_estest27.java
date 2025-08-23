package org.apache.commons.io.output;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link XmlStreamWriter.Builder}.
 */
public class XmlStreamWriterBuilderTest {

    @Test
    public void getShouldThrowIllegalStateExceptionWhenNoOutputIsSet() throws IOException {
        // Arrange: Create a builder without specifying an output destination (the "origin").
        final XmlStreamWriter.Builder builder = XmlStreamWriter.builder();

        // Act & Assert: Verify that calling get() on the builder throws an IllegalStateException
        // because no output origin (like a File or OutputStream) has been configured.
        final IllegalStateException thrown = assertThrows(
            "Building a writer requires an output origin.",
            IllegalStateException.class,
            builder::get
        );

        // Assert: Check the exception message for correctness.
        assertEquals("origin == null", thrown.getMessage());
    }
}