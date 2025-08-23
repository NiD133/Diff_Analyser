package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link XmlDeclaration} class, focusing on its behavior as a LeafNode.
 */
public class XmlDeclarationTest {

    /**
     * Verifies that the coreValue() method returns the name of the XML declaration.
     * This test specifically checks the case where the name is an empty string.
     */
    @Test
    public void coreValueShouldReturnDeclarationName() {
        // Arrange: The "core value" of an XmlDeclaration is its name.
        // We create a declaration with an empty name to test this behavior.
        XmlDeclaration declaration = new XmlDeclaration("", false);

        // Act: Retrieve the core value.
        String coreValue = declaration.coreValue();

        // Assert: The returned value should be the same empty string used for the name.
        assertEquals("The core value should match the name provided at construction.", "", coreValue);
    }
}