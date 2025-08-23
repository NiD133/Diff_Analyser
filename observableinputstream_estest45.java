package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * Contains an improved test for the {@link ObservableInputStream.Builder}.
 *
 * Note: The original class name "ObservableInputStream_ESTestTest45" is kept,
 * but a more descriptive name like "ObservableInputStreamBuilderTest" would be
 * standard practice.
 */
public class ObservableInputStream_ESTestTest45 {

    /**
     * Tests that the {@link ObservableInputStream.Builder#setObservers(List)} method
     * correctly configures the observers for the resulting {@link ObservableInputStream}.
     * This test verifies the behavior when an empty list of observers is provided.
     */
    @Test
    public void builderSetObserversShouldCorrectlyInitializeStreamWithEmptyList() throws IOException {
        // Arrange: Create a builder and the necessary components.
        // A dummy input stream is required for the builder to construct the final object.
        final InputStream dummyInputStream = new ByteArrayInputStream(new byte[0]);
        final List<ObservableInputStream.Observer> emptyObserverList = Collections.emptyList();
        final ObservableInputStream.Builder builder = new ObservableInputStream.Builder();

        // Act: Configure the builder with the input stream and observers, then build the object.
        builder.setInputStream(dummyInputStream);
        builder.setObservers(emptyObserverList);
        final ObservableInputStream observableInputStream = builder.get();

        // Assert: Verify that the created stream has the correct (empty) list of observers.
        // This improved assertion checks the state of the constructed object, which is the
        // correct way to test the builder's functionality.
        final List<ObservableInputStream.Observer> actualObservers = observableInputStream.getObservers();
        assertNotNull("The list of observers should not be null.", actualObservers);
        assertTrue("The list of observers should be empty.", actualObservers.isEmpty());
    }
}