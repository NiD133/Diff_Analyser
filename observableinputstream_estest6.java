package org.apache.commons.io.input;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.util.List;

/**
 * This test suite evaluates the functionality of the {@link ObservableInputStream} class,
 * focusing specifically on the management of observers.
 */
public class ObservableInputStreamTest {

    /**
     * Tests that calling {@link ObservableInputStream#removeAllObservers()} on an instance
     * that has no registered observers completes successfully without throwing an exception.
     * It also verifies that the observer list remains empty after the call.
     */
    @Test
    public void removeAllObservers_withNoExistingObservers_shouldSucceed() {
        // Arrange: Create an ObservableInputStream. By default, it has no observers.
        // The underlying InputStream is not relevant for this test and can be null.
        final ObservableInputStream observableInputStream = new ObservableInputStream(null);

        // Act: Call the method under test.
        observableInputStream.removeAllObservers();

        // Assert: Verify that the list of observers is empty and no exception was thrown.
        final List<ObservableInputStream.Observer> observers = observableInputStream.getObservers();
        assertTrue("The observer list should be empty after calling removeAllObservers.", observers.isEmpty());
    }
}