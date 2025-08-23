package com.itextpdf.text.io;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

/**
 * Test suite for the {@link GetBufferedRandomAccessSource} class.
 *
 * This version has been refactored for understandability from an auto-generated EvoSuite test.
 */
public class GetBufferedRandomAccessSourceTest {

    /**
     * Tests that calling get() with a negative position, which is always out of bounds,
     * returns -1 to indicate the end of the source.
     *
     * The original auto-generated test used a complex setup with a negative-length source
     * and incorrectly asserted a return value of 0. This revised test simplifies the setup
     * and asserts the correct, expected behavior according to standard I/O contracts.
     */
    @Test
    public void get_withNegativePosition_returnsEndOfFileMarker() throws IOException {
        // Arrange: Create a simple, non-empty source to test the boundary condition.
        byte[] data = new byte[] { 10, 20, 30 };
        RandomAccessSource source = new ArrayRandomAccessSource(data);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(source);

        // Act: Attempt to read from a negative position, which is an invalid index.
        int result = bufferedSource.get(-1L);

        // Assert: The expected behavior for an out-of-bounds read is to return -1,
        // signifying the end of the file or an invalid position.
        assertEquals("get() should return -1 for a negative position.", -1, result);
    }
}