package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class DefaultIndenterTest {

    // Helpers

    private static String writeWithWriterGenerator(DefaultIndenter indenter, int level) throws Exception {
        StringWriter writer = new StringWriter();
        JsonGenerator g = new JsonFactory().createGenerator(writer);
        indenter.writeIndentation(g, level);
        g.flush();
        return writer.toString();
    }

    private static String writeWithUtf8Generator(DefaultIndenter indenter, int level) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonGenerator g = new JsonFactory().createGenerator(out);
        indenter.writeIndentation(g, level);
        g.flush();
        return new String(out.toByteArray(), StandardCharsets.UTF_8);
    }

    private static String repeat(String s, int times) {
        StringBuilder b = new StringBuilder(s.length() * Math.max(times, 0));
        for (int i = 0; i < times; i++) {
            b.append(s);
        }
        return b.toString();
    }

    // Basic configuration

    @Test
    public void defaultConstructor_usesSystemLfAndTwoSpaces() {
        DefaultIndenter indenter = new DefaultIndenter();

        assertEquals(DefaultIndenter.SYS_LF, indenter.getEol());
        assertEquals("  ", indenter.getIndent());
        assertFalse(indenter.isInline());
    }

    @Test
    public void systemInstance_matchesDefaultSettings() {
        DefaultIndenter indenter = DefaultIndenter.SYSTEM_LINEFEED_INSTANCE;

        assertEquals(DefaultIndenter.SYS_LF, indenter.getEol());
        assertEquals("  ", indenter.getIndent());
        assertFalse(indenter.isInline());
    }

    // Immutability and factories

    @Test
    public void withLinefeed_returnsSameInstanceWhenUnchanged() {
        DefaultIndenter base = new DefaultIndenter();
        DefaultIndenter same = base.withLinefeed(base.getEol());

        assertSame(base, same);
    }

    @Test
    public void withLinefeed_returnsNewInstanceWhenChanged() {
        DefaultIndenter base = new DefaultIndenter();
        DefaultIndenter changed = base.withLinefeed("\r\n");

        assertNotSame(base, changed);
        assertEquals("\r\n", changed.getEol());
        // original remains unchanged
        assertEquals(DefaultIndenter.SYS_LF, base.getEol());
    }

    @Test
    public void withIndent_returnsSameInstanceWhenUnchanged() {
        DefaultIndenter base = new DefaultIndenter("..", DefaultIndenter.SYS_LF);
        DefaultIndenter same = base.withIndent("..");

        assertSame(base, same);
    }

    @Test
    public void withIndent_returnsNewInstanceWhenChanged() {
        DefaultIndenter base = new DefaultIndenter();
        DefaultIndenter changed = base.withIndent("\t");

        assertNotSame(base, changed);
        assertEquals("\t", changed.getIndent());
        // original remains unchanged
        assertEquals("  ", base.getIndent());
    }

    @Test
    public void withLinefeed_nullThrowsNpe() {
        DefaultIndenter base = new DefaultIndenter();
        assertThrows(NullPointerException.class, () -> base.withLinefeed(null));
    }

    @Test
    public void withIndent_nullThrowsNpe() {
        DefaultIndenter base = new DefaultIndenter();
        assertThrows(NullPointerException.class, () -> base.withIndent(null));
    }

    // Writing indentation

    @Test
    public void writeIndentation_writesEolPlusIndentPerLevel_withWriterGenerator() throws Exception {
        String indent = "..";
        String eol = "EOL";
        int level = 3;
        DefaultIndenter indenter = new DefaultIndenter(indent, eol);

        String out = writeWithWriterGenerator(indenter, level);

        assertEquals(eol + repeat(indent, level), out);
    }

    @Test
    public void writeIndentation_writesEolOnly_whenLevelIsZero() throws Exception {
        DefaultIndenter indenter = new DefaultIndenter(">", "LF");
        String out = writeWithWriterGenerator(indenter, 0);

        assertEquals("LF", out);
    }

    @Test
    public void writeIndentation_writesEolOnly_whenLevelIsNegative() throws Exception {
        DefaultIndenter indenter = new DefaultIndenter(">", "LF");
        String out = writeWithWriterGenerator(indenter, -10);

        assertEquals("LF", out);
    }

    @Test
    public void writeIndentation_handlesEmptyIndent() throws Exception {
        DefaultIndenter indenter = new DefaultIndenter("", "X");
        String out = writeWithWriterGenerator(indenter, 100);

        // No per-level characters when indent is empty
        assertEquals("X", out);
    }

    @Test
    public void writeIndentation_handlesLargeLevelsBeyondPrecomputedBuffer() throws Exception {
        // INDENT_LEVELS in the implementation is 16; use a larger level to exercise chunking
        int level = 40;
        DefaultIndenter indenter = new DefaultIndenter(" ", "\n");

        String out = writeWithWriterGenerator(indenter, level);

        assertEquals("\n" + repeat(" ", level), out);
    }

    @Test
    public void writeIndentation_worksWithUtf8GeneratorToo() throws Exception {
        DefaultIndenter indenter = new DefaultIndenter("  ", ":");
        String out = writeWithUtf8Generator(indenter, 2);

        assertEquals(":" + "    ", out);
    }
}