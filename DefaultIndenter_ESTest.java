package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.*;

/**
 * Unit tests for the {@link DefaultIndenter} class, focusing on its construction,
 * configuration, and indentation writing logic.
 */
public class DefaultIndenterTest {

    private final JsonFactory jsonFactory = new JsonFactory();

    private JsonGenerator createGenerator(Writer writer) throws IOException {
        return jsonFactory.createGenerator(writer);
    }

    @Test
    public void constructor_shouldSetIndentAndEol() {
        DefaultIndenter indenter = new DefaultIndenter("    ", "\r\n");
        assertEquals("    ", indenter.getIndent());
        assertEquals("\r\n", indenter.getEol());
    }

    @Test(expected = NullPointerException.class)
    public void constructor_withNullIndent_shouldThrowNullPointerException() {
        new DefaultIndenter(null, "\n");
    }

    @Test
    public void withIndent_shouldCreateNewInstanceWithUpdatedIndent() {
        DefaultIndenter original = new DefaultIndenter("  ", "\n");
        DefaultIndenter updated = original.withIndent("\t");

        assertNotSame("A new instance should be created", original, updated);
        assertEquals("Indent should be updated", "\t", updated.getIndent());
        assertEquals("EOL should be preserved", "\n", updated.getEol());
    }

    @Test
    public void withIndent_withSameIndent_shouldReturnSameInstance() {
        DefaultIndenter original = new DefaultIndenter("  ", "\n");
        DefaultIndenter updated = original.withIndent("  ");
        assertSame("The same instance should be returned for the same indent", original, updated);
    }

    @Test
    public void withLinefeed_shouldCreateNewInstanceWithUpdatedLinefeed() {
        DefaultIndenter original = new DefaultIndenter("  ", "\n");
        DefaultIndenter updated = original.withLinefeed("\r\n");

        assertNotSame("A new instance should be created", original, updated);
        assertEquals("Indent should be preserved", "  ", updated.getIndent());
        assertEquals("EOL should be updated", "\r\n", updated.getEol());
    }

    @Test
    public void withLinefeed_withSameLinefeed_shouldReturnSameInstance() {
        DefaultIndenter original = new DefaultIndenter("  ", "\n");
        DefaultIndenter updated = original.withLinefeed("\n");
        assertSame("The same instance should be returned for the same linefeed", original, updated);
    }

    @Test
    public void isInline_shouldAlwaysReturnFalse() {
        assertFalse(new DefaultIndenter().isInline());
        assertFalse(new DefaultIndenter(" ", null).isInline());
    }

    @Test
    public void writeIndentation_withNegativeLevel_shouldWriteOnlyEol() throws IOException {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = createGenerator(writer);
        DefaultIndenter indenter = new DefaultIndenter("  ", "\n");

        indenter.writeIndentation(generator, -1);
        generator.flush();

        assertEquals("Only the end-of-line character should be written", "\n", writer.toString());
    }

    @Test
    public void writeIndentation_withZeroLevel_shouldWriteOnlyEol() throws IOException {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = createGenerator(writer);
        DefaultIndenter indenter = new DefaultIndenter("  ", "\n");

        indenter.writeIndentation(generator, 0);
        generator.flush();

        assertEquals("Only the end-of-line character should be written", "\n", writer.toString());
    }

    @Test
    public void writeIndentation_withPositiveLevel_shouldWriteIndentAndEol() throws IOException {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = createGenerator(writer);
        DefaultIndenter indenter = new DefaultIndenter("  ", "\n");

        indenter.writeIndentation(generator, 1);
        generator.flush();

        assertEquals("EOL and one level of indentation should be written", "\n  ", writer.toString());
    }

    @Test
    public void writeIndentation_withDeepLevel_shouldWriteMultipleIndentsAndEol() throws IOException {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = createGenerator(writer);
        // Use a single character indent for easier counting
        DefaultIndenter indenter = new DefaultIndenter(">", "\n");

        // Level 20 is deeper than the pre-calculated cache (16 levels)
        indenter.writeIndentation(generator, 20);
        generator.flush();

        // Expect EOL + 20 indent characters
        StringBuilder expected = new StringBuilder("\n");
        for (int i = 0; i < 20; i++) {
            expected.append(">");
        }
        assertEquals(expected.toString(), writer.toString());
    }

    @Test(expected = IOException.class)
    public void writeIndentation_withVeryLargeLevel_shouldCauseExceptionDueToOverflow() throws IOException {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = createGenerator(writer);
        DefaultIndenter indenter = new DefaultIndenter("  ", "\n"); // indent length > 1

        // This level, when multiplied by indent length (2), causes an integer overflow,
        // resulting in a negative length being passed to the underlying writer.
        indenter.writeIndentation(generator, Integer.MAX_VALUE);
    }

    @Test(expected = NullPointerException.class)
    public void writeIndentation_withNullGenerator_shouldThrowException() throws IOException {
        DefaultIndenter indenter = new DefaultIndenter();
        indenter.writeIndentation(null, 1);
    }
}