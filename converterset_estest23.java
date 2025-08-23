package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link ConverterSet} class.
 */
public class ConverterSetTest {

    /**
     * Tests that removing an existing converter from a ConverterSet returns a new,
     * modified set instance, confirming the class's immutability.
     */
    @Test
    public void remove_whenConverterExists_shouldReturnNewSetInstance() {
        // Arrange: Create a ConverterSet containing a specific converter and a null element.
        // This setup mirrors the original test's structure.
        Converter converterToRemove = ReadablePartialConverter.INSTANCE;
        Converter[] initialConverters = {null, converterToRemove};
        ConverterSet initialSet = new ConverterSet(initialConverters);

        // The 'remove' method can optionally return the removed converter in this array.
        Converter[] removedConverterContainer = new Converter[1];

        // Act: Remove the specific converter from the set.
        ConverterSet resultSet = initialSet.remove(converterToRemove, removedConverterContainer);

        // Assert: Verify the results of the remove operation.

        // 1. A new instance must be returned, as ConverterSet is immutable.
        assertNotSame("Removing a converter should return a new set instance.", initialSet, resultSet);

        // 2. The size of the new set should be one less than the original.
        assertEquals("The new set should contain one less converter.", 1, resultSet.size());

        // 3. The container array should now hold the converter that was removed.
        assertSame("The removed converter should be captured in the provided array.",
                   converterToRemove, removedConverterContainer[0]);
    }
}