package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link ConverterSet} class.
 */
public class ConverterSetTest {

    /**
     * Tests that the add() method returns a new ConverterSet instance when a
     * converter is replaced with a new instance of the same type.
     * The ConverterSet is immutable, so modifications should always result in a new object.
     */
    @Test
    public void add_whenReplacingConverter_returnsNewInstance() {
        // Arrange: Create a set containing an initial converter.
        Converter existingConverter = new CalendarConverter();
        ConverterSet initialSet = new ConverterSet(new Converter[]{existingConverter});

        // The converter to add is a new instance of a type that's already in the set.
        Converter newConverter = new CalendarConverter();

        // Act: Add the new converter, which should replace the existing one.
        // We pass 'null' for the 'removed' parameter as we don't need to inspect it.
        ConverterSet updatedSet = initialSet.add(newConverter, null);

        // Assert: The operation should return a new ConverterSet instance because
        // the contents of the set have changed.
        assertNotNull("The updated set should not be null.", updatedSet);
        assertNotSame(
            "A new ConverterSet instance should be returned when a converter is replaced.",
            initialSet,
            updatedSet
        );
    }
}