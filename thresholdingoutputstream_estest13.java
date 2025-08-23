package org.apache.commons.io.output;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;

import static org.evosuite.runtime.EvoAssertions.verifyException;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * This test class focuses on specific behaviors of DeferredFileOutputStream,
 * a subclass of ThresholdingOutputStream.
 */
public class ThresholdingOutputStream_ESTestTest13 extends ThresholdingOutputStream_ESTest_scaffolding {

    /**
     * Tests that calling thresholdReached() on a DeferredFileOutputStream throws a
     * NullPointerException if it was created without a configured output directory.
     *
     * The default builder creates an instance where the output directory is null.
     * When thresholdReached() is invoked, it attempts to create a temporary file
     * using java.nio.file.Files, which throws an NPE when the directory path is null.
     */
    @Test(timeout = 4000)
    public void thresholdReachedShouldThrowNpeWhenDirectoryIsNull() {
        // Arrange: Create a stream using the default builder, which leaves the
        // output file directory as null.
        final DeferredFileOutputStream stream = new DeferredFileOutputStream.Builder().get();

        // Act & Assert
        try {
            // The thresholdReached() method is responsible for switching from memory
            // to a physical file. This action will fail without a directory.
            stream.thresholdReached();
            fail("A NullPointerException was expected but not thrown.");
        } catch (final NullPointerException e) {
            // Assert: Verify that the expected exception was thrown.
            // The exception message is null, as is common for this specific NPE.
            assertNull("The exception message should be null.", e.getMessage());

            // This EvoSuite-specific assertion confirms the NPE originates from the
            // correct class, which is a valuable check in this case.
            verifyException(Files.class.getName(), e);
        } catch (final IOException e) {
            // The method signature includes IOException, so we catch it to ensure
            // the test fails clearly if an unexpected IO error occurs.
            fail("Expected a NullPointerException, but an IOException was thrown: " + e.getMessage());
        }
    }
}