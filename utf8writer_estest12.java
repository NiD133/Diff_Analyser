package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.CharBuffer;

/**
 * Contains tests for the {@link UTF8Writer} class, focusing on handling very large inputs.
 */
// Note: The original test class extended a scaffolding class. This inheritance is
// maintained for context, but in a real-world refactoring, it might be removed.
public class UTF8WriterLargeInputTest extends UTF8Writer_ESTest_scaffolding {

    /**
     * Tests that attempting to append an extremely large CharSequence to the writer
     * results in an {@link OutOfMemoryError}.
     * <p>
     * This behavior is expected because the default implementation of {@link java.io.Writer#append(CharSequence)}
     * first converts the entire sequence to a {@link String} by calling {@code toString()},
     * which can exhaust heap memory for very large sequences.
     */
    @Test(timeout = 4000, expected = OutOfMemoryError.class)
    public void append_withExtremelyLargeCharSequence_shouldThrowOutOfMemoryError() throws IOException {
        // Arrange
        // A size large enough to likely cause an OutOfMemoryError when the CharBuffer
        // is converted to a String on typical heap sizes.
        final int HUGE_SEQUENCE_SIZE = 20_000_000;

        // The UTF8Writer requires an IOContext, which we configure with default settings.
        // The ContentReference is not relevant for this test and can be null.
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                bufferRecycler,
                null, // contentReference
                false); // isResourceManaged

        // Use a ByteArrayOutputStream as a sink for the writer's output.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        UTF8Writer utf8Writer = new UTF8Writer(ioContext, outputStream);

        // Create a character sequence that is too large to fit in memory as a String.
        CharSequence largeCharSequence = CharBuffer.allocate(HUGE_SEQUENCE_SIZE);

        // Act & Assert
        // The append operation is expected to fail with an OutOfMemoryError.
        utf8Writer.append(largeCharSequence);
    }
}