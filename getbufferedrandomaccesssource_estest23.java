package com.itextpdf.text.io;

import org.junit.Test;
import java.io.IOException;

/**
 * Contains tests for the {@link GetBufferedRandomAccessSource} class.
 */
public class GetBufferedRandomAccessSourceTest {

    /**
     * Verifies that calling the get() method with a negative position
     * throws an ArrayIndexOutOfBoundsException. A negative position is
     * always invalid, regardless of the buffer's state.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void get_withNegativePosition_shouldThrowException() throws IOException {
        // Arrange: Create a source and wrap it with the buffered source under test.
        byte[] sourceData = new byte[8];
        RandomAccessSource source = new ArrayRandomAccessSource(sourceData);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(source);

        // Act: Attempt to get data from an invalid negative position.
        bufferedSource.get(-1L);

        // Assert: The test expects an ArrayIndexOutOfBoundsException, which is
        // declared in the @Test annotation. If the exception is not thrown,
        // the test will fail.
    }
}