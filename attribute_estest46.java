package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for the {@link Attribute#isDataAttribute(String)} method.
 */
public class AttributeTest {

    /**
     * Verifies that isDataAttribute() returns false for a key that consists
     * only of the "data-" prefix. A valid data attribute requires a name
     * following the prefix (e.g., "data-foo"). This test checks that boundary case.
     */
    @Test
    public void isDataAttributeShouldReturnFalseForPrefixOnlyKey() {
        // Arrange: Define a key that is exactly the data attribute prefix.
        String keyWithPrefixOnly = "data-";

        // Act: Call the method under test.
        boolean result = Attribute.isDataAttribute(keyWithPrefixOnly);

        // Assert: The method should return false, as the key has no name part.
        assertFalse("A key consisting only of 'data-' should not be a valid data attribute.", result);
    }
}