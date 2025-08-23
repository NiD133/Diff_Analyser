package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Attribute} class.
 * This refactored test focuses on the behavior of the setValue method.
 */
// The original test class name and scaffolding are kept for context,
// but a more descriptive name like `AttributeTest` would be preferable.
public class Attribute_ESTestTest69 extends Attribute_ESTest_scaffolding {

    /**
     * Verifies that {@link Attribute#setValue(String)} correctly updates the attribute's value
     * and returns the previous value.
     */
    @Test
    public void setValueShouldUpdateValueAndReturnOldValue() {
        // Arrange: Create an attribute with an initial key and value.
        // The parent Attributes object is not relevant to this test's logic, so it's set to null.
        String key = "id";
        String initialValue = "product-123";
        Attribute attribute = new Attribute(key, initialValue, null);

        // Act: Set a new value for the attribute.
        String newValue = "product-456";
        String returnedOldValue = attribute.setValue(newValue);

        // Assert: Check that the value was updated and the old value was returned.
        assertEquals("The attribute's value should be updated to the new value.", newValue, attribute.getValue());
        assertEquals("The method should return the previous value.", initialValue, returnedOldValue);
    }
}