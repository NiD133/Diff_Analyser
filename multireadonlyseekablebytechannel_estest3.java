package org.apache.commons.compress.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link MultiReadOnlySeekableByteChannel}.
 * This version of the test is self-contained and does not rely on test generation frameworks.
 */
public class MultiReadOnlySeekableByteChannelTest {

    @Rule
    public final TemporaryFolder tempFolder = new TemporaryFolder();

    /**
     * Tests that positioning the channel using a specific channel index and a relative offset
     * correctly calculates the overall global position. The global position should be the sum of
     * the sizes of all preceding channels plus the relative offset in the target channel.
     */
    @Test
    public void shouldCalculateCorrectGlobalPositionWhenPositioningByChannelAndOffset() throws IOException {
        // Arrange
        // 1. Define file sizes and the target offset to make the calculation clear.
        final long firstFileSize = 704L;
        final long offsetInSecondFile = 1L;
        final long expectedGlobalPosition = firstFileSize + offsetInSecondFile;

        // 2. Create two temporary files with specific sizes.
        File firstFile = tempFolder.newFile("first.txt");
        Files.write(firstFile.toPath(), new byte[(int) firstFileSize]);

        File secondFile = tempFolder.newFile("second.txt");
        // The second file just needs to be large enough to seek to the desired offset.
        Files.write(secondFile.toPath(), new byte[(int) offsetInSecondFile + 10]);

        File[] files = {firstFile, secondFile};

        // The try-with-resources statement ensures the channel is closed automatically.
        // We cast to the concrete class to access the specific `position(long, long)` method.
        try (MultiReadOnlySeekableByteChannel channel =
                     (MultiReadOnlySeekableByteChannel) MultiReadOnlySeekableByteChannel.forFiles(files)) {

            // Act
            // Position the channel at an offset of 1 byte into the second file (index 1).
            channel.position(1, offsetInSecondFile);
            long actualGlobalPosition = channel.position();

            // Assert
            // The global position should equal the size of the first file plus the offset into the second.
            assertEquals("The global position was not calculated correctly.",
                expectedGlobalPosition, actualGlobalPosition);
        }
    }
}