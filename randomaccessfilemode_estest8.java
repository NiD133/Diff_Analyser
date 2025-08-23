package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.testdata.EvoSuiteFile;
import org.evosuite.runtime.testdata.FileSystemHandling;

public class RandomAccessFileMode_ESTestTest8 extends RandomAccessFileMode_ESTest_scaffolding {

    /**
     * Tests that {@link RandomAccessFileMode#READ_ONLY#create(Path)} successfully
     * opens an existing file and allows its contents to be read.
     */
    @Test(timeout = 4000)
    public void create_whenModeIsReadOnlyAndFileExists_thenReturnsReadableFile() throws IOException {
        // Arrange: Create a temporary file with known content.
        final String fileName = "testReadOnly.txt";
        final String expectedContent = "This is a test.";

        // The test setup uses a mocked file system provided by the test framework.
        final EvoSuiteFile evoSuiteFile = new EvoSuiteFile(fileName);
        FileSystemHandling.appendStringToFile(evoSuiteFile, expectedContent);
        final Path filePath = new MockFile(fileName).toPath();

        final RandomAccessFileMode readOnlyMode = RandomAccessFileMode.READ_ONLY;

        // Act & Assert: Open the file in read-only mode and verify its contents.
        // Using a try-with-resources statement ensures the file is closed automatically.
        try (RandomAccessFile raf = readOnlyMode.create(filePath)) {
            assertNotNull("The created RandomAccessFile should not be null.", raf);

            final String actualContent = raf.readLine();
            assertEquals("The content read from the file should match the original content.",
                expectedContent, actualContent);

            // Verify that reading past the end of the file returns null.
            assertNull("Reading beyond the file's content should return null.", raf.readLine());
        }
    }
}