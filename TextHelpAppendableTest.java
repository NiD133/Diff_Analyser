package org.apache.commons.cli.help;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests for {@link TextHelpAppendable}.
 */
public final class TextHelpAppendableTest {

    private static final String SAMPLE_TEXT = "The quick brown fox jumps over the lazy dog";
    private static final String HELLO_WORLD = "Hello World";
    private static final String EMPTY_STRING = "";
    private static final String NULL_STRING = null;
    
    private StringBuilder sb;
    private TextHelpAppendable underTest;

    @BeforeEach
    public void setUp() {
        sb = new StringBuilder();
        underTest = new TextHelpAppendable(sb);
    }

    @Test
    void testMakeColumnQueue() {
        // Test left alignment
        Queue<String> expectedLeftAlign = createQueue("The quick ", "brown fox ", "jumps over", "the lazy  ", "dog       ");
        assertColumnQueue(SAMPLE_TEXT, TextStyle.Alignment.LEFT, expectedLeftAlign, "left aligned failed");

        // Test right alignment
        Queue<String> expectedRightAlign = createQueue(" The quick", " brown fox", "jumps over", "  the lazy", "       dog");
        assertColumnQueue(SAMPLE_TEXT, TextStyle.Alignment.RIGHT, expectedRightAlign, "right aligned failed");

        // Test center alignment
        Queue<String> expectedCenterAlign = createQueue("The quick ", "brown fox ", "jumps over", " the lazy ", "   dog    ");
        assertColumnQueue(SAMPLE_TEXT, TextStyle.Alignment.CENTER, expectedCenterAlign, "center aligned failed");

        // Test right alignment with padding
        Queue<String> expectedRightAlignWithPadding = createQueue("      The quick", "          brown", "            fox", "          jumps", "       over the", "       lazy dog");
        assertColumnQueueWithPadding(SAMPLE_TEXT, TextStyle.Alignment.RIGHT, 5, 2, expectedRightAlignWithPadding, "right aligned with padding failed");
    }

    @Test
    void testAdjustTableFormat() {
        // Test table adjustment with width smaller than header
        final TableDefinition tableDefinition = TableDefinition.from("Testing",
                Collections.singletonList(TextStyle.builder().setMaxWidth(3).get()),
                Collections.singletonList("header"),
                Collections.singletonList(Collections.singletonList("data"))
        );
        final TableDefinition actual = underTest.adjustTableFormat(tableDefinition);
        assertEquals("header".length(), actual.columnTextStyles().get(0).getMaxWidth());
        assertEquals("header".length(), actual.columnTextStyles().get(0).getMinWidth());
    }

    @Test
    void testAppend() throws IOException {
        // Test appending a character
        final char c = (char) 0x1F44D;
        underTest.append(c);
        assertEquals(1, sb.length());
        assertEquals(String.valueOf(c), sb.toString());

        // Test appending a string
        sb.setLength(0);
        underTest.append("Hello");
        assertEquals("Hello", sb.toString());
    }

    @Test
    void testAppendHeader() throws IOException {
        // Test appending headers with different levels
        assertAppendHeader(1, " Hello World", " ===========");
        assertAppendHeader(2, " Hello World", " %%%%%%%%%%%");
        assertAppendHeader(3, " Hello World", " +++++++++++");
        assertAppendHeader(4, " Hello World", " ___________");
        assertAppendHeader(5, " Hello World", " ___________");

        // Test invalid header level
        sb.setLength(0);
        assertThrows(IllegalArgumentException.class, () -> underTest.appendHeader(0, HELLO_WORLD));

        // Test empty and null headers
        assertEmptyOrNullHeader(5, EMPTY_STRING);
        assertEmptyOrNullHeader(5, NULL_STRING);
    }

    @Test
    void testAppendList() throws IOException {
        // Test ordered list
        List<String> orderedList = createList("  1. one", "  2. two", "  3. three", EMPTY_STRING);
        assertAppendList(true, Arrays.asList("one", "two", "three"), orderedList, "ordered list failed");

        // Test unordered list
        List<String> unorderedList = createList("  * one", "  * two", "  * three", EMPTY_STRING);
        assertAppendList(false, Arrays.asList("one", "two", "three"), unorderedList, "unordered list failed");

        // Test empty and null lists
        assertAppendList(false, Collections.emptyList(), Collections.emptyList(), "empty list failed");
        assertAppendList(false, null, Collections.emptyList(), "null list failed");
    }

    @Test
    void testAppendParagraph() throws IOException {
        // Test appending a paragraph
        assertAppendParagraph("Hello World", " Hello World", EMPTY_STRING);

        // Test empty and null paragraphs
        assertEmptyOrNullParagraph(EMPTY_STRING);
        assertEmptyOrNullParagraph(NULL_STRING);
    }

    @Test
    void testAppendParagraphFormat() throws IOException {
        // Test formatted paragraph
        assertAppendParagraphFormat("Hello %s World %,d", "Joe", 309, " Hello Joe World 309", EMPTY_STRING);

        // Test empty formatted paragraph
        assertEmptyOrNullParagraphFormat(EMPTY_STRING);
    }

    @Test
    void testAppendTable() throws IOException {
        // Test appending a table
        final TextStyle.Builder styleBuilder = TextStyle.builder();
        final List<TextStyle> styles = new ArrayList<>();
        styles.add(styleBuilder.setIndent(2).get());
        styles.add(styleBuilder.setIndent(0).setLeftPad(5).setAlignment(TextStyle.Alignment.RIGHT).get());
        final String[] headers = { "fox", "time" };
        final List<List<String>> rows = Arrays.asList(
                Arrays.asList("The quick brown fox jumps over the lazy dog",
                        "Now is the time for all good people to come to the aid of their country"),
                Arrays.asList("Léimeann an sionnach donn gasta thar an madra leisciúil",
                        "Anois an t-am do na daoine maithe go léir teacht i gcabhair ar a dtír")
        );

        List<String> expected = createList(
                " Common Phrases",
                EMPTY_STRING,
                "               fox                                       time                   ",
                " The quick brown fox jumps over           Now is the time for all good people to",
                "   the lazy dog                                 come to the aid of their country",
                " Léimeann an sionnach donn gasta       Anois an t-am do na daoine maithe go léir",
                "   thar an madra leisciúil                           teacht i gcabhair ar a dtír",
                EMPTY_STRING
        );

        TableDefinition table = TableDefinition.from("Common Phrases", styles, Arrays.asList(headers), rows);
        sb.setLength(0);
        underTest.setMaxWidth(80);
        underTest.appendTable(table);
        List<String> actual = IOUtils.readLines(new StringReader(sb.toString()));
        assertEquals(expected, actual, "full table failed");

        table = TableDefinition.from(null, styles, Arrays.asList(headers), rows);
        expected.remove(1);
        expected.remove(0);
        sb.setLength(0);
        underTest.appendTable(table);
        actual = IOUtils.readLines(new StringReader(sb.toString()));
        assertEquals(expected, actual);

        table = TableDefinition.from(null, styles, Arrays.asList(headers), Collections.emptyList());
        expected = createList(" fox     time", EMPTY_STRING);
        sb.setLength(0);
        underTest.appendTable(table);
        actual = IOUtils.readLines(new StringReader(sb.toString()));
        assertEquals(expected, actual, "no rows test failed");
    }

    @Test
    void testAppendTitle() throws IOException {
        // Test appending a title
        assertAppendTitle("Hello World", " Hello World", " ###########", EMPTY_STRING);

        // Test empty and null titles
        assertEmptyOrNullTitle(EMPTY_STRING);
        assertEmptyOrNullTitle(NULL_STRING);
    }

    @Test
    void testGetStyleBuilder() {
        // Test default style builder values
        final TextStyle.Builder builder = underTest.getTextStyleBuilder();
        assertEquals(TextHelpAppendable.DEFAULT_INDENT, builder.getIndent(), "Default indent value was changed, some tests may fail");
        assertEquals(TextHelpAppendable.DEFAULT_LEFT_PAD, builder.getLeftPad(), "Default left pad value was changed, some tests may fail");
        assertEquals(TextHelpAppendable.DEFAULT_WIDTH, builder.getMaxWidth(), "Default width value was changed, some tests may fail");
    }

    @Test
    void testIndexOfWrapPos() {
        final String testString = "The quick brown fox jumps over\tthe lazy dog";

        assertEquals(9, TextHelpAppendable.indexOfWrap(testString, 10, 0), "did not find end of word");
        assertEquals(9, TextHelpAppendable.indexOfWrap(testString, 14, 0), "did not backup to end of word");
        assertEquals(15, TextHelpAppendable.indexOfWrap(testString, 15, 0), "did not find word at 15");
        assertEquals(15, TextHelpAppendable.indexOfWrap(testString, 16, 0));
        assertEquals(30, TextHelpAppendable.indexOfWrap(testString, 15, 20), "did not find break character");
        assertEquals(30, TextHelpAppendable.indexOfWrap(testString, 150, 0), "did not handle text shorter than width");

        assertThrows(IllegalArgumentException.class, () -> TextHelpAppendable.indexOfWrap("", 0, 0));
        assertEquals(3, TextHelpAppendable.indexOfWrap("Hello", 4, 0));
    }

    @ParameterizedTest
    @MethodSource("org.apache.commons.cli.help.UtilTest#charArgs")
    void testIndexOfWrapPosWithWhitespace(final Character c, final boolean isWhitespace) {
        final String text = String.format("Hello%cWorld", c);
        assertEquals(isWhitespace ? 5 : 6, TextHelpAppendable.indexOfWrap(text, 7, 0));
    }

    @Test
    void testPrintWrapped() throws IOException {
        // Test printing wrapped text with different alignments
        assertPrintWrapped(SAMPLE_TEXT, TextStyle.Alignment.LEFT, createList("The quick", "brown fox", "jumps over", "the lazy", "dog"), "left aligned failed");
        assertPrintWrapped(SAMPLE_TEXT, TextStyle.Alignment.RIGHT, createList(" The quick", " brown fox", "jumps over", "  the lazy", "       dog"), "right aligned failed");
        assertPrintWrapped(SAMPLE_TEXT, TextStyle.Alignment.CENTER, createList("The quick", "brown fox", "jumps over", " the lazy", "   dog"), "center aligned failed");

        // Test default format
        sb.setLength(0);
        List<String> expected = createList(" The quick brown fox jumps over the lazy dog");
        assertEquals(1, underTest.getLeftPad(), "unexpected page left pad");
        assertEquals(3, underTest.getIndent(), "unexpected page indent");
        assertEquals(74, underTest.getMaxWidth(), "unexpected page width");
        underTest.printWrapped(SAMPLE_TEXT);
        List<String> actual = IOUtils.readLines(new StringReader(sb.toString()));
        assertEquals(expected, actual, "default format aligned failed");

        // Test default format with multiline text
        sb.setLength(0);
        String multilineText = SAMPLE_TEXT + ".\nNow is the time for all good people to come to the aid of their country.";
        expected = createList(
                " The quick brown fox jumps over the lazy dog.",
                "    Now is the time for all good people to come to the aid of their",
                "    country."
        );
        underTest.printWrapped(multilineText);
        actual = IOUtils.readLines(new StringReader(sb.toString()));
        assertEquals(expected, actual, "default format aligned failed");
    }

    @Test
    void testResize() {
        // Test resizing with different fractions
        TextStyle.Builder tsBuilder = TextStyle.builder().setIndent(2).setMaxWidth(3);
        underTest.resize(tsBuilder, 0.5);
        assertEquals(0, tsBuilder.getIndent());

        tsBuilder = TextStyle.builder().setIndent(4).setMaxWidth(6);
        underTest.resize(tsBuilder, 0.5);
        assertEquals(1, tsBuilder.getIndent());
    }

    @Test
    void testResizeTableFormat() {
        // Test resizing table format
        underTest.setMaxWidth(150);
        final TableDefinition tableDefinition = TableDefinition.from("Caption",
                Collections.singletonList(TextStyle.builder().setMinWidth(20).setMaxWidth(100).get()), Collections.singletonList("header"),
                Collections.singletonList(Collections.singletonList("one")));
        final TableDefinition result = underTest.adjustTableFormat(tableDefinition);
        assertEquals(20, result.columnTextStyles().get(0).getMinWidth(), "Minimum width should not be reset");
        assertEquals(100, result.columnTextStyles().get(0).getMaxWidth(), "Maximum width should not be reset");
    }

    @Test
    void testSetIndent() {
        // Test setting indent
        assertEquals(TextHelpAppendable.DEFAULT_INDENT, underTest.getIndent(), "Default indent value was changed, some tests may fail");
        underTest.setIndent(TextHelpAppendable.DEFAULT_INDENT + 2);
        assertEquals(underTest.getIndent(), TextHelpAppendable.DEFAULT_INDENT + 2);
    }

    @Test
    void testWriteColumnQueues() throws IOException {
        // Test writing column queues
        final List<Queue<String>> queues = new ArrayList<>();

        Queue<String> queue1 = createQueue("The quick ", "brown fox ", "jumps over", "the lazy  ", "dog       ");
        queues.add(queue1);

        Queue<String> queue2 = createQueue("     Now is the", "     time for  ", "     all good  ", "     people to ", "     come to   ", "     the aid of", "     their     ", "     country   ");
        queues.add(queue2);

        final TextStyle.Builder styleBuilder = TextStyle.builder().setMaxWidth(10).setIndent(0).setLeftPad(0);

        final List<TextStyle> columns = new ArrayList<>();
        columns.add(styleBuilder.get());
        columns.add(styleBuilder.setLeftPad(5).get());

        final List<String> expected = createList(
                " The quick      Now is the",
                " brown fox      time for  ",
                " jumps over     all good  ",
                " the lazy       people to ",
                " dog            come to   ",
                "                the aid of",
                "                their     ",
                "                country   "
        );

        sb.setLength(0);
        underTest.writeColumnQueues(queues, columns);
        final List<String> actual = IOUtils.readLines(new StringReader(sb.toString()));
        assertEquals(expected, actual);
    }

    // Helper methods for creating and asserting queues and lists
    private Queue<String> createQueue(String... lines) {
        return new LinkedList<>(Arrays.asList(lines));
    }

    private List<String> createList(String... lines) {
        return new ArrayList<>(Arrays.asList(lines));
    }

    private void assertColumnQueue(String text, TextStyle.Alignment alignment, Queue<String> expected, String message) {
        TextStyle.Builder styleBuilder = TextStyle.builder().setMaxWidth(10).setIndent(0).setLeftPad(0).setAlignment(alignment);
        Queue<String> result = underTest.makeColumnQueue(text, styleBuilder.get());
        assertEquals(expected, result, message);
    }

    private void assertColumnQueueWithPadding(String text, TextStyle.Alignment alignment, int leftPad, int indent, Queue<String> expected, String message) {
        TextStyle.Builder styleBuilder = TextStyle.builder().setMaxWidth(10).setIndent(indent).setLeftPad(leftPad).setAlignment(alignment);
        Queue<String> result = underTest.makeColumnQueue(text, styleBuilder.get());
        assertEquals(expected, result, message);
    }

    private void assertAppendHeader(int level, String expectedLine1, String expectedLine2) throws IOException {
        sb.setLength(0);
        underTest.appendHeader(level, HELLO_WORLD);
        List<String> actual = IOUtils.readLines(new StringReader(sb.toString()));
        assertEquals(Arrays.asList(expectedLine1, expectedLine2, EMPTY_STRING), actual, "header " + level + " failed");
    }

    private void assertEmptyOrNullHeader(int level, String text) throws IOException {
        sb.setLength(0);
        underTest.appendHeader(level, text);
        assertEquals(0, sb.length(), "empty or null header test failed");
    }

    private void assertAppendList(boolean ordered, List<String> entries, List<String> expected, String message) throws IOException {
        sb.setLength(0);
        underTest.appendList(ordered, entries);
        List<String> actual = IOUtils.readLines(new StringReader(sb.toString()));
        assertEquals(expected, actual, message);
    }

    private void assertAppendParagraph(String text, String expectedLine1, String expectedLine2) throws IOException {
        sb.setLength(0);
        underTest.appendParagraph(text);
        List<String> actual = IOUtils.readLines(new StringReader(sb.toString()));
        assertEquals(Arrays.asList(expectedLine1, expectedLine2), actual);
    }

    private void assertEmptyOrNullParagraph(String text) throws IOException {
        sb.setLength(0);
        underTest.appendParagraph(text);
        assertEquals(0, sb.length(), "empty or null paragraph test failed");
    }

    private void assertAppendParagraphFormat(String format, Object arg1, Object arg2, String expectedLine1, String expectedLine2) throws IOException {
        sb.setLength(0);
        underTest.appendParagraphFormat(format, arg1, arg2);
        List<String> actual = IOUtils.readLines(new StringReader(sb.toString()));
        assertEquals(Arrays.asList(expectedLine1, expectedLine2), actual);
    }

    private void assertEmptyOrNullParagraphFormat(String format) throws IOException {
        sb.setLength(0);
        underTest.appendParagraphFormat(format);
        assertEquals(0, sb.length(), "empty or null paragraph format test failed");
    }

    private void assertAppendTitle(String title, String expectedLine1, String expectedLine2, String expectedLine3) throws IOException {
        sb.setLength(0);
        underTest.appendTitle(title);
        List<String> actual = IOUtils.readLines(new StringReader(sb.toString()));
        assertEquals(Arrays.asList(expectedLine1, expectedLine2, expectedLine3), actual);
    }

    private void assertEmptyOrNullTitle(String title) throws IOException {
        sb.setLength(0);
        underTest.appendTitle(title);
        assertEquals(0, sb.length(), "empty or null title test failed");
    }

    private void assertPrintWrapped(String text, TextStyle.Alignment alignment, List<String> expected, String message) throws IOException {
        sb.setLength(0);
        TextStyle.Builder styleBuilder = TextStyle.builder().setMaxWidth(10).setIndent(0).setLeftPad(0).setAlignment(alignment);
        underTest.printWrapped(text, styleBuilder.get());
        List<String> actual = IOUtils.readLines(new StringReader(sb.toString()));
        assertEquals(expected, actual, message);
    }
}