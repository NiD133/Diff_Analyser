package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Attribute#localName()} method.
 */
public class AttributeLocalNameTest {

    /**
     * Verifies that localName() returns the full attribute key
     * when the key does not contain a namespace prefix.
     */
    @Test
    public void localNameShouldReturnFullKeyWhenKeyHasNoPrefix() {
        // Arrange: Create an attribute with a key that has no prefix (no colon).
        // The key contains special characters to ensure they are handled correctly.
        String keyWithoutPrefix = "XII YZ}5!";
        String value = "async";
        Attribute attribute = Attribute.createFromEncoded(keyWithoutPrefix, value);

        // Act: Get the local name from the attribute.
        String localName = attribute.localName();

        // Assert: The local name should be identical to the original key.
        assertEquals("The local name should be the full key when no prefix is present.", keyWithoutPrefix, localName);
        
        // Also, confirm the value was set correctly as a basic sanity check.
        assertEquals("The attribute value should be correctly preserved.", value, attribute.getValue());
    }
}