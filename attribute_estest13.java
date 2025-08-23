package org.jsoup.nodes;

import org.jsoup.nodes.Document.OutputSettings.Syntax;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the static utility methods in the {@link Attribute} class.
 */
public class AttributeTest {

    /**
     * Verifies that getValidKey returns an empty string when the input key is empty,
     * regardless of the specified syntax.
     */
    @Test
    public void getValidKeyForEmptyInputReturnsEmptyString() {
        // Arrange: An empty input key. The syntax is null to confirm the method handles it,
        // but the primary check is for the empty key behavior.
        String emptyKey = "";
        Syntax nullSyntax = null;

        // Act: Call the method under test.
        String result = Attribute.getValidKey(emptyKey, nullSyntax);

        // Assert: The method should return an empty string for an empty key.
        assertEquals("", result);
    }
}