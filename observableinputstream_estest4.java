package org.apache.commons.io.input;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * This test suite focuses on verifying the behavior of the ObservableInputStream class.
 */
public class ObservableInputStreamTest {

    /**
     * Tests that calling noteFinished() on an ObservableInputStream with no registered
     * observers completes without throwing an exception. This ensures the method
     * gracefully handles cases where there are no listeners to notify.
     */
    @Test
    public void noteFinishedShouldNotThrowExceptionForStreamWithNoObservers() throws IOException {
        // Arrange: Create an ObservableInputStream with a null underlying stream and no observers.
        // The underlying stream is irrelevant for this test, as we are only verifying the
        // observer notification logic.
        final ObservableInputStream observableInputStream = new ObservableInputStream((InputStream) null);

        // Act: Manually trigger the "finished" notification.
        observableInputStream.noteFinished();

        // Assert: The test is successful if no exception is thrown, confirming that the
        // method operates correctly when the observer list is empty.
    }
}