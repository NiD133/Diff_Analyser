package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link Attribute} class.
 */
public class AttributeTest {

    @Test
    public void equalsReturnsFalseForClonedAttributeWithModifiedValue() {
        // Arrange: Create an attribute and a clone of it.
        Attribute originalAttribute = new Attribute("key", "value");
        Attribute clonedAttribute = originalAttribute.clone();

        // Sanity check: ensure the clone is equal to the original initially.
        assertEquals(originalAttribute, clonedAttribute);
        
        // Act: Modify the value of the original attribute.
        originalAttribute.setValue("newValue");

        // Assert: The original and the clone should no longer be equal.
        assertNotEquals("The modified original attribute should not be equal to its unmodified clone.",
            originalAttribute, clonedAttribute);

        // Also, verify the value was indeed changed on the original attribute.
        assertEquals("newValue", originalAttribute.getValue());
        // And that the clone's value remained unchanged.
        assertEquals("value", clonedAttribute.getValue());
    }
}