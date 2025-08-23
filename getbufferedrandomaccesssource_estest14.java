package com.itextpdf.text.io;

import org.junit.Test;
import java.io.IOException;

/**
 * Tests for {@link GetBufferedRandomAccessSource}.
 */
public class GetBufferedRandomAccessSourceTest {

    /**
     * Verifies that the get() method correctly propagates a NullPointerException
     * when the underlying data source is invalid. This ensures that the buffering
     * layer properly handles exceptions from its delegate source.
     */
    @Test(expected = NullPointerException.class)
    public void get_whenUnderlyingSourceIsInvalid_throwsNullPointerException() throws IOException {
        // Arrange: Create a source that is guaranteed to throw a NullPointerException when accessed.
        // A WindowRandomAccessSource wrapping a null source is used to simulate this failure condition.
        RandomAccessSource faultyUnderlyingSource = new WindowRandomAccessSource(null, 100L, 50L);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(faultyUnderlyingSource);

        // Act: Attempt to read from the buffered source. This should trigger the NPE
        // in the underlying faulty source, which should be propagated up.
        // The test will pass if a NullPointerException is thrown, as declared by the annotation.
        bufferedSource.get(0L);
    }
}