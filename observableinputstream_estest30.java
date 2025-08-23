package org.apache.commons.io.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringWriter;
import org.junit.Test;

/**
 * This test class contains the refactored test case for {@link ObservableInputStream}.
 * The original test was auto-generated and has been improved for understandability.
 */
public class ObservableInputStream_ESTestTest30 extends ObservableInputStream_ESTest_scaffolding {

    /**
     * Tests that building an {@link ObservableInputStream} from a {@link ObservableInputStream.Builder}
     * configured with a {@link StringWriter} fails with an {@link UnsupportedOperationException}.
     * <p>
     * This occurs because the underlying builder logic attempts to resolve a file path from the
     * provided origin, an operation that a {@code StringWriter} does not support.
     * </p>
     */
    @Test(timeout = 4000)
    public void testBuilderWithUnsupportedWriterOriginThrowsException() throws IOException {
        // Arrange: Create a builder and configure it with a StringWriter, which is an
        // unsupported origin for operations that require a file path.
        final ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        final StringWriter stringWriter = new StringWriter();
        builder.setWriter(stringWriter);

        // Act & Assert: Expect an UnsupportedOperationException when constructing the stream.
        try {
            new ObservableInputStream(builder);
            fail("Expected an UnsupportedOperationException because StringWriter does not support getPath().");
        } catch (final UnsupportedOperationException e) {
            // Verify that the exception and its message are correct.
            final String expectedMessage = "WriterOrigin#getPath() for StringWriter origin";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}