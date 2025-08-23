package org.apache.commons.io.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link ObservableInputStream.Builder}.
 */
public class ObservableInputStream_ESTestTest32 {

    /**
     * Tests that constructing an {@link ObservableInputStream} from a builder
     * without an origin (the underlying InputStream) throws an
     * {@link IllegalStateException}.
     */
    @Test
    public void buildShouldThrowIllegalStateExceptionWhenOriginInputStreamNotSet() {
        // Arrange: Create a builder without setting the required origin InputStream.
        final ObservableInputStream.Builder builder = new ObservableInputStream.Builder();

        // Act & Assert: Attempting to construct the stream should fail.
        try {
            new ObservableInputStream(builder);
            fail("Expected an IllegalStateException to be thrown because no origin InputStream was set on the builder.");
        } catch (final IllegalStateException e) {
            // This is the expected outcome. The message is thrown by a parent class
            // to indicate the missing configuration.
            assertEquals("origin == null", e.getMessage());
        } catch (final IOException e) {
            // The constructor signature includes 'throws IOException'.
            // If this is thrown, the test should fail as it's not the expected exception.
            fail("Expected an IllegalStateException, but caught an unexpected IOException: " + e.getMessage());
        }
    }
}