package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.List;
import org.apache.commons.io.input.ObservableInputStream.Observer;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ObservableInputStream}, focusing on observer management.
 */
public class ObservableInputStreamTest {

    /**
     * A minimal, no-op observer implementation for use in tests where the observer's
     * behavior is not under scrutiny.
     */
    private static class TestObserver extends Observer {
        // No behavior is needed for this test's purpose.
    }

    /**
     * Tests that getObservers() returns the same observer instance that was
     * provided at construction time.
     */
    @Test
    void getObservers_whenConstructedWithSingleObserver_returnsListContainingThatObserver() throws IOException {
        // Arrange
        final Observer observer = new TestObserver();
        // The underlying stream is not used, so NullInputStream is a suitable stand-in.
        try (final ObservableInputStream ois = new ObservableInputStream(new NullInputStream(), observer)) {

            // Act
            final List<Observer> retrievedObservers = ois.getObservers();

            // Assert
            assertNotNull(retrievedObservers, "The list of observers should not be null.");
            assertEquals(1, retrievedObservers.size(), "The list should contain exactly one observer.");
            assertEquals(observer, retrievedObservers.get(0), "The observer in the list should be the one provided at construction.");
        }
    }
}