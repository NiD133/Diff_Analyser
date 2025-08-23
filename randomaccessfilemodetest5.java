package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link RandomAccessFileMode}.
 */
@DisplayName("Tests for RandomAccessFileMode")
public class RandomAccessFileModeTest {

    private static final byte[] TEST_DATA = "Foo".getBytes(StandardCharsets.US_ASCII);
    private static final String TEST_FILE_NAME = "test.txt";

    /**
     * Temporary directory for test files.
     */
    @TempDir
    public Path tempDir;

    /**
     * Reads all bytes from a RandomAccessFile, assuming its length fits in an integer.
     */
    private byte[] readAllBytes(final RandomAccessFile randomAccessFile) throws IOException {
        return RandomAccessFiles.read(randomAccessFile, 0, (int) randomAccessFile.length());
    }

    /**
     * Creates a test file with predefined content in the temporary directory.
     */
    private Path createTestFile() throws IOException {
        return Files.write(tempDir.resolve(TEST_FILE_NAME), TEST_DATA, StandardOpenOption.CREATE);
    }

    @Nested
    @DisplayName("Factory methods for creating RandomAccessFile")
    class FactoryMethodsTest {

        @DisplayName("create(File) should open and read an existing file")
        @ParameterizedTest(name = "Mode: {0}")
        @EnumSource(RandomAccessFileMode.class)
        void create_withFile_opensAndReadsFile(final RandomAccessFileMode mode) throws IOException {
            final Path testFile = createTestFile();
            try (RandomAccessFile randomAccessFile = mode.create(testFile.toFile())) {
                assertArrayEquals(TEST_DATA, readAllBytes(randomAccessFile));
            }
        }

        @DisplayName("accept(Path, consumer) should open and process a file")
        @ParameterizedTest(name = "Mode: {0}")
        @EnumSource(RandomAccessFileMode.class)
        void accept_withPath_opensAndReadsFile(final RandomAccessFileMode mode) throws IOException {
            final Path testFile = createTestFile();
            mode.accept(testFile, raf -> assertArrayEquals(TEST_DATA, readAllBytes(raf)));
        }

        @DisplayName("create(String) should open and read an existing file")
        @ParameterizedTest(name = "Mode: {0}")
        @EnumSource(RandomAccessFileMode.class)
        void create_withStringPath_opensAndReadsFile(final RandomAccessFileMode mode) throws IOException {
            final Path testFile = createTestFile();
            try (RandomAccessFile randomAccessFile = mode.create(testFile.toString())) {
                assertArrayEquals(TEST_DATA, readAllBytes(randomAccessFile));
            }
        }

        @DisplayName("io(String) should open and read an existing file")
        @ParameterizedTest(name = "Mode: {0}")
        @EnumSource(RandomAccessFileMode.class)
        void io_withStringPath_opensAndReadsFile(final RandomAccessFileMode mode) throws IOException {
            final Path testFile = createTestFile();
            try (IORandomAccessFile randomAccessFile = mode.io(testFile.toString())) {
                assertArrayEquals(TEST_DATA, readAllBytes(randomAccessFile));
            }
        }
    }

    @Nested
    @DisplayName("valueOf(OpenOption...) conversion")
    class ValueOfOpenOptionTest {

        static Stream<Arguments> valueOfOpenOptionsProvider() {
            return Stream.of(
                // Default to READ_ONLY
                Arguments.of(RandomAccessFileMode.READ_ONLY, new OpenOption[]{StandardOpenOption.READ}),
                Arguments.of(RandomAccessFileMode.READ_ONLY, new OpenOption[]{StandardOpenOption.APPEND}),
                Arguments.of(RandomAccessFileMode.READ_ONLY, new OpenOption[]{}),

                // READ_WRITE
                Arguments.of(RandomAccessFileMode.READ_WRITE, new OpenOption[]{StandardOpenOption.WRITE}),
                Arguments.of(RandomAccessFileMode.READ_WRITE, new OpenOption[]{StandardOpenOption.READ, StandardOpenOption.WRITE}),

                // READ_WRITE_SYNC_CONTENT (rwd)
                Arguments.of(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT, new OpenOption[]{StandardOpenOption.DSYNC}),
                Arguments.of(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT, new OpenOption[]{StandardOpenOption.WRITE, StandardOpenOption.DSYNC}),
                Arguments.of(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT, new OpenOption[]{StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.DSYNC}),

                // READ_WRITE_SYNC_ALL (rws)
                Arguments.of(RandomAccessFileMode.READ_WRITE_SYNC_ALL, new OpenOption[]{StandardOpenOption.SYNC}),
                Arguments.of(RandomAccessFileMode.READ_WRITE_SYNC_ALL, new OpenOption[]{StandardOpenOption.READ, StandardOpenOption.SYNC}),
                Arguments.of(RandomAccessFileMode.READ_WRITE_SYNC_ALL, new OpenOption[]{StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.SYNC})
            );
        }

        @DisplayName("Should convert StandardOpenOption combinations to the correct mode")
        @ParameterizedTest(name = "valueOf({1}) should be {0}")
        @MethodSource("valueOfOpenOptionsProvider")
        void valueOf_withOpenOptions_returnsCorrectMode(final RandomAccessFileMode expectedMode, final OpenOption... options) {
            assertEquals(expectedMode, RandomAccessFileMode.valueOf(options));
        }
    }
}