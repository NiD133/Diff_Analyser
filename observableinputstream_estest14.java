package org.apache.commons.io.input;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * This test case focuses on the behavior of the ObservableInputStream when its
 * list of observers is immutable.
 */
public class ObservableInputStream_ESTestTest14 {

    /**
     * Tests that removeAllObservers() throws an UnsupportedOperationException
     * when the ObservableInputStream is constructed with a varargs array of observers.
     * This occurs because the constructor uses Arrays.asList(), which returns a
     * fixed-size list that does not support modification (like the clear() operation).
     */
    @Test(expected = UnsupportedOperationException.class)
    public void removeAllObserversShouldThrowExceptionWhenConstructedWithObserverArray() {
        // Arrange: Create an ObservableInputStream with a fixed-size observer list.
        // An empty stream is sufficient as we are not testing read operations.
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        
        // The varargs constructor creates a fixed-size list from this array.
        final ObservableInputStream.Observer[] observers = new ObservableInputStream.Observer[1];
        final ObservableInputStream observableStream = new ObservableInputStream(inputStream, observers);

        // Act: Attempt to modify the fixed-size list of observers.
        // This call will internally invoke list.clear(), which is not supported.
        // The @Test(expected=...) annotation handles the assertion.
        observableStream.removeAllObservers();
    }
}