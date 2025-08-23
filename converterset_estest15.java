package org.joda.time.convert;

import org.junit.Test;

/**
 * This test class has been improved for understandability.
 * The original test was auto-generated and tested an edge case of the ConverterSet.add method.
 * This version focuses on making the test's purpose clear and easy to maintain.
 */
public class ConverterSetTest {

    /**
     * Verifies that the add() method throws an ArrayIndexOutOfBoundsException
     * if the 'removed' array parameter is too small (e.g., size 0) to hold
     * a potentially replaced converter.
     *
     * The 'removed' array is an output parameter designed to hold a converter
     * that is replaced during the add operation. If it's non-null, the implementation
     * will attempt to write to its first element (index 0).
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void add_withEmptyRemovedArray_shouldThrowArrayIndexOutOfBoundsException() {
        // Arrange
        // Create an empty ConverterSet.
        ConverterSet emptySet = new ConverterSet(new Converter[0]);

        // The converter we intend to add.
        Converter converterToAdd = StringConverter.INSTANCE;

        // The 'removed' array is intentionally created with size 0. The add method
        // will attempt to write to removed[0], which will cause the exception.
        Converter[] emptyRemovedArray = new Converter[0];

        // Act & Assert
        // This call is expected to throw an ArrayIndexOutOfBoundsException because
        // the 'emptyRemovedArray' cannot be written to.
        emptySet.add(converterToAdd, emptyRemovedArray);
    }
}