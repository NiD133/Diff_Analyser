package org.jsoup.nodes;

import org.jsoup.internal.QuietAppendable;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Attribute_ESTestTest6 extends Attribute_ESTest_scaffolding {

    /**
     * Tests that the internal `html(Appendable, OutputSettings)` method correctly formats an attribute
     * (as ' key="value"') and appends it to the given Appendable.
     */
    @Test
    public void htmlMethodAppendsFormattedAttribute() {
        // Arrange
        // Use a standard HTML attribute and value for better readability.
        Attribute attribute = new Attribute("class", "container");
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        // The html() method should append to existing content.
        StringBuilder stringBuilder = new StringBuilder("<div");
        QuietAppendable appendable = QuietAppendable.wrap(stringBuilder);

        // The expected format includes a leading space before the attribute.
        String expectedHtml = "<div class=\"container\"";

        // Act
        // The method under test writes the attribute's HTML representation to the appendable.
        attribute.html(appendable, outputSettings);

        // Assert
        assertEquals(expectedHtml, stringBuilder.toString());
    }
}