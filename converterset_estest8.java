package org.joda.time.convert;

import org.junit.Test;

/**
 * Unit tests for {@link ConverterSet}.
 */
public class ConverterSetTest {

    /**
     * Tests that calling remove() for a non-existent converter with a zero-length
     * 'removed' array throws an exception.
     * <p>
     * The implementation of {@link ConverterSet#remove(Converter, Converter[])} attempts
     * to write {@code null} to {@code removed[0]} if the converter is not found.
     * This test verifies that this behavior correctly throws an
     * {@link ArrayIndexOutOfBoundsException} when a non-null, zero-length array
     * is supplied.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void remove_nonExistentConverterWithZeroLengthRemovedArray_throwsException() {
        // Arrange: Create an empty ConverterSet.
        ConverterSet emptySet = new ConverterSet(new Converter[0]);

        // Arrange: Define a converter that is not in the set.
        Converter converterToRemove = StringConverter.INSTANCE;

        // Arrange: Create a non-null, but zero-length, array for the 'removed' out-parameter.
        Converter[] zeroLengthRemovedArray = new Converter[0];

        // Act: Attempt to remove the converter. This should throw an exception
        // because the method tries to access index 0 of the zero-length array.
        emptySet.remove(converterToRemove, zeroLengthRemovedArray);
    }
}