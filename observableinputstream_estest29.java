package org.apache.commons.io.input;

import org.junit.Test;
import java.io.IOException;
import java.nio.file.NoSuchFileException;

/**
 * Tests for {@link ObservableInputStream} focusing on construction via its builder.
 */
public class ObservableInputStream_ESTestTest29 extends ObservableInputStream_ESTest_scaffolding {

    /**
     * Verifies that the ObservableInputStream constructor throws a NoSuchFileException
     * when its builder is configured with a path to a non-existent file.
     */
    @Test(expected = NoSuchFileException.class)
    public void constructorWithBuilderForNonExistentPathThrowsException() throws IOException {
        // Arrange: Create a builder and configure it with a path that does not exist.
        final String nonExistentPath = "PZkNv|C6'u*&le;,"; // An invalid and unlikely to exist path
        final ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        builder.setPath(nonExistentPath);

        // Act: Attempt to construct the ObservableInputStream from the builder.
        // This action is expected to throw a NoSuchFileException.
        new ObservableInputStream(builder);

        // Assert: The test succeeds if the expected NoSuchFileException is thrown,
        // which is handled by the @Test(expected) annotation.
    }
}