package org.apache.commons.collections4.properties;

import org.junit.Test;
import static org.junit.Assert.assertThrows;

/**
 * Unit tests for {@link OrderedProperties}.
 * This test focuses on the exception-handling behavior of the remove() method.
 */
public class OrderedPropertiesTest {

    /**
     * Tests that calling remove() with a null key throws a NullPointerException.
     * This is the expected behavior for map-like collections that do not permit null keys,
     * as specified by the contract of java.util.Hashtable, the superclass of Properties.
     */
    @Test
    public void removeWithNullKeyShouldThrowNullPointerException() {
        // Arrange: Create an empty OrderedProperties instance.
        final OrderedProperties orderedProperties = new OrderedProperties();

        // Act & Assert: Verify that calling remove(null) throws a NullPointerException.
        // The lambda expression concisely captures the action that is expected to fail.
        assertThrows(NullPointerException.class, () -> orderedProperties.remove(null));
    }
}