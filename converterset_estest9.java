package org.joda.time.convert;

import org.joda.time.convert.Converter;
import org.joda.time.convert.ConverterSet;
import org.junit.Test;

/**
 * Tests for the {@link ConverterSet} class, focusing on its behavior when handling invalid states.
 */
public class ConverterSetTest {

    /**
     * Tests that calling remove() by index on a ConverterSet initialized with a null
     * array of converters throws a NullPointerException.
     *
     * The package-private constructor for ConverterSet does not perform null checks,
     * leading to a NullPointerException when methods attempt to access the internal array.
     */
    @Test(expected = NullPointerException.class)
    public void removeByIndex_whenConstructedWithNullArray_throwsNullPointerException() {
        // Arrange: Create a ConverterSet with a null internal array. This is an
        // invalid state that the current implementation allows via its constructor.
        ConverterSet converterSet = new ConverterSet(null);

        // Act & Assert: Attempting to remove a converter by index should throw
        // a NullPointerException because the internal converter array is null.
        // The index (0) and the 'removed' array (null) are irrelevant, as the
        // exception occurs when the method first tries to access the array's length.
        converterSet.remove(0, null);
    }
}