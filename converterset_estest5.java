package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link ConverterSet}.
 */
public class ConverterSetTest {

    /**
     * Tests that the size() method correctly returns the number of converters
     * the set was initialized with.
     */
    @Test
    public void size_shouldReturnNumberOfConvertersProvidedAtConstruction() {
        // Arrange: Create a ConverterSet with a single converter.
        // The test verifies the count, so the element itself can be null for this test case.
        Converter[] converters = new Converter[1];
        ConverterSet converterSet = new ConverterSet(converters);

        // Act: Get the size of the converter set.
        int actualSize = converterSet.size();

        // Assert: The size should be equal to the number of converters passed in.
        assertEquals(1, actualSize);
    }
}