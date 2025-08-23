package org.apache.commons.io.input;

import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 * Tests for {@link ObservableInputStream}.
 */
public class ObservableInputStreamTest {

    /**
     * Tests that attempting to remove an observer that has not been added
     * is handled gracefully without throwing an exception.
     */
    @Test
    public void testRemoveObserverThatWasNotAdded() {
        // Arrange
        // The underlying stream is not used in this test.
        final ObservableInputStream stream = new ObservableInputStream(null);
        final ObservableInputStream.Observer observer = new ObservableInputStream.Observer() {
            // A simple anonymous observer for test purposes.
        };

        // Act
        // Attempting to remove an observer that was never added should be a no-op.
        // The primary goal is to ensure this action does not throw an exception.
        stream.remove(observer);

        // Assert
        // The test implicitly verifies that no exception was thrown.
        // Further assertions could be added if the observer had state to check.
    }
}