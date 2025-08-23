package org.jsoup.nodes;

import org.jsoup.internal.QuietAppendable;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the static Attribute.html() rendering method.
 */
public class Attribute_ESTestTest61 {

    /**
     * Tests that a boolean attribute, when rendered in XML syntax mode, is not collapsed
     * but is instead output with an empty value. For example, 'autofocus' becomes 'autofocus=""'.
     * This contrasts with HTML syntax, where it would be a collapsed name-only attribute.
     */
    @Test
    public void booleanAttributeInXmlModeIsRenderedWithValue() {
        // Arrange: Set up output settings for XML syntax.
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.syntax(Document.OutputSettings.Syntax.xml);

        // A StringBuilder will capture the output from the html() method.
        StringBuilder accumulator = new StringBuilder();
        // QuietAppendable is a jsoup internal class that wraps our StringBuilder.
        QuietAppendable quietAppendable = new QuietAppendable(accumulator);

        String booleanAttributeKey = "autofocus";

        // Act: Render the attribute using the static html() method.
        // We test with a null value, which is a common representation for boolean attributes.
        Attribute.html(booleanAttributeKey, null, quietAppendable, settings);

        // Assert: Verify the attribute is rendered as key="", with a leading space.
        // The leading space is intentional, as attributes are appended after an element name.
        assertEquals(" autofocus=\"\"", accumulator.toString());
    }
}