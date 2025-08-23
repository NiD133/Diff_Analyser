package com.google.common.io;

import static org.junit.Assert.assertSame;

import java.io.Writer;
import org.junit.Test;

/**
 * Tests for {@link CharStreams}.
 */
public class CharStreamsTest {

    /**
     * Tests that {@link CharStreams#asWriter(Appendable)} returns the same instance
     * when the provided {@link Appendable} is already a {@link Writer}.
     * This is an optimization to avoid unnecessary wrapping.
     */
    @Test
    public void asWriter_whenTargetIsAlreadyAWriter_returnsSameInstance() {
        // Arrange: Create a Writer instance to use as the target.
        // CharStreams.nullWriter() is a convenient way to get a simple Writer.
        Writer targetWriter = CharStreams.nullWriter();

        // Act: Call the asWriter method with the Writer instance.
        Writer resultWriter = CharStreams.asWriter(targetWriter);

        // Assert: The returned object should be the exact same instance as the input,
        // not a new wrapper object.
        assertSame("Expected asWriter to return the original Writer instance without wrapping",
                targetWriter, resultWriter);
    }
}