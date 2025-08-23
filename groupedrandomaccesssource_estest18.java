package com.itextpdf.text.io;

import org.junit.Test;
import java.io.IOException;

/**
 * Contains tests for the constructor of the {@link GroupedRandomAccessSource} class.
 */
public class GroupedRandomAccessSourceTest {

    /**
     * Verifies that the {@link GroupedRandomAccessSource} constructor throws an
     * {@link ArrayIndexOutOfBoundsException} when initialized with an empty array of sources.
     * <p>
     * This is an important edge case, as the constructor logic attempts to access
     * the last element of the sources array to set an initial internal state, which is
     * not possible for an empty array.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void constructor_whenGivenEmptySourceArray_throwsArrayIndexOutOfBoundsException() throws IOException {
        // Arrange: Create an empty array of sources.
        RandomAccessSource[] emptySources = new RandomAccessSource[0];

        // Act & Assert: Attempt to create a GroupedRandomAccessSource with the empty array.
        // The test will pass if an ArrayIndexOutOfBoundsException is thrown, and fail otherwise.
        new GroupedRandomAccessSource(emptySources);
    }
}