package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link DefaultIndenter} class, focusing on edge cases.
 */
public class DefaultIndenterTest {

    /**
     * This test verifies that providing a very large indentation level causes an
     * integer overflow when calculating the number of characters to write.
     * The SYSTEM_LINEFEED_INSTANCE uses 2 characters for indentation. Multiplying
     * the large level by 2 results in a negative number, which causes the
     * underlying JsonGenerator to throw an IOException.
     */
    @Test
    public void writeIndentation_withLargeLevelCausingIntegerOverflow_shouldThrowIOException() throws IOException {
        // Arrange
        DefaultIndenter indenter = DefaultIndenter.SYSTEM_LINEFEED_INSTANCE;

        // Use a level so large that (level * 2) overflows the integer capacity.
        // Specifically, 2147483645 * 2 wraps around to -6.
        int largeLevelThatCausesOverflow = 2147483645;

        JsonFactory factory = new JsonFactory();
        Writer writer = new StringWriter();
        JsonGenerator generator = factory.createGenerator(writer);

        // Act & Assert
        try {
            indenter.writeIndentation(generator, largeLevelThatCausesOverflow);
            fail("Expected an IOException because the calculated indentation length is negative due to integer overflow.");
        } catch (IOException e) {
            // The JsonGenerator correctly rejects requests to write a negative number of characters.
            String message = e.getMessage();
            assertTrue("Exception message should indicate an invalid length argument.",
                    message.contains("Invalid") && message.contains("len"));
        } finally {
            generator.close();
        }
    }
}