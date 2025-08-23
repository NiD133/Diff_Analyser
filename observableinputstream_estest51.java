package org.apache.commons.io.input;

import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.StringWriter;
import org.junit.Test;

/**
 * This test class contains tests for {@link ObservableInputStream}.
 * The original test class structure and name are preserved.
 */
public class ObservableInputStream_ESTestTest51 extends ObservableInputStream_ESTest_scaffolding {

    /**
     * Tests that attempting to remove an observer from an ObservableInputStream
     * created via a builder without any observers set results in a NullPointerException.
     * This is because the builder does not initialize an empty list by default.
     */
    @Test
    public void removeObserverFromStreamBuiltWithoutObserversThrowsException() throws IOException {
        // Arrange: Create an ObservableInputStream using a builder without providing an observer list.
        // The CharSequence is required to build the stream, but its content is irrelevant for this test.
        final ObservableInputStream observableInputStream = new ObservableInputStream.Builder()
                .setCharSequence(new StringWriter().getBuffer())
                .get();

        final ObservableInputStream.Observer observerToRemove = new TimestampedObserver();

        // Act & Assert: Expect a NullPointerException when trying to remove an observer,
        // as the internal list of observers is null.
        assertThrows(NullPointerException.class, () -> observableInputStream.remove(observerToRemove));
    }
}