package org.apache.commons.cli.help;

import org.apache.commons.cli.help.TableDefinition;
import org.apache.commons.cli.help.TextHelpAppendable;
import org.apache.commons.cli.help.TextStyle;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Collections;
import java.util.List;

/**
 * Test suite for {@link TextHelpAppendable}.
 */
public class TextHelpAppendableTest {

    /**
     * Tests that adjustTableFormat throws an exception when the number of headers
     * is less than the number of styles, leading to an out-of-bounds access attempt.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testAdjustTableFormatWithMismatchedHeadersAndStylesThrowsException() {
        // Arrange: Create a TextHelpAppendable and a TableDefinition with a mismatch
        // between the number of styles (1) and the number of headers (0).
        TextHelpAppendable helpAppendable = new TextHelpAppendable(new StringWriter());

        List<TextStyle> styles = Collections.singletonList(TextStyle.DEFAULT);
        List<String> emptyHeaders = Collections.emptyList();

        TableDefinition tableDefinition = TableDefinition.from("Test Table", styles, emptyHeaders, Collections.emptySet());

        // Act: Call the method under test. This is expected to throw an exception
        // because it will likely try to access a header that does not exist.
        helpAppendable.adjustTableFormat(tableDefinition);

        // Assert: The test will pass if an ArrayIndexOutOfBoundsException is thrown,
        // as declared by the @Test(expected=...) annotation.
    }
}