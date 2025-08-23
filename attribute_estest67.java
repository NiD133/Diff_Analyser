package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Attribute} class, focusing on key prefixing logic.
 */
public class AttributeTest {

    /**
     * Tests that prefix() correctly extracts the part of the key before the first colon.
     * This is a common use case for namespaced attributes like 'og:title', which is used
     * in the method's own documentation.
     */
    @Test
    public void prefixShouldExtractNameBeforeColon() {
        // Arrange: Create an attribute with a standard namespaced key.
        Attribute attribute = new Attribute("og:title", "The Rock");
        String expectedPrefix = "og";

        // Act: Get the prefix from the attribute.
        String actualPrefix = attribute.prefix();

        // Assert: The extracted prefix should match the part of the key before the colon.
        assertEquals(expectedPrefix, actualPrefix);
    }

    /**
     * Tests that prefix() returns an empty string if the attribute key does not contain a colon.
     */
    @Test
    public void prefixShouldBeEmptyWhenNoColonInKey() {
        // Arrange: Create an attribute with a simple key without a namespace.
        Attribute attribute = new Attribute("title", "The Rock");

        // Act: Get the prefix.
        String actualPrefix = attribute.prefix();

        // Assert: The prefix should be an empty string.
        assertEquals("", actualPrefix);
    }

    /**
     * This test preserves the logic of the original auto-generated test but uses a
     * descriptive name to clarify its purpose: ensuring the prefix logic handles
     * unconventional keys correctly.
     */
    @Test
    public void prefixShouldHandleUnconventionalKeysWithColon() {
        // Arrange: Use the original complex key to verify robustness.
        Attribute attribute = new Attribute("^-a-A-Z0-z_:.]+", "some value");
        String expectedPrefix = "^-a-A-Z0-z_";

        // Act: Get the prefix.
        String actualPrefix = attribute.prefix();

        // Assert: The logic should still split correctly at the first colon.
        assertEquals(expectedPrefix, actualPrefix);
    }
}