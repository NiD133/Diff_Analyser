package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link ConverterSet} class.
 */
public class ConverterSetTest {

    /**
     * Tests that removing a converter by its index returns a new, modified set.
     */
    @Test
    public void removeByIndex_shouldReturnNewSetWithoutSpecifiedConverter() {
        // Arrange: Create a ConverterSet with a few known converters.
        Converter converter1 = LongConverter.INSTANCE;
        Converter converterToRemove = StringConverter.INSTANCE;
        Converter converter3 = CalendarConverter.INSTANCE;

        Converter[] initialConverters = {converter1, converterToRemove, converter3};
        ConverterSet originalSet = new ConverterSet(initialConverters);

        // The 'remove' method can optionally return the removed converter in this array.
        Converter[] removedContainer = new Converter[1];
        int indexToRemove = 1;

        // Act: Remove the converter at the specified index.
        ConverterSet newSet = originalSet.remove(indexToRemove, removedContainer);

        // Assert: Verify the new set is a distinct, smaller instance and the correct
        // converter was removed.
        assertNotSame("The remove operation should return a new instance.", originalSet, newSet);
        assertNotEquals("The new set should not be equal to the original.", originalSet, newSet);

        assertEquals("The new set should have one less converter.",
                     initialConverters.length - 1, newSet.size());

        assertSame("The correct converter should be reported as removed.",
                   converterToRemove, removedContainer[0]);
    }
}