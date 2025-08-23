package org.apache.commons.io.input;

import org.junit.Test;
import java.io.InputStream;

/**
 * This test suite focuses on verifying the behavior of the {@link ObservableInputStream} class.
 */
public class ObservableInputStream_ESTestTest8 extends ObservableInputStream_ESTest_scaffolding {

    /**
     * Tests that adding a null observer to the stream does not cause an exception.
     * This ensures the method is robust against null inputs.
     */
    @Test
    public void addShouldNotThrowExceptionWhenObserverIsNull() {
        // Arrange: Create an ObservableInputStream. The underlying stream is not
        // relevant for this test, so it can be null.
        ObservableInputStream observableInputStream = new ObservableInputStream(null);

        // Act: Attempt to add a null observer.
        observableInputStream.add(null);

        // Assert: The test implicitly passes if no exception is thrown,
        // confirming the desired behavior.
    }
}