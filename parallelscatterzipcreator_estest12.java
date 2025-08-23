package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import org.evosuite.runtime.mock.java.io.MockFile;

// Note: The test class name, likely auto-generated, is misleading.
// The tests within do not pertain to ParallelScatterZipCreator.
public class ParallelScatterZipCreator_ESTestTest12 extends ParallelScatterZipCreator_ESTest_scaffolding {

    /**
     * Verifies that the ZipArchiveOutputStream constructor throws a NoClassDefFoundError
     * when its static dependency, ZipEncodingHelper, fails to initialize.
     *
     * <p>This test case likely originates from an automated test generation tool (EvoSuite)
     * and captures a specific, environment-dependent failure. In a typical development
     * scenario, a NoClassDefFoundError would indicate a build or classpath issue rather
     * than a bug in the code's logic. This test is preserved to document that specific
     * failure mode.
     */
    @Test(timeout = 4000)
    public void zipArchiveOutputStreamConstructorFailsWhenDependencyInitializationFails() {
        // Arrange: Create a mock file to pass to the constructor. No other setup is needed.
        File dummyOutputFile = new MockFile("dummy.zip");

        // Act & Assert: Expect a NoClassDefFoundError when instantiating the stream.
        try {
            new ZipArchiveOutputStream(dummyOutputFile, 0);
            fail("Expected a NoClassDefFoundError because a required class could not be initialized.");
        } catch (NoClassDefFoundError e) {
            // This error is expected in the specific test environment where the
            // ZipEncodingHelper class fails its static initialization.
            String message = e.getMessage();
            assertNotNull("The error message should not be null.", message);
            assertTrue(
                "The error message should indicate a problem with ZipEncodingHelper. Actual: " + message,
                message.contains("ZipEncodingHelper")
            );
        }
    }
}