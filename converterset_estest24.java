package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.assertNotSame;

/**
 * Unit tests for {@link ConverterSet}.
 */
public class ConverterSetTest {

    /**
     * Tests that adding a new converter to a set returns a new instance,
     * confirming the immutability of the ConverterSet.
     */
    @Test
    public void add_whenAddingNewConverter_shouldReturnNewInstance() {
        // Arrange: Create an empty ConverterSet.
        ConverterSet initialSet = new ConverterSet(new Converter[0]);
        Converter converterToAdd = ReadableInstantConverter.INSTANCE;

        // Act: Add a new converter to the set.
        // The second argument (`removed`) is null because we don't need to capture
        // the converter that might have been replaced.
        ConverterSet resultSet = initialSet.add(converterToAdd, null);

        // Assert: The returned set should be a different instance from the original.
        assertNotSame("Adding a converter should produce a new ConverterSet instance.", initialSet, resultSet);
    }
}