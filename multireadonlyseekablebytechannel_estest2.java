package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Test suite for {@link MultiReadOnlySeekableByteChannel}.
 * This class focuses on verifying the behavior of the size() method.
 */
public class MultiReadOnlySeekableByteChannelTest {

    // JUnit rule to create and automatically clean up temporary files and folders.
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    /**
     * Tests that the size() method returns the combined total size of all concatenated files.
     */
    @Test
    public void sizeShouldReturnSumOfIndividualFileSizes() throws IOException {
        // --- Arrange ---
        // Create two temporary files with distinct, known content.
        File file1 = tempFolder.newFile("file1.txt");
        Files.write(file1.toPath(), "hello".getBytes(StandardCharsets.UTF_8)); // 5 bytes

        File file2 = tempFolder.newFile("file2.txt");
        Files.write(file2.toPath(), "world!".getBytes(StandardCharsets.UTF_8)); // 6 bytes

        File[] filesToCombine = { file1, file2 };
        long expectedTotalSize = file1.length() + file2.length(); // 5 + 6 = 11 bytes

        // --- Act ---
        // Create the multi-channel and get its size.
        // Using try-with-resources to ensure the channel is closed automatically.
        try (SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forFiles(filesToCombine)) {
            long actualTotalSize = channel.size();

            // --- Assert ---
            // Verify that the channel's size is the sum of the individual file sizes.
            assertEquals("The total size should be the sum of the input file sizes.",
                expectedTotalSize, actualTotalSize);
        }
    }
}