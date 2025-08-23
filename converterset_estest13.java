package org.joda.time.convert;

import org.junit.Test;

/**
 * Unit tests for the {@link ConverterSet} class, focusing on the copyInto method.
 */
public class ConverterSetTest {

    /**
     * Verifies that copyInto() throws an ArrayIndexOutOfBoundsException
     * when the destination array is smaller than the number of converters in the set.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void copyInto_shouldThrowException_whenDestinationArrayIsTooSmall() {
        // Arrange: Create a ConverterSet with 7 converters.
        Converter[] sourceConverters = new Converter[7];
        ConverterSet converterSet = new ConverterSet(sourceConverters);

        // Arrange: Create a destination array that is too small to hold the converters.
        Converter[] destinationArray = new Converter[1];

        // Act: Attempt to copy the converters into the smaller destination array.
        // The @Test(expected=...) annotation will assert that the correct exception is thrown.
        converterSet.copyInto(destinationArray);
    }
}