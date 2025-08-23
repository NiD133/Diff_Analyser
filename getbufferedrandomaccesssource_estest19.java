package com.itextpdf.text.io;

import org.junit.Test;
import java.io.IOException;

/**
 * Contains tests for the {@link GetBufferedRandomAccessSource} class.
 * Note: This class is a refactored version of an auto-generated test suite to improve understandability.
 */
public class GetBufferedRandomAccessSourceTest {

    /**
     * Verifies that the close() method correctly propagates exceptions thrown by the underlying source.
     * If the wrapped source fails during its close operation, the GetBufferedRandomAccessSource
     * should not suppress the exception.
     */
    @Test(expected = NullPointerException.class)
    public void close_whenUnderlyingSourceThrowsException_propagatesException() throws IOException {
        // Arrange: Create a faulty underlying source that is guaranteed to throw an exception
        // when its close() method is invoked. A WindowRandomAccessSource wrapping a null
        // source serves this purpose, as it will throw a NullPointerException.
        RandomAccessSource faultyUnderlyingSource = new WindowRandomAccessSource(null, 0L, 0L);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(faultyUnderlyingSource);

        // Act: Call the method under test. This is expected to trigger the exception.
        bufferedSource.close();

        // Assert: The test framework verifies that a NullPointerException was thrown.
        // This is handled by the `expected` attribute of the @Test annotation.
    }
}