package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Attribute} class.
 */
public class AttributeTest {

    @Test
    public void getValueShouldReturnEmptyStringForAttributeWithNullValue() {
        // Arrange: Create an attribute with a valid key and a null value.
        // The constructor should handle the null value gracefully.
        Attribute attribute = new Attribute("id", null);

        // Act: Retrieve the value from the attribute.
        String actualValue = attribute.getValue();

        // Assert: The getValue() method is expected to return an empty string, not null,
        // to prevent NullPointerExceptions in consuming code.
        assertEquals("", actualValue);
    }
}