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
 * Tests {@link TextHelpAppendable}.
 * 
 * The tests favor readability by:
 * - Using expressive test names.
 * - Extracting helpers for repeated patterns (reset, read output lines, expected builders).
 * - Using clear Arrange-Act-Assert structure.
 */
public final class TextHelpAppendableTest {

    private static final String HELLO_WORLD = "Hello World";

    private StringBuilder sb;
    private TextHelpAppendable underTest;

    @BeforeEach
    void setUp() {
        sb = new StringBuilder();
        underTest = new TextHelpAppendable(sb);
    }

    // Helpers

    private void resetOutput() {
        sb.setLength(0);
    }

    private List<String> readOutputLines() throws IOException {
        return IOUtils.readLines(new StringReader(sb.toString()));
    }

    private static List<String> expectedParagraph(String text) {
        final List<String> expected = new ArrayList<>(2);
        expected.add(" " + text);
        expected.add("");
        return expected;
    }

    private static List<String> expectedHeader(String text, char underlineChar) {
        final char[] underline = new char[text.length()];
        Arrays.fill(underline, underlineChar);
        final List<String> expected = new ArrayList<>(3);
        expected.add(" " + text);
        expected.add(" " + new String(underline));
        expected.add("");
        return expected;
    }

    private static Queue<String> queueOf(String... lines) {
        final Queue<String> q = new LinkedList<>();
        q.addAll(Arrays.asList(lines));
        return q;
    }

    // Tests

    @Test
    void testMakeColumnQueue() {
        final String text = "The quick brown fox jumps over the lazy dog";
        final TextStyle.Builder style = TextStyle.builder().setMaxWidth(10).setIndent(0).setLeftPad(0);

        // Left aligned
        Queue<String> expected = queueOf(
                "The quick ",
                "brown fox ",
                "jumps over",
                "the lazy  ",
                "dog       ");
        assertEquals(expected, underTest.makeColumnQueue(text, style.get()), "left aligned failed");

        // Right aligned
        expected = queueOf(
                " The quick",
                " brown fox",
                "jumps over",
                "  the lazy",
                "       dog");
        style.setAlignment(TextStyle.Alignment.RIGHT);
        assertEquals(expected, underTest.makeColumnQueue(text, style.get()), "right aligned failed");

        // Center aligned
        expected = queueOf(
                "The quick ",
                "brown fox ",
                "jumps over",
                " the lazy ",
                "   dog    ");
        style.setAlignment(TextStyle.Alignment.CENTER);
        assertEquals(expected, underTest.makeColumnQueue(text, style.get()), "center aligned failed");

        // Right aligned with left pad and indent
        expected = queueOf(
                "      The quick",
                "          brown",
                "            fox",
                "          jumps",
                "       over the",
                "       lazy dog");
        style.setAlignment(TextStyle.Alignment.RIGHT).setLeftPad(5).setIndent(2);
        assertEquals(expected, underTest.makeColumnQueue(text, style.get()), "right aligned with pad/indent failed");
    }

    @Test
    void testAdjustTableFormatResizesToHeaderLength() {
        // Given: header longer than requested width
        final TableDefinition tableDefinition = TableDefinition.from(
                "Testing",
                Collections.singletonList(TextStyle.builder().setMaxWidth(3).get()),
                Collections.singletonList("header"),
                Collections.singletonList(Collections.singletonList("data"))); // "data" shorter than "header"

        // When
        final TableDefinition actual = underTest.adjustTableFormat(tableDefinition);

        // Then: min/max are at least header length
        assertEquals("header".length(), actual.columnTextStyles().get(0).getMaxWidth());
        assertEquals("header".length(), actual.columnTextStyles().get(0).getMinWidth());
    }

    @Test
    void testAppendCharAndString() throws IOException {
        // Append a single char
        final char c = (char) 0x1F44D; // 👍
        underTest.append(c);
        assertEquals(1, sb.length());
        assertEquals(String.valueOf(c), sb.toString());

        // Append a string
        resetOutput();
        underTest.append("Hello");
        assertEquals("Hello", sb.toString());
    }

    @Test
    void testAppendHeaderLevels() throws IOException {
        // Level 1 uses '='
        resetOutput();
        underTest.appendHeader(1, HELLO_WORLD);
        assertEquals(expectedHeader(HELLO_WORLD, '='), readOutputLines(), "header 1 failed");

        // Level 2 uses '%'
        resetOutput();
        underTest.appendHeader(2, HELLO_WORLD);
        assertEquals(expectedHeader(HELLO_WORLD, '%'), readOutputLines(), "header 2 failed");

        // Level 3 uses '+'
        resetOutput();
        underTest.appendHeader(3, HELLO_WORLD);
        assertEquals(expectedHeader(HELLO_WORLD, '+'), readOutputLines(), "header 3 failed");

        // Level 4 uses '_'
        resetOutput();
        underTest.appendHeader(4, HELLO_WORLD);
        assertEquals(expectedHeader(HELLO_WORLD, '_'), readOutputLines(), "header 4 failed");

        // Level 5 also uses '_'
        resetOutput();
        underTest.appendHeader(5, HELLO_WORLD);
        assertEquals(expectedHeader(HELLO_WORLD, '_'), readOutputLines(), "header 5 failed");

        // Invalid level
        resetOutput();
        assertThrows(IllegalArgumentException.class, () -> underTest.appendHeader(0, HELLO_WORLD));

        // Empty text is a no-op
        resetOutput();
        underTest.appendHeader(5, "");
        assertEquals(0, sb.length(), "empty string should not produce output");

        // Null is a no-op
        resetOutput();
        underTest.appendHeader(5, null);
        assertEquals(0, sb.length(), "null should not produce output");
    }

    @Test
    void testAppendListOrderedAndUnordered() throws IOException {
        final String[] items = { "one", "two", "three" };

        // Ordered
        resetOutput();
        underTest.appendList(true, Arrays.asList(items));
        final List<String> expectedOrdered = new ArrayList<>();
        for (int i = 0; i < items.length; i++) {
            expectedOrdered.add(String.format("  %d. %s", i + 1, items[i]));
        }
        expectedOrdered.add("");
        assertEquals(expectedOrdered, readOutputLines(), "ordered list failed");

        // Unordered
        resetOutput();
        underTest.appendList(false, Arrays.asList(items));
        final List<String> expectedUnordered = new ArrayList<>();
        for (final String s : items) {
            expectedUnordered.add("  * " + s);
        }
        expectedUnordered.add("");
        assertEquals(expectedUnordered, readOutputLines(), "unordered list failed");

        // Empty list is a no-op (no trailing empty line)
        resetOutput();
        underTest.appendList(false, Collections.emptyList());
        assertEquals(Collections.emptyList(), readOutputLines(), "empty list failed");

        // Null list is a no-op
        resetOutput();
        underTest.appendList(false, null);
        assertEquals(Collections.emptyList(), readOutputLines(), "null list failed");
    }

    @Test
    void testAppendParagraph() throws IOException {
        // Non-empty
        resetOutput();
        underTest.appendParagraph(HELLO_WORLD);
        assertEquals(expectedParagraph(HELLO_WORLD), readOutputLines());

        // Empty is a no-op
        resetOutput();
        underTest.appendParagraph("");
        assertEquals(0, sb.length(), "empty string should not produce output");

        // Null is a no-op
        resetOutput();
        underTest.appendParagraph(null);
        assertEquals(0, sb.length(), "null should not produce output");
    }

    @Test
    void testAppendParagraphFormat() throws IOException {
        // Non-empty format
        resetOutput();
        underTest.appendParagraphFormat("Hello %s World %,d", "Joe", 309);
        assertEquals(expectedParagraph("Hello Joe World 309"), readOutputLines());

        // Empty format is a no-op
        resetOutput();
        underTest.appendParagraphFormat("");
        assertEquals(0, sb.length(), "empty format should not produce output");
    }

    @Test
    void testAppendTable() throws IOException {
        // Column styles: first column indented; second right-aligned with left pad
        final TextStyle.Builder styleBuilder = TextStyle.builder();
        final List<TextStyle> styles = new ArrayList<>();
        styles.add(styleBuilder.setIndent(2).get());
        styles.add(styleBuilder.setIndent(0).setLeftPad(5).setAlignment(TextStyle.Alignment.RIGHT).get());
        final String[] headers = { "fox", "time" };

        // Rows contain ASCII and non-ASCII content to test wrapping behavior
        final List<List<String>> rows = Arrays.asList(
                Arrays.asList(
                        "The quick brown fox jumps over the lazy dog",
                        "Now is the time for all good people to come to the aid of their country"),
                Arrays.asList(
                        "Léimeann an sionnach donn gasta thar an madra leisciúil",
                        "Anois an t-am do na daoine maithe go léir teacht i gcabhair ar a dtír")
        );

        // With caption and width 80
        final List<String> expectedWithCaption = Arrays.asList(
                " Common Phrases",
                "",
                "               fox                                       time                   ",
                " The quick brown fox jumps over           Now is the time for all good people to",
                "   the lazy dog                                 come to the aid of their country",
                " Léimeann an sionnach donn gasta       Anois an t-am do na daoine maithe go léir",
                "   thar an madra leisciúil                           teacht i gcabhair ar a dtír",
                ""
        );

        TableDefinition table = TableDefinition.from("Common Phrases", styles, Arrays.asList(headers), rows);
        resetOutput();
        underTest.setMaxWidth(80);
        underTest.appendTable(table);
        assertEquals(expectedWithCaption, readOutputLines(), "full table failed");

        // Without caption
        final List<String> expectedWithoutCaption = expectedWithCaption.subList(2, expectedWithCaption.size());
        table = TableDefinition.from(null, styles, Arrays.asList(headers), rows);
        resetOutput();
        underTest.appendTable(table);
        assertEquals(expectedWithoutCaption, readOutputLines());

        // No rows: print only header and an empty line
        final List<String> expectedNoRows = Arrays.asList(
                " fox     time",
                ""
        );
        table = TableDefinition.from(null, styles, Arrays.asList(headers), Collections.emptyList());
        resetOutput();
        underTest.appendTable(table);
        assertEquals(expectedNoRows, readOutputLines(), "no rows test failed");
    }

    @Test
    void testAppendTitle() throws IOException {
        resetOutput();
        underTest.appendTitle(HELLO_WORLD);
        assertEquals(expectedHeader(HELLO_WORLD, '#'), readOutputLines());

        resetOutput();
        underTest.appendTitle("");
        assertEquals(0, sb.length(), "empty string should not produce output");

        resetOutput();
        underTest.appendTitle(null);
        assertEquals(0, sb.length(), "null should not produce output");
    }

    @Test
    void testGetTextStyleBuilderDefaults() {
        final TextStyle.Builder builder = underTest.getTextStyleBuilder();
        assertEquals(TextHelpAppendable.DEFAULT_INDENT, builder.getIndent(), "Default indent value was changed, some tests may fail");
        assertEquals(TextHelpAppendable.DEFAULT_LEFT_PAD, builder.getLeftPad(), "Default left pad value was changed, some tests may fail");
        assertEquals(TextHelpAppendable.DEFAULT_WIDTH, builder.getMaxWidth(), "Default width value was changed, some tests may fail");
    }

    @Test
    void testIndexOfWrap() {
        final String text = "The quick brown fox jumps over\tthe lazy dog";

        assertEquals(9, TextHelpAppendable.indexOfWrap(text, 10, 0), "did not find end of word");
        assertEquals(9, TextHelpAppendable.indexOfWrap(text, 14, 0), "did not backup to end of word");
        assertEquals(15, TextHelpAppendable.indexOfWrap(text, 15, 0), "did not find word at 15");
        assertEquals(15, TextHelpAppendable.indexOfWrap(text, 16, 0));
        assertEquals(30, TextHelpAppendable.indexOfWrap(text, 15, 20), "did not find break character");
        assertEquals(30, TextHelpAppendable.indexOfWrap(text, 150, 0), "did not handle text shorter than width");

        assertThrows(IllegalArgumentException.class, () -> TextHelpAppendable.indexOfWrap("", 0, 0));
        assertEquals(3, TextHelpAppendable.indexOfWrap("Hello", 4, 0));
    }

    @ParameterizedTest
    @MethodSource("org.apache.commons.cli.help.UtilTest#charArgs")
    void testIndexOfWrapWithWhitespace(final Character c, final boolean isWhitespace) {
        final String text = String.format("Hello%cWorld", c);
        assertEquals(isWhitespace ? 5 : 6, TextHelpAppendable.indexOfWrap(text, 7, 0));
    }

    @Test
    void testPrintWrappedWithCustomStyleAndDefaults() throws IOException {
        String text = "The quick brown fox jumps over the lazy dog";
        final TextStyle.Builder style = TextStyle.builder().setMaxWidth(10).setIndent(0).setLeftPad(0);

        // Left aligned
        resetOutput();
        underTest.printWrapped(text, style.get());
        assertEquals(Arrays.asList("The quick", "brown fox", "jumps over", "the lazy", "dog"), readOutputLines(), "left aligned failed");

        // Right aligned
        resetOutput();
        style.setAlignment(TextStyle.Alignment.RIGHT);
        underTest.printWrapped(text, style.get());
        assertEquals(Arrays.asList(" The quick", " brown fox", "jumps over", "  the lazy", "       dog"), readOutputLines(), "right aligned failed");

        // Center aligned
        resetOutput();
        style.setAlignment(TextStyle.Alignment.CENTER);
        underTest.printWrapped(text, style.get());
        assertEquals(Arrays.asList("The quick", "brown fox", "jumps over", " the lazy", "   dog"), readOutputLines(), "center aligned failed");

        // Defaults from underTest (pad=1, indent=3, width=74)
        resetOutput();
        assertEquals(1, underTest.getLeftPad(), "unexpected page left pad");
        assertEquals(3, underTest.getIndent(), "unexpected page indent");
        assertEquals(74, underTest.getMaxWidth(), "unexpected page width");
        underTest.printWrapped(text);
        assertEquals(Arrays.asList(" The quick brown fox jumps over the lazy dog"), readOutputLines(), "default format failed");

        // Defaults with multiple paragraphs
        resetOutput();
        text += ".\nNow is the time for all good people to come to the aid of their country.";
        underTest.printWrapped(text);
        assertEquals(
                Arrays.asList(
                        " The quick brown fox jumps over the lazy dog.",
                        "    Now is the time for all good people to come to the aid of their",
                        "    country."
                ),
                readOutputLines(),
                "default multi-paragraph format failed");
    }

    @Test
    void testResizeIndentBasedOnFraction() {
        TextStyle.Builder ts = TextStyle.builder().setIndent(2).setMaxWidth(3);
        underTest.resize(ts, 0.5);
        assertEquals(0, ts.getIndent(), "indent should be resized down");

        ts = TextStyle.builder().setIndent(4).setMaxWidth(6);
        underTest.resize(ts, 0.5);
        assertEquals(1, ts.getIndent(), "indent should be resized and rounded");
    }

    @Test
    void testAdjustTableFormatDoesNotOverrideExplicitMinMax() {
        underTest.setMaxWidth(150);
        final TableDefinition tableDefinition = TableDefinition.from(
                "Caption",
                Collections.singletonList(TextStyle.builder().setMinWidth(20).setMaxWidth(100).get()),
                Collections.singletonList("header"),
                Collections.singletonList(Collections.singletonList("one")));
        final TableDefinition result = underTest.adjustTableFormat(tableDefinition);
        assertEquals(20, result.columnTextStyles().get(0).getMinWidth(), "Minimum width should not be reset");
        assertEquals(100, result.columnTextStyles().get(0).getMaxWidth(), "Maximum width should not be reset");
    }

    @Test
    void testSetAndGetIndent() {
        assertEquals(TextHelpAppendable.DEFAULT_INDENT, underTest.getIndent(), "Default indent value was changed, some tests may fail");
        underTest.setIndent(TextHelpAppendable.DEFAULT_INDENT + 2);
        assertEquals(TextHelpAppendable.DEFAULT_INDENT + 2, underTest.getIndent());
    }

    @Test
    void testWriteColumnQueues() throws IOException {
        // Two columns of equal width (10), second column has left pad of 5
        final List<Queue<String>> queues = new ArrayList<>();
        queues.add(queueOf(
                "The quick ",
                "brown fox ",
                "jumps over",
                "the lazy  ",
                "dog       "));
        queues.add(queueOf(
                "     Now is the",
                "     time for  ",
                "     all good  ",
                "     people to ",
                "     come to   ",
                "     the aid of",
                "     their     ",
                "     country   "));

        final TextStyle.Builder style = TextStyle.builder().setMaxWidth(10).setIndent(0).setLeftPad(0);
        final List<TextStyle> columns = new ArrayList<>();
        columns.add(style.get());
        columns.add(style.setLeftPad(5).get());

        resetOutput();
        underTest.writeColumnQueues(queues, columns);

        final List<String> expected = Arrays.asList(
                " The quick      Now is the",
                " brown fox      time for  ",
                " jumps over     all good  ",
                " the lazy       people to ",
                " dog            come to   ",
                "                the aid of",
                "                their     ",
                "                country   ");
        assertEquals(expected, readOutputLines());
    }
}