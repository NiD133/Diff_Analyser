package org.apache.commons.collections4.properties;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link OrderedProperties} class.
 */
public class OrderedPropertiesTest {

    /**
     * Tests that the toString() method on a new, empty OrderedProperties instance
     * returns the string representation of an empty map, "{}".
     */
    @Test
    public void toString_onEmptyProperties_returnsEmptyBraces() {
        // Arrange: Create a new, empty OrderedProperties instance.
        final OrderedProperties properties = new OrderedProperties();
        final String expectedString = "{}";

        // Act: Call the toString() method.
        final String actualString = properties.toString();

        // Assert: Verify the result is the expected empty map representation.
        assertEquals(expectedString, actualString);
    }
}