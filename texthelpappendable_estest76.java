package org.apache.commons.cli.help;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

/**
 * This test class contains tests for the TextHelpAppendable class.
 * This particular test was improved for clarity.
 */
public class TextHelpAppendable_ESTestTest76 extends TextHelpAppendable_ESTest_scaffolding {

    /**
     * Verifies that calling adjustTableFormat does not modify the internal state
     * (indent, max width, left padding) of the TextHelpAppendable instance.
     * The method should be a pure function concerning the instance it's called on,
     * returning a new TableDefinition without side effects.
     */
    @Test
    public void adjustTableFormatShouldNotModifyInternalStateOfAppendable() {
        // Arrange: Create a TextHelpAppendable with default settings.
        TextHelpAppendable textHelpAppendable = TextHelpAppendable.systemOut();

        // Create a minimal but valid TableDefinition to pass to the method.
        List<TextStyle> columnStyles = Collections.singletonList(TextStyle.DEFAULT);
        List<String> columnHeaders = Collections.singletonList("Header");
        TableDefinition tableDefinition = TableDefinition.from("Title", columnStyles, columnHeaders, new TreeSet<>());

        // Act: Call the method under test. The returned value is not needed for this assertion.
        textHelpAppendable.adjustTableFormat(tableDefinition);

        // Assert: Verify that the properties of the TextHelpAppendable instance remain unchanged
        // and are still set to their default values.
        assertEquals("Indent should remain at the default value",
                TextHelpAppendable.DEFAULT_INDENT, textHelpAppendable.getIndent());
        assertEquals("Max width should remain at the default value",
                TextHelpAppendable.DEFAULT_WIDTH, textHelpAppendable.getMaxWidth());
        assertEquals("Left pad should remain at the default value",
                TextHelpAppendable.DEFAULT_LEFT_PAD, textHelpAppendable.getLeftPad());
    }
}