package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link UTF8Writer} class, focusing on specific edge cases.
 */
public class UTF8WriterTest {

    /**
     * Tests the behavior of the {@code convertSurrogate} method when it is called
     * in an invalid state—that is, without a preceding high surrogate character.
     *
     * In this scenario, the writer's internal high surrogate value is 0. The method
     * does not throw an exception but instead calculates and returns a meaningless,
     * incorrect code point. This test verifies this specific, albeit unusual, behavior.
     */
    @Test
    public void convertSurrogate_whenHighSurrogateIsMissing_returnsIncorrectCodepoint() throws IOException {
        // Arrange: Set up the UTF8Writer.
        // The IOContext and OutputStream are required for the constructor, but their
        // specific configurations are not relevant to the convertSurrogate method itself.
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                bufferRecycler,
                null, // content reference
                true  // recycling
        );
        // Using a simple ByteArrayOutputStream as the actual output is not tested.
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        UTF8Writer writer = new UTF8Writer(ioContext, out);

        // The input is the highest possible value for a low surrogate character.
        final int lowSurrogate = 0xDFFF; // 57343 in decimal

        // Act: Call convertSurrogate without first setting a high surrogate.
        // The writer's internal _surrogate field will be its default value, 0.
        int actualCodepoint = writer.convertSurrogate(lowSurrogate);

        // Assert: The result should be a garbage value based on the internal calculation.
        // The formula used is: (highSurrogate << 10) + lowSurrogate + SURROGATE_BASE
        // Since highSurrogate is 0, the expected result is simply lowSurrogate + SURROGATE_BASE.
        int expectedCodepoint = lowSurrogate + UTF8Writer.SURROGATE_BASE;
        assertEquals(expectedCodepoint, actualCodepoint);

        // For reference, the original test asserted against the magic number -56556545.
        // Our calculated value should match this.
        assertEquals(-56556545, actualCodepoint);
    }
}