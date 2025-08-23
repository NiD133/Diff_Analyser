package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Attribute} class.
 */
// The original class name `Attribute_ESTestTest42` was tool-generated.
// A simpler name like `AttributeTest` is more conventional.
public class AttributeTest {

    /**
     * Tests the reflexive property of the equals method, ensuring an attribute is equal to itself.
     */
    @Test
    public void attributeIsEqualToItself() {
        // Arrange: Create an attribute instance with a specific key and value.
        final String key = "_Tr_2_";
        final String value = "ope3n";
        Attribute attribute = Attribute.createFromEncoded(key, value);

        // Act & Assert: An object must be equal to itself.
        // Using assertEquals clearly states the intent and provides a better failure message
        // than assertTrue(attribute.equals(attribute)).
        assertEquals(attribute, attribute);

        // Also, verify the state of the created attribute to ensure the test setup is correct.
        assertEquals(key, attribute.getKey());
        assertEquals(value, attribute.getValue());
    }
}