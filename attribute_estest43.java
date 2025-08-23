package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for the {@link Attribute} class.
 */
public class AttributeTest {

    @Test
    public void equalsShouldReturnFalseForNullComparison() {
        // Arrange: Create a standard attribute instance.
        Attribute attribute = new Attribute("key", "value");

        // Act & Assert: The equals method must return false when the object is compared with null.
        // This is a standard requirement for any .equals() implementation.
        assertFalse("An attribute instance should never be equal to null.", attribute.equals(null));
    }
}