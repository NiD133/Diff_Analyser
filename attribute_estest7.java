package org.jsoup.nodes;

import org.jsoup.nodes.Document.OutputSettings;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the static html generation methods in the {@link Attribute} class.
 */
// The original test class name "Attribute_ESTestTest7" is an artifact from a test generation tool.
// A more conventional name would be AttributeTest. We retain the original name for this refactoring exercise.
public class Attribute_ESTestTest7 extends Attribute_ESTest_scaffolding {

    /**
     * Verifies that the static Attribute.html() method correctly sanitizes an attribute key
     * containing invalid characters (like a double quote) by replacing them with an underscore.
     */
    @Test
    public void htmlMethodReplacesInvalidCharacterInKey() throws IOException {
        // Arrange
        // According to the HTML specification, attribute names cannot contain double quotes.
        // This test uses a key with a leading double quote to verify sanitization.
        String invalidKey = "\"4}zG";
        String value = "_";

        StringBuilder outputBuilder = new StringBuilder();
        // Use default output settings, which correspond to HTML syntax.
        OutputSettings settings = new OutputSettings();

        // Act
        // Generate the HTML for the attribute. The method is expected to handle the invalid key.
        Attribute.html(invalidKey, value, outputBuilder, settings);

        // Assert
        // The invalid double quote in the key should be replaced with an underscore,
        // and the final output should be a correctly formatted HTML attribute string.
        String expectedHtml = "_4}zG=\"_\"";
        assertEquals(expectedHtml, outputBuilder.toString());
    }
}