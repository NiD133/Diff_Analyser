package com.itextpdf.text.pdf;

import org.junit.Test;

import java.io.IOException;

/**
 * Unit tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that attempting to read from a RandomAccessFileOrArray instance
     * after it has been closed results in a NullPointerException.
     * <p>
     * The {@code close()} method is expected to release internal resources,
     * making subsequent read operations invalid.
     * </p>
     */
    @Test(expected = NullPointerException.class)
    public void readUTF_onClosedStream_throwsNullPointerException() throws IOException {
        // Arrange: Create a RandomAccessFileOrArray instance from a byte array.
        byte[] sourceData = new byte[10]; // Content is irrelevant, only its existence matters.
        RandomAccessFileOrArray randomAccess = new RandomAccessFileOrArray(sourceData);

        // Act: Close the stream, which puts the object into the state under test.
        randomAccess.close();

        // Assert: Attempt to read from the now-closed stream.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        randomAccess.readUTF();
    }
}