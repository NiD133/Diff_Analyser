package com.google.common.io;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.io.StringWriter;
import java.io.Writer;
import org.jspecify.annotations.NullUnmarked;

/**
 * Tests for {@link CharStreams#asWriter(Appendable)}.
 */
@NullUnmarked
public class CharStreamsTestTest6 extends IoTestCase {

    /**
     * Tests that asWriter returns a new Writer instance when the given Appendable
     * is not already a Writer. This is the wrapping behavior.
     */
    public void testAsWriter_withNonWriterAppendable_returnsNewWriter() {
        // Arrange: Create an Appendable that is not a Writer.
        Appendable appendable = new StringBuilder();

        // Act: Convert the Appendable to a Writer.
        Writer writer = CharStreams.asWriter(appendable);

        // Assert: The method should return a new, distinct Writer instance.
        assertNotSame("asWriter should wrap a non-Writer Appendable in a new object",
            appendable, writer);
    }

    /**
     * Tests that asWriter returns the original instance when the given Appendable
     * is already a Writer. This is the optimization to avoid unnecessary wrapping.
     */
    public void testAsWriter_withWriterInstance_returnsSameInstance() {
        // Arrange: Create an Appendable that is already a Writer.
        Writer writerAppendable = new StringWriter();

        // Act: "Convert" the Writer to a Writer.
        Writer resultWriter = CharStreams.asWriter(writerAppendable);

        // Assert: The method should return the original instance, as no wrapping is needed.
        assertSame("asWriter should not wrap an object that is already a Writer",
            writerAppendable, resultWriter);
    }
}