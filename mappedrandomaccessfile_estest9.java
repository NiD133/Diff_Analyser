package com.itextpdf.text.pdf;

import org.junit.Test;
import org.evosuite.runtime.testdata.EvoSuiteFile;
import org.evosuite.runtime.testdata.FileSystemHandling;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the MappedRandomAccessFile class, focusing on its exception handling.
 * This improved version replaces a tool-generated test that was difficult to understand.
 */
public class MappedRandomAccessFileTest {

    private static final String DUMMY_FILENAME = "test-file.dat";
    private static final String READ_WRITE_MODE = "rw";

    /**
     * Verifies that the close() method correctly propagates an IOException when the
     * underlying file system fails during the close operation.
     */
    @Test
    public void closePropagatesIOExceptionWhenUnderlyingChannelFailsToClose() throws IOException {
        // ARRANGE: Create a valid MappedRandomAccessFile instance. To do this, we first
        // need a file to exist on the (virtual) file system so the constructor succeeds.
        EvoSuiteFile tempFile = new EvoSuiteFile(DUMMY_FILENAME);
        FileSystemHandling.appendStringToFile(tempFile, "some initial content");
        MappedRandomAccessFile file = new MappedRandomAccessFile(DUMMY_FILENAME, READ_WRITE_MODE);

        // Next, simulate a global I/O failure. Any subsequent file operation,
        // including closing the file channel, will now throw an exception.
        FileSystemHandling.shouldAllThrowIOExceptions();

        // ACT & ASSERT: Attempt to close the file and assert that the expected
        // IOException is thrown and propagated.
        try {
            file.close();
            fail("Expected an IOException because the file system was configured to fail on close.");
        } catch (IOException e) {
            // Check that the exception is the one simulated by the testing framework.
            assertEquals("Simulated IOException", e.getMessage());
        }
    }
}