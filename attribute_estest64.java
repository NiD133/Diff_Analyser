package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Attribute#namespace()} method.
 */
public class AttributeNamespaceTest {

    @Test
    public void namespaceShouldBeEmptyForAttributeWithoutPrefix() {
        // Arrange: Create an attribute with a key that does not have a namespace prefix.
        // The parent of the attribute is null when created via this static factory method.
        Attribute attribute = Attribute.createFromEncoded("_Tr_2_", "ope3n");

        // Act: Retrieve the namespace from the attribute.
        String namespace = attribute.namespace();

        // Assert: The namespace should be an empty string because the key has no prefix.
        // We also verify that the key and value were correctly initialized.
        assertEquals("", namespace);
        assertEquals("_Tr_2_", attribute.getKey());
        assertEquals("ope3n", attribute.getValue());
    }
}