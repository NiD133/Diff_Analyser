package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the static helper methods in the {@link Attribute} class.
 */
public class AttributeTest {

    /**
     * Verifies that attributes are never collapsed when the document syntax is set to XML.
     * Attribute collapsing (e.g., rendering `<input disabled>` instead of `<input disabled="">`)
     * is a feature specific to HTML syntax.
     */
    @Test
    public void shouldCollapseAttributeReturnsFalseWhenSyntaxIsXml() {
        // Arrange: Configure output settings for XML syntax.
        // The specific attribute key and value are not relevant for this test,
        // as the XML syntax setting is the deciding factor.
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.xml);

        String attributeKey = "some-attribute";
        String attributeValue = "some-value";

        // Act: Call the method under test.
        boolean shouldCollapse = Attribute.shouldCollapseAttribute(attributeKey, attributeValue, outputSettings);

        // Assert: The result must be false because the syntax is XML.
        assertFalse("Attributes should not be collapsed in XML mode.", shouldCollapse);
    }
}