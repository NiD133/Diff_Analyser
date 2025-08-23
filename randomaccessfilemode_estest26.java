package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;

/**
 * Contains tests for the {@link RandomAccessFileMode} enum.
 */
public class RandomAccessFileModeTest {

    /**
     * Tests that {@link RandomAccessFileMode#valueOf(OpenOption...)} correctly identifies
     * the {@code READ_WRITE_SYNC_ALL} mode when {@code StandardOpenOption.SYNC} is present,
     * even if the input array also contains nulls and duplicates.
     */
    @Test
    public void valueOfOpenOptionsWithSyncReturnsReadWriteSyncAll() {
        // Arrange: Create an array of OpenOptions that includes nulls and duplicates
        // to ensure the valueOf method is robust.
        final OpenOption[] openOptions = {
            null,
            StandardOpenOption.SYNC,
            StandardOpenOption.SYNC, // A duplicate to test robustness
            null
        };
        final RandomAccessFileMode expectedMode = RandomAccessFileMode.READ_WRITE_SYNC_ALL;

        // Act: Determine the RandomAccessFileMode from the given options.
        final RandomAccessFileMode actualMode = RandomAccessFileMode.valueOf(openOptions);

        // Assert: The presence of SYNC should result in the READ_WRITE_SYNC_ALL mode.
        assertEquals("The mode should be READ_WRITE_SYNC_ALL for options containing SYNC.",
                expectedMode, actualMode);
    }
}