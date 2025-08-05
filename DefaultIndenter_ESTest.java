package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import java.io.StringWriter;
import java.io.IOException;

/**
 * Test suite for DefaultIndenter class functionality.
 * Tests indentation behavior, configuration options, and edge cases.
 */
public class DefaultIndenterTest {

    @Test
    public void testDefaultConstructor_UsesSystemLinefeedAndTwoSpaces() {
        DefaultIndenter indenter = new DefaultIndenter();
        
        // Default constructor should use system line separator
        assertEquals(DefaultIndenter.SYS_LF, indenter.getEol());
        assertEquals("  ", indenter.getIndent());
        assertFalse(indenter.isInline());
    }

    @Test
    public void testCustomConstructor_UsesProvidedIndentAndLinefeed() {
        String customIndent = "    "; // 4 spaces
        String customLinefeed = "\r\n"; // Windows-style
        
        DefaultIndenter indenter = new DefaultIndenter(customIndent, customLinefeed);
        
        assertEquals(customLinefeed, indenter.getEol());
        assertEquals(customIndent, indenter.getIndent());
    }

    @Test
    public void testWithLinefeed_CreatesNewInstanceWithDifferentLinefeed() {
        DefaultIndenter original = new DefaultIndenter();
        String newLinefeed = "\r";
        
        DefaultIndenter modified = original.withLinefeed(newLinefeed);
        
        assertEquals(newLinefeed, modified.getEol());
        // Original should be unchanged
        assertEquals(DefaultIndenter.SYS_LF, original.getEol());
    }

    @Test
    public void testWithLinefeed_ReturnsSameInstanceWhenLinefeedUnchanged() {
        DefaultIndenter indenter = new DefaultIndenter();
        
        DefaultIndenter result = indenter.withLinefeed(DefaultIndenter.SYS_LF);
        
        assertSame(indenter, result);
    }

    @Test
    public void testWithIndent_CreatesNewInstanceWithDifferentIndent() {
        DefaultIndenter original = new DefaultIndenter();
        String newIndent = "\t"; // Tab character
        
        DefaultIndenter modified = original.withIndent(newIndent);
        
        assertEquals(newIndent, modified.getIndent());
        // Original should be unchanged
        assertEquals("  ", original.getIndent());
    }

    @Test
    public void testWithIndent_ReturnsSameInstanceWhenIndentUnchanged() {
        DefaultIndenter indenter = new DefaultIndenter("", "");
        
        DefaultIndenter result = indenter.withIndent("");
        
        assertSame(indenter, result);
    }

    @Test
    public void testWriteIndentation_WritesCorrectIndentationForLevel() throws IOException {
        DefaultIndenter indenter = new DefaultIndenter("--", "|");
        JsonFactory factory = new JsonFactory();
        StringWriter writer = new StringWriter();
        JsonGenerator generator = factory.createGenerator(writer);
        
        indenter.writeIndentation(generator, 2); // Level 2 indentation
        generator.flush();
        
        String output = writer.toString();
        assertEquals("|----", output); // linefeed + 2 levels of "--"
    }

    @Test
    public void testWriteIndentation_WritesOnlyLinefeedForZeroLevel() throws IOException {
        DefaultIndenter indenter = new DefaultIndenter("  ", "\n");
        JsonFactory factory = new JsonFactory();
        StringWriter writer = new StringWriter();
        JsonGenerator generator = factory.createGenerator(writer);
        
        indenter.writeIndentation(generator, 0);
        generator.flush();
        
        String output = writer.toString();
        assertEquals("\n", output); // Only linefeed, no indentation
    }

    @Test
    public void testWriteIndentation_HandlesNegativeLevel() throws IOException {
        DefaultIndenter indenter = new DefaultIndenter("  ", "\n");
        JsonFactory factory = new JsonFactory();
        StringWriter writer = new StringWriter();
        JsonGenerator generator = factory.createGenerator(writer);
        
        indenter.writeIndentation(generator, -5);
        generator.flush();
        
        String output = writer.toString();
        assertEquals("\n", output); // Should only write linefeed for negative levels
    }

    @Test
    public void testSystemLinefeedInstance_UsesSystemDefaults() {
        DefaultIndenter systemInstance = DefaultIndenter.SYSTEM_LINEFEED_INSTANCE;
        
        assertEquals("  ", systemInstance.getIndent());
        assertEquals(DefaultIndenter.SYS_LF, systemInstance.getEol());
        assertFalse(systemInstance.isInline());
    }

    @Test
    public void testIsInline_AlwaysReturnsFalse() {
        DefaultIndenter indenter1 = new DefaultIndenter();
        DefaultIndenter indenter2 = new DefaultIndenter("", "");
        DefaultIndenter indenter3 = new DefaultIndenter("\t", "\r\n");
        
        assertFalse(indenter1.isInline());
        assertFalse(indenter2.isInline());
        assertFalse(indenter3.isInline());
    }

    @Test
    public void testConstructorWithNullIndent_ThrowsNullPointerException() {
        try {
            new DefaultIndenter(null, "\n");
            fail("Expected NullPointerException for null indent");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test
    public void testWithLinefeedNull_ThrowsNullPointerException() {
        DefaultIndenter indenter = new DefaultIndenter();
        
        try {
            indenter.withLinefeed(null);
            fail("Expected NullPointerException for null linefeed");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test
    public void testWithIndentNull_ThrowsNullPointerException() {
        DefaultIndenter indenter = new DefaultIndenter();
        
        try {
            indenter.withIndent(null);
            fail("Expected NullPointerException for null indent");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test
    public void testWriteIndentationWithNullGenerator_ThrowsNullPointerException() {
        DefaultIndenter indenter = new DefaultIndenter();
        
        try {
            indenter.writeIndentation(null, 1);
            fail("Expected NullPointerException for null generator");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test
    public void testEmptyIndentAndLinefeed_WorksCorrectly() {
        DefaultIndenter indenter = new DefaultIndenter("", "");
        
        assertEquals("", indenter.getIndent());
        assertEquals("", indenter.getEol());
    }

    @Test
    public void testConstructorWithNullLinefeed_AllowsNullEol() {
        DefaultIndenter indenter = new DefaultIndenter("  ", null);
        
        assertNull(indenter.getEol());
        assertEquals("  ", indenter.getIndent());
    }
}