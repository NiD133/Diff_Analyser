package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test class focuses on the behavior of the Attribute class.
 * The original test was auto-generated; this version has been refactored for clarity.
 */
public class Attribute_ESTestTest14 extends Attribute_ESTest_scaffolding {

    /**
     * Tests that cloning an Attribute created with a null value results in a new Attribute
     * whose value is an empty string. This aligns with the contract of Attribute#getValue(),
     * which returns an empty string for an internally null value.
     */
    @Test
    public void cloneOfAttributeWithNullValueShouldHaveEmptyStringValue() {
        // Arrange: Create an attribute with a key and a null value.
        // This is a valid state, often used for boolean attributes like 'disabled'.
        Attribute originalAttribute = new Attribute("disabled", null);

        // Act: Clone the attribute.
        Attribute clonedAttribute = originalAttribute.clone();

        // Assert: The cloned attribute's value should be an empty string, not null.
        assertEquals("", clonedAttribute.getValue());
    }
}