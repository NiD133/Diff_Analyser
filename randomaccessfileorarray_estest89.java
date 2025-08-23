package com.itextpdf.text.pdf;

import com.itextpdf.text.io.RandomAccessSource;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import org.junit.Test;

import java.io.EOFException;
import java.io.IOException;

/**
 * Test suite for {@link RandomAccessFileOrArray}.
 * This class demonstrates an improved, more understandable version of the original generated test case.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that attempting to read a little-endian double (8 bytes) from a data source
     * that is too short correctly throws an EOFException.
     *
     * This test ensures the method correctly handles unexpected end-of-file scenarios.
     */
    @Test(expected = EOFException.class)
    public void readDoubleLEShouldThrowEOFExceptionWhenDataSourceIsTooShort() throws IOException {
        // Arrange: Create a data source with fewer bytes than required for a double.
        // Using Double.BYTES makes the intent clear and avoids magic numbers.
        byte[] insufficientData = new byte[Double.BYTES - 1]; // 7 bytes
        
        // Use the recommended factory method to create the source, avoiding deprecated constructors.
        RandomAccessSource source = new RandomAccessSourceFactory().createSource(insufficientData);
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(source);

        // Act: Attempt to read a double from the undersized data source.
        reader.readDoubleLE();

        // Assert: The test passes if an EOFException is thrown. This is handled by the
        // @Test(expected = EOFException.class) annotation, making the test's purpose explicit.
    }
}