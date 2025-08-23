package org.apache.commons.cli.help;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collection;
import org.junit.Test;

/**
 * Tests for {@link TextHelpAppendable}.
 *
 * Note: The original test class name "TextHelpAppendable_ESTestTest83" and its
 * inheritance from scaffolding classes were artifacts of a test generation tool
 * and have been simplified for clarity.
 */
public class TextHelpAppendableTest {

    /**
     * Tests that calling appendList with a null collection is handled gracefully.
     * It should not throw a NullPointerException and should not alter the
     * appendable's configuration.
     */
    @Test
    public void appendListWithNullCollectionShouldBeANoOp() throws IOException {
        // Arrange: Create an instance of TextHelpAppendable.
        // The test verifies internal state, so the actual output target (System.out)
        // is not relevant here.
        final TextHelpAppendable helpAppendable = TextHelpAppendable.systemOut();

        // Act: Call the method under test with a null collection.
        // The primary check is that this does not throw an exception.
        helpAppendable.appendList(false, (Collection<CharSequence>) null);

        // Assert: Verify that the state of the object remains unchanged and
        // still holds its default configuration values.
        assertEquals("Max width should remain at default", TextHelpAppendable.DEFAULT_WIDTH, helpAppendable.getMaxWidth());
        assertEquals("Left pad should remain at default", TextHelpAppendable.DEFAULT_LEFT_PAD, helpAppendable.getLeftPad());
        assertEquals("Indent should remain at default", TextHelpAppendable.DEFAULT_INDENT, helpAppendable.getIndent());
    }
}