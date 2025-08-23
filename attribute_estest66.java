package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This class contains tests for the {@link Attribute} class.
 * This specific test was refactored for clarity from an auto-generated test case.
 */
public class Attribute_ESTestTest66 extends Attribute_ESTest_scaffolding {

    /**
     * Tests that the localName() method correctly returns an empty string for an attribute key that ends with a colon.
     * A key like "prefix:" implies a namespace prefix is present, but the local name part is empty.
     */
    @Test
    public void localNameShouldBeEmptyWhenKeyEndsWithColon() {
        // Arrange: Create an attribute with a key that has a prefix but an empty local name (e.g., "ns:").
        // The original test used a complex key "Rr7w\u0000k7A:", which obscured this intent.
        Attribute attribute = new Attribute("ns:", "some-value");

        // Act: Get the local name from the attribute.
        String localName = attribute.localName();

        // Assert: The local name should be an empty string, as it's the part after the colon.
        assertEquals("The local name should be empty for a key ending with a colon.", "", localName);
    }
}