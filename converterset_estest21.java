package org.joda.time.convert;

import org.junit.Test;

/**
 * Unit tests for the {@link ConverterSet} class.
 */
public class ConverterSetTest {

    /**
     * Verifies that attempting to remove a converter by an index that is out of the set's bounds
     * correctly throws an IndexOutOfBoundsException.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void removeByIndex_shouldThrowException_whenIndexIsOutOfBounds() {
        // Arrange: Create a ConverterSet with a single element.
        // The only valid index for removal is 0.
        Converter[] initialConverters = new Converter[1];
        ConverterSet converterSet = new ConverterSet(initialConverters);

        int outOfBoundsIndex = 1; // Any index >= 1 is out of bounds.

        // Act: Attempt to remove the element at the invalid index.
        // The second argument (for returning the removed converter) is not relevant to this test.
        converterSet.remove(outOfBoundsIndex, null);

        // Assert: The test expects an IndexOutOfBoundsException, which is handled by the
        // @Test(expected=...) annotation. If no exception is thrown, the test will fail.
    }
}