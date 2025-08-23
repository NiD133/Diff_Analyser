package org.jsoup.nodes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for {@link Attribute}.
 */
public class AttributeTest {

    @Test
    void attributeShouldBeMutableAfterDetachmentFromParent() {
        // This test verifies that an Attribute object, once removed from its parent
        // Attributes collection, can still have its key and value modified independently.

        // Arrange: Create an attribute within a parent Attributes collection.
        Attributes parentAttributes = new Attributes();
        parentAttributes.put("originalKey", "originalValue");
        Attribute attribute = parentAttributes.attribute("originalKey");
        assertNotNull(attribute, "Attribute should exist before being detached.");

        // Act: Remove the attribute from the parent, effectively detaching it.
        parentAttributes.remove("originalKey");

        // Assert: The attribute is detached but initially retains its state.
        assertFalse(parentAttributes.hasKey("originalKey"), "Attribute should no longer exist in the parent collection.");
        assertEquals("originalKey", attribute.getKey(), "Detached attribute should retain its original key.");
        assertEquals("originalValue", attribute.getValue(), "Detached attribute should retain its original value.");

        // Act: Modify the now-detached attribute.
        attribute.setKey("newKey");
        attribute.setValue("newValue");

        // Assert: The detached attribute reflects the new key and value.
        assertEquals("newKey", attribute.getKey(), "Key of the detached attribute should be updatable.");
        assertEquals("newValue", attribute.getValue(), "Value of the detached attribute should be updatable.");
    }
}