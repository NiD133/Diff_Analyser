package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import java.util.zip.ZipException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Contains tests for {@link ExtraFieldUtils}.
 * This class focuses on a specific test case from a larger generated suite.
 */
public class ExtraFieldUtilsTest {

    /**
     * Tests that {@link ExtraFieldUtils#fillExtraField(ZipExtraField, byte[], int, int, boolean)}
     * correctly handles an empty data buffer (i.e., a length of 0) without throwing an exception.
     * This is an important edge case for data parsing.
     */
    @Test
    public void fillExtraFieldWithEmptyDataShouldNotThrowException() throws ZipException {
        // Arrange: Set up the input data and the target field.
        final byte[] emptyExtraFieldData = new byte[0];

        // To test fillExtraField, we need a concrete ZipExtraField instance.
        // We can create an UnparseableExtraFieldData instance for this purpose.
        final ExtraFieldUtils.UnparseableExtraField readBehavior = ExtraFieldUtils.UnparseableExtraField.READ;
        final ZipExtraField fieldToFill = readBehavior.onUnparseableExtraField(
                emptyExtraFieldData, 0, 0, false, 0);
        assertNotNull("Precondition: The extra field to be filled must not be null", fieldToFill);

        // Act: Call the method under test with a zero-length buffer.
        // The primary goal of this test is to ensure this call completes successfully.
        final boolean isLocalData = true;
        ExtraFieldUtils.fillExtraField(fieldToFill, emptyExtraFieldData, 0, 0, isLocalData);

        // Assert: The main assertion is implicit: no exception was thrown.
        // We can also verify that the input data was not modified.
        assertArrayEquals("The input data array should remain unchanged", new byte[]{}, emptyExtraFieldData);
    }
}