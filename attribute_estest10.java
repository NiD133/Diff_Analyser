package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Attribute} class.
 */
public class AttributeTest {

    /**
     * Verifies that an attribute's value can be updated to an empty string.
     * This test ensures that after calling setValue(""), the getValue() method
     * correctly returns the new empty string value.
     */
    @Test
    public void settingValueToEmptyStringIsReflectedInGetValue() {
        // Arrange: Create an attribute with an initial non-empty value.
        Attribute attribute = new Attribute("id", "initial-value");

        // Act: Set the attribute's value to an empty string.
        attribute.setValue("");

        // Assert: Verify that the new value is indeed an empty string.
        assertEquals("", attribute.getValue());
    }
}