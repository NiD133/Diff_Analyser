package org.jsoup.nodes;

import org.jsoup.internal.QuietAppendable;
import org.jsoup.nodes.Document.OutputSettings;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite focusing on the Entities.escape() method.
 * This provides a more descriptive class name than the original "Entities_ESTestTest32".
 */
public class EntitiesEscapeTest {

    /**
     * Verifies that a string with no special characters that require escaping is
     * written to the provided Appendable without any changes.
     */
    @Test
    public void escapeWritesStringWithNoSpecialCharsUnchanged() {
        // Arrange
        // Use a StringBuilder as a simple, in-memory Appendable. This is clearer
        // and more efficient than using a mock file and print stream.
        StringBuilder stringBuilder = new StringBuilder();
        QuietAppendable appendable = QuietAppendable.wrap(stringBuilder);

        String input = "A string with no special characters.";
        OutputSettings settings = new Document.OutputSettings();

        // The method under test takes an 'options' integer. Using a defined constant
        // makes the test's intent clear, unlike the original magic number '732'.
        int options = Entities.ForText;

        // Act
        Entities.escape(appendable, input, settings, options);

        // Assert
        // Asserting the final string content is more explicit and robust than
        // just checking the length. This ensures the output is exactly as expected.
        assertEquals(input, stringBuilder.toString());
    }
}