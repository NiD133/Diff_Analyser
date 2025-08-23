package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the behavior of an Attribute when its value is set to null.
 */
public class AttributeValueTest {

    @Test
    public void settingValueToNullMeansAttributeHasNoDeclaredValue() {
        // Arrange: Create an attribute that initially has a value.
        Attribute attribute = new Attribute("id", "initial-value");
        assertTrue("Precondition failed: Attribute should have a declared value upon creation.", attribute.hasDeclaredValue());

        // Act: Set the attribute's value to null.
        attribute.setValue(null);

        // Assert: The attribute should no longer have a declared value, and its
        // getValue() method should return an empty string.
        assertFalse("hasDeclaredValue() should return false after value is set to null.", attribute.hasDeclaredValue());
        assertEquals("getValue() should return an empty string when the value is null.", "", attribute.getValue());
    }
}