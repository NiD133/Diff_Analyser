package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link Attribute} class.
 */
public class AttributeTest {

    /**
     * Verifies that isDataAttribute() returns false for an attribute key
     * that does not begin with the "data-" prefix.
     */
    @Test
    public void isDataAttributeShouldReturnFalseForNonDataAttributeKey() {
        // Arrange: Create an attribute with a key that is not a data attribute.
        // The key "XII YZ}5!" is unusual but valid for this test as it does not start with "data-".
        String key = "XII YZ}5!";
        String value = "async";
        Attribute attribute = Attribute.createFromEncoded(key, value);

        // Act: Check if the attribute is a data attribute.
        boolean isDataAttribute = attribute.isDataAttribute();

        // Assert: The result should be false, and the attribute's properties should be correct.
        assertFalse("Attribute key does not start with 'data-', so it should not be a data attribute.", isDataAttribute);
        
        // Also verify the attribute was constructed correctly.
        assertEquals("The key should be preserved.", key, attribute.getKey());
        assertEquals("The value should be preserved.", value, attribute.getValue());
    }
}