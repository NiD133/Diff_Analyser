package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link ConverterSet} class.
 */
public class ConverterSetTest {

    /**
     * Tests that adding a converter that is already present in the set
     * returns the same ConverterSet instance, confirming its immutable behavior
     * in this scenario.
     */
    @Test
    public void add_whenConverterIsAlreadyPresent_shouldReturnSameInstance() {
        // Arrange: Create a ConverterSet that already contains a StringConverter.
        Converter existingConverter = StringConverter.INSTANCE;
        Converter[] initialConverters = { existingConverter };
        ConverterSet initialSet = new ConverterSet(initialConverters);

        // The 'add' method can optionally return the converter that was replaced.
        // We expect no replacement, so this array should remain unchanged.
        Converter[] replacedConverterContainer = new Converter[1];

        // Act: Attempt to add the same converter instance to the set.
        ConverterSet resultSet = initialSet.add(existingConverter, replacedConverterContainer);

        // Assert: The returned set should be the exact same instance as the original,
        // not a new copy, because no change was made.
        assertSame(
            "Adding an existing converter should return the same set instance",
            initialSet,
            resultSet
        );
        assertNull(
            "No converter should have been replaced",
            replacedConverterContainer[0]
        );
    }
}