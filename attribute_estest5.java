package org.jsoup.nodes;

import org.jsoup.internal.QuietAppendable;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the static helper methods in the {@link Attribute} class.
 */
public class AttributeStaticHelpersTest {

    /**
     * Verifies that Attribute.htmlNoValidate() gracefully handles a null attribute key
     * by not writing any output. This prevents potential NullPointerExceptions and ensures
     * robust behavior with invalid input.
     */
    @Test
    public void htmlNoValidateWithNullKeyWritesNothing() {
        // Arrange: Create a simple StringBuilder to capture the output.
        final StringBuilder accumulator = new StringBuilder();
        final QuietAppendable quietAppendable = new QuietAppendable(accumulator);
        final Document.OutputSettings settings = new Document.OutputSettings();

        // Act: Call the method with a null key. The value is irrelevant in this case.
        Attribute.htmlNoValidate(null, "any-value", quietAppendable, settings);

        // Assert: Verify that nothing was appended to the StringBuilder.
        assertEquals("Output should be empty when the attribute key is null.", "", accumulator.toString());
    }
}