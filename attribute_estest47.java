package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link Attribute} class.
 */
public class AttributeTest {

    @Test
    public void shouldCorrectlyIdentifyDataAttribute() {
        // Arrange: An attribute is considered a "data attribute" if its key starts
        // with the "data-" prefix. We create a representative example.
        String dataKey = "data-user-id";
        String value = "12345";
        Attribute attribute = new Attribute(dataKey, value);

        // Act: Check if the attribute is recognized as a data attribute.
        boolean isDataAttribute = attribute.isDataAttribute();

        // Assert: The method should return true, and the attribute's properties
        // should remain unchanged.
        assertTrue("Attribute with 'data-' prefix should be identified as a data attribute.", isDataAttribute);
        assertEquals("The attribute's value should not be altered by the check.", value, attribute.getValue());
    }
}