package org.jsoup.nodes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link Attribute} class, focusing on namespace handling.
 */
public class AttributeTest {

    /**
     * Verifies that an attribute created without a namespace prefix returns an empty string for its namespace.
     * An attribute's namespace is only determined by a prefix (e.g., "xlink:href") when parsed within
     * an element that defines that namespace. A standalone attribute has no such context.
     */
    @Test
    void namespaceShouldBeEmptyForAttributeWithoutPrefix() {
        // Arrange: Create a standard attribute with a key that has no namespace prefix.
        Attribute attribute = new Attribute("key", "value");

        // Act: Retrieve the namespace from the attribute.
        String namespace = attribute.namespace();

        // Assert: The namespace should be empty.
        assertEquals("", namespace, "A standalone attribute without a prefix should have an empty namespace.");
    }
}