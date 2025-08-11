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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests {@link TextHelpAppendable}.
 */
public final class TextHelpAppendableTest {

    private static final String SAMPLE_TEXT = "The quick brown fox jumps over the lazy dog";
    private static final int COLUMN_WIDTH_10 = 10;
    private static final int NO_INDENT = 0;
    private static final int NO_LEFT_PAD = 0;
    private static final int SMALL_INDENT = 2;
    private static final int MEDIUM_LEFT_PAD = 5;
    private static final int PAGE_WIDTH_80 = 80;
    private static final int PAGE_WIDTH_150 = 150;

    private StringBuilder outputBuffer;
    private TextHelpAppendable textAppendable;

    @BeforeEach
    public void setUp() {
        outputBuffer = new StringBuilder();
        textAppendable = new TextHelpAppendable(outputBuffer);
    }

    // ========== Column Queue Tests ==========

    @Test
    void testMakeColumnQueue_LeftAlignment() {
        // Given
        TextStyle leftAlignedStyle = createTextStyle(COLUMN_WIDTH_10, NO_INDENT, NO_LEFT_PAD, TextStyle.Alignment.LEFT);
        Queue<String> expectedLeftAligned = createQueue("The quick ", "brown fox ", "jumps over", "the lazy  ", "dog       ");

        // When
        Queue<String> actualResult = textAppendable.makeColumnQueue(SAMPLE_TEXT, leftAlignedStyle);

        // Then
        assertEquals(expectedLeftAligned, actualResult, "Left alignment should wrap text correctly");
    }

    @Test
    void testMakeColumnQueue_RightAlignment() {
        // Given
        TextStyle rightAlignedStyle = createTextStyle(COLUMN_WIDTH_10, NO_INDENT, NO_LEFT_PAD, TextStyle.Alignment.RIGHT);
        Queue<String> expectedRightAligned = createQueue(" The quick", " brown fox", "jumps over", "  the lazy", "       dog");

        // When
        Queue<String> actualResult = textAppendable.makeColumnQueue(SAMPLE_TEXT, rightAlignedStyle);

        // Then
        assertEquals(expectedRightAligned, actualResult, "Right alignment should wrap text correctly");
    }

    @Test
    void testMakeColumnQueue_CenterAlignment() {
        // Given
        TextStyle centerAlignedStyle = createTextStyle(COLUMN_WIDTH_10, NO_INDENT, NO_LEFT_PAD, TextStyle.Alignment.CENTER);
        Queue<String> expectedCenterAligned = createQueue("The quick ", "brown fox ", "jumps over", " the lazy ", "   dog    ");

        // When
        Queue<String> actualResult = textAppendable.makeColumnQueue(SAMPLE_TEXT, centerAlignedStyle);

        // Then
        assertEquals(expectedCenterAligned, actualResult, "Center alignment should wrap text correctly");
    }

    @Test
    void testMakeColumnQueue_WithPaddingAndIndent() {
        // Given
        TextStyle styledText = createTextStyle(COLUMN_WIDTH_10, SMALL_INDENT, MEDIUM_LEFT_PAD, TextStyle.Alignment.RIGHT);
        Queue<String> expectedWithPadding = createQueue(
            "      The quick", "          brown", "            fox", "          jumps", 
            "       over the", "       lazy dog"
        );

        // When
        Queue<String> actualResult = textAppendable.makeColumnQueue(SAMPLE_TEXT, styledText);

        // Then
        assertEquals(expectedWithPadding, actualResult, "Right alignment with padding and indent should format correctly");
    }

    // ========== Table Format Adjustment Tests ==========

    @Test
    void testAdjustTableFormat_ExpandsColumnToFitHeader() {
        // Given: Column width smaller than header text
        String headerText = "header";
        String dataText = "data"; // shorter than header
        TableDefinition originalTable = TableDefinition.from(
            "Testing",
            Collections.singletonList(TextStyle.builder().setMaxWidth(3).get()),
            Collections.singletonList(headerText),
            Collections.singletonList(Collections.singletonList(dataText))
        );

        // When
        TableDefinition adjustedTable = textAppendable.adjustTableFormat(originalTable);

        // Then
        int expectedWidth = headerText.length();
        assertEquals(expectedWidth, adjustedTable.columnTextStyles().get(0).getMaxWidth(),
            "Column width should expand to fit header");
        assertEquals(expectedWidth, adjustedTable.columnTextStyles().get(0).getMinWidth(),
            "Column min width should match header length");
    }

    @Test
    void testAdjustTableFormat_PreservesExistingDimensions() {
        // Given
        int minWidth = 20;
        int maxWidth = 100;
        textAppendable.setMaxWidth(PAGE_WIDTH_150);
        
        TableDefinition originalTable = TableDefinition.from(
            "Caption",
            Collections.singletonList(TextStyle.builder().setMinWidth(minWidth).setMaxWidth(maxWidth).get()),
            Collections.singletonList("header"),
            Collections.singletonList(Collections.singletonList("one"))
        );

        // When
        TableDefinition adjustedTable = textAppendable.adjustTableFormat(originalTable);

        // Then
        assertEquals(minWidth, adjustedTable.columnTextStyles().get(0).getMinWidth(),
            "Minimum width should be preserved");
        assertEquals(maxWidth, adjustedTable.columnTextStyles().get(0).getMaxWidth(),
            "Maximum width should be preserved");
    }

    // ========== Basic Append Tests ==========

    @Test
    void testAppend_Character() throws IOException {
        // Given
        char testChar = (char) 0x1F44D;

        // When
        textAppendable.append(testChar);

        // Then
        assertEquals(1, outputBuffer.length(), "Should append single character");
        assertEquals(String.valueOf(testChar), outputBuffer.toString(), "Character should match");
    }

    @Test
    void testAppend_String() throws IOException {
        // Given
        String testString = "Hello";

        // When
        textAppendable.append(testString);

        // Then
        assertEquals(testString, outputBuffer.toString(), "String should be appended correctly");
    }

    // ========== Header Tests ==========

    @Test
    void testAppendHeader_Level1() throws IOException {
        testHeaderLevel(1, "Hello World", "===========");
    }

    @Test
    void testAppendHeader_Level2() throws IOException {
        testHeaderLevel(2, "Hello World", "%%%%%%%%%%%");
    }

    @Test
    void testAppendHeader_Level3() throws IOException {
        testHeaderLevel(3, "Hello World", "+++++++++++");
    }

    @Test
    void testAppendHeader_Level4And5() throws IOException {
        testHeaderLevel(4, "Hello World", "___________");
        
        outputBuffer.setLength(0);
        testHeaderLevel(5, "Hello World", "___________");
    }

    @Test
    void testAppendHeader_InvalidLevel() {
        assertThrows(IllegalArgumentException.class, 
            () -> textAppendable.appendHeader(0, "Hello World"),
            "Should throw exception for invalid header level");
    }

    @Test
    void testAppendHeader_EmptyAndNullText() throws IOException {
        // Empty string
        textAppendable.appendHeader(1, "");
        assertEquals(0, outputBuffer.length(), "Empty header should produce no output");

        // Null string
        outputBuffer.setLength(0);
        textAppendable.appendHeader(1, null);
        assertEquals(0, outputBuffer.length(), "Null header should produce no output");
    }

    // ========== List Tests ==========

    @Test
    void testAppendList_Ordered() throws IOException {
        // Given
        List<String> items = Arrays.asList("one", "two", "three");
        List<String> expectedOutput = Arrays.asList(
            "  1. one", "  2. two", "  3. three", ""
        );

        // When
        textAppendable.appendList(true, items);

        // Then
        List<String> actualOutput = readOutputLines();
        assertEquals(expectedOutput, actualOutput, "Ordered list should be numbered correctly");
    }

    @Test
    void testAppendList_Unordered() throws IOException {
        // Given
        List<String> items = Arrays.asList("one", "two", "three");
        List<String> expectedOutput = Arrays.asList(
            "  * one", "  * two", "  * three", ""
        );

        // When
        textAppendable.appendList(false, items);

        // Then
        List<String> actualOutput = readOutputLines();
        assertEquals(expectedOutput, actualOutput, "Unordered list should use bullet points");
    }

    @Test
    void testAppendList_EmptyAndNull() throws IOException {
        // Empty list
        textAppendable.appendList(false, Collections.emptyList());
        assertEquals(Collections.emptyList(), readOutputLines(), "Empty list should produce no output");

        // Null list
        outputBuffer.setLength(0);
        textAppendable.appendList(false, null);
        assertEquals(Collections.emptyList(), readOutputLines(), "Null list should produce no output");
    }

    // ========== Paragraph Tests ==========

    @Test
    void testAppendParagraph_Normal() throws IOException {
        // Given
        String paragraphText = "Hello World";
        List<String> expectedOutput = Arrays.asList(" Hello World", "");

        // When
        textAppendable.appendParagraph(paragraphText);

        // Then
        List<String> actualOutput = readOutputLines();
        assertEquals(expectedOutput, actualOutput, "Paragraph should be formatted with padding");
    }

    @Test
    void testAppendParagraph_EmptyAndNull() throws IOException {
        // Empty string
        textAppendable.appendParagraph("");
        assertEquals(0, outputBuffer.length(), "Empty paragraph should produce no output");

        // Null string
        outputBuffer.setLength(0);
        textAppendable.appendParagraph(null);
        assertEquals(0, outputBuffer.length(), "Null paragraph should produce no output");
    }

    @Test
    void testAppendParagraphFormat() throws IOException {
        // Given
        String template = "Hello %s World %,d";
        String name = "Joe";
        int number = 309;
        List<String> expectedOutput = Arrays.asList(" Hello Joe World 309", "");

        // When
        textAppendable.appendParagraphFormat(template, name, number);

        // Then
        List<String> actualOutput = readOutputLines();
        assertEquals(expectedOutput, actualOutput, "Formatted paragraph should substitute parameters");
    }

    @Test
    void testAppendParagraphFormat_EmptyTemplate() throws IOException {
        textAppendable.appendParagraphFormat("");
        assertEquals(0, outputBuffer.length(), "Empty format template should produce no output");
    }

    // ========== Table Tests ==========

    @Test
    void testAppendTable_CompleteTable() throws IOException {
        // Given
        TableDefinition completeTable = createSampleTable("Common Phrases");
        List<String> expectedOutput = Arrays.asList(
            " Common Phrases", "",
            "               fox                                       time                   ",
            " The quick brown fox jumps over           Now is the time for all good people to",
            "   the lazy dog                                 come to the aid of their country",
            " Léimeann an sionnach donn gasta       Anois an t-am do na daoine maithe go léir",
            "   thar an madra leisciúil                           teacht i gcabhair ar a dtír",
            ""
        );

        // When
        textAppendable.setMaxWidth(PAGE_WIDTH_80);
        textAppendable.appendTable(completeTable);

        // Then
        List<String> actualOutput = readOutputLines();
        assertEquals(expectedOutput, actualOutput, "Complete table should include title and formatted data");
    }

    @Test
    void testAppendTable_NoTitle() throws IOException {
        // Given
        TableDefinition tableWithoutTitle = createSampleTable(null);
        List<String> expectedOutput = Arrays.asList(
            "               fox                                       time                   ",
            " The quick brown fox jumps over           Now is the time for all good people to",
            "   the lazy dog                                 come to the aid of their country",
            " Léimeann an sionnach donn gasta       Anois an t-am do na daoine maithe go léir",
            "   thar an madra leisciúil                           teacht i gcabhair ar a dtír",
            ""
        );

        // When
        textAppendable.setMaxWidth(PAGE_WIDTH_80);
        textAppendable.appendTable(tableWithoutTitle);

        // Then
        List<String> actualOutput = readOutputLines();
        assertEquals(expectedOutput, actualOutput, "Table without title should only show headers and data");
    }

    @Test
    void testAppendTable_HeadersOnly() throws IOException {
        // Given
        List<TextStyle> columnStyles = createSampleColumnStyles();
        String[] headers = {"fox", "time"};
        TableDefinition headersOnlyTable = TableDefinition.from(
            null, columnStyles, Arrays.asList(headers), Collections.emptyList()
        );
        List<String> expectedOutput = Arrays.asList(" fox     time", "");

        // When
        textAppendable.appendTable(headersOnlyTable);

        // Then
        List<String> actualOutput = readOutputLines();
        assertEquals(expectedOutput, actualOutput, "Table with no data should show only headers");
    }

    // ========== Title Tests ==========

    @Test
    void testAppendTitle() throws IOException {
        // Given
        String titleText = "Hello World";
        List<String> expectedOutput = Arrays.asList(" Hello World", " ###########", "");

        // When
        textAppendable.appendTitle(titleText);

        // Then
        List<String> actualOutput = readOutputLines();
        assertEquals(expectedOutput, actualOutput, "Title should be underlined with hash marks");
    }

    @Test
    void testAppendTitle_EmptyAndNull() throws IOException {
        // Empty string
        textAppendable.appendTitle("");
        assertEquals(0, outputBuffer.length(), "Empty title should produce no output");

        // Null string
        outputBuffer.setLength(0);
        textAppendable.appendTitle(null);
        assertEquals(0, outputBuffer.length(), "Null title should produce no output");
    }

    // ========== Style Builder Tests ==========

    @Test
    void testGetTextStyleBuilder_DefaultValues() {
        TextStyle.Builder builder = textAppendable.getTextStyleBuilder();
        
        assertEquals(TextHelpAppendable.DEFAULT_INDENT, builder.getIndent(),
            "Default indent should match constant");
        assertEquals(TextHelpAppendable.DEFAULT_LEFT_PAD, builder.getLeftPad(),
            "Default left pad should match constant");
        assertEquals(TextHelpAppendable.DEFAULT_WIDTH, builder.getMaxWidth(),
            "Default width should match constant");
    }

    // ========== Text Wrapping Tests ==========

    @Test
    void testIndexOfWrap_FindEndOfWord() {
        String testText = "The quick brown fox jumps over\tthe lazy dog";
        
        assertEquals(9, TextHelpAppendable.indexOfWrap(testText, 10, 0),
            "Should find end of word within width");
        assertEquals(9, TextHelpAppendable.indexOfWrap(testText, 14, 0),
            "Should backup to end of word when exceeding width");
        assertEquals(15, TextHelpAppendable.indexOfWrap(testText, 15, 0),
            "Should find exact word boundary");
        assertEquals(30, TextHelpAppendable.indexOfWrap(testText, 15, 20),
            "Should find tab character as break point");
        assertEquals(30, TextHelpAppendable.indexOfWrap(testText, 150, 0),
            "Should handle text shorter than width");
    }

    @Test
    void testIndexOfWrap_EdgeCases() {
        assertThrows(IllegalArgumentException.class, 
            () -> TextHelpAppendable.indexOfWrap("", 0, 0),
            "Should throw exception for empty string with zero width");
        
        assertEquals(3, TextHelpAppendable.indexOfWrap("Hello", 4, 0),
            "Should handle text longer than width");
    }

    @ParameterizedTest
    @MethodSource("org.apache.commons.cli.help.UtilTest#charArgs")
    void testIndexOfWrap_WhitespaceHandling(final Character character, final boolean isWhitespace) {
        String testText = String.format("Hello%cWorld", character);
        int expectedPosition = isWhitespace ? 5 : 6;
        
        assertEquals(expectedPosition, TextHelpAppendable.indexOfWrap(testText, 7, 0),
            "Whitespace handling should depend on character type");
    }

    // ========== Print Wrapped Tests ==========

    @Test
    void testPrintWrapped_LeftAlignment() throws IOException {
        testWrappedAlignment(TextStyle.Alignment.LEFT, 
            Arrays.asList("The quick", "brown fox", "jumps over", "the lazy", "dog"));
    }

    @Test
    void testPrintWrapped_RightAlignment() throws IOException {
        testWrappedAlignment(TextStyle.Alignment.RIGHT,
            Arrays.asList(" The quick", " brown fox", "jumps over", "  the lazy", "       dog"));
    }

    @Test
    void testPrintWrapped_CenterAlignment() throws IOException {
        testWrappedAlignment(TextStyle.Alignment.CENTER,
            Arrays.asList("The quick", "brown fox", "jumps over", " the lazy", "   dog"));
    }

    @Test
    void testPrintWrapped_DefaultFormat() throws IOException {
        // Given
        List<String> expectedOutput = Arrays.asList(" The quick brown fox jumps over the lazy dog");

        // When
        textAppendable.printWrapped(SAMPLE_TEXT);

        // Then
        List<String> actualOutput = readOutputLines();
        assertEquals(expectedOutput, actualOutput, "Default format should use page settings");
        
        // Verify default settings
        assertEquals(1, textAppendable.getLeftPad(), "Default left pad should be 1");
        assertEquals(3, textAppendable.getIndent(), "Default indent should be 3");
        assertEquals(74, textAppendable.getMaxWidth(), "Default width should be 74");
    }

    @Test
    void testPrintWrapped_MultipleLines() throws IOException {
        // Given
        String multiLineText = SAMPLE_TEXT + ".\nNow is the time for all good people to come to the aid of their country.";
        List<String> expectedOutput = Arrays.asList(
            " The quick brown fox jumps over the lazy dog.",
            "    Now is the time for all good people to come to the aid of their",
            "    country."
        );

        // When
        textAppendable.printWrapped(multiLineText);

        // Then
        List<String> actualOutput = readOutputLines();
        assertEquals(expectedOutput, actualOutput, "Multiple lines should handle indentation correctly");
    }

    // ========== Resize Tests ==========

    @Test
    void testResize_HalvesValues() {
        TextStyle.Builder builder = TextStyle.builder().setIndent(2).setMaxWidth(3);
        
        textAppendable.resize(builder, 0.5);
        
        assertEquals(0, builder.getIndent(), "Indent should be halved and rounded down");
    }

    @Test
    void testResize_RoundsDown() {
        TextStyle.Builder builder = TextStyle.builder().setIndent(4).setMaxWidth(6);
        
        textAppendable.resize(builder, 0.5);
        
        assertEquals(1, builder.getIndent(), "Odd numbers should round down when halved");
    }

    // ========== Setter Tests ==========

    @Test
    void testSetIndent() {
        int newIndent = TextHelpAppendable.DEFAULT_INDENT + 2;
        
        textAppendable.setIndent(newIndent);
        
        assertEquals(newIndent, textAppendable.getIndent(), "Indent should be updated");
    }

    // ========== Column Queue Writing Tests ==========

    @Test
    void testWriteColumnQueues() throws IOException {
        // Given
        List<Queue<String>> columnQueues = createSampleColumnQueues();
        List<TextStyle> columnStyles = createColumnQueueStyles();
        List<String> expectedOutput = Arrays.asList(
            " The quick      Now is the",
            " brown fox      time for  ",
            " jumps over     all good  ",
            " the lazy       people to ",
            " dog            come to   ",
            "                the aid of",
            "                their     ",
            "                country   "
        );

        // When
        textAppendable.writeColumnQueues(columnQueues, columnStyles);

        // Then
        List<String> actualOutput = readOutputLines();
        assertEquals(expectedOutput, actualOutput, "Column queues should be written side by side");
    }

    // ========== Helper Methods ==========

    private TextStyle createTextStyle(int maxWidth, int indent, int leftPad, TextStyle.Alignment alignment) {
        return TextStyle.builder()
            .setMaxWidth(maxWidth)
            .setIndent(indent)
            .setLeftPad(leftPad)
            .setAlignment(alignment)
            .get();
    }

    private Queue<String> createQueue(String... items) {
        Queue<String> queue = new LinkedList<>();
        Collections.addAll(queue, items);
        return queue;
    }

    private void testHeaderLevel(int level, String headerText, String underlineChar) throws IOException {
        List<String> expectedOutput = Arrays.asList(
            " " + headerText,
            " " + underlineChar,
            ""
        );

        outputBuffer.setLength(0);
        textAppendable.appendHeader(level, headerText);
        List<String> actualOutput = readOutputLines();
        
        assertEquals(expectedOutput, actualOutput, "Header level " + level + " should format correctly");
    }

    private void testWrappedAlignment(TextStyle.Alignment alignment, List<String> expectedLines) throws IOException {
        TextStyle style = createTextStyle(COLUMN_WIDTH_10, NO_INDENT, NO_LEFT_PAD, alignment);
        
        outputBuffer.setLength(0);
        textAppendable.printWrapped(SAMPLE_TEXT, style);
        List<String> actualOutput = readOutputLines();
        
        assertEquals(expectedLines, actualOutput, alignment + " alignment should wrap correctly");
    }

    private TableDefinition createSampleTable(String title) {
        List<TextStyle> columnStyles = createSampleColumnStyles();
        String[] headers = {"fox", "time"};
        List<List<String>> rows = Arrays.asList(
            Arrays.asList(
                "The quick brown fox jumps over the lazy dog",
                "Now is the time for all good people to come to the aid of their country"
            ),
            Arrays.asList(
                "Léimeann an sionnach donn gasta thar an madra leisciúil",
                "Anois an t-am do na daoine maithe go léir teacht i gcabhair ar a dtír"
            )
        );
        
        return TableDefinition.from(title, columnStyles, Arrays.asList(headers), rows);
    }

    private List<TextStyle> createSampleColumnStyles() {
        TextStyle.Builder styleBuilder = TextStyle.builder();
        List<TextStyle> styles = new ArrayList<>();
        styles.add(styleBuilder.setIndent(SMALL_INDENT).get());
        styles.add(styleBuilder.setIndent(NO_INDENT).setLeftPad(MEDIUM_LEFT_PAD)
            .setAlignment(TextStyle.Alignment.RIGHT).get());
        return styles;
    }

    private List<Queue<String>> createSampleColumnQueues() {
        List<Queue<String>> queues = new ArrayList<>();
        
        // First column
        Queue<String> firstColumn = createQueue(
            "The quick ", "brown fox ", "jumps over", "the lazy  ", "dog       "
        );
        queues.add(firstColumn);
        
        // Second column
        Queue<String> secondColumn = createQueue(
            "     Now is the", "     time for  ", "     all good  ", "     people to ",
            "     come to   ", "     the aid of", "     their     ", "     country   "
        );
        queues.add(secondColumn);
        
        return queues;
    }

    private List<TextStyle> createColumnQueueStyles() {
        TextStyle.Builder styleBuilder = TextStyle.builder().setMaxWidth(COLUMN_WIDTH_10)
            .setIndent(NO_INDENT).setLeftPad(NO_LEFT_PAD);
        
        List<TextStyle> columns = new ArrayList<>();
        columns.add(styleBuilder.get());
        columns.add(styleBuilder.setLeftPad(MEDIUM_LEFT_PAD).get());
        return columns;
    }

    private List<String> readOutputLines() throws IOException {
        return IOUtils.readLines(new StringReader(outputBuffer.toString()));
    }
}