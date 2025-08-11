/*
  Licensed to the Apache Software Foundation (ASF) under one or more
  contributor license agreements.  See the NOTICE file distributed with
  this work for additional information regarding copyright ownership.
  The ASF licenses this file to You under the Apache License, Version 2.0
  (the "License"); you may not use this file except in compliance with
  the License.  You may obtain a copy of the License at

      https://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
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
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link TextHelpAppendable}, focusing on understandability and maintainability.
 */
@DisplayName("TextHelpAppendable Tests")
public final class TextHelpAppendableTest {

    private StringBuilder sb;
    private TextHelpAppendable underTest;

    @BeforeEach
    void setUp() {
        sb = new StringBuilder();
        underTest = new TextHelpAppendable(sb);
    }

    /**
     * Helper to read the content of the StringBuilder into a list of lines.
     * This encapsulates the repeated I/O logic.
     */
    private List<String> getActualLines() throws IOException {
        return IOUtils.readLines(new StringReader(sb.toString()));
    }

    @Nested
    @DisplayName("Simple Text Appending Tests")
    class SimpleTextAppendingTests {

        @Test
        @DisplayName("append(char) should add a single character")
        void append_shouldAddCharacter() throws IOException {
            final char c = '\uD83D\uDC4D'; // 👍 emoji
            underTest.append(c);
            assertEquals(String.valueOf(c), sb.toString());
        }

        @Test
        @DisplayName("append(CharSequence) should add a string")
        void append_shouldAddCharSequence() throws IOException {
            underTest.append("Hello");
            assertEquals("Hello", sb.toString());
        }

        @Test
        @DisplayName("appendTitle() should format title with a hash underline")
        void appendTitle_shouldFormatTitleWithUnderline() throws IOException {
            final List<String> expectedLines = Arrays.asList(" Hello World", " ###########", "");
            underTest.appendTitle("Hello World");
            assertEquals(expectedLines, getActualLines());
        }

        @Test
        @DisplayName("appendTitle() should do nothing for null or empty input")
        void appendTitle_shouldDoNothingForNullOrEmpty() throws IOException {
            underTest.appendTitle("");
            assertEquals(0, sb.length(), "Empty title should not produce output");

            underTest.appendTitle(null);
            assertEquals(0, sb.length(), "Null title should not produce output");
        }

        @ParameterizedTest(name = "Level {0} should use ''{1}'' for underline")
        @MethodSource("org.apache.commons.cli.help.TextHelpAppendableTest#headerLevelsProvider")
        @DisplayName("appendHeader() should format headers correctly for levels 1-5")
        void appendHeader_shouldFormatHeaderCorrectly(final int level, final String underlineChar) throws IOException {
            final String headerText = "Hello World";
            final List<String> expectedLines = Arrays.asList(
                " " + headerText,
                " " + String.join("", Collections.nCopies(headerText.length(), underlineChar)),
                ""
            );

            underTest.appendHeader(level, headerText);
            assertEquals(expectedLines, getActualLines());
        }

        @Test
        @DisplayName("appendHeader() should throw exception for invalid level 0")
        void appendHeader_shouldThrowExceptionForInvalidLevel() {
            assertThrows(IllegalArgumentException.class, () -> underTest.appendHeader(0, "Hello World"));
        }

        @Test
        @DisplayName("appendHeader() should do nothing for null or empty input")
        void appendHeader_shouldDoNothingForNullOrEmpty() throws IOException {
            underTest.appendHeader(1, "");
            assertEquals(0, sb.length(), "Empty header should not produce output");

            underTest.appendHeader(1, null);
            assertEquals(0, sb.length(), "Null header should not produce output");
        }

        @Test
        @DisplayName("appendParagraph() should indent paragraph and add a new line")
        void appendParagraph_shouldIndentParagraph() throws IOException {
            final List<String> expectedLines = Arrays.asList(" Hello World", "");
            underTest.appendParagraph("Hello World");
            assertEquals(expectedLines, getActualLines());
        }

        @Test
        @DisplayName("appendParagraph() should do nothing for null or empty input")
        void appendParagraph_shouldDoNothingForNullOrEmpty() throws IOException {
            underTest.appendParagraph("");
            assertEquals(0, sb.length(), "Empty paragraph should not produce output");

            underTest.appendParagraph(null);
            assertEquals(0, sb.length(), "Null paragraph should not produce output");
        }

        @Test
        @DisplayName("appendParagraphFormat() should format and indent a paragraph")
        void appendParagraphFormat_shouldFormatAndIndentParagraph() throws IOException {
            final List<String> expectedLines = Arrays.asList(" Hello Joe World 309", "");
            underTest.appendParagraphFormat("Hello %s World %,d", "Joe", 309);
            assertEquals(expectedLines, getActualLines());
        }
    }

    @Nested
    @DisplayName("Collection Appending Tests")
    class CollectionAppendingTests {

        private final List<String> listEntries = Arrays.asList("one", "two", "three");

        @Test
        @DisplayName("appendList() should format an ordered list correctly")
        void appendList_shouldFormatOrderedList() throws IOException {
            final List<String> expectedLines = Arrays.asList("  1. one", "  2. two", "  3. three", "");
            underTest.appendList(true, listEntries);
            assertEquals(expectedLines, getActualLines());
        }

        @Test
        @DisplayName("appendList() should format an unordered list correctly")
        void appendList_shouldFormatUnorderedList() throws IOException {
            final List<String> expectedLines = Arrays.asList("  * one", "  * two", "  * three", "");
            underTest.appendList(false, listEntries);
            assertEquals(expectedLines, getActualLines());
        }

        @Test
        @DisplayName("appendList() should do nothing for null or empty lists")
        void appendList_shouldDoNothingForNullOrEmpty() throws IOException {
            underTest.appendList(true, Collections.emptyList());
            assertEquals(0, sb.length(), "Empty list should not produce output");

            underTest.appendList(true, null);
            assertEquals(0, sb.length(), "Null list should not produce output");
        }

        @Test
        @DisplayName("appendTable() should render a full table with a title")
        void appendTable_shouldRenderFullTable() throws IOException {
            final List<TextStyle> styles = Arrays.asList(
                TextStyle.builder().setIndent(2).get(),
                TextStyle.builder().setIndent(0).setLeftPad(5).setAlignment(TextStyle.Alignment.RIGHT).get()
            );
            final List<String> headers = Arrays.asList("fox", "time");
            final List<List<String>> rows = Arrays.asList(
                Arrays.asList("The quick brown fox jumps over the lazy dog", "Now is the time for all good people to come to the aid of their country"),
                Arrays.asList("Léimeann an sionnach donn gasta thar an madra leisciúil", "Anois an t-am do na daoine maithe go léir teacht i gcabhair ar a dtír")
            );
            final TableDefinition table = TableDefinition.from("Common Phrases", styles, headers, rows);

            final List<String> expectedLines = Arrays.asList(
                " Common Phrases",
                "",
                "               fox                                       time                   ",
                " The quick brown fox jumps over           Now is the time for all good people to",
                "   the lazy dog                                 come to the aid of their country",
                " Léimeann an sionnach donn gasta       Anois an t-am do na daoine maithe go léir",
                "   thar an madra leisciúil                           teacht i gcabhair ar a dtír",
                ""
            );

            underTest.setMaxWidth(80);
            underTest.appendTable(table);
            assertEquals(expectedLines, getActualLines());
        }

        @Test
        @DisplayName("appendTable() should render a table without a title")
        void appendTable_shouldRenderTableWithoutTitle() throws IOException {
            final List<TextStyle> styles = Arrays.asList(
                TextStyle.builder().setIndent(2).get(),
                TextStyle.builder().setIndent(0).setLeftPad(5).setAlignment(TextStyle.Alignment.RIGHT).get()
            );
            final List<String> headers = Arrays.asList("fox", "time");
            final List<List<String>> rows = Arrays.asList(
                Arrays.asList("short", "short")
            );
            final TableDefinition table = TableDefinition.from(null, styles, headers, rows);

            final List<String> expectedLines = Arrays.asList(
                " fox      time",
                " short   short",
                ""
            );

            underTest.appendTable(table);
            assertEquals(expectedLines, getActualLines());
        }

        @Test
        @DisplayName("appendTable() should render only headers for a table with no rows")
        void appendTable_shouldRenderTableWithHeadersOnly() throws IOException {
            final TableDefinition table = TableDefinition.from(null,
                Collections.singletonList(TextStyle.builder().get()),
                Collections.singletonList("header"),
                Collections.emptyList());

            final List<String> expectedLines = Arrays.asList(" header", "");

            underTest.appendTable(table);
            assertEquals(expectedLines, getActualLines());
        }
    }

    @Nested
    @DisplayName("Formatting and Wrapping Tests")
    class FormattingAndWrappingTests {
        private final String text = "The quick brown fox jumps over the lazy dog";

        @Test
        @DisplayName("makeColumnQueue() should wrap and left-align text")
        void makeColumnQueue_shouldAlignLeft() {
            final TextStyle style = TextStyle.builder().setMaxWidth(10).setAlignment(TextStyle.Alignment.LEFT).get();
            final Queue<String> expected = new LinkedList<>(Arrays.asList("The quick ", "brown fox ", "jumps over", "the lazy  ", "dog       "));
            final Queue<String> actual = underTest.makeColumnQueue(text, style);
            assertEquals(expected, actual);
        }

        @Test
        @DisplayName("makeColumnQueue() should wrap and right-align text")
        void makeColumnQueue_shouldAlignRight() {
            final TextStyle style = TextStyle.builder().setMaxWidth(10).setAlignment(TextStyle.Alignment.RIGHT).get();
            final Queue<String> expected = new LinkedList<>(Arrays.asList(" The quick", " brown fox", "jumps over", "  the lazy", "       dog"));
            final Queue<String> actual = underTest.makeColumnQueue(text, style);
            assertEquals(expected, actual);
        }

        @Test
        @DisplayName("makeColumnQueue() should wrap and center-align text")
        void makeColumnQueue_shouldAlignCenter() {
            final TextStyle style = TextStyle.builder().setMaxWidth(10).setAlignment(TextStyle.Alignment.CENTER).get();
            final Queue<String> expected = new LinkedList<>(Arrays.asList("The quick ", "brown fox ", "jumps over", " the lazy ", "   dog    "));
            final Queue<String> actual = underTest.makeColumnQueue(text, style);
            assertEquals(expected, actual);
        }

        @Test
        @DisplayName("makeColumnQueue() should apply padding and indentation")
        void makeColumnQueue_shouldApplyPaddingAndIndent() {
            final TextStyle style = TextStyle.builder().setAlignment(TextStyle.Alignment.RIGHT).setLeftPad(5).setIndent(2).get();
            final Queue<String> expected = new LinkedList<>(Arrays.asList("      The quick", "          brown", "            fox", "          jumps", "       over the", "       lazy dog"));
            final Queue<String> actual = underTest.makeColumnQueue(text, style);
            assertEquals(expected, actual);
        }

        @Test
        @DisplayName("printWrapped() should wrap and align text according to the given style")
        void printWrapped_withStyle() throws IOException {
            final TextStyle style = TextStyle.builder().setMaxWidth(10).setAlignment(TextStyle.Alignment.CENTER).get();
            final List<String> expected = Arrays.asList("The quick", "brown fox", "jumps over", " the lazy", "   dog");
            underTest.printWrapped(text, style);
            assertEquals(expected, getActualLines());
        }

        @Test
        @DisplayName("printWrapped() should wrap text using default settings")
        void printWrapped_withDefaults() throws IOException {
            // Default settings: width=74, leftPad=1, indent=3
            final List<String> expected = Collections.singletonList(" The quick brown fox jumps over the lazy dog");
            underTest.printWrapped(text);
            assertEquals(expected, getActualLines());
        }

        @Test
        @DisplayName("printWrapped() should handle newlines and indent subsequent lines")
        void printWrapped_withNewlinesAndIndent() throws IOException {
            final String multiLineText = "The quick brown fox jumps over the lazy dog.\nNow is the time for all good people to come to the aid of their country.";
            final List<String> expected = Arrays.asList(
                " The quick brown fox jumps over the lazy dog.",
                "    Now is the time for all good people to come to the aid of their",
                "    country."
            );
            underTest.printWrapped(multiLineText);
            assertEquals(expected, getActualLines());
        }

        @Test
        @DisplayName("indexOfWrap() should find correct wrap positions in a string")
        void indexOfWrap_shouldFindCorrectWrapPosition() {
            final String testString = "The quick brown fox jumps over\tthe lazy dog";
            assertEquals(9, TextHelpAppendable.indexOfWrap(testString, 10, 0), "Should find end of word");
            assertEquals(9, TextHelpAppendable.indexOfWrap(testString, 14, 0), "Should back up to end of previous word");
            assertEquals(15, TextHelpAppendable.indexOfWrap(testString, 15, 0), "Should find word at exact position");
            assertEquals(30, TextHelpAppendable.indexOfWrap(testString, 15, 20), "Should find break character after start position");
        }

        @Test
        @DisplayName("indexOfWrap() should handle edge cases")
        void indexOfWrap_shouldHandleEdgeCases() {
            assertEquals(43, TextHelpAppendable.indexOfWrap("The quick brown fox jumps over\tthe lazy dog", 150, 0), "Should handle text shorter than width");
            assertEquals(3, TextHelpAppendable.indexOfWrap("Hello", 4, 0));
            assertThrows(IllegalArgumentException.class, () -> TextHelpAppendable.indexOfWrap("", 0, 0), "Should throw on empty text");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.cli.help.UtilTest#charArgs")
        @DisplayName("indexOfWrap() should correctly identify whitespace break characters")
        void indexOfWrap_shouldIdentifyBreakChars(final Character c, final boolean isWhitespace) {
            final String text = String.format("Hello%cWorld", c);
            final int expectedIndex = isWhitespace ? 5 : 6; // 5 if 'c' is a space, 6 if it's part of "World"
            assertEquals(expectedIndex, TextHelpAppendable.indexOfWrap(text, 7, 0));
        }

        @Test
        @DisplayName("writeColumnQueues() should combine multiple columns correctly")
        void writeColumnQueues_shouldCombineColumns() throws IOException {
            final Queue<String> queue1 = new LinkedList<>(Arrays.asList("The quick ", "brown fox ", "jumps over", "the lazy  ", "dog       "));
            final Queue<String> queue2 = new LinkedList<>(Arrays.asList("     Now is the", "     time for  ", "     all good  ", "     people to ", "     come to   ", "     the aid of", "     their     ", "     country   "));
            final List<Queue<String>> queues = Arrays.asList(queue1, queue2);

            final List<TextStyle> columns = Arrays.asList(
                TextStyle.builder().setMaxWidth(10).get(),
                TextStyle.builder().setMaxWidth(10).setLeftPad(5).get()
            );

            final List<String> expected = Arrays.asList(
                " The quick      Now is the",
                " brown fox      time for  ",
                " jumps over     all good  ",
                " the lazy       people to ",
                " dog            come to   ",
                "                the aid of",
                "                their     ",
                "                country   "
            );

            underTest.writeColumnQueues(queues, columns);
            assertEquals(expected, getActualLines());
        }
    }

    @Nested
    @DisplayName("Table Sizing Tests")
    class TableSizingTests {

        @Test
        @DisplayName("adjustTableFormat() should expand column width to fit a long header")
        void adjustTableFormat_shouldExpandColumnWidthToFitHeader() {
            final TableDefinition tableDefinition = TableDefinition.from("Testing",
                Collections.singletonList(TextStyle.builder().setMaxWidth(3).get()), // Max width is smaller than header
                Collections.singletonList("header"),
                Collections.singletonList(Collections.singletonList("data"))
            );

            final TableDefinition actual = underTest.adjustTableFormat(tableDefinition);

            final int expectedWidth = "header".length();
            assertEquals(expectedWidth, actual.columnTextStyles().get(0).getMaxWidth());
            assertEquals(expectedWidth, actual.columnTextStyles().get(0).getMinWidth());
        }

        @Test
        @DisplayName("adjustTableFormat() should preserve column widths when table fits the page")
        void adjustTableFormat_shouldPreserveColumnWidthsWhenTableFits() {
            underTest.setMaxWidth(150);
            final TableDefinition tableDefinition = TableDefinition.from("Caption",
                Collections.singletonList(TextStyle.builder().setMinWidth(20).setMaxWidth(100).get()),
                Collections.singletonList("header"),
                Collections.singletonList(Collections.singletonList("one")));

            final TableDefinition result = underTest.adjustTableFormat(tableDefinition);

            assertEquals(20, result.columnTextStyles().get(0).getMinWidth(), "Minimum width should not be changed");
            assertEquals(100, result.columnTextStyles().get(0).getMaxWidth(), "Maximum width should not be changed");
        }
    }

    @Nested
    @DisplayName("Configuration and State Tests")
    class ConfigurationTests {
        @Test
        @DisplayName("getTextStyleBuilder() should return a builder with default values")
        void getTextStyleBuilder_shouldReturnBuilderWithDefaults() {
            final TextStyle.Builder builder = underTest.getTextStyleBuilder();
            assertEquals(TextHelpAppendable.DEFAULT_INDENT, builder.getIndent(), "Default indent should match constant");
            assertEquals(TextHelpAppendable.DEFAULT_LEFT_PAD, builder.getLeftPad(), "Default left pad should match constant");
            assertEquals(TextHelpAppendable.DEFAULT_WIDTH, builder.getMaxWidth(), "Default width should match constant");
        }

        @Test
        @DisplayName("setIndent() should update the indent value")
        void setIndent_shouldUpdateIndent() {
            final int newIndent = TextHelpAppendable.DEFAULT_INDENT + 5;
            underTest.setIndent(newIndent);
            assertEquals(newIndent, underTest.getIndent());
        }

        @ParameterizedTest
        @ValueSource(doubles = {0.5, 0.75})
        @DisplayName("resize() should scale down builder properties correctly")
        void resize_shouldScaleBuilderProperties(final double fraction) {
            final TextStyle.Builder tsBuilder = TextStyle.builder().setIndent(10).setMaxWidth(20);
            underTest.resize(tsBuilder, fraction);

            // The resize logic appears to be (int)(original * fraction) for max width
            // and (int)((original-1) * fraction) for indent.
            // This test verifies the behavior observed in the original test.
            final int expectedIndent = (int) ((10 - 1) * fraction);
            final int expectedWidth = (int) (20 * fraction);

            assertEquals(expectedIndent, tsBuilder.getIndent());
            assertEquals(expectedWidth, tsBuilder.getMaxWidth());
        }
    }

    /**
     * Provides arguments for the parameterized header test.
     */
    static Stream<Arguments> headerLevelsProvider() {
        return Stream.of(
            Arguments.of(1, "="),
            Arguments.of(2, "%"),
            Arguments.of(3, "+"),
            Arguments.of(4, "_"),
            Arguments.of(5, "_") // Level 5 also uses '_'
        );
    }
}