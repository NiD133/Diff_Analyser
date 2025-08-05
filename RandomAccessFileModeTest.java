package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * Unit tests for {@link RandomAccessFileMode}.
 */
class RandomAccessFileModeTest {

    private static final byte[] SAMPLE_BYTES = "Foo".getBytes(StandardCharsets.US_ASCII);
    private static final String SAMPLE_FILENAME = "test.txt";

    /**
     * Temporary directory for test files.
     */
    @TempDir
    public Path temporaryDirectory;

    /**
     * Reads all bytes from the given RandomAccessFile.
     *
     * @param randomAccessFile the file to read from
     * @return the bytes read from the file
     * @throws IOException if an I/O error occurs
     */
    private byte[] readAllBytes(final RandomAccessFile randomAccessFile) throws IOException {
        return RandomAccessFiles.read(randomAccessFile, 0, (int) randomAccessFile.length());
    }

    /**
     * Tests creating a file using different RandomAccessFileModes.
     *
     * @param mode the RandomAccessFileMode to test
     * @throws IOException if an I/O error occurs
     */
    @ParameterizedTest
    @EnumSource(RandomAccessFileMode.class)
    void testCreateFile(final RandomAccessFileMode mode) throws IOException {
        final byte[] expectedBytes = SAMPLE_BYTES;
        final Path filePath = createTestFile(expectedBytes);
        
        try (RandomAccessFile randomAccessFile = mode.create(filePath.toFile())) {
            assertArrayEquals(expectedBytes, readAllBytes(randomAccessFile));
        }
    }

    /**
     * Tests creating a file using a Path and different RandomAccessFileModes.
     *
     * @param mode the RandomAccessFileMode to test
     * @throws IOException if an I/O error occurs
     */
    @ParameterizedTest
    @EnumSource(RandomAccessFileMode.class)
    void testCreatePath(final RandomAccessFileMode mode) throws IOException {
        final byte[] expectedBytes = SAMPLE_BYTES;
        final Path filePath = createTestFile(expectedBytes);
        
        mode.accept(filePath, raf -> assertArrayEquals(expectedBytes, readAllBytes(raf)));
    }

    /**
     * Tests creating a file using a String filename and different RandomAccessFileModes.
     *
     * @param mode the RandomAccessFileMode to test
     * @throws IOException if an I/O error occurs
     */
    @ParameterizedTest
    @EnumSource(RandomAccessFileMode.class)
    void testCreateString(final RandomAccessFileMode mode) throws IOException {
        final byte[] expectedBytes = SAMPLE_BYTES;
        final Path filePath = createTestFile(expectedBytes);
        
        try (RandomAccessFile randomAccessFile = mode.create(filePath.toString())) {
            assertArrayEquals(expectedBytes, readAllBytes(randomAccessFile));
        }
    }

    /**
     * Tests the mode strings of RandomAccessFileMode.
     */
    @Test
    void testGetMode() {
        assertEquals("r", RandomAccessFileMode.READ_ONLY.getMode());
        assertEquals("rw", RandomAccessFileMode.READ_WRITE.getMode());
        assertEquals("rwd", RandomAccessFileMode.READ_WRITE_SYNC_CONTENT.getMode());
        assertEquals("rws", RandomAccessFileMode.READ_WRITE_SYNC_ALL.getMode());
    }

    /**
     * Tests the implies method of RandomAccessFileMode.
     */
    @Test
    void testImplies() {
        assertTrue(RandomAccessFileMode.READ_WRITE_SYNC_ALL.implies(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT));
        assertTrue(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT.implies(RandomAccessFileMode.READ_WRITE));
        assertTrue(RandomAccessFileMode.READ_WRITE.implies(RandomAccessFileMode.READ_ONLY));
        assertFalse(RandomAccessFileMode.READ_ONLY.implies(RandomAccessFileMode.READ_WRITE_SYNC_ALL));
    }

    /**
     * Tests creating a file using IORandomAccessFile and different RandomAccessFileModes.
     *
     * @param mode the RandomAccessFileMode to test
     * @throws IOException if an I/O error occurs
     */
    @ParameterizedTest
    @EnumSource(RandomAccessFileMode.class)
    void testIoString(final RandomAccessFileMode mode) throws IOException {
        final byte[] expectedBytes = SAMPLE_BYTES;
        final Path filePath = createTestFile(expectedBytes);
        
        try (IORandomAccessFile randomAccessFile = mode.io(filePath.toString())) {
            assertArrayEquals(expectedBytes, readAllBytes(randomAccessFile));
        }
    }

    /**
     * Tests the standard toString behavior of RandomAccessFileMode.
     */
    @Test
    void testToString() {
        assertEquals("READ_ONLY", RandomAccessFileMode.READ_ONLY.toString());
        assertEquals("READ_WRITE", RandomAccessFileMode.READ_WRITE.toString());
        assertEquals("READ_WRITE_SYNC_ALL", RandomAccessFileMode.READ_WRITE_SYNC_ALL.toString());
        assertEquals("READ_WRITE_SYNC_CONTENT", RandomAccessFileMode.READ_WRITE_SYNC_CONTENT.toString());
    }

    /**
     * Tests the valueOf method with LinkOption.
     *
     * @param option the LinkOption to test
     */
    @ParameterizedTest
    @EnumSource(LinkOption.class)
    void testValueOfLinkOption(final LinkOption option) {
        assertTrue(RandomAccessFileMode.valueOf(option).implies(RandomAccessFileMode.READ_ONLY));
    }

    /**
     * Tests the valueOf method with StandardOpenOption.
     *
     * @param option the StandardOpenOption to test
     */
    @ParameterizedTest
    @EnumSource(StandardOpenOption.class)
    void testValueOfStandardOpenOption(final StandardOpenOption option) {
        assertTrue(RandomAccessFileMode.valueOf(option).implies(RandomAccessFileMode.READ_ONLY));
    }

    /**
     * Tests the valueOfMode method.
     */
    @Test
    void testValueOfMode() {
        assertEquals(RandomAccessFileMode.READ_ONLY, RandomAccessFileMode.valueOfMode("r"));
        assertEquals(RandomAccessFileMode.READ_WRITE, RandomAccessFileMode.valueOfMode("rw"));
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT, RandomAccessFileMode.valueOfMode("rwd"));
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_ALL, RandomAccessFileMode.valueOfMode("rws"));
    }

    /**
     * Tests the valueOf method with OpenOptions.
     */
    @Test
    void testValueOfOpenOptions() {
        // READ_ONLY
        assertEquals(RandomAccessFileMode.READ_ONLY, RandomAccessFileMode.valueOf(StandardOpenOption.READ));
        // READ_WRITE
        assertEquals(RandomAccessFileMode.READ_WRITE, RandomAccessFileMode.valueOf(StandardOpenOption.WRITE));
        assertEquals(RandomAccessFileMode.READ_WRITE, RandomAccessFileMode.valueOf(StandardOpenOption.READ, StandardOpenOption.WRITE));
        // READ_WRITE_SYNC_CONTENT
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT, RandomAccessFileMode.valueOf(StandardOpenOption.DSYNC));
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT, RandomAccessFileMode.valueOf(StandardOpenOption.WRITE, StandardOpenOption.DSYNC));
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT,
                RandomAccessFileMode.valueOf(StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.DSYNC));
        // READ_WRITE_SYNC_ALL
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_ALL, RandomAccessFileMode.valueOf(StandardOpenOption.SYNC));
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_ALL, RandomAccessFileMode.valueOf(StandardOpenOption.READ, StandardOpenOption.SYNC));
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_ALL,
                RandomAccessFileMode.valueOf(StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.SYNC));
    }

    /**
     * Creates a test file with the given bytes.
     *
     * @param bytes the bytes to write to the file
     * @return the path to the created file
     * @throws IOException if an I/O error occurs
     */
    private Path createTestFile(final byte[] bytes) throws IOException {
        return Files.write(temporaryDirectory.resolve(SAMPLE_FILENAME), bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}