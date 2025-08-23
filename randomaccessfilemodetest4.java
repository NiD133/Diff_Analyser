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
 * Tests for the {@link RandomAccessFileMode} enum.
 *
 * <p>This test suite verifies that:
 * <ul>
 *   <li>File creation and access methods correctly open a readable file, regardless of the mode.</li>
 *   <li>Static factory methods return the expected {@link RandomAccessFileMode} instances.</li>
 * </ul>
 */
@DisplayName("Tests for RandomAccessFileMode")
public class RandomAccessFileModeTest {

    private static final byte[] TEST_CONTENT_BYTES = "Foo".getBytes(StandardCharsets.US_ASCII);
    private static final String TEST_FILE_NAME = "test.txt";

    /**
     * Temporary directory provided by JUnit.
     */
    @TempDir
    public Path tempDir;

    /**
     * Creates a test file in the temporary directory with predefined content.
     *
     * @return The path to the newly created test file.
     * @throws IOException if an I/O error occurs.
     */
    private Path createTestFile() throws IOException {
        final Path testFilePath = tempDir.resolve(TEST_FILE_NAME);
        return Files.write(testFilePath, TEST_CONTENT_BYTES, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    /**
     * Reads all bytes from a RandomAccessFile.
     */
    private byte[] readAllBytes(final RandomAccessFile randomAccessFile) throws IOException {
        return RandomAccessFiles.read(randomAccessFile, 0, (int) randomAccessFile.length());
    }

    /**
     * Asserts that the content of the given RandomAccessFile matches the predefined test content.
     */
    private void assertFileContentIsReadable(final RandomAccessFile raf) throws IOException {
        assertArrayEquals(TEST_CONTENT_BYTES, readAllBytes(raf));
    }

    /**
     * Tests for methods that create or provide access to a RandomAccessFile.
     * These tests ensure that for any given mode, an existing file can be opened and its contents read.
     */
    @Nested
    @DisplayName("File Creation and Access Methods")
    class CreationAndAccessTests {

        @ParameterizedTest
        @EnumSource(RandomAccessFileMode.class)
        @DisplayName("create(File) should open a readable file for all modes")
        void createWithFile_shouldOpenReadableFile(final RandomAccessFileMode mode) throws IOException {
            final Path testFile = createTestFile();
            try (RandomAccessFile randomAccessFile = mode.create(testFile.toFile())) {
                assertFileContentIsReadable(randomAccessFile);
            }
        }

        @ParameterizedTest
        @EnumSource(RandomAccessFileMode.class)
        @DisplayName("accept(Path, IOConsumer) should provide a readable file for all modes")
        void acceptWithPath_shouldProvideReadableFile(final RandomAccessFileMode mode) throws IOException {
            final Path testFile = createTestFile();
            // The 'accept' method handles resource management, so no try-with-resources is needed.
            mode.accept(testFile, RandomAccessFileModeTest.this::assertFileContentIsReadable);
        }

        @ParameterizedTest
        @EnumSource(RandomAccessFileMode.class)
        @DisplayName("create(String) should open a readable file for all modes")
        void createWithStringPath_shouldOpenReadableFile(final RandomAccessFileMode mode) throws IOException {
            final Path testFile = createTestFile();
            try (RandomAccessFile randomAccessFile = mode.create(testFile.toString())) {
                assertFileContentIsReadable(randomAccessFile);
            }
        }

        @ParameterizedTest
        @EnumSource(RandomAccessFileMode.class)
        @DisplayName("io(String) should open a readable file for all modes")
        void ioWithStringPath_shouldOpenReadableFile(final RandomAccessFileMode mode) throws IOException {
            final Path testFile = createTestFile();
            try (IORandomAccessFile randomAccessFile = mode.io(testFile.toString())) {
                assertFileContentIsReadable(randomAccessFile);
            }
        }
    }

    /**
     * Tests for the static factory methods of RandomAccessFileMode.
     */
    @Nested
    @DisplayName("Factory Methods")
    class FactoryMethodTests {

        @Test
        @DisplayName("valueOfMode() should return correct enum for valid mode strings")
        void valueOfMode_shouldReturnCorrectEnumForValidStrings() {
            assertEquals(RandomAccessFileMode.READ_ONLY, RandomAccessFileMode.valueOfMode("r"));
            assertEquals(RandomAccessFileMode.READ_WRITE, RandomAccessFileMode.valueOfMode("rw"));
            assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT, RandomAccessFileMode.valueOfMode("rwd"));
            assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_ALL, RandomAccessFileMode.valueOfMode("rws"));
        }

        /**
         * Tests that valueOf(OpenOption...) returns a mode that at least allows reading.
         * According to the Javadoc, the default returned mode is READ_ONLY, so all
         * resulting modes should imply read access.
         */
        @ParameterizedTest
        @EnumSource(StandardOpenOption.class)
        @DisplayName("valueOf(StandardOpenOption) should return a mode with at least read access")
        void valueOf_withStandardOpenOption_shouldImplyReadOnly(final StandardOpenOption option) {
            final RandomAccessFileMode mode = RandomAccessFileMode.valueOf(option);
            assertTrue(mode.implies(RandomAccessFileMode.READ_ONLY),
                () -> "Mode " + mode + " for option " + option + " should imply READ_ONLY");
        }

        /**
         * Tests that valueOf(OpenOption...) returns a mode that at least allows reading.
         * According to the Javadoc, the default returned mode is READ_ONLY, so all
         * resulting modes should imply read access.
         */
        @ParameterizedTest
        @EnumSource(LinkOption.class)
        @DisplayName("valueOf(LinkOption) should return a mode with at least read access")
        void valueOf_withLinkOption_shouldImplyReadOnly(final LinkOption option) {
            final RandomAccessFileMode mode = RandomAccessFileMode.valueOf(option);
            assertTrue(mode.implies(RandomAccessFileMode.READ_ONLY),
                () -> "Mode " + mode + " for option " + option + " should imply READ_ONLY");
        }
    }
}