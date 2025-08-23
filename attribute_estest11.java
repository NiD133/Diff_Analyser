package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link Attribute} class.
 */
public class AttributeTest {

    /**
     * Verifies that {@link Attribute#isDataAttribute(String)} correctly identifies
     * an attribute key that starts with the "data-" prefix.
     */
    @Test
    public void isDataAttributeReturnsTrueForAttributeStartingWithDataPrefix() {
        // Arrange: A key that represents a valid data attribute.
        // The only requirement is that the key starts with "data-" and has content after the prefix.
        String validDataAttributeKey = "data-data@X-";

        // Act: Call the method under test.
        boolean isDataAttribute = Attribute.isDataAttribute(validDataAttributeKey);

        // Assert: The method should return true.
        assertTrue(
            "Keys starting with 'data-' should be identified as data attributes.",
            isDataAttribute
        );
    }
}