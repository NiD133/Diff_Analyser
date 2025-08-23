package org.apache.commons.compress.archivers.ar;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

// The original test used a mock file, which is a good practice to avoid
// actual file system interactions. We'll continue to use it.
import org.evosuite.runtime.mock.java.io.MockFile;

/**
 * An improved test suite for {@link ArArchiveOutputStream}.
 *
 * This version clarifies the intent of the original auto-generated test by
 * using descriptive names, explaining magic numbers, and focusing on a single,
 * clear objective per test.
 */
// The original class name "ArArchiveOutputStream_ESTestTest2" is kept for context,
// but a name like "ArArchiveOutputStreamTest" would be more conventional.
public class ArArchiveOutputStream_ESTestTest2 {

    /**
     * Verifies that the first call to putArchiveEntry writes both the global AR
     * archive header and the specific entry's header to the output stream.
     */
    @Test
    public void firstPutArchiveEntryWritesGlobalAndEntryHeaders() throws IOException {
        // --- Arrange ---
        final String entryName = "test-entry.txt";

        // A mock file is used to create the ArArchiveEntry. Its content is irrelevant
        // for this test, as we are only checking the writing of the headers.
        final File inputFile = new MockFile(entryName);

        // Use a ByteArrayOutputStream to capture the raw bytes of the archive in memory.
        // This is cleaner than writing to a mock file on the file system.
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ArArchiveOutputStream archiveOutputStream = new ArArchiveOutputStream(byteArrayOutputStream);

        final ArArchiveEntry archiveEntry = archiveOutputStream.createArchiveEntry(inputFile, entryName);

        // --- Act ---
        // Add the new entry to the archive. This single action should trigger writing the headers.
        archiveOutputStream.putArchiveEntry(archiveEntry);

        // --- Assert ---
        // The total bytes written should be the sum of the global header and the entry header.
        // 1. Global AR header ("!<arch>\n"): 8 bytes.
        // 2. Standard AR entry header (name, timestamp, uid, gid, mode, size, etc.): 60 bytes.
        final long expectedBytesWritten = 8L + 60L;

        assertEquals("The total bytes written should be the size of the global and entry headers.",
                expectedBytesWritten, archiveOutputStream.getBytesWritten());
        assertEquals("The underlying stream should contain the written header bytes.",
                (int) expectedBytesWritten, byteArrayOutputStream.size());
    }
}