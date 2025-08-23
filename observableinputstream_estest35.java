package org.apache.commons.io.input;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link ObservableInputStream.Observer} class.
 */
public class ObservableInputStreamObserverTest {

    /**
     * Tests that the default implementation of the {@code Observer.error()} method
     * re-throws the exact exception instance it receives. This ensures that
     * errors are correctly propagated through the observer chain.
     */
    @Test
    public void testErrorCallbackRethrowsGivenException() {
        // Arrange: Create a concrete observer instance and the exception to be thrown.
        // An anonymous class is used to test the default behavior of the abstract
        // Observer class without relying on other specific implementations.
        final ObservableInputStream.Observer observer = new ObservableInputStream.Observer() {
            // No methods are overridden; we are testing the default implementation.
        };
        final IOException expectedException = new IOException("Test exception");

        // Act & Assert: Call the error method and verify the outcome.
        try {
            observer.error(expectedException);
            fail("Expected an IOException to be thrown, but no exception occurred.");
        } catch (final IOException actualException) {
            // Verify that the thrown exception is the same instance that was passed in.
            assertSame("The exception thrown by the error() method should be the same instance " +
                       "as the one provided.", expectedException, actualException);
        }
    }
}