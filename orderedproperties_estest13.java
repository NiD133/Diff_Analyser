package org.apache.commons.collections4.properties;

import org.junit.Test;
import java.util.Map;

/**
 * Test suite for the {@link OrderedProperties} class.
 * This improved version focuses on clarity and standard testing practices.
 */
public class OrderedPropertiesTest {

    /**
     * Verifies that calling putAll() with a null map argument results in a NullPointerException.
     * This behavior is inherited from {@link java.util.Hashtable}.
     */
    @Test(expected = NullPointerException.class)
    public void putAll_withNullMap_shouldThrowNullPointerException() {
        // Arrange
        final OrderedProperties properties = new OrderedProperties();
        final Map<Object, Object> nullMap = null;

        // Act & Assert
        // The putAll method is called with a null map, which is expected to
        // throw a NullPointerException. The assertion is handled by the
        // @Test(expected) annotation.
        properties.putAll(nullMap);
    }
}