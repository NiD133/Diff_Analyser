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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * Tests for the {@link RandomAccessFileMode} enum.
 *
 * This test suite is structured to verify:
 * 1. Correct file opening and readability via various factory methods.
 * 2. Correct conversion from NIO options using {@code valueOf()}.
 * 3. Correct string representation from {@code getMode()}.
 */
@DisplayName("Tests for RandomAccessFileMode")
public class RandomAccessFileModeTest {

    // Constants are given more descriptive names for better clarity.
    private static final String TEST_FILE_NAME = "test.txt";
    private static final byte[] TEST_DATA = "Foo".getBytes(StandardCharsets.US_ASCII);

    @TempDir
    private Path tempDir;

    /**
     * Helper to read all bytes from a RandomAccessFile for use in assertions.
     */
    private byte[] readAllBytes(final RandomAccessFile randomAccessFile) throws IOException {
        return RandomAccessFiles.read(randomAccessFile, 0, (int) randomAccessFile.length());
    }

    /**
     * Groups tests for factory methods that open files. Using @Nested improves
     * structure and makes test reports more readable.
     */
    @Nested
    @DisplayName("Factory methods for opening files")
    class FactoryMethodsTest {

        private Path testFile;

        /**
         * Creates a test file before each test in this nested class, reducing code duplication.
         */
        @BeforeEach
        void setUp() throws IOException {
            testFile = Files.write(tempDir.resolve(TEST_FILE_NAME), TEST_DATA, StandardOpenOption.CREATE);
        }

        @ParameterizedTest
        @EnumSource(RandomAccessFileMode.class)
        @DisplayName("create(File) should open a readable file")
        void testCreateWithFile(final RandomAccessFileMode mode) throws IOException {
            try (RandomAccessFile randomAccessFile = mode.create(testFile.toFile())) {
                assertArrayEquals(TEST_DATA, readAllBytes(randomAccessFile));
            }
        }

        /**
         * This test was missing from the original suite, improving test coverage for the create(Path) method.
         */
        @ParameterizedTest
        @EnumSource(RandomAccessFileMode.class)
        @DisplayName("create(Path) should open a readable file")
        void testCreateWithPath(final RandomAccessFileMode mode) throws IOException {
            try (RandomAccessFile randomAccessFile = mode.create(testFile)) {
                assertArrayEquals(TEST_DATA, readAllBytes(randomAccessFile));
            }
        }

        /**
         * The original test was misnamed ('testCreatePath'). It is now correctly named to reflect the 'accept' method it tests.
         */
        @ParameterizedTest
        @EnumSource(RandomAccessFileMode.class)
        @DisplayName("accept(Path, consumer) should provide a readable file")
        void testAccept(final RandomAccessFileMode mode) throws IOException {
            mode.accept(testFile, raf -> assertArrayEquals(TEST_DATA, readAllBytes(raf)));
        }

        @ParameterizedTest
        @EnumSource(RandomAccessFileMode.class)
        @DisplayName("create(String) should open a readable file")
        void testCreateWithString(final RandomAccessFileMode mode) throws IOException {
            try (RandomAccessFile randomAccessFile = mode.create(testFile.toString())) {
                assertArrayEquals(TEST_DATA, readAllBytes(randomAccessFile));
            }
        }

        @ParameterizedTest
        @EnumSource(RandomAccessFileMode.class)
        @DisplayName("io(String) should open a readable file")
        void testIoWithString(final RandomAccessFileMode mode) throws IOException {
            try (RandomAccessFile randomAccessFile = mode.io(testFile.toString())) {
                assertArrayEquals(TEST_DATA, readAllBytes(randomAccessFile));
            }
        }
    }

    /**
     * Groups tests related to the valueOf() conversion logic.
     */
    @Nested
    @DisplayName("valueOf() conversion")
    class ValueOfTest {

        @ParameterizedTest
        @EnumSource(LinkOption.class)
        @DisplayName("valueOf(LinkOption) should return a mode that implies read-only")
        void testValueOfLinkOption(final LinkOption option) {
            final RandomAccessFileMode mode = RandomAccessFileMode.valueOf(option);
            assertTrue(mode.implies(RandomAccessFileMode.READ_ONLY),
                () -> "Expected mode for " + option + " to imply READ_ONLY, but was " + mode);
        }

        @ParameterizedTest
        @EnumSource(StandardOpenOption.class)
        @DisplayName("valueOf(StandardOpenOption) should return a mode that implies read-only")
        void testValueOfStandardOpenOption(final StandardOpenOption option) {
            final RandomAccessFileMode mode = RandomAccessFileMode.valueOf(option);
            assertTrue(mode.implies(RandomAccessFileMode.READ_ONLY),
                () -> "Expected mode for " + option + " to imply READ_ONLY, but was " + mode);
        }
    }

    @Test
    @DisplayName("getMode() should return the correct mode string")
    void testGetMode() {
        assertEquals("r", RandomAccessFileMode.READ_ONLY.getMode());
        assertEquals("rw", RandomAccessFileMode.READ_WRITE.getMode());
        assertEquals("rwd", RandomAccessFileMode.READ_WRITE_SYNC_CONTENT.getMode());
        assertEquals("rws", RandomAccessFileMode.READ_WRITE_SYNC_ALL.getMode());
    }
}