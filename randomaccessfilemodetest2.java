package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
 * Tests for the {@link RandomAccessFileMode} enum.
 */
@DisplayName("Tests for RandomAccessFileMode")
public class RandomAccessFileModeTest {

    private static final byte[] TEST_DATA = "Foo".getBytes(StandardCharsets.US_ASCII);
    private static final String TEST_FILE_NAME = "test.txt";

    /**
     * Temporary directory managed by JUnit.
     */
    @TempDir
    public Path tempDir;

    /**
     * Creates a test file with predefined content in the temp directory.
     *
     * @return The path to the newly created file.
     * @throws IOException if an I/O error occurs.
     */
    private Path createTestFileWithContent() throws IOException {
        return Files.write(tempDir.resolve(TEST_FILE_NAME), TEST_DATA, StandardOpenOption.CREATE);
    }

    /**
     * Reads all bytes from a RandomAccessFile, assuming the file pointer can be reset.
     *
     * @param randomAccessFile The file to read from.
     * @return The contents of the file as a byte array.
     * @throws IOException if an I/O error occurs.
     */
    private byte[] readAllBytes(final RandomAccessFile randomAccessFile) throws IOException {
        randomAccessFile.seek(0);
        final byte[] bytes = new byte[(int) randomAccessFile.length()];
        randomAccessFile.readFully(bytes);
        return bytes;
    }

    @Nested
    @DisplayName("File Creation and Accessor Method Tests")
    class FileAccessTests {

        @ParameterizedTest
        @EnumSource(RandomAccessFileMode.class)
        @DisplayName("create(File) should open an existing file and allow reading its content")
        void createFromFile_whenFileExists_thenContentIsReadable(final RandomAccessFileMode mode) throws IOException {
            final Path testFile = createTestFileWithContent();
            try (RandomAccessFile raf = mode.create(testFile.toFile())) {
                assertArrayEquals(TEST_DATA, readAllBytes(raf));
            }
        }

        @ParameterizedTest
        @EnumSource(RandomAccessFileMode.class)
        @DisplayName("create(Path) should open an existing file and allow reading its content")
        void createFromPath_whenFileExists_thenContentIsReadable(final RandomAccessFileMode mode) throws IOException {
            final Path testFile = createTestFileWithContent();
            try (RandomAccessFile raf = mode.create(testFile)) {
                assertArrayEquals(TEST_DATA, readAllBytes(raf));
            }
        }

        @ParameterizedTest
        @EnumSource(RandomAccessFileMode.class)
        @DisplayName("create(String) should open an existing file and allow reading its content")
        void createFromStringPath_whenFileExists_thenContentIsReadable(final RandomAccessFileMode mode) throws IOException {
            final Path testFile = createTestFileWithContent();
            try (RandomAccessFile raf = mode.create(testFile.toString())) {
                assertArrayEquals(TEST_DATA, readAllBytes(raf));
            }
        }

        @ParameterizedTest
        @EnumSource(RandomAccessFileMode.class)
        @DisplayName("io(String) should open an existing file and allow reading its content")
        void ioFromStringPath_whenFileExists_thenContentIsReadable(final RandomAccessFileMode mode) throws IOException {
            final Path testFile = createTestFileWithContent();
            try (IORandomAccessFile raf = mode.io(testFile.toString())) {
                assertArrayEquals(TEST_DATA, readAllBytes(raf));
            }
        }

        @ParameterizedTest
        @EnumSource(RandomAccessFileMode.class)
        @DisplayName("accept(Path, Consumer) should provide a readable RandomAccessFile to the consumer")
        void acceptWithConsumer_whenFileExists_thenContentIsReadable(final RandomAccessFileMode mode) throws IOException {
            final Path testFile = createTestFileWithContent();
            mode.accept(testFile, raf -> assertArrayEquals(TEST_DATA, readAllBytes(raf)));
        }
    }

    @Nested
    @DisplayName("valueOf(OpenOption...) Factory Method Tests")
    class ValueOfTests {

        // The SUT Javadoc states: "Gets the enum value that best fits the given OpenOption... by default READ_ONLY."
        // This test verifies that for any single option, the resulting mode is at least READ_ONLY,
        // which aligns with the documented safe default behavior.
        @ParameterizedTest
        @EnumSource(LinkOption.class)
        @DisplayName("valueOf() with any LinkOption should result in a mode that is at least read-only")
        void valueOf_withAnyLinkOption_isAtLeastReadOnly(final LinkOption option) {
            final RandomAccessFileMode mode = RandomAccessFileMode.valueOf(option);
            assertTrue(mode.implies(RandomAccessFileMode.READ_ONLY), "Mode for " + option + " should imply READ_ONLY");
        }

        @ParameterizedTest
        @EnumSource(StandardOpenOption.class)
        @DisplayName("valueOf() with any StandardOpenOption should result in a mode that is at least read-only")
        void valueOf_withAnyStandardOpenOption_isAtLeastReadOnly(final StandardOpenOption option) {
            final RandomAccessFileMode mode = RandomAccessFileMode.valueOf(option);
            assertTrue(mode.implies(RandomAccessFileMode.READ_ONLY), "Mode for " + option + " should imply READ_ONLY");
        }
    }

    @Nested
    @DisplayName("implies() Method Tests")
    class ImpliesTests {

        @Test
        @DisplayName("implies() should correctly reflect the permission hierarchy")
        void implies_shouldReflectPermissionHierarchy() {
            // Test the chain of implications from most to least permissive: RWS -> RWD -> RW -> R
            assertTrue(RandomAccessFileMode.READ_WRITE_SYNC_ALL.implies(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT), "RWS should imply RWD");
            assertTrue(RandomAcscessFileMode.READ_WRITE_SYNC_CONTENT.implies(RandomAccessFileMode.READ_WRITE), "RWD should imply RW");
            assertTrue(RandomAccessFileMode.READ_WRITE.implies(RandomAccessFileMode.READ_ONLY), "RW should imply R");

            // Test a negative case: a less permissive mode should not imply a more permissive one.
            assertFalse(RandomAccessFileMode.READ_ONLY.implies(RandomAccessFileMode.READ_WRITE_SYNC_ALL), "R should not imply RWS");
        }
    }
}