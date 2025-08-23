package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link Attribute} class.
 * Note: The original test class name "Attribute_ESTestTest8" was auto-generated.
 * A more conventional name like "AttributeTest" is recommended.
 */
public class AttributeTest {

    @Test
    public void booleanAttributeWithValueEqualToKeyShouldCollapseInHtml() {
        // Arrange
        // "ismap" is a standard HTML boolean attribute.
        // We initialize it with a dummy value that will be overwritten.
        Attribute attribute = new Attribute("ismap", "initial-value");

        // The key condition for this test: set the attribute's value to be the same as its key.
        attribute.setValue("ismap");

        // Use default output settings, which specifies HTML syntax.
        Document.OutputSettings settings = new Document.OutputSettings();

        // Act
        boolean shouldCollapse = attribute.shouldCollapseAttribute(settings);

        // Assert
        assertTrue(
            "A boolean attribute should be collapsible when its value equals its key in HTML mode.",
            shouldCollapse
        );
    }
}