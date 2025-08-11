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

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests {@link TextHelpAppendable}.
 */
class TextHelpAppendableTest {

    private StringBuilder sb;
    private TextHelpAppendable underTest;

    @BeforeEach
    void setUp() {
        sb = new StringBuilder();
        underTest = new TextHelpAppendable(sb);
    }

    @Nested
    class MakeColumnQueueTests {
        private static final String TEXT = "The quick brown fox jumps over the lazy dog";
        private static final int MAX_WIDTH = 10;
        private TextStyle.Builder styleBuilder;

        @BeforeEach
        void initStyleBuilder() {
            styleBuilder = TextStyle.builder()
                .setMaxWidth(MAX_WIDTH)
                .setIndent(0)
                .setLeftPad(0);
        }

        @Test
        void leftAligned() {
            Queue<String> expected = new LinkedList<>(Arrays.asList(
                "The quick ",
                "brown fox ",
                "jumps over",
                "the lazy  ",
                "dog       "
            ));

            Queue<String> result = underTest.makeColumnQueue(TEXT, styleBuilder.get());
            assertEquals(expected, result);
        }

        @Test
        void rightAligned() {
            Queue<String> expected = new LinkedList<>(Arrays.asList(
                " The quick",
                " brown fox",
                "jumps over",
                "  the lazy",
                "       dog"
            ));
            styleBuilder.setAlignment(TextStyle.Alignment.RIGHT);

            Queue<String> result = underTest.makeColumnQueue(TEXT, styleBuilder.get());
            assertEquals(expected, result);
        }

        @Test
        void centerAligned() {
            Queue<String> expected = new LinkedList<>(Arrays.asList(
                "The quick ",
                "brown fox ",
                "jumps over",
                " the lazy ",
                "   dog    "
            ));
            styleBuilder.setAlignment(TextStyle.Alignment.CENTER);

            Queue<String> result = underTest.makeColumnQueue(TEXT, styleBuilder.get());
            assertEquals(expected, result);
        }

        @Test
        void withLeftPadAndIndent() {
            Queue<String> expected = new LinkedList<>(Arrays.asList(
                "      The quick",
                "          brown",
                "            fox",
                "          jumps",
                "       over the",
                "       lazy dog"
            ));
            styleBuilder
                .setAlignment(TextStyle.Alignment.RIGHT)
                .setLeftPad(5)
                .setIndent(2);

            Queue<String> result = underTest.makeColumnQueue(TEXT, styleBuilder.get());
            assertEquals(expected, result);
        }
    }

    @Test
    void testAdjustTableFormat_ExpandsWidthWhenDataSmallerThanHeader() {
        final TableDefinition tableDefinition = TableDefinition.from(
            "Testing",
            Collections.singletonList(TextStyle.builder().setMaxWidth(3).get()),
            Collections.singletonList("header"),
            Collections.singletonList(Collections.singletonList("data"))
        );

        final TableDefinition adjusted = underTest.adjustTableFormat(tableDefinition);

        final TextStyle columnStyle = adjusted.columnTextStyles().get(0);
        assertEquals("header".length(), columnStyle.getMaxWidth());
        assertEquals("header".length(), columnStyle.getMinWidth());
    }

    @Test
    void testAppendChar() throws IOException {
        final char testChar = (char) 0x1F44D;
        underTest.append(testChar);
        assertEquals(String.valueOf(testChar), sb.toString());
    }

    @Test
    void testAppendString() throws IOException {
        underTest.append("Hello");
        assertEquals("Hello", sb.toString());
    }

    @Nested
    class AppendHeaderTests {
        private static final String HEADER_TEXT = "Hello World";
        private static final String BLANK = "";

        @Test
        void level1() throws IOException {
            underTest.appendHeader(1, HEADER_TEXT);
            assertOutputEquals(
                " Hello World",
                " ===========",
                BLANK
            );
        }

        @Test
        void level2() throws IOException {
            underTest.appendHeader(2, HEADER_TEXT);
            assertOutputEquals(
                " Hello World",
                " %%%%%%%%%%%",
                BLANK
            );
        }

        @Test
        void level3() throws IOException {
            underTest.appendHeader(3, HEADER_TEXT);
            assertOutputEquals(
                " Hello World",
                " +++++++++++",
                BLANK
            );
        }

        @Test
        void level4() throws IOException {
            underTest.appendHeader(4, HEADER_TEXT);
            assertOutputEquals(
                " Hello World",
                " ___________",
                BLANK
            );
        }

        @Test
        void level5() throws IOException {
            underTest.appendHeader(5, HEADER_TEXT);
            assertOutputEquals(
                " Hello World",
                " ___________",
                BLANK
            );
        }

        @Test
        void level0ThrowsException() {
            assertThrows(IllegalArgumentException.class, 
                () -> underTest.appendHeader(0, HEADER_TEXT));
        }

        @Test
        void emptyStringProducesNoOutput() throws IOException {
            underTest.appendHeader(1, "");
            assertEquals(0, sb.length());
        }

        @Test
        void nullProducesNoOutput() throws IOException {
            underTest.appendHeader(1, null);
            assertEquals(0, sb.length());
        }

        private void assertOutputEquals(String... expected) throws IOException {
            List<String> actual = IOUtils.readLines(new StringReader(sb.toString()));
            assertEquals(Arrays.asList(expected), actual);
        }
    }

    @Nested
    class AppendListTests {
        private static final List<String> ENTRIES = Arrays.asList("one", "two", "three");
        private static final String BLANK = "";

        @Test
        void orderedList() throws IOException {
            underTest.appendList(true, ENTRIES);
            assertOutputEquals(
                "  1. one",
                "  2. two",
                "  3. three",
                BLANK
            );
        }

        @Test
        void unorderedList() throws IOException {
            underTest.appendList(false, ENTRIES);
            assertOutputEquals(
                "  * one",
                "  * two",
                "  * three",
                BLANK
            );
        }

        @Test
        void emptyListProducesNoOutput() throws IOException {
            underTest.appendList(false, Collections.emptyList());
            assertEquals(0, sb.length());
        }

        @Test
        void nullListProducesNoOutput() throws IOException {
            underTest.appendList(false, null);
            assertEquals(0, sb.length());
        }

        private void assertOutputEquals(String... expected) throws IOException {
            List<String> actual = IOUtils.readLines(new StringReader(sb.toString()));
            assertEquals(Arrays.asList(expected), actual);
        }
    }

    @Test
    void testAppendParagraph() throws IOException {
        underTest.appendParagraph("Hello World");
        List<String> lines = IOUtils.readLines(new StringReader(sb.toString()));
        assertEquals(Arrays.asList(" Hello World", ""), lines);
    }

    @Test
    void testAppendParagraphFormat() throws IOException {
        underTest.appendParagraphFormat("Hello %s World %,d", "Joe", 309);
        List<String> lines = IOUtils.readLines(new StringReader(sb.toString()));
        assertEquals(Arrays.asList(" Hello Joe World 309", ""), lines);
    }

    @Nested
    class AppendTableTests {
        private static final String BLANK = "";
        private static final String CAPTION = "Common Phrases";
        private static final List<String> HEADERS = Arrays.asList("fox", "time");
        private static final List<List<String>> ROWS = Arrays.asList(
            Arrays.asList(
                "The quick brown fox jumps over the lazy dog",
                "Now is the time for all good people to come to the aid of their country"
            ),
            Arrays.asList(
                "Léimeann an sionnach donn gasta thar an madra leisciúil",
                "Anois an t-am do na daoine maithe go léir teacht i gcabhair ar a dtír"
            )
        );
        private final List<TextStyle> styles = new ArrayList<>();

        @BeforeEach
        void initStyles() {
            TextStyle.Builder styleBuilder = TextStyle.builder();
            styles.add(styleBuilder.setIndent(2).get());
            styles.add(styleBuilder.setIndent(0).setLeftPad(5).setAlignment(TextStyle.Alignment.RIGHT).get());
            underTest.setMaxWidth(80);
        }

        @Test
        void withCaption() throws IOException {
            TableDefinition table = TableDefinition.from(CAPTION, styles, HEADERS, ROWS);
            underTest.appendTable(table);
            assertOutputEquals(
                " Common Phrases",
                "",
                "               fox                                       time                   ",
                " The quick brown fox jumps over           Now is the time for all good people to",
                "   the lazy dog                                 come to the aid of their country",
                " Léimeann an sionnach donn gasta       Anois an t-am do na daoine maithe go léir",
                "   thar an madra leisciúil                           teacht i gcabhair ar a dtír",
                BLANK
            );
        }

        @Test
        void withoutCaption() throws IOException {
            TableDefinition table = TableDefinition.from(null, styles, HEADERS, ROWS);
            underTest.appendTable(table);
            assertOutputEquals(
                "               fox                                       time                   ",
                " The quick brown fox jumps over           Now is the time for all good people to",
                "   the lazy dog                                 come to the aid of their country",
                " Léimeann an sionnach donn gasta       Anois an t-am do na daoine maithe go léir",
                "   thar an madra leisciúil                           teacht i gcabhair ar a dtír",
                BLANK
            );
        }

        @Test
        void noRows() throws IOException {
            TableDefinition table = TableDefinition.from(null, styles, HEADERS, Collections.emptyList());
            underTest.appendTable(table);
            assertOutputEquals(
                " fox     time",
                BLANK
            );
        }

        private void assertOutputEquals(String... expected) throws IOException {
            List<String> actual = IOUtils.readLines(new StringReader(sb.toString()));
            assertEquals(Arrays.asList(expected), actual);
        }
    }

    @Test
    void testAppendTitle() throws IOException {
        underTest.appendTitle("Hello World");
        List<String> lines = IOUtils.readLines(new StringReader(sb.toString()));
        assertEquals(Arrays.asList(" Hello World", " ###########", ""), lines);
    }

    @Test
    void testGetStyleBuilder_ReturnsDefaultValues() {
        TextStyle.Builder builder = underTest.getTextStyleBuilder();
        assertEquals(TextHelpAppendable.DEFAULT_INDENT, builder.getIndent());
        assertEquals(TextHelpAppendable.DEFAULT_LEFT_PAD, builder.getLeftPad());
        assertEquals(TextHelpAppendable.DEFAULT_WIDTH, builder.getMaxWidth());
    }

    @Nested
    class IndexOfWrapTests {
        private static final String TEST_STRING = "The quick brown fox jumps over\tthe lazy dog";

        @Test
        void findsEndOfWord() {
            int pos = TextHelpAppendable.indexOfWrap(TEST_STRING, 10, 0);
            assertEquals(9, pos); // "The quick" (9 chars + space)
        }

        @Test
        void backsUpToEndOfWord() {
            int pos = TextHelpAppendable.indexOfWrap(TEST_STRING, 14, 0);
            assertEquals(9, pos); // Same as above (word boundary at 9)
        }

        @Test
        void findsWordAtPosition() {
            int pos = TextHelpAppendable.indexOfWrap(TEST_STRING, 15, 0);
            assertEquals(15, pos); // "brown" starts at 10, ends at 15
        }

        @Test
        void findsBreakCharacter() {
            int pos = TextHelpAppendable.indexOfWrap(TEST_STRING, 15, 20);
            assertEquals(30, pos); // Tab character at position 30
        }

        @Test
        void handlesTextShorterThanWidth() {
            int pos = TextHelpAppendable.indexOfWrap(TEST_STRING, 150, 0);
            assertEquals(30, pos); // Full text length
        }

        @Test
        void throwsForZeroWidth() {
            assertThrows(IllegalArgumentException.class, 
                () -> TextHelpAppendable.indexOfWrap("", 0, 0));
        }

        @Test
        void handlesSingleWord() {
            int pos = TextHelpAppendable.indexOfWrap("Hello", 4, 0);
            assertEquals(3, pos); // Breaks at end of "Hell"
        }
    }

    @ParameterizedTest
    @MethodSource("org.apache.commons.cli.help.UtilTest#charArgs")
    void testIndexOfWrapWithWhitespace(final Character c, final boolean isWhitespace) {
        final String text = String.format("Hello%cWorld", c);
        int expected = isWhitespace ? 5 : 6;
        int actual = TextHelpAppendable.indexOfWrap(text, 7, 0);
        assertEquals(expected, actual);
    }

    @Nested
    class PrintWrappedTests {
        private static final String TEXT = "The quick brown fox jumps over the lazy dog";
        private static final int MAX_WIDTH = 10;

        @Test
        void leftAligned() throws IOException {
            TextStyle style = TextStyle.builder()
                .setMaxWidth(MAX_WIDTH)
                .setIndent(0)
                .setLeftPad(0)
                .get();
            
            underTest.printWrapped(TEXT, style);
            assertOutputEquals(
                "The quick",
                "brown fox",
                "jumps over",
                "the lazy",
                "dog"
            );
        }

        @Test
        void rightAligned() throws IOException {
            TextStyle style = TextStyle.builder()
                .setMaxWidth(MAX_WIDTH)
                .setIndent(0)
                .setLeftPad(0)
                .setAlignment(TextStyle.Alignment.RIGHT)
                .get();
            
            underTest.printWrapped(TEXT, style);
            assertOutputEquals(
                " The quick",
                " brown fox",
                "jumps over",
                "  the lazy",
                "       dog"
            );
        }

        @Test
        void centerAligned() throws IOException {
            TextStyle style = TextStyle.builder()
                .setMaxWidth(MAX_WIDTH)
                .setIndent(0)
                .setLeftPad(0)
                .setAlignment(TextStyle.Alignment.CENTER)
                .get();
            
            underTest.printWrapped(TEXT, style);
            assertOutputEquals(
                "The quick",
                "brown fox",
                "jumps over",
                " the lazy",
                "   dog"
            );
        }

        @Test
        void withDefaultFormat() throws IOException {
            underTest.printWrapped(TEXT);
            assertOutputEquals(" The quick brown fox jumps over the lazy dog");
        }

        @Test
        void withNewlines() throws IOException {
            String multiLineText = TEXT + ".\nNow is the time for all good people to come to the aid of their country.";
            underTest.printWrapped(multiLineText);
            assertOutputEquals(
                " The quick brown fox jumps over the lazy dog.",
                "    Now is the time for all good people to come to the aid of their",
                "    country."
            );
        }

        private void assertOutputEquals(String... expected) throws IOException {
            List<String> actual = IOUtils.readLines(new StringReader(sb.toString()));
            assertEquals(Arrays.asList(expected), actual);
        }
    }

    @Test
    void testResize() {
        TextStyle.Builder builder = TextStyle.builder().setIndent(2).setMaxWidth(3);
        underTest.resize(builder, 0.5);
        assertEquals(0, builder.getIndent());
    }

    @Test
    void testResizeTableFormat_PreservesMinMaxWidths() {
        underTest.setMaxWidth(150);
        TableDefinition table = TableDefinition.from(
            "Caption",
            Collections.singletonList(TextStyle.builder().setMinWidth(20).setMaxWidth(100).get()),
            Collections.singletonList("header"),
            Collections.singletonList(Collections.singletonList("one"))
        );

        TableDefinition result = underTest.adjustTableFormat(table);
        TextStyle style = result.columnTextStyles().get(0);
        
        assertEquals(20, style.getMinWidth());
        assertEquals(100, style.getMaxWidth());
    }

    @Test
    void testSetIndent() {
        int newIndent = TextHelpAppendable.DEFAULT_INDENT + 2;
        underTest.setIndent(newIndent);
        assertEquals(newIndent, underTest.getIndent());
    }

    @Test
    void testWriteColumnQueues() throws IOException {
        // Setup queues
        Queue<String> col1 = new LinkedList<>(Arrays.asList(
            "The quick ", "brown fox ", "jumps over", "the lazy  ", "dog       "
        ));
        Queue<String> col2 = new LinkedList<>(Arrays.asList(
            "     Now is the",
            "     time for  ",
            "     all good  ",
            "     people to ",
            "     come to   ",
            "     the aid of",
            "     their     ",
            "     country   "
        ));
        List<Queue<String>> queues = Arrays.asList(col1, col2);

        // Setup styles
        TextStyle style1 = TextStyle.builder().setMaxWidth(10).setIndent(0).setLeftPad(0).get();
        TextStyle style2 = TextStyle.builder().setMaxWidth(10).setIndent(0).setLeftPad(5).get();
        List<TextStyle> styles = Arrays.asList(style1, style2);

        underTest.writeColumnQueues(queues, styles);
        assertOutputEquals(
            " The quick      Now is the",
            " brown fox      time for  ",
            " jumps over     all good  ",
            " the lazy       people to ",
            " dog            come to   ",
            "                the aid of",
            "                their     ",
            "                country   "
        );
    }

    private void assertOutputEquals(String... expected) throws IOException {
        List<String> actual = IOUtils.readLines(new StringReader(sb.toString()));
        assertEquals(Arrays.asList(expected), actual);
    }
}