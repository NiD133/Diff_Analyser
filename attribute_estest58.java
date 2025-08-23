package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Tests for the {@link Attribute} class.
 */
public class AttributeTest {

    /**
     * Verifies that getValidKey() returns null when the input key is an empty string.
     * An empty string is not a valid attribute key in either HTML or XML syntax.
     */
    @Test
    public void getValidKeyWithEmptyStringReturnsNull() {
        // Arrange: Set up the test inputs. The behavior should be consistent for any syntax.
        Document.OutputSettings.Syntax syntax = Document.OutputSettings.Syntax.xml;
        String emptyKey = "";

        // Act: Call the method under test with the empty key.
        String result = Attribute.getValidKey(emptyKey, syntax);

        // Assert: Verify that the result is null, as an empty key is invalid.
        assertNull("getValidKey should return null for an empty input string.", result);
    }
}