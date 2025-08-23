package org.apache.commons.cli.help;

import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

public class TextHelpAppendable_ESTestTest57 extends TextHelpAppendable_ESTest_scaffolding {

    /**
     * Tests that calling {@link TextHelpAppendable#appendTable(TableDefinition)} throws a
     * {@link NegativeArraySizeException} if the left padding has been set to a negative value.
     * This occurs because an internal utility attempts to create a character array of a negative
     * size for the padding.
     */
    @Test(expected = NegativeArraySizeException.class)
    public void testAppendTableWithNegativeLeftPaddingThrowsException() throws IOException {
        // Arrange: Create a TextHelpAppendable and set its left padding to a negative value.
        TextHelpAppendable helpAppendable = TextHelpAppendable.systemOut();
        helpAppendable.setLeftPad(-1);

        // An empty table definition is sufficient to trigger the code path under test.
        TableDefinition emptyTable = TableDefinition.from(
                "Header",
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptySet());

        // Act: Attempt to append the table. This should fail when trying to create the negative padding.
        helpAppendable.appendTable(emptyTable);

        // Assert: The NegativeArraySizeException is expected, as declared in the @Test annotation.
    }
}