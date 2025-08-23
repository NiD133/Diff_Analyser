package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ObservableInputStream}.
 * This test focuses on the management of observers.
 */
public class ObservableInputStreamTest {

    /**
     * Tests that the getObservers() method returns an empty list when the
     * stream is created without any observers.
     */
    @Test
    void getObserversShouldReturnEmptyListWhenConstructedWithoutObservers() throws IOException {
        // Arrange: Create an ObservableInputStream without providing any observers.
        // A NullInputStream is a simple, empty stream suitable for this test.
        try (final ObservableInputStream ois = new ObservableInputStream(new NullInputStream())) {

            // Act: Get the list of observers from the stream.
            final var observers = ois.getObservers();

            // Assert: The list of observers should be empty.
            assertTrue(observers.isEmpty(), "Expected the observer list to be empty for a new stream with no observers.");
        }
    }
}