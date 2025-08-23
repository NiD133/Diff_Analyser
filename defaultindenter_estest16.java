package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link DefaultIndenter} class.
 */
public class DefaultIndenterTest {

    /**
     * Verifies that calling writeIndentation with a negative indentation level
     * results in only the end-of-line (EOL) sequence being written, with no
     * preceding indentation spaces.
     */
    @Test
    public void writeIndentation_withNegativeLevel_shouldWriteOnlyEol() throws IOException {
        // Arrange
        final String customEol = "As4M!C";
        final String emptyIndent = "";
        // A negative level should be treated as a level of 0 (no indentation).
        final int negativeLevel = -1;

        DefaultIndenter indenter = new DefaultIndenter(emptyIndent, customEol);
        StringWriter outputWriter = new StringWriter();
        JsonFactory factory = new JsonFactory();
        JsonGenerator jsonGenerator = factory.createGenerator(outputWriter);

        // Act
        indenter.writeIndentation(jsonGenerator, negativeLevel);
        jsonGenerator.flush(); // Ensure the output is written to the StringWriter

        // Assert
        // The generator should have written only the custom EOL string.
        assertEquals(customEol, outputWriter.toString());
    }
}