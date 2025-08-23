package org.joda.time.convert;

import org.junit.Test;

/**
 * Test suite for the {@link ConverterSet} class.
 * 
 * This version has been refactored from an auto-generated test to improve clarity and maintainability.
 */
public class ConverterSetTest {

    /**
     * Verifies that calling remove() with an index that is out of bounds
     * (i.e., greater than or equal to the set's size) correctly throws an
     * IndexOutOfBoundsException.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void removeByIndex_whenIndexIsOutOfBounds_throwsException() {
        // Arrange: Create a ConverterSet with a single element.
        // The only valid index for removal is 0.
        Converter[] initialConverters = new Converter[1]; // An array with one (null) converter
        ConverterSet converterSet = new ConverterSet(initialConverters);

        // Act: Attempt to remove an element at an invalid index (1).
        // The second argument for capturing the removed converter can be null, as we are not checking it.
        int invalidIndex = 1;
        converterSet.remove(invalidIndex, null);

        // Assert: The test passes if an IndexOutOfBoundsException is thrown,
        // as specified by the @Test(expected=...) annotation. If no exception or a
        // different one is thrown, the test will fail.
    }
}