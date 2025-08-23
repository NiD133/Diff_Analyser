package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * This test class contains tests for the {@link Attribute} class.
 * This specific test case focuses on the behavior of attribute collapsing.
 */
public class Attribute_ESTestTest76 { // Retaining original class name for context

    /**
     * Verifies that an attribute is not collapsed if it is not a standard HTML boolean attribute.
     * An attribute should only be collapsed (e.g., rendered as just 'disabled' instead of 'disabled=""')
     * if it is a known boolean attribute like 'checked', 'disabled', 'selected', etc.
     */
    @Test
    public void shouldNotCollapseWhenAttributeIsNotBoolean() {
        // Arrange
        // Create a custom attribute that is not a standard boolean attribute.
        // The value is set to match the key, which is a condition that would cause
        // a *boolean* attribute to be collapsed.
        String attributeName = "data-custom";
        Attribute attribute = new Attribute(attributeName, attributeName);
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        // Act
        // The shouldCollapseAttribute method is deprecated, but we are testing its logic.
        @SuppressWarnings("deprecation")
        boolean shouldCollapse = attribute.shouldCollapseAttribute(outputSettings);

        // Assert
        assertFalse("A non-boolean attribute should not be collapsed, even if its value matches its key.", shouldCollapse);
    }
}