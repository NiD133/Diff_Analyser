package org.joda.time.convert;

import org.junit.Test;

/**
 * Unit tests for the {@link ConverterSet} class, focusing on the remove-by-index functionality.
 */
public class ConverterSetTest {

    /**
     * Verifies that calling remove() with a negative index throws an
     * ArrayIndexOutOfBoundsException, as the index must be within the bounds of the
     * internal converter array.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void removeByIndex_withNegativeIndex_throwsArrayIndexOutOfBoundsException() {
        // Arrange: Create an empty ConverterSet.
        ConverterSet emptyConverterSet = new ConverterSet(new Converter[0]);
        int negativeIndex = -1;

        // Act: Attempt to remove a converter using a negative index.
        // The expected exception is declared in the @Test annotation.
        emptyConverterSet.remove(negativeIndex, null);
    }
}