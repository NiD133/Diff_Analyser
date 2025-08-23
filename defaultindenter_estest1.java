package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link DefaultIndenter} class.
 * Note: The original test class name 'DefaultIndenter_ESTestTest1' was likely auto-generated
 * and has been renamed for clarity.
 */
public class DefaultIndenterTest {

    private final JsonFactory jsonFactory = new JsonFactory();

    /**
     * Verifies that writing an indentation at level 0 results in only a
     * linefeed character being written, with no preceding spaces.
     */
    @Test
    public void writeIndentation_withLevelZero_shouldWriteOnlyLinefeed() throws IOException {
        // Arrange
        StringWriter outputWriter = new StringWriter();
        JsonGenerator generator = jsonFactory.createGenerator(outputWriter);
        DefaultIndenter indenter = DefaultIndenter.SYSTEM_LINEFEED_INSTANCE;
        String expectedOutput = System.getProperty("line.separator");

        // Act
        indenter.writeIndentation(generator, 0);
        generator.flush(); // Ensure all buffered content is written to the writer

        // Assert
        assertEquals("The output should be just the system line separator.",
                expectedOutput, outputWriter.toString());
    }
}