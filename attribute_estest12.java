package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Attribute} class.
 * Note: The original test class name "Attribute_ESTestTest12" and its base class
 * are artifacts of a test generation tool. A more conventional name is used here.
 */
public class AttributeTest {

    /**
     * Verifies that getValue() returns an empty string for an attribute initialized with a null value.
     * This is the defined behavior for attributes that are present but have no assigned value,
     * such as boolean attributes in HTML.
     */
    @Test
    public void getValueShouldReturnEmptyStringWhenAttributeValueIsNull() {
        // Arrange: Create an attribute with a key but a null value.
        // Using a common boolean attribute name like "disabled" makes the test case more realistic.
        Attribute attributeWithNullValue = new Attribute("disabled", null);

        // Act: Call the method under test.
        String actualValue = attributeWithNullValue.getValue();

        // Assert: The returned value should be an empty string, not null.
        assertEquals("", actualValue);
    }
}