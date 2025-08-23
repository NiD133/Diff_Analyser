package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Attribute} class, focusing on key prefixing.
 */
public class AttributeTest {

    /**
     * Verifies that calling {@link Attribute#prefix()} on an attribute without a
     * namespace prefix in its key returns an empty string.
     */
    @Test
    public void prefixReturnsEmptyStringWhenNoPrefixExists() {
        // Arrange: Create an attribute with a standard key like "id", which has no prefix.
        Attribute attribute = new Attribute("id", "main-content");

        // Act: Get the prefix from the attribute.
        String prefix = attribute.prefix();

        // Assert: The result should be an empty string.
        assertEquals("", prefix);
    }

    /**
     * Verifies that calling {@link Attribute#prefix()} on an attribute with a
     * namespace prefix (e.g., "og:title") correctly extracts the prefix part.
     * This test is added for completeness and to provide context for the test above.
     */
    @Test
    public void prefixReturnsCorrectlyWhenPrefixExists() {
        // Arrange: Create an attribute with a namespaced key.
        Attribute attribute = new Attribute("og:title", "A Great Title");

        // Act: Get the prefix from the attribute.
        String prefix = attribute.prefix();

        // Assert: The result should be the namespace prefix, "og".
        assertEquals("og", prefix);
    }
}