package com.itextpdf.text.pdf;

import org.junit.Test;
import org.evosuite.runtime.testdata.FileSystemHandling;

import java.io.IOException;

import static org.evosuite.runtime.EvoAssertions.verifyException;
import static org.junit.Assert.fail;

/**
 * This test suite contains tests for the {@link MappedRandomAccessFile} class.
 * This specific test focuses on the behavior of the finalize() method under I/O error conditions.
 */
public class MappedRandomAccessFile_ESTestTest8 extends MappedRandomAccessFile_ESTest_scaffolding {

    /**
     * Verifies that the finalize() method propagates an IOException when the underlying
     * file channel fails to close. The `finalize()` method is expected to call `close()`,
     * and this test ensures that exceptions from `close()` are not silently swallowed.
     */
    @Test(timeout = 4000)
    public void finalizeShouldPropagateIOExceptionWhenClosingFails() throws Exception {
        // Arrange: Configure a mock file system to throw an IOException on any file operation.
        // This simulates a disk error or other I/O problem that would prevent the file from closing.
        FileSystemHandling.shouldAllThrowIOExceptions();
        MappedRandomAccessFile file = new MappedRandomAccessFile("test.dat", "rw");

        // Act & Assert: Call finalize() and expect an IOException.
        try {
            file.finalize();
            fail("Expected an IOException to be thrown because the underlying file system is configured to fail.");
        } catch (IOException e) {
            // Assert: Confirm that the exception is the one simulated by the virtual file system.
            // This ensures the exception originated from the expected I/O failure.
            verifyException("org.evosuite.runtime.vfs.VirtualFileSystem", e);
        }
    }
}