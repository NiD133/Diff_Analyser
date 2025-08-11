package org.apache.commons.cli.help;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Test suite for TextHelpAppendable functionality.
 * Tests text formatting, wrapping, table generation, and various output scenarios.
 */
public class TextHelpAppendableTest {

    // Test Constants
    private static final String SAMPLE_TEXT = "Width must be greater than 0";
    private static final String EMPTY_STRING = "";
    private static final int DEFAULT_WIDTH = 74;
    private static final int DEFAULT_LEFT_PAD = 1;
    private static final int DEFAULT_INDENT = 3;

    // ========== Basic Functionality Tests ==========

    @Test
    public void testSystemOutCreation() {
        TextHelpAppendable appendable = TextHelpAppendable.systemOut();
        
        assertEquals("Default left pad should be 1", DEFAULT_LEFT_PAD, appendable.getLeftPad());
        assertEquals("Default indent should be 3", DEFAULT_INDENT, appendable.getIndent());
        assertEquals("Default max width should be 74", DEFAULT_WIDTH, appendable.getMaxWidth());
    }

    @Test
    public void testAppendListWithMultipleItems() throws IOException {
        TextHelpAppendable appendable = TextHelpAppendable.systemOut();
        CharBuffer item = CharBuffer.allocate(3);
        List<CharSequence> items = Arrays.asList(item, item);
        
        appendable.appendList(true, items);
        
        // Verify default settings remain unchanged
        assertEquals(DEFAULT_LEFT_PAD, appendable.getLeftPad());
        assertEquals(DEFAULT_INDENT, appendable.getIndent());
        assertEquals(DEFAULT_WIDTH, appendable.getMaxWidth());
    }

    @Test
    public void testAppendHeaderWithLevel() throws IOException {
        TextHelpAppendable appendable = TextHelpAppendable.systemOut();
        char[] headerText = new char[6];
        CharBuffer header = CharBuffer.wrap(headerText);
        
        appendable.appendHeader(1, header);
        
        assertEquals(DEFAULT_LEFT_PAD, appendable.getLeftPad());
        assertEquals(DEFAULT_INDENT, appendable.getIndent());
        assertEquals(DEFAULT_WIDTH, appendable.getMaxWidth());
    }

    // ========== Table Functionality Tests ==========

    @Test
    public void testAppendTableWithValidData() throws IOException {
        TextHelpAppendable appendable = TextHelpAppendable.systemOut();
        
        List<TextStyle> styles = Arrays.asList(TextStyle.DEFAULT);
        List<String> headers = Arrays.asList("Header");
        List<List<String>> rows = Arrays.asList(Arrays.asList("Data"));
        
        TableDefinition table = TableDefinition.from("TestTable", styles, headers, rows);
        appendable.appendTable(table);
        
        assertEquals(DEFAULT_INDENT, appendable.getIndent());
        assertEquals(DEFAULT_WIDTH, appendable.getMaxWidth());
    }

    @Test
    public void testWriteEmptyColumnQueues() throws IOException {
        TextHelpAppendable appendable = TextHelpAppendable.systemOut();
        List<Queue<String>> emptyQueues = new LinkedList<>();
        List<TextStyle> emptyStyles = new ArrayList<>();
        
        appendable.writeColumnQueues(emptyQueues, emptyStyles);
        
        assertEquals(DEFAULT_LEFT_PAD, appendable.getLeftPad());
        assertEquals(DEFAULT_INDENT, appendable.getIndent());
        assertEquals(DEFAULT_WIDTH, appendable.getMaxWidth());
    }

    // ========== Text Wrapping Tests ==========

    @Test
    public void testIndexOfWrapAtEndOfText() {
        int wrapIndex = TextHelpAppendable.indexOfWrap(SAMPLE_TEXT, 1, 26);
        assertEquals("Should wrap at position 26", 26, wrapIndex);
    }

    @Test
    public void testIndexOfWrapWithinText() {
        int wrapIndex = TextHelpAppendable.indexOfWrap(SAMPLE_TEXT, 3, 3);
        assertEquals("Should find wrap position at 5", 5, wrapIndex);
    }

    @Test
    public void testIndexOfWrapWithEmptyBuffer() {
        StringWriter writer = new StringWriter();
        StringBuffer buffer = writer.getBuffer();
        
        int wrapIndex = TextHelpAppendable.indexOfWrap(buffer, 74, 1047);
        assertEquals("Empty buffer should return 0", 0, wrapIndex);
    }

    // ========== Style and Formatting Tests ==========

    @Test
    public void testResizeNonScalableStyle() {
        TextHelpAppendable appendable = TextHelpAppendable.systemOut();
        TextStyle.Builder builder = TextStyle.builder();
        builder.setScalable(false);
        
        TextStyle.Builder resized = appendable.resize(builder, 1);
        
        assertEquals(DEFAULT_LEFT_PAD, appendable.getLeftPad());
        assertEquals(0, resized.getIndent());
        assertEquals(DEFAULT_WIDTH, appendable.getMaxWidth());
        assertEquals(Integer.MAX_VALUE, resized.getMaxWidth());
        assertEquals(DEFAULT_INDENT, appendable.getIndent());
    }

    @Test
    public void testResizeWithPositiveFraction() {
        TextHelpAppendable appendable = TextHelpAppendable.systemOut();
        TextStyle.Builder builder = appendable.getTextStyleBuilder();
        builder.setMinWidth(1);
        
        appendable.resize(builder, 993.5739606);
        
        assertEquals(73524, builder.getMaxWidth());
        assertEquals(DEFAULT_INDENT, appendable.getIndent());
    }

    @Test
    public void testMakeColumnQueueWithValidText() {
        TextHelpAppendable appendable = TextHelpAppendable.systemOut();
        String testText = "org.apache.commons.cli.help.TextStyle$1";
        
        Queue<String> queue = appendable.makeColumnQueue(testText, TextStyle.DEFAULT);
        
        assertTrue("Queue should contain the test text", queue.contains(testText));
        assertEquals(DEFAULT_INDENT, appendable.getIndent());
        assertEquals(DEFAULT_WIDTH, appendable.getMaxWidth());
    }

    // ========== Getter/Setter Tests ==========

    @Test
    public void testSetAndGetMaxWidth() {
        TextHelpAppendable appendable = TextHelpAppendable.systemOut();
        int newWidth = 100;
        
        appendable.setMaxWidth(newWidth);
        
        assertEquals("Max width should be updated", newWidth, appendable.getMaxWidth());
    }

    @Test
    public void testSetAndGetLeftPad() {
        TextHelpAppendable appendable = TextHelpAppendable.systemOut();
        int newLeftPad = 5;
        
        appendable.setLeftPad(newLeftPad);
        
        assertEquals("Left pad should be updated", newLeftPad, appendable.getLeftPad());
    }

    @Test
    public void testSetAndGetIndent() {
        TextHelpAppendable appendable = TextHelpAppendable.systemOut();
        int newIndent = 10;
        
        appendable.setIndent(newIndent);
        
        assertEquals("Indent should be updated", newIndent, appendable.getIndent());
    }

    // ========== Error Condition Tests ==========

    @Test(expected = NullPointerException.class)
    public void testWriteColumnQueuesWithNullQueues() throws IOException {
        TextHelpAppendable appendable = TextHelpAppendable.systemOut();
        List<TextStyle> styles = new ArrayList<>();
        
        appendable.writeColumnQueues(null, styles);
    }

    @Test(expected = NullPointerException.class)
    public void testResizeWithNullBuilder() {
        TextHelpAppendable appendable = TextHelpAppendable.systemOut();
        
        appendable.resize(null, 1);
    }

    @Test(expected = NullPointerException.class)
    public void testPrintWrappedWithNullText() throws IOException {
        TextHelpAppendable appendable = TextHelpAppendable.systemOut();
        
        appendable.printWrapped((String) null);
    }

    @Test(expected = NullPointerException.class)
    public void testPrintWrappedWithNullTextAndStyle() throws IOException {
        TextHelpAppendable appendable = TextHelpAppendable.systemOut();
        
        appendable.printWrapped(null, TextStyle.DEFAULT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIndexOfWrapWithInvalidWidth() {
        TextHelpAppendable.indexOfWrap("test", -1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendHeaderWithInvalidLevel() throws IOException {
        TextHelpAppendable appendable = TextHelpAppendable.systemOut();
        CharBuffer buffer = CharBuffer.allocate(74);
        
        appendable.appendHeader(-1, buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintWrappedWithZeroWidth() throws IOException {
        TextHelpAppendable appendable = TextHelpAppendable.systemOut();
        TextStyle.Builder builder = appendable.getTextStyleBuilder();
        appendable.resize(builder, -1606.83);
        
        appendable.printWrapped("/");
    }

    @Test(expected = NullPointerException.class)
    public void testAppendTableWithNullTable() throws IOException {
        TextHelpAppendable appendable = TextHelpAppendable.systemOut();
        
        appendable.appendTable(null);
    }

    @Test(expected = NullPointerException.class)
    public void testAdjustTableFormatWithNullTable() {
        TextHelpAppendable appendable = TextHelpAppendable.systemOut();
        
        appendable.adjustTableFormat(null);
    }

    // ========== Buffer Exception Tests ==========

    @Test(expected = ReadOnlyBufferException.class)
    public void testWriteToReadOnlyBuffer() throws IOException {
        CharBuffer readOnlyBuffer = CharBuffer.wrap(EMPTY_STRING);
        TextHelpAppendable appendable = new TextHelpAppendable(readOnlyBuffer);
        List<String> data = Arrays.asList(EMPTY_STRING);
        List<TextStyle> styles = Arrays.asList(TextStyle.DEFAULT);
        
        List<Queue<String>> queues = appendable.makeColumnQueues(data, styles);
        appendable.writeColumnQueues(queues, styles);
    }

    @Test(expected = BufferOverflowException.class)
    public void testWriteToSmallBuffer() throws IOException {
        CharBuffer smallBuffer = CharBuffer.allocate(1);
        TextHelpAppendable appendable = new TextHelpAppendable(smallBuffer);
        
        appendable.printWrapped(SAMPLE_TEXT, TextStyle.DEFAULT);
    }

    // ========== Integration Tests ==========

    @Test
    public void testCompleteTableWorkflow() throws IOException {
        TextHelpAppendable appendable = TextHelpAppendable.systemOut();
        
        List<TextStyle> styles = Arrays.asList(TextStyle.DEFAULT);
        List<String> headers = Arrays.asList("Command");
        List<List<String>> rows = Arrays.asList(Arrays.asList("help"));
        
        TableDefinition originalTable = TableDefinition.from("Commands", styles, headers, rows);
        TableDefinition adjustedTable = appendable.adjustTableFormat(originalTable);
        appendable.appendTable(adjustedTable);
        
        assertEquals(DEFAULT_INDENT, appendable.getIndent());
        assertEquals(DEFAULT_WIDTH, appendable.getMaxWidth());
        assertEquals(DEFAULT_LEFT_PAD, appendable.getLeftPad());
    }

    @Test
    public void testPrintWrappedWithDefaultStyle() throws IOException {
        TextHelpAppendable appendable = TextHelpAppendable.systemOut();
        
        appendable.printWrapped(SAMPLE_TEXT, TextStyle.DEFAULT);
        
        assertEquals(DEFAULT_INDENT, appendable.getIndent());
        assertEquals(DEFAULT_WIDTH, appendable.getMaxWidth());
    }

    @Test
    public void testAppendEmptyList() throws IOException {
        TextHelpAppendable appendable = TextHelpAppendable.systemOut();
        
        appendable.appendList(false, null);
        
        assertEquals(DEFAULT_WIDTH, appendable.getMaxWidth());
        assertEquals(DEFAULT_LEFT_PAD, appendable.getLeftPad());
        assertEquals(DEFAULT_INDENT, appendable.getIndent());
    }
}