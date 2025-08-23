package org.apache.commons.io.input;

import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

/**
 * This test suite contains tests for the {@link ObservableInputStream} class,
 * focusing on the notification mechanism for observers.
 */
public class ObservableInputStream_ESTestTest5 extends ObservableInputStream_ESTest_scaffolding {

    /**
     * Tests that calling {@link ObservableInputStream#noteClosed()} on a stream
     * with no registered observers completes without throwing an exception.
     *
     * @throws IOException if an I/O error occurs during the test.
     */
    @Test(timeout = 4000)
    public void noteClosedWithNoObserversDoesNotThrowException() throws IOException {
        // Arrange: Create an ObservableInputStream with an empty source and no observers.
        final ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        final StringWriter stringWriter = new StringWriter();
        final StringBuffer stringBuffer = stringWriter.getBuffer();
        builder.setCharSequence(stringBuffer);
        final ObservableInputStream observableInputStream = new ObservableInputStream(builder);

        // Act: Notify potential observers that the stream has been closed.
        observableInputStream.noteClosed();

        // Assert: The test is successful if no exception is thrown.
        // The JUnit framework handles this implicitly.
    }
}