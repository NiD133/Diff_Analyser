package org.apache.commons.cli.help;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Readable, behavior-focused tests for TextHelpAppendable.
 * These tests aim to validate key behaviors and guard-clauses with clear intent.
 */
public class TextHelpAppendableTest {

    // -----------------------------
    // Defaults and basic configuration
    // -----------------------------

    @Test
    public void systemOut_hasExpectedDefaults() {
        TextHelpAppendable help = TextHelpAppendable.systemOut();

        assertEquals("Default max width", TextHelpAppendable.DEFAULT_WIDTH, help.getMaxWidth());
        assertEquals("Default indent", TextHelpAppendable.DEFAULT_INDENT, help.getIndent());
        assertEquals("Default left pad", TextHelpAppendable.DEFAULT_LEFT_PAD, help.getLeftPad());
    }

    @Test
    public void setters_updateConfigurationAndBuilder() {
        TextHelpAppendable help = new TextHelpAppendable(new StringBuilder());

        help.setMaxWidth(100);
        help.setIndent(5);
        help.setLeftPad(2);

        assertEquals(100, help.getMaxWidth());
        assertEquals(5, help.getIndent());
        assertEquals(2, help.getLeftPad());

        TextStyle.Builder builder = help.getTextStyleBuilder();
        assertEquals(100, builder.getMaxWidth());
        assertEquals(5, builder.getIndent());
        assertEquals(2, builder.getLeftPad());
    }

    // -----------------------------
    // Header behavior
    // -----------------------------

    @Test
    public void appendHeader_rejectsLevelLessThanOne() {
        TextHelpAppendable help = new TextHelpAppendable(new StringBuilder());
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> help.appendHeader(0, "Header")
        );
        assertTrue(ex.getMessage() == null || ex.getMessage().toLowerCase().contains("level"));
    }

    @Test
    public void appendHeader_allowsNullText() throws IOException {
        TextHelpAppendable help = new TextHelpAppendable(new StringBuilder());
        // Should not throw
        help.appendHeader(1, null);
    }

    // -----------------------------
    // Printing and wrapping
    // -----------------------------

    @Test
    public void printWrapped_rejectsNullText() {
        TextHelpAppendable help = new TextHelpAppendable(new StringBuilder());
        assertThrows(NullPointerException.class, () -> help.printWrapped((String) null));
    }

    @Test
    public void printWrappedWithStyle_rejectsNullText() {
        TextHelpAppendable help = new TextHelpAppendable(new StringBuilder());
        assertThrows(NullPointerException.class, () -> help.printWrapped(null, TextStyle.DEFAULT));
    }

    // -----------------------------
    // Lists
    // -----------------------------

    @Test
    public void appendList_ignoresNullCollectionWhenUnordered() throws IOException {
        TextHelpAppendable help = new TextHelpAppendable(new StringBuilder());
        // Should not throw or fail when list is null and ordered == false.
        help.appendList(false, null);
    }

    // -----------------------------
    // Tables
    // -----------------------------

    @Test
    public void appendTable_rejectsNullDefinition() {
        TextHelpAppendable help = new TextHelpAppendable(new StringBuilder());
        assertThrows(NullPointerException.class, () -> help.appendTable(null));
    }

    @Test
    public void adjustTableFormat_rejectsNullDefinition() {
        TextHelpAppendable help = new TextHelpAppendable(new StringBuilder());
        assertThrows(NullPointerException.class, () -> help.adjustTableFormat(null));
    }

    // -----------------------------
    // Column queues
    // -----------------------------

    @Test
    public void writeColumnQueues_rejectsNullQueues() {
        TextHelpAppendable help = new TextHelpAppendable(new StringBuilder());
        assertThrows(NullPointerException.class, () -> help.writeColumnQueues(null, new ArrayList<>()));
    }

    @Test
    public void makeColumnQueue_rejectsNullCharSequence() {
        TextHelpAppendable help = new TextHelpAppendable(new StringBuilder());
        assertThrows(NullPointerException.class, () -> help.makeColumnQueue(null, TextStyle.DEFAULT));
    }

    // -----------------------------
    // Titles and paragraphs
    // -----------------------------

    @Test
    public void appendTitle_throwsWhenNoUnderlyingAppendable() {
        TextHelpAppendable help = new TextHelpAppendable(null);
        assertThrows(NullPointerException.class, () -> help.appendTitle("Title"));
    }

    @Test
    public void appendParagraph_writesToProvidedAppendable() throws IOException {
        StringBuilder sink = new StringBuilder();
        TextHelpAppendable help = new TextHelpAppendable(sink);

        help.appendParagraph("Hello, world!");
        // Do not assert exact formatting; just ensure something was appended.
        assertTrue(sink.length() > 0);
    }

    // -----------------------------
    // indexOfWrap validation
    // -----------------------------

    @Test
    public void indexOfWrap_rejectsNullText() {
        assertThrows(NullPointerException.class, () -> TextHelpAppendable.indexOfWrap(null, 10, 0));
    }

    @Test
    public void indexOfWrap_rejectsNonPositiveWidth() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> TextHelpAppendable.indexOfWrap("abc", 0, 0)
        );
        assertTrue(ex.getMessage() == null || ex.getMessage().toLowerCase().contains("width"));
    }
}