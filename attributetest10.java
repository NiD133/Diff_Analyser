package org.jsoup.nodes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link Attribute} class, focusing on value manipulation.
 */
public class AttributeTest {

    @Test
    public void setValueShouldCorrectlyHandleNullAndReturnPreviousValue() {
        // Arrange: Create an attribute with an initial key and value.
        Attribute attribute = new Attribute("key", "initialValue");

        // Act: Set the attribute's value to null.
        String previousValue = attribute.setValue(null);

        // Assert: The original value is returned, and the attribute now renders as a boolean attribute (key only).
        assertEquals("initialValue", previousValue);
        assertEquals("key", attribute.html(), "An attribute with a null value should render as a boolean attribute (key only).");

        // Act: Set a new string value, replacing the null.
        String previousValueFromNull = attribute.setValue("newValue");

        // Assert: The previous value (which was null) is returned as an empty string, as per the method's contract.
        assertEquals("", previousValueFromNull, "When the previous value was null, setValue should return an empty string.");
        assertEquals("key=\"newValue\"", attribute.html(), "The attribute should now render with its new value.");
    }
}