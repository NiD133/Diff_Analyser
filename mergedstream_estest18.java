package com.fasterxml.jackson.core.io;

import org.junit.Test;
import java.io.InputStream;

/**
 * Unit tests for the {@link MergedStream} class.
 */
public class MergedStreamTest {

    /**
     * Verifies that calling markSupported() throws a NullPointerException
     * when the underlying InputStream is null. The method is expected to
     * delegate the call, which results in an NPE.
     */
    @Test(expected = NullPointerException.class)
    public void markSupported_whenInputStreamIsNull_throwsNullPointerException() {
        // Arrange: Create a MergedStream with a null underlying InputStream.
        // The IOContext and buffer arguments are not used by the markSupported() method,
        // so they can be null for this specific test case.
        MergedStream mergedStream = new MergedStream(null, (InputStream) null, null, 0, 0);

        // Act: Call the method under test.
        // The @Test(expected) annotation will handle the assertion.
        mergedStream.markSupported();
    }
}