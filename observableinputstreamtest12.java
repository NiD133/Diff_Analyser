package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.commons.io.input.ObservableInputStream.Observer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ObservableInputStream}.
 * This test class focuses on verifying the behavior of observer management.
 */
public class ObservableInputStreamTest {

    /**
     * A test-specific observer that counts method invocations.
     * Serves as a base for more specialized test observers.
     */
    private static class MethodCountObserver extends Observer {
        // Fields for counting are omitted for brevity as they are not used in the specific test.
        // They would be included in a full test suite.
    }

    /**
     * A test-specific observer that captures the data passed to it.
     * Extends MethodCountObserver to inherit counting behavior if needed.
     */
    private static final class DataViewObserver extends MethodCountObserver {
        // Fields for capturing data are omitted for brevity as they are not used in this specific test.
    }

    @Test
    @DisplayName("getObservers() should return observers in the order they were added")
    void getObservers_shouldReturnObserversInAdditionOrder() throws IOException {
        // Arrange: Create two distinct observers to be added to the stream.
        final Observer firstObserver = new DataViewObserver();
        final Observer secondObserver = new DataViewObserver();
        final List<Observer> expectedObservers = List.of(firstObserver, secondObserver);

        // Act: Create a stream with the observers and then retrieve the list of observers.
        // A NullInputStream is used as the underlying stream because we are not testing read behavior here.
        final List<Observer> actualObservers;
        try (ObservableInputStream observableInputStream = new ObservableInputStream(new NullInputStream(), firstObserver, secondObserver)) {
            actualObservers = observableInputStream.getObservers();
        }

        // Assert: The returned list should contain the observers in the same order they were added.
        assertIterableEquals(expectedObservers, actualObservers,
            "The list of observers should match the order of their addition.");
    }
}