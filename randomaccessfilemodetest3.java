package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * Tests for {@link RandomAccessFileMode}.
 */
class RandomAccessFileModeTest {

    private static final byte[] TEST_CONTENT = "Foo".getBytes(StandardCharsets.US_ASCII);
    private static final String TEST_FILE_NAME = "test.txt";

    /**
     * Temporary directory for test files.
     */
    @TempDir
    public Path tempDir;

    /**
     * Reads all bytes from a RandomAccessFile starting from the beginning.
     */
    private byte[] readAllBytes(final RandomAccessFile randomAccessFile) throws IOException {
        return RandomAccessFiles.read(randomAccessFile, 0, (int) randomAccessFile.length());
    }

    /**
     * Creates a test file with the given content in the temporary directory.
     */
    private Path createTestFile(final byte[] content) throws IOException {
        return Files.write(tempDir.resolve(TEST_FILE_NAME), content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Nested
    @DisplayName("File Access Method Tests")
    class FileAccessTests {

        @ParameterizedTest
        @EnumSource(RandomAccessFileMode.class)
        @DisplayName("create(File) should open a file that can be read")
        void create_withFile_shouldOpenFileForReading(final RandomAccessFileMode mode) throws IOException {
            // Arrange
            final Path testFile = createTestFile(TEST_CONTENT);

            // Act & Assert
            try (RandomAccessFile randomAccessFile = mode.create(testFile.toFile())) {
                assertArrayEquals(TEST_CONTENT, readAllBytes(randomAccessFile));
            }
        }

        @ParameterizedTest
        @EnumSource(RandomAccessFileMode.class)
        @DisplayName("accept(Path, Consumer) should open a file that can be read")
        void accept_withPath_shouldOpenFileForReading(final RandomAccessFileMode mode) throws IOException {
            // Arrange
            final Path testFile = createTestFile(TEST_CONTENT);

            // Act & Assert
            mode.accept(testFile, raf -> assertArrayEquals(TEST_CONTENT, readAllBytes(raf)));
        }

        @ParameterizedTest
        @EnumSource(RandomAccessFileMode.class)
        @DisplayName("create(String) should open a file that can be read")
        void create_withStringPath_shouldOpenFileForReading(final RandomAccessFileMode mode) throws IOException {
            // Arrange
            final Path testFile = createTestFile(TEST_CONTENT);

            // Act & Assert
            try (RandomAccessFile randomAccessFile = mode.create(testFile.toString())) {
                assertArrayEquals(TEST_CONTENT, readAllBytes(randomAccessFile));
            }
        }

        @ParameterizedTest
        @EnumSource(RandomAccessFileMode.class)
        @DisplayName("io(String) should open a file that can be read")
        void io_withStringPath_shouldOpenFileForReading(final RandomAccessFileMode mode) throws IOException {
            // Arrange
            final Path testFile = createTestFile(TEST_CONTENT);

            // Act & Assert
            try (IORandomAccessFile randomAccessFile = mode.io(testFile.toString())) {
                assertArrayEquals(TEST_CONTENT, readAllBytes(randomAccessFile));
            }
        }
    }

    @Nested
    @DisplayName("valueOf() Factory Method Tests")
    class ValueOfTests {

        @ParameterizedTest
        @EnumSource(LinkOption.class)
        @DisplayName("valueOf(LinkOption) should return a mode that implies read-only")
        void valueOf_forLinkOption_returnsModeImplyingReadOnly(final LinkOption option) {
            // Arrange
            final RandomAccessFileMode mode = RandomAccessFileMode.valueOf(option);

            // Act & Assert
            // All modes, including write modes, imply read-only access.
            // This test verifies that valueOf() returns a valid, non-null mode.
            assertTrue(mode.implies(RandomAccessFileMode.READ_ONLY),
                () -> "Mode " + mode + " for option " + option + " should imply READ_ONLY");
        }

        @ParameterizedTest
        @EnumSource(StandardOpenOption.class)
        @DisplayName("valueOf(StandardOpenOption) should return a mode that implies read-only")
        void valueOf_forStandardOpenOption_returnsModeImplyingReadOnly(final StandardOpenOption option) {
            // Arrange
            final RandomAccessFileMode mode = RandomAccessFileMode.valueOf(option);

            // Act & Assert
            // All modes, including write modes, imply read-only access.
            // This test verifies that valueOf() returns a valid, non-null mode.
            assertTrue(mode.implies(RandomAccessFileMode.READ_ONLY),
                () -> "Mode " + mode + " for option " + option + " should imply READ_ONLY");
        }
    }

    @Test
    @DisplayName("toString() should return the enum constant's name")
    void toString_shouldReturnEnumName() {
        assertEquals("READ_ONLY", RandomAccessFileMode.READ_ONLY.toString());
        assertEquals("READ_WRITE", RandomAccessFileMode.READ_WRITE.toString());
        assertEquals("READ_WRITE_SYNC_ALL", RandomAccessFileMode.READ_WRITE_SYNC_ALL.toString());
        assertEquals("READ_WRITE_SYNC_CONTENT", RandomAccessFileMode.READ_WRITE_SYNC_CONTENT.toString());
    }
}