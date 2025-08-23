package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the static helper methods in the {@link Attribute} class.
 */
public class AttributeTest {

    /**
     * Verifies that an attribute with a null value is considered "collapsible"
     * when using HTML syntax. A collapsible attribute can be rendered without a
     * value (e.g., {@code <div my-attribute>}).
     */
    @Test
    public void attributeWithNullValueShouldCollapseInHtmlMode() {
        // Arrange: Create standard HTML output settings.
        // The default syntax is HTML, which allows for attribute collapsing.
        Document.OutputSettings settings = new Document.OutputSettings();
        String attributeKey = "custom-attribute"; // The key does not need to be a known boolean attribute for this case.

        // Act: Check if an attribute with a null value should be collapsed.
        boolean shouldCollapse = Attribute.shouldCollapseAttribute(attributeKey, null, settings);

        // Assert: In HTML mode, any attribute with a null value should be collapsible.
        assertTrue("Any attribute with a null value should be collapsible in HTML mode.", shouldCollapse);
    }
}