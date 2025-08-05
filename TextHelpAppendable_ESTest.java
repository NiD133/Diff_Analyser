package org.apache.commons.cli.help;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for the {@link TextHelpAppendable} class, focusing on its text formatting capabilities.
 */
public class TextHelpAppendable_ESTest {

    private StringWriter writer;
    private TextHelpAppendable helpAppendable;
    private static final String EOL = System.lineSeparator();

    @Before
    public void setUp() {
        writer = new StringWriter();
        // Use a smaller width for easier testing of wrapping behavior.
        helpAppendable = new TextHelpAppendable(writer);
        helpAppendable.setMaxWidth(30);
        helpAppendable.setLeftPad(2);
        helpAppendable.setIndent(3);
    }

    @Test
    public void constructor_shouldSetDefaultFormattingValues() {
        // Arrange
        TextHelpAppendable defaultAppendable = new TextHelpAppendable(new StringWriter());

        // Assert
        assertEquals("Default max width should be 74", 74, defaultAppendable.getMaxWidth());
        assertEquals("Default left pad should be 1", 1, defaultAppendable.getLeftPad());
        assertEquals("Default indent should be 3", 3, defaultAppendable.getIndent());
    }

    @Test
    public void setters_shouldUpdateFormattingValues() {
        // Act
        helpAppendable.setMaxWidth(80);
        helpAppendable.setLeftPad(4);
        helpAppendable.setIndent(5);

        // Assert
        assertEquals(80, helpAppendable.getMaxWidth());
        assertEquals(4, helpAppendable.getLeftPad());
        assertEquals(5, helpAppendable.getIndent());
    }

    @Test
    public void appendParagraph_whenTextIsLongerThanWidth_shouldWrapText() throws IOException {
        // Arrange
        String longText = "This is a long paragraph that should be wrapped into multiple lines.";
        String expected =
                "  This is a long paragraph" + EOL +
                "     that should be wrapped" + EOL +
                "     into multiple lines." + EOL;

        // Act
        helpAppendable.appendParagraph(longText);

        // Assert
        assertEquals(expected, writer.toString());
    }

    @Test
    public void appendParagraph_whenTextIsShort_shouldNotWrapText() throws IOException {
        // Arrange
        String shortText = "A short line.";
        String expected = "  A short line." + EOL;

        // Act
        helpAppendable.appendParagraph(shortText);

        // Assert
        assertEquals(expected, writer.toString());
    }

    @Test
    public void appendParagraph_whenTextIsEmpty_shouldPrintBlankLine() throws IOException {
        // Arrange
        String expected = "  " + EOL;

        // Act
        helpAppendable.appendParagraph("");

        // Assert
        assertEquals(expected, writer.toString());
    }

    @Test
    public void appendHeader_shouldFormatHeaderWithUnderline() throws IOException {
        // Arrange
        String headerText = "Usage";
        // The formatting logic centers the text and adds a decorative underline.
        String expected =
                "  ============================" + EOL +
                "            Usage             " + EOL +
                "  ============================" + EOL;

        // Act
        helpAppendable.appendHeader(1, headerText);

        // Assert
        assertEquals(expected, writer.toString());
    }

    @Test
    public void appendHeader_withInvalidLevel_shouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> helpAppendable.appendHeader(0, "Invalid"));
    }



    @Test
    public void appendTitle_shouldCenterTitle() throws IOException {
        // Arrange
        String titleText = "My App";
        String expected =
                "  ****************************" + EOL +
                "  *         My App         *" + EOL +
                "  ****************************" + EOL;

        // Act
        helpAppendable.appendTitle(titleText);

        // Assert
        assertEquals(expected, writer.toString());
    }

    @Test
    public void appendList_shouldFormatUnorderedListWithWrapping() throws IOException {
        // Arrange
        List<CharSequence> items = Arrays.asList(
                "First item in the list.",
                "A second, much longer item that will definitely need to be wrapped."
        );
        String expected =
                "  * First item in the list." + EOL +
                "  * A second, much longer" + EOL +
                "       item that will" + EOL +
                "       definitely need to be" + EOL +
                "       wrapped." + EOL;

        // Act
        helpAppendable.appendList(false, items); // 'false' for unordered list

        // Assert
        assertEquals(expected, writer.toString());
    }

    @Test
    public void appendList_withEmptyList_shouldProduceNoOutput() throws IOException {
        // Act
        helpAppendable.appendList(false, Collections.emptyList());

        // Assert
        assertEquals("", writer.toString());
    }

    @Test
    public void appendTable_shouldFormatTableWithWrappedCellContent() throws IOException {
        // Arrange
        List<TextStyle> styles = Arrays.asList(TextStyle.DEFAULT, TextStyle.DEFAULT);
        List<String> headers = Arrays.asList("Option", "Description");
        List<List<String>> data = Collections.singletonList(
                Arrays.asList("-v, --verbose", "Enable verbose output for detailed logging information.")
        );
        TableDefinition table = TableDefinition.from(null, styles, headers, data);

        // The table columns are auto-sized.
        String expected =
                "  -v, --verbose  Enable" + EOL +
                "                 verbose" + EOL +
                "                 output for" + EOL +
                "                 detailed" + EOL +
                "                 logging" + EOL +
                "                 information." + EOL;

        // Act
        helpAppendable.appendTable(table);

        // Assert
        assertEquals(expected, writer.toString());
    }
    
    @Test
    public void appendTable_withNoRows_shouldPrintHeaderOnly() throws IOException {
        // Arrange
        List<TextStyle> styles = Arrays.asList(TextStyle.DEFAULT, TextStyle.DEFAULT);
        List<String> headers = Arrays.asList("Option", "Description");
        TableDefinition table = TableDefinition.from(null, styles, headers, Collections.emptyList());

        // Act
        helpAppendable.appendTable(table);

        // Assert
        // With no data, a table typically prints nothing, not even headers.
        assertEquals("", writer.toString());
    }

    @Test
    public void printWrapped_withZeroWidth_shouldThrowException() {
        // Arrange
        helpAppendable.setMaxWidth(0);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> helpAppendable.printWrapped("some text"));
    }

    @Test
    public void indexOfWrap_whenWrapCharExists_shouldReturnPositionOfLastSpace() {
        // Arrange
        String text = "wrap at the last space"; // length 22
        int width = 15;
        int startPos = 0;

        // Act
        // Search window is [0, 15). "wrap at the las". Last space is at index 11.
        int wrapIndex = TextHelpAppendable.indexOfWrap(text, width, startPos);

        // Assert
        assertEquals(11, wrapIndex);
    }

    @Test
    public void indexOfWrap_whenNoWrapCharInWindow_shouldReturnEndOfWindow() {
        // Arrange
        String text = "nospacesinthispart then a space";
        int width = 10;
        int startPos = 0;

        // Act
        // Search window is [0, 10). "nospacesin". No space, so it should return startPos + width.
        int wrapIndex = TextHelpAppendable.indexOfWrap(text, width, startPos);

        // Assert
        assertEquals(10, wrapIndex);
    }

    @Test
    public void indexOfWrap_withNegativeWidth_shouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> TextHelpAppendable.indexOfWrap("any text", -1, 0));
    }
}