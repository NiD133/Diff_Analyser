package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Attribute} class.
 * This improved test focuses on the behavior of the setKey() method.
 */
public class AttributeTest {

    /**
     * Verifies that calling setKey() on an Attribute updates its key
     * while leaving its value unchanged.
     *
     * The original test was confusing because it used the same string ("async")
     * for the new key and the original value, making it unclear whether setKey
     * also modified the value. This test uses distinct strings to clarify the behavior.
     */
    @Test
    public void setKeyShouldUpdateKeyWhileValueRemainsUnchanged() {
        // Arrange: Create an attribute with a distinct initial key and value.
        String initialKey = "initial-key";
        String value = "attribute-value";
        Attribute attribute = new Attribute(initialKey, value);

        // Pre-assertion to ensure the initial state is correct.
        assertEquals("The initial key should be set correctly.", initialKey, attribute.getKey());
        assertEquals("The initial value should be set correctly.", value, attribute.getValue());

        // Act: Change the key of the attribute.
        String newKey = "new-key";
        attribute.setKey(newKey);

        // Assert: Verify the key is updated and the value is preserved.
        assertEquals("The key should be updated to the new value.", newKey, attribute.getKey());
        assertEquals("The value should remain unchanged after setting a new key.", value, attribute.getValue());
    }
}