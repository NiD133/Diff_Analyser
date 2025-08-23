package org.apache.commons.io.input;

import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link ObservableInputStream} class.
 */
public class ObservableInputStreamTest {

    /**
     * Tests that a new ObservableInputStream instance, created without explicitly
     * providing observers, has an empty list of observers.
     */
    @Test
    public void newInstanceShouldHaveNoObservers() {
        // Arrange: Create a new ObservableInputStream. The underlying stream can be
        // null for this test, as getObservers() does not interact with it.
        final ObservableInputStream observableInputStream = new ObservableInputStream(null);

        // Act: Retrieve the list of observers from the new instance.
        final List<ObservableInputStream.Observer> observers = observableInputStream.getObservers();

        // Assert: The returned list should be non-null and empty.
        assertNotNull("The list of observers should never be null.", observers);
        assertTrue("A newly created ObservableInputStream should have an empty observer list.", observers.isEmpty());
    }
}