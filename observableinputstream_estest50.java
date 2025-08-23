package org.apache.commons.io.input;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Contains tests for the {@link ObservableInputStream} class, focusing on observer management.
 */
public class ObservableInputStream_ESTestTest50 extends ObservableInputStream_ESTest_scaffolding {

    /**
     * Tests that the constructor correctly handles an array containing a null observer,
     * and that getObservers() returns a list reflecting this.
     */
    @Test
    public void getObserversShouldReturnListWithNullWhenConstructedWithNullObserver() {
        // Arrange
        // An empty input stream is sufficient as we are only testing the observer list.
        final InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);

        // The constructor accepts a varargs array of observers. We test its behavior
        // with an array containing a single null element, which was the implicit
        // behavior of the original test's `new ObservableInputStream.Observer[1]`.
        final ObservableInputStream.Observer[] observersWithNull = { null };

        // Act
        final ObservableInputStream observableStream = new ObservableInputStream(emptyInputStream, observersWithNull);
        final List<ObservableInputStream.Observer> retrievedObservers = observableStream.getObservers();

        // Assert
        // The returned list should not be null.
        assertNotNull("The list of observers should not be null.", retrievedObservers);
        // The list size should match the number of observers passed to the constructor.
        assertEquals("The list should contain one observer.", 1, retrievedObservers.size());
        // The element in the list should be the null we passed in.
        assertNull("The observer in the list should be null.", retrievedObservers.get(0));
    }
}