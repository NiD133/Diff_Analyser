package com.itextpdf.text.pdf;

import com.itextpdf.text.io.RandomAccessSource;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that calling getByteSource() is a non-mutating operation
     * that does not advance the internal file pointer.
     */
    @Test
    public void getByteSource_shouldNotAdvanceFilePointer() throws IOException {
        // Arrange: Create a RandomAccessFileOrArray instance from a sample byte array.
        byte[] sourceData = new byte[16];
        RandomAccessFileOrArray randomAccessFile = new RandomAccessFileOrArray(sourceData);
        
        // Sanity check to ensure the file pointer starts at 0.
        assertEquals("Pre-condition failed: File pointer should be at the beginning.", 0L, randomAccessFile.getFilePointer());

        // Act: Call the method under test to get the underlying data source.
        RandomAccessSource resultSource = randomAccessFile.getByteSource();

        // Assert: The file pointer should remain unchanged, and the returned source should be valid.
        assertNotNull("The byte source should not be null.", resultSource);
        assertEquals("Calling getByteSource should not change the file pointer.", 0L, randomAccessFile.getFilePointer());
    }
}