package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class focuses on the behavior of the ConverterSet.remove() method.
 * The original test was auto-generated; this version has been refactored for
 * better human readability and maintainability.
 */
public class ConverterSet_ESTestTest4 {

    /**
     * Tests that removing the only converter from a set results in an empty set.
     * It also verifies that the returned set is a new instance (due to immutability)
     * and that the removed converter is correctly reported.
     */
    @Test
    public void remove_whenLastConverterIsRemoved_shouldReturnEmptySet() {
        // Arrange: Create a ConverterSet containing a single StringConverter.
        Converter converterToRemove = StringConverter.INSTANCE;
        ConverterSet initialSet = new ConverterSet(new Converter[]{converterToRemove});
        assertEquals("Precondition: The initial set must contain one converter.", 1, initialSet.size());

        // The 'remove' method can optionally report which converter was removed
        // by placing it in the first element of a provided array.
        Converter[] removedConverterHolder = new Converter[1];

        // Act: Remove the single converter from the set.
        ConverterSet resultSet = initialSet.remove(converterToRemove, removedConverterHolder);

        // Assert: The resulting set should be a new, empty set.
        assertNotNull("The resulting set should not be null.", resultSet);
        assertNotSame("Because ConverterSet is immutable, a new instance should be returned.", initialSet, resultSet);
        assertEquals("The resulting set should be empty.", 0, resultSet.size());

        // Assert: The correct converter was reported as removed.
        assertSame("The removed converter should be the one we specified.",
                     converterToRemove, removedConverterHolder[0]);
    }
}