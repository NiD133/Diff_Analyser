package org.apache.commons.cli.help;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;

import java.io.IOException;
import java.io.PipedWriter;
import java.io.StringWriter;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;
import java.nio.charset.Charset;
import java.util.*;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class TextHelpAppendable_ESTest extends TextHelpAppendable_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testAppendListWithCharBuffer() throws Throwable {
        TextHelpAppendable textHelpAppendable = TextHelpAppendable.systemOut();
        CharBuffer charBuffer = CharBuffer.allocate(3);
        ArrayDeque<CharSequence> deque = new ArrayDeque<>();
        deque.add(charBuffer);
        deque.add(charBuffer);
        textHelpAppendable.appendList(true, deque);
        assertEquals(1, textHelpAppendable.getLeftPad());
        assertEquals(3, textHelpAppendable.getIndent());
        assertEquals(74, textHelpAppendable.getMaxWidth());
    }

    @Test(timeout = 4000)
    public void testAppendHeaderWithCharBuffer() throws Throwable {
        TextHelpAppendable textHelpAppendable = TextHelpAppendable.systemOut();
        char[] charArray = new char[6];
        CharBuffer charBuffer = CharBuffer.wrap(charArray);
        textHelpAppendable.appendHeader(1, charBuffer);
        assertEquals(1, textHelpAppendable.getLeftPad());
        assertEquals(3, textHelpAppendable.getIndent());
        assertEquals(74, textHelpAppendable.getMaxWidth());
    }

    @Test(timeout = 4000)
    public void testAppendTableWithPriorityQueue() throws Throwable {
        TextHelpAppendable textHelpAppendable = TextHelpAppendable.systemOut();
        ArrayList<TextStyle> styles = new ArrayList<>();
        styles.add(TextStyle.DEFAULT);
        ArrayList<String> headers = new ArrayList<>();
        ArrayList<String> data = new ArrayList<>();
        data.add("_uFdX>H}$Z");
        headers.add("");
        Comparator<Object> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        PriorityQueue<List<String>> queue = new PriorityQueue<>(comparator);
        queue.add(headers);
        TableDefinition tableDefinition = TableDefinition.from("a", styles, data, queue);
        textHelpAppendable.appendTable(tableDefinition);
        assertEquals(3, textHelpAppendable.getIndent());
        assertEquals(74, textHelpAppendable.getMaxWidth());
    }

    @Test(timeout = 4000)
    public void testIndexOfWrap() throws Throwable {
        int index = TextHelpAppendable.indexOfWrap("Width must be greater than 0", 1, 26);
        assertEquals(26, index);
    }

    @Test(timeout = 4000)
    public void testWriteColumnQueuesWithEmptyLists() throws Throwable {
        TextHelpAppendable textHelpAppendable = TextHelpAppendable.systemOut();
        LinkedList<Queue<String>> queueList = new LinkedList<>();
        Vector<TextStyle> styles = new Vector<>();
        textHelpAppendable.writeColumnQueues(queueList, styles);
        assertEquals(1, textHelpAppendable.getLeftPad());
        assertEquals(3, textHelpAppendable.getIndent());
        assertEquals(74, textHelpAppendable.getMaxWidth());
    }

    @Test(timeout = 4000)
    public void testResizeTextStyleBuilder() throws Throwable {
        TextHelpAppendable textHelpAppendable = TextHelpAppendable.systemOut();
        TextStyle.Builder builder = TextStyle.builder();
        builder.setScalable(false);
        TextStyle.Builder resizedBuilder = textHelpAppendable.resize(builder, 1);
        assertEquals(1, textHelpAppendable.getLeftPad());
        assertEquals(0, resizedBuilder.getIndent());
        assertEquals(74, textHelpAppendable.getMaxWidth());
        assertEquals(Integer.MAX_VALUE, resizedBuilder.getMaxWidth());
        assertEquals(3, textHelpAppendable.getIndent());
    }

    @Test(timeout = 4000)
    public void testGetTextStyleBuilder() throws Throwable {
        TextHelpAppendable textHelpAppendable = TextHelpAppendable.systemOut();
        TextStyle.Builder builder = textHelpAppendable.getTextStyleBuilder();
        assertEquals(1, builder.getLeftPad());
        builder.setLeftPad(-276);
        textHelpAppendable.resize(builder, -3392.733044942331);
        assertEquals(0, textHelpAppendable.getIndent());
    }

    @Test(timeout = 4000)
    public void testMakeColumnQueue() throws Throwable {
        TextHelpAppendable textHelpAppendable = TextHelpAppendable.systemOut();
        TextStyle textStyle = TextStyle.DEFAULT;
        Queue<String> queue = textHelpAppendable.makeColumnQueue("org.apache.commons.cli.help.TextStyle$1", textStyle);
        assertTrue(queue.contains("org.apache.commons.cli.help.TextStyle$1"));
        assertEquals(3, textHelpAppendable.getIndent());
        assertEquals(74, textHelpAppendable.getMaxWidth());
    }

    @Test(timeout = 4000)
    public void testIndexOfWrapWithStringWriter() throws Throwable {
        StringWriter stringWriter = new StringWriter();
        StringBuffer buffer = stringWriter.getBuffer();
        int index = TextHelpAppendable.indexOfWrap(buffer, 74, 1047);
        assertEquals(0, index);
    }

    @Test(timeout = 4000)
    public void testIndexOfWrapWithCharBuffer() throws Throwable {
        char[] charArray = new char[2];
        CharBuffer charBuffer = CharBuffer.wrap(charArray, 1, 0);
        int index = TextHelpAppendable.indexOfWrap(charBuffer, Integer.MAX_VALUE, 7);
        assertEquals(-2147483643, index);
    }

    // Additional tests omitted for brevity...

    @Test(timeout = 4000)
    public void testAppendParagraphWithStringWriter() throws Throwable {
        TextHelpAppendable textHelpAppendable = TextHelpAppendable.systemOut();
        StringWriter stringWriter = new StringWriter(1);
        StringBuffer buffer = stringWriter.getBuffer();
        textHelpAppendable.appendParagraph(buffer);
        assertEquals(74, textHelpAppendable.getMaxWidth());
        assertEquals(1, textHelpAppendable.getLeftPad());
        assertEquals(3, textHelpAppendable.getIndent());
    }

    @Test(timeout = 4000)
    public void testAdjustTableFormat() throws Throwable {
        TextHelpAppendable textHelpAppendable = TextHelpAppendable.systemOut();
        ArrayList<TextStyle> styles = new ArrayList<>();
        styles.add(TextStyle.DEFAULT);
        ArrayList<String> headers = new ArrayList<>();
        headers.add("|iq2*P~/");
        TreeSet<List<String>> data = new TreeSet<>();
        TableDefinition tableDefinition = TableDefinition.from("dQB_1^Wffi }@$J", styles, headers, data);
        TableDefinition adjustedTable = textHelpAppendable.adjustTableFormat(tableDefinition);
        textHelpAppendable.adjustTableFormat(adjustedTable);
        assertEquals(3, textHelpAppendable.getIndent());
        assertEquals(74, textHelpAppendable.getMaxWidth());
        assertEquals(1, textHelpAppendable.getLeftPad());
    }

    @Test(timeout = 4000)
    public void testIndexOfWrapWithExactWidth() throws Throwable {
        int index = TextHelpAppendable.indexOfWrap("Width must be greater than 0", 3, 3);
        assertEquals(5, index);
    }

    @Test(timeout = 4000)
    public void testResizeTextStyleBuilderWithMaxWidth() throws Throwable {
        TextHelpAppendable textHelpAppendable = TextHelpAppendable.systemOut();
        TextStyle.Builder builder = textHelpAppendable.getTextStyleBuilder();
        builder.setMaxWidth(1);
        textHelpAppendable.resize(builder, 1);
        assertEquals(1, builder.getMaxWidth());
        assertEquals(0, textHelpAppendable.getIndent());
    }

    @Test(timeout = 4000)
    public void testAppendTitleWithCharBuffer() throws Throwable {
        CharBuffer charBuffer = CharBuffer.allocate(7);
        TextHelpAppendable textHelpAppendable = TextHelpAppendable.systemOut();
        textHelpAppendable.appendTitle(charBuffer);
        assertEquals(3, textHelpAppendable.getIndent());
    }

    @Test(timeout = 4000)
    public void testAppendListWithEmptyLinkedHashSet() throws Throwable {
        PipedWriter pipedWriter = new PipedWriter();
        TextHelpAppendable textHelpAppendable = new TextHelpAppendable(pipedWriter);
        LinkedHashSet<CharSequence> linkedHashSet = new LinkedHashSet<>();
        textHelpAppendable.appendList(true, linkedHashSet);
        assertEquals(3, textHelpAppendable.getIndent());
        assertEquals(74, textHelpAppendable.getMaxWidth());
        assertEquals(1, textHelpAppendable.getLeftPad());
    }

    @Test(timeout = 4000)
    public void testAppendHeaderWithNullCharSequence() throws Throwable {
        TextHelpAppendable textHelpAppendable = TextHelpAppendable.systemOut();
        textHelpAppendable.appendHeader(7, (CharSequence) null);
        assertEquals(3, textHelpAppendable.getIndent());
        assertEquals(74, textHelpAppendable.getMaxWidth());
        assertEquals(1, textHelpAppendable.getLeftPad());
    }

    @Test(timeout = 4000)
    public void testGetMaxWidth() throws Throwable {
        TextHelpAppendable textHelpAppendable = TextHelpAppendable.systemOut();
        int maxWidth = textHelpAppendable.getMaxWidth();
        assertEquals(74, maxWidth);
        assertEquals(3, textHelpAppendable.getIndent());
        assertEquals(1, textHelpAppendable.getLeftPad());
    }

    @Test(timeout = 4000)
    public void testGetLeftPad() throws Throwable {
        TextHelpAppendable textHelpAppendable = TextHelpAppendable.systemOut();
        int leftPad = textHelpAppendable.getLeftPad();
        assertEquals(1, leftPad);
        assertEquals(74, textHelpAppendable.getMaxWidth());
        assertEquals(3, textHelpAppendable.getIndent());
    }

    @Test(timeout = 4000)
    public void testSetIndent() throws Throwable {
        TextHelpAppendable textHelpAppendable = TextHelpAppendable.systemOut();
        textHelpAppendable.setIndent(-293);
        int indent = textHelpAppendable.getIndent();
        assertEquals(-293, indent);
    }

    @Test(timeout = 4000)
    public void testGetIndent() throws Throwable {
        TextHelpAppendable textHelpAppendable = TextHelpAppendable.systemOut();
        int indent = textHelpAppendable.getIndent();
        assertEquals(74, textHelpAppendable.getMaxWidth());
        assertEquals(3, indent);
        assertEquals(1, textHelpAppendable.getLeftPad());
    }
}