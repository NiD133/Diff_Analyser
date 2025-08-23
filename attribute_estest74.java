package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link Attribute} class.
 */
public class AttributeTest {

    /**
     * Verifies that updating an attribute's key does not affect its existing value.
     */
    @Test
    public void setKeyShouldUpdateKeyWithoutAffectingValue() {
        // Arrange: Create an attribute with an initial key and a non-null value.
        // The parent Attributes object is required by the constructor but is not the focus of this test.
        Attributes parentAttributes = new Attributes();
        Attribute attribute = new Attribute("initial-key", "some-value", parentAttributes);

        // Act: Change the attribute's key.
        String newKey = "updated-key";
        attribute.setKey(newKey);

        // Assert: Verify the key was updated and the original value remains unchanged.
        assertEquals("The key should be updated to the new value.", newKey, attribute.getKey());
        assertTrue("The attribute should still have a declared value.", attribute.hasDeclaredValue());
        assertEquals("The original value should not be affected by the key change.", "some-value", attribute.getValue());
    }
}