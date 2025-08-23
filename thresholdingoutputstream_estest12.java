package org.apache.commons.io.output;

import org.junit.Test;
import java.io.IOException;

/**
 * Tests for {@link DeferredFileOutputStream}, a subclass of {@link ThresholdingOutputStream}.
 */
public class DeferredFileOutputStreamTest {

    /**
     * Tests that attempting to write to a DeferredFileOutputStream created with a
     * default builder throws a NullPointerException. This occurs because the
     * default builder does not configure a destination file, leading to an
     * attempt to create a file with a null path upon the first write.
     */
    @Test(expected = NullPointerException.class)
    public void writeToStreamWithDefaultBuilderShouldThrowNullPointerException() throws IOException {
        // Arrange: Create a stream using the default builder, which does not set an output file.
        final DeferredFileOutputStream stream = new DeferredFileOutputStream.Builder().get();

        // Act: The first write triggers the creation of the underlying file stream.
        // Since no file was specified in the builder, this operation fails with an NPE
        // when trying to access the null file path.
        stream.write(0);

        // Assert: The expected NullPointerException is verified by the @Test annotation.
    }
}