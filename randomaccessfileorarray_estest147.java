package com.itextpdf.text.pdf;

import com.itextpdf.text.io.RandomAccessSource;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link RandomAccessFileOrArray} class.
 * This improved version focuses on the understandability and maintainability of the original test.
 */
// The original test extended a scaffolding class. We retain this inheritance,
// as its purpose is unknown and it may be required for the test environment setup.
public class RandomAccessFileOrArrayTest extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that readUnsignedByte() correctly reads the first byte from a data source,
     * returns its value, and advances the internal file pointer by one.
     *
     * @throws IOException if an I/O error occurs during the test.
     */
    @Test
    public void readUnsignedByte_fromByteArraySource_readsFirstByteAndAdvancesPointer() throws IOException {
        // --- Arrange ---
        // Create a byte array that will serve as the data source.
        // A new byte array is initialized with all zeros by default in Java.
        byte[] sourceData = new byte[13];

        // Use the recommended factory to create the RandomAccessSource, avoiding deprecated constructors.
        RandomAccessSource source = new RandomAccessSourceFactory().createSource(sourceData);
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(source);

        // --- Act ---
        // Read a single unsigned byte. The file pointer is initially at position 0.
        int actualValue = reader.readUnsignedByte();
        long newFilePointer = reader.getFilePointer();

        // --- Assert ---
        // The first byte in the sourceData array is 0.
        assertEquals("The method should read the correct byte value.", 0, actualValue);
        // After reading one byte, the pointer should be at position 1.
        assertEquals("The file pointer should advance by one.", 1L, newFilePointer);
    }
}