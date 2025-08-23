package com.itextpdf.text.pdf;

import org.junit.Test;
import org.evosuite.runtime.testdata.EvoSuiteFile;
import org.evosuite.runtime.testdata.FileSystemHandling;

import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This class contains tests for {@link MappedRandomAccessFile}.
 * Note: The original test class name and scaffolding are retained for context.
 */
public class MappedRandomAccessFile_ESTestTest18 extends MappedRandomAccessFile_ESTest_scaffolding {

    /**
     * Tests that the MappedRandomAccessFile constructor throws an IOException
     * when using an invalid mode in a test environment where memory mapping is not supported.
     *
     * The constructor's logic treats any mode other than "rw" as a read-only request.
     * This test verifies the behavior when the subsequent memory mapping operation fails,
     * which is the case in the EvoSuite mock environment.
     */
    @Test(timeout = 4000)
    public void constructorWithInvalidModeShouldThrowIOExceptionIfMappingFails() {
        // Arrange: Create a temporary file. The constructor requires the file to exist
        // to create a FileInputStream for non-"rw" modes.
        String filename = "testFile.tmp";
        // Use an invalid mode to confirm the constructor falls back to the read-only path.
        String invalidMode = "qw";
        EvoSuiteFile testFile = new EvoSuiteFile(filename);
        FileSystemHandling.appendStringToFile(testFile, "some test data");

        // Act & Assert: Attempt to create the MappedRandomAccessFile.
        try {
            new MappedRandomAccessFile(filename, invalidMode);
            fail("Expected an IOException because memory mapping is unsupported in the test environment.");
        } catch (IOException e) {
            // Verify that the exception is the one expected from the mock environment.
            String expectedMessageFragment = "MappedByteBuffer mocks are not supported yet";
            assertTrue(
                "The exception message should indicate that MappedByteBuffer mocks are not supported. Actual message: " + e.getMessage(),
                e.getMessage().contains(expectedMessageFragment)
            );
        } catch (Exception e) {
            // Fail the test if an unexpected exception type is thrown.
            fail("Caught an unexpected exception type: " + e.getClass().getName());
        }
    }
}