package org.jsoup.nodes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link Attribute} class, focusing on attributes that are "orphaned"
 * (i.e., not attached to an element's Attributes collection).
 */
public class AttributeTest {

    @Test
    void setKeyOnOrphanAttributeShouldUpdateTheKey() {
        // Arrange: Create an orphan attribute with an initial key and value.
        Attribute attribute = new Attribute("initialKey", "value");

        // Act: Update the key.
        attribute.setKey("newKey");

        // Assert: Verify the key was updated and other properties remain unchanged.
        assertEquals("newKey", attribute.getKey(), "The key should be updated to the new value.");
        assertEquals("value", attribute.getValue(), "The value should not be affected by setting the key.");
        assertNull(attribute.parent, "The attribute should remain an orphan with a null parent.");
    }

    @Test
    void setValueOnOrphanAttributeShouldUpdateValueAndReturnOldValue() {
        // Arrange: Create an orphan attribute with an initial key and value.
        Attribute attribute = new Attribute("key", "initialValue");

        // Act: Update the value and capture the returned previous value.
        String previousValue = attribute.setValue("newValue");

        // Assert: Verify the value was updated, the correct old value was returned,
        // and other properties remain unchanged.
        assertEquals("initialValue", previousValue, "The previous value should be returned.");
        assertEquals("newValue", attribute.getValue(), "The value should be updated to the new value.");
        assertEquals("key", attribute.getKey(), "The key should not be affected by setting the value.");
        assertNull(attribute.parent, "The attribute should remain an orphan with a null parent.");
    }
}