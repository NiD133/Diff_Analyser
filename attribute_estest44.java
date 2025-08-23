package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Attribute} class, focusing on its string representation.
 */
public class AttributeTest {

    /**
     * Verifies that an Attribute with an empty string value is correctly
     * formatted by the toString() method. The expected output is {@code key=""}.
     */
    @Test
    public void toStringForAttributeWithEmptyValueShouldBeFormattedAsKeyEqualsEmptyString() {
        // Arrange
        Attribute attribute = new Attribute("nyc_q_", "");
        String expectedHtml = "nyc_q_=\"\"";

        // Act
        String actualHtml = attribute.toString();

        // Assert
        assertEquals(expectedHtml, actualHtml);
    }
}