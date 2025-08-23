package org.apache.commons.io.input;

import org.apache.commons.io.IOExceptionList;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Contains improved tests for the {@link ObservableInputStream} class.
 */
public class ObservableInputStreamTest {

    /**
     * Tests that when an observer's error() method throws an exception,
     * the noteError() method catches it and re-throws it wrapped in an
     * {@link IOExceptionList}.
     */
    @Test
    public void noteErrorShouldNotifyObserverAndPropagateObserverException() throws IOException {
        // Arrange
        // 1. Create a mock observer that throws a specific exception when its
        //    error() method is invoked. This allows us to control and verify the
        //    behavior during the error notification.
        final ObservableInputStream.Observer observer = mock(ObservableInputStream.Observer.class);
        final IOException observerException = new IOException("Observer failed on error");
        final IOException sourceException = new IOException("Original stream error");

        // Configure the mock to throw our custom exception when its error() method is called.
        doThrow(observerException).when(observer).error(sourceException);

        // 2. Create the ObservableInputStream with an empty underlying stream and the mock observer.
        //    The underlying stream's content is not relevant for this test.
        final ObservableInputStream stream = new ObservableInputStream(new ByteArrayInputStream(new byte[0]), observer);

        // Act & Assert
        try {
            // Call the method under test. We expect this to throw an exception.
            stream.noteError(sourceException);
            fail("Expected an IOExceptionList to be thrown because the observer throws an exception.");
        } catch (final IOExceptionList e) {
            // Assert that the observer's error() method was called with the original exception.
            verify(observer).error(sourceException);

            // Assert that the caught exception list contains the exception thrown by our mock observer.
            final List<Throwable> causes = e.getCauseList();
            assertEquals("The exception list should contain exactly one cause.", 1, causes.size());
            assertEquals("The cause should be the exception thrown by the observer.", observerException, causes.get(0));
        }
    }
}