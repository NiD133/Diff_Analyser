package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

/**
 * Contains tests for the {@link RandomAccessFileOrArray#readUnsignedByte()} method.
 */
public class RandomAccessFileOrArrayReadUnsignedByteTest {

    @Test
    public void readUnsignedByte_shouldReadByteAndAdvancePointer() throws IOException {
        // Arrange: Create a data source with a known byte value at the start.
        byte[] sourceData = new byte[]{16, 20, 30}; // The value 16 will be read.
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(sourceData);

        int expectedValue = 16;
        long expectedPointerPosition = 1L;

        // Act: Read a single unsigned byte from the source.
        int actualValue = reader.readUnsignedByte();
        long actualPointerPosition = reader.getFilePointer();

        // Assert: Verify that the correct value was read and the internal pointer advanced.
        assertEquals("The method should return the correct unsigned byte value.", expectedValue, actualValue);
        assertEquals("The file pointer should advance by one byte after reading.", expectedPointerPosition, actualPointerPosition);
    }
}