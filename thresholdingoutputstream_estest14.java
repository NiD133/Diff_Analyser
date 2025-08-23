package org.apache.commons.io.output;

import static org.evosuite.runtime.EvoAssertions.verifyException;
import static org.junit.Assert.fail;

import org.junit.Test;

public class ThresholdingOutputStream_ESTestTest14 extends ThresholdingOutputStream_ESTest_scaffolding {

    /**
     * Tests that calling {@code checkThreshold} on a {@link DeferredFileOutputStream}
     * created with a default builder throws a {@link NullPointerException}.
     * <p>
     * The default builder does not configure an output directory. When the threshold
     * is exceeded, the stream attempts to create a temporary file in a null directory,
     * causing an NPE inside {@code java.nio.file.Files}.
     * </p>
     */
    @Test(timeout = 4000)
    public void checkThresholdWithDefaultBuilderThrowsNullPointerException() throws Throwable {
        // Arrange: Create a DeferredFileOutputStream using the default builder, which
        // does not set a directory for the output file.
        final DeferredFileOutputStream.Builder builder = new DeferredFileOutputStream.Builder();
        final DeferredFileOutputStream outputStream = builder.get();

        // Act & Assert: Expect a NullPointerException when the threshold is checked.
        try {
            // A count of 1 is sufficient to trigger the threshold-exceeded logic.
            outputStream.checkThreshold(1);
            fail("A NullPointerException was expected because no output directory was configured.");
        } catch (final NullPointerException e) {
            // Verify that the NPE originates from the file system utility class,
            // confirming our assumption about the cause of the failure.
            verifyException("java.nio.file.Files", e);
        }
    }
}