package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

/**
 * Unit tests for the {@link ExtraFieldUtils} class.
 */
public class ExtraFieldUtilsTest {

    /**
     * Tests that {@link ExtraFieldUtils#mergeCentralDirectoryData(ZipExtraField[])}
     * throws a NullPointerException if the input array contains null elements.
     * The method is expected to iterate over the array, and will fail when it
     * attempts to access a method on a null object.
     */
    @Test(expected = NullPointerException.class)
    public void mergeCentralDirectoryDataShouldThrowNpeWhenArrayContainsNull() {
        // Arrange: Create an array where elements are initialized to null by default.
        // A single null element is sufficient to trigger the exception.
        final ZipExtraField[] fieldsWithNull = new ZipExtraField[1];

        // Act: Attempt to merge the central directory data from the array.
        // This call is expected to throw a NullPointerException.
        ExtraFieldUtils.mergeCentralDirectoryData(fieldsWithNull);

        // Assert: The test passes if a NullPointerException is thrown, as
        // specified by the 'expected' attribute of the @Test annotation.
    }
}