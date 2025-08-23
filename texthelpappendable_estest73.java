package org.apache.commons.cli.help;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import org.junit.Test;

/**
 * Tests for the {@link TextHelpAppendable} class, focusing on table formatting logic.
 */
public class TextHelpAppendableTest {

    /**
     * Verifies that {@link TextHelpAppendable#adjustTableFormat(TableDefinition)} throws
     * an {@link IndexOutOfBoundsException} when the table definition provides more
     * style columns than header columns. This is an invalid state, as each style
     * corresponds to a header.
     */
    @Test
    public void adjustTableFormatThrowsExceptionWhenStylesExceedHeaders() {
        // Arrange: Create a TextHelpAppendable instance.
        final TextHelpAppendable textHelpAppendable = TextHelpAppendable.systemOut();

        // Arrange: Define a table with one style but zero headers. This mismatch is
        // expected to cause an IndexOutOfBoundsException when the method tries to
        // access a header that doesn't exist.
        final ArrayList<TextStyle> styles = new ArrayList<>();
        styles.add(TextStyle.DEFAULT);

        final ArrayList<String> emptyHeaders = new ArrayList<>();
        final LinkedHashSet<List<String>> noRows = new LinkedHashSet<>();

        final TableDefinition tableDefinition = TableDefinition.from("Test Table", styles, emptyHeaders, noRows);

        // Act & Assert
        try {
            textHelpAppendable.adjustTableFormat(tableDefinition);
            fail("Expected an IndexOutOfBoundsException to be thrown due to more styles than headers.");
        } catch (final IndexOutOfBoundsException e) {
            // Assert: Verify the exception is thrown because of an attempt to access
            // index 0 of an empty list (the headers).
            final String expectedMessage = "Index: 0, Size: 0";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}