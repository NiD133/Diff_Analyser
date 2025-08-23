package org.apache.commons.io.input;

import org.apache.commons.io.input.ObservableInputStream.Observer;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link ObservableInputStream} and its observers.
 */
public class ObservableInputStreamTest {

    /**
     * A simple observer for testing purposes that tracks its closed state.
     * This helper class makes the test self-contained and easier to understand.
     */
    private static class StateTrackingObserver extends Observer {
        private boolean isClosed = false;

        @Override
        public void closed() {
            this.isClosed = true;
        }

        public boolean isClosed() {
            return isClosed;
        }
    }

    /**
     * Verifies that invoking the data(int) callback on an observer does not
     * incorrectly change its lifecycle state to "closed".
     */
    @Test
    public void observerDataCallbackDoesNotAffectClosedState() {
        // Arrange: Create a new observer instance, which is initially not closed.
        StateTrackingObserver observer = new StateTrackingObserver();

        // Act: Simulate a data-read event by directly calling the data() callback.
        // This method should only process the data and not alter the observer's state.
        observer.data(100);

        // Assert: Verify that the observer is still not marked as closed.
        // The closed state should only be affected by the closed() callback.
        assertFalse("Calling data() should not mark the observer as closed.", observer.isClosed());
    }
}