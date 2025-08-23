package org.apache.commons.collections4.properties;

import org.junit.Test;

/**
 * Unit tests for {@link OrderedProperties}.
 * This test focuses on the behavior of the remove(key, value) method.
 */
public class OrderedPropertiesTest {

    /**
     * Tests that calling remove(key, value) with a null key throws a NullPointerException.
     * This behavior is inherited from {@link java.util.Hashtable}, the superclass of
     * {@link java.util.Properties}, which does not permit null keys.
     */
    @Test(expected = NullPointerException.class)
    public void removeWithNullKeyShouldThrowNullPointerException() {
        // Given: An empty OrderedProperties instance
        final OrderedProperties properties = new OrderedProperties();

        // When: remove() is called with a null key
        // Then: A NullPointerException is thrown (as declared by the @Test annotation)
        properties.remove(null, "anyValue");
    }
}