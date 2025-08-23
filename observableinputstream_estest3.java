package org.apache.commons.io.input;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Unit tests for {@link ObservableInputStream}.
 */
public class ObservableInputStreamTest {

    /**
     * Tests that calling noteDataByte() on an instance with no observers
     * executes without throwing an exception.
     */
    @Test
    public void noteDataByteShouldNotThrowExceptionWhenNoObserversAreRegistered() throws IOException {
        // Arrange: Create an ObservableInputStream with a null underlying stream,
        // as it is not used by the noteDataByte() method.
        final ObservableInputStream observableInputStream = new ObservableInputStream((InputStream) null);

        // Act: Call the method under test. The test's success is confirmed
        // by this action not throwing an exception.
        observableInputStream.noteDataByte(123);

        // Assert: No explicit assertion is needed; the test passes if no exception is thrown.
    }
}