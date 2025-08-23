package org.joda.time.convert;

import org.junit.Test;

/**
 * Unit tests for {@link ConverterSet}.
 */
public class ConverterSetTest {

    /**
     * The add() method is documented to throw a NullPointerException if the converter to be added is null.
     * This test verifies that behavior.
     */
    @Test(expected = NullPointerException.class)
    public void add_whenConverterIsNull_shouldThrowNullPointerException() {
        // Arrange: Create an empty ConverterSet. The initial state of the set
        // does not affect the outcome of adding a null converter.
        Converter[] initialConverters = new Converter[0];
        ConverterSet converterSet = new ConverterSet(initialConverters);

        // This array is required by the method signature to hold any converter that might be replaced.
        Converter[] removedConverterContainer = new Converter[1];

        // Act: Attempt to add a null converter to the set.
        // The @Test(expected) annotation will cause the test to pass only if this line
        // throws a NullPointerException.
        converterSet.add(null, removedConverterContainer);
    }
}