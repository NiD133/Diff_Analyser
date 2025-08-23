package org.apache.commons.io.input;

import org.junit.Test;
import java.io.ByteArrayInputStream;

/**
 * Tests for the {@link ObservableInputStream} class, focusing on observer management.
 */
public class ObservableInputStream_ESTestTest48 extends ObservableInputStream_ESTest_scaffolding {

    /**
     * Tests that attempting to add a null observer to the stream
     * results in a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void add_whenObserverIsNull_throwsNullPointerException() {
        // Arrange: Create an ObservableInputStream with a dummy underlying stream.
        // The content of the stream is irrelevant for this test.
        final ObservableInputStream observableInputStream = new ObservableInputStream(new ByteArrayInputStream(new byte[0]));

        // Act: Attempt to add a null observer.
        // Assert: The @Test(expected) annotation verifies that a NullPointerException is thrown.
        observableInputStream.add(null);
    }
}