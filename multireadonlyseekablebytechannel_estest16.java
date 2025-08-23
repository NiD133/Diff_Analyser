package org.apache.commons.compress.utils;

import org.junit.Test;
import java.io.File;

/**
 * Unit tests for the {@link MultiReadOnlySeekableByteChannel} class.
 */
public class MultiReadOnlySeekableByteChannelTest {

    /**
     * Tests that MultiReadOnlySeekableByteChannel.forFiles() throws a NullPointerException
     * when the input array of files contains a null element.
     */
    @Test(expected = NullPointerException.class)
    public void forFilesShouldThrowNullPointerExceptionWhenFileArrayContainsNull() {
        // Arrange: Create an array of File objects where at least one element is null.
        // The method under test is expected to throw an exception when it attempts
        // to process the null element.
        final File[] filesWithNullElement = { null };

        // Act: Call the method with the invalid input.
        // The @Test(expected=...) annotation handles the assertion.
        MultiReadOnlySeekableByteChannel.forFiles(filesWithNullElement);
    }
}