package org.apache.commons.io.input;

import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This test verifies the behavior of the {@link ObservableInputStream} when
 * {@code removeAllObservers()} is called on an instance created by a builder
 * where no observers were configured.
 */
public class ObservableInputStream_ESTestTest53 extends ObservableInputStream_ESTest_scaffolding {

    /**
     * Tests that calling removeAllObservers() throws a NullPointerException if the
     * stream was constructed via a builder without setting any observers. This occurs
     * because the internal list of observers is not initialized in this scenario.
     */
    @Test(expected = NullPointerException.class)
    public void removeAllObservers_throwsNpe_whenBuiltWithoutObservers() throws IOException {
        // Arrange: Create an ObservableInputStream using a builder without providing an observer list.
        // A dummy input stream is required for the builder to create an instance.
        final InputStream dummyInputStream = new ByteArrayInputStream(new byte[0]);
        final ObservableInputStream.Builder builder = new ObservableInputStream.Builder()
                .setInputStream(dummyInputStream);
        final ObservableInputStream observableInputStream = builder.get();

        // Act: Attempt to remove all observers. This should fail because the internal
        // observer list is null.
        observableInputStream.removeAllObservers();

        // Assert: The @Test(expected) annotation verifies that a NullPointerException is thrown.
    }
}