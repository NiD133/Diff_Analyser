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

public class RandomAccessFileModeTestTest3 {

    private static final byte[] BYTES_FIXTURE = "Foo".getBytes(StandardCharsets.US_ASCII);

    private static final String FIXTURE = "test.txt";

    /**
     * Temporary directory.
     */
    @TempDir
    public Path tempDir;

    private byte[] read(final RandomAccessFile randomAccessFile) throws IOException {
        return RandomAccessFiles.read(randomAccessFile, 0, (int) randomAccessFile.length());
    }

    @ParameterizedTest
    @EnumSource(RandomAccessFileMode.class)
    void testCreateFile(final RandomAccessFileMode randomAccessFileMode) throws IOException {
        final byte[] expected = BYTES_FIXTURE;
        final Path fixture = writeFixture(expected);
        try (RandomAccessFile randomAccessFile = randomAccessFileMode.create(fixture.toFile())) {
            assertArrayEquals(expected, read(randomAccessFile));
        }
    }

    @ParameterizedTest
    @EnumSource(RandomAccessFileMode.class)
    void testCreatePath(final RandomAccessFileMode randomAccessFileMode) throws IOException {
        final byte[] expected = BYTES_FIXTURE;
        final Path fixture = writeFixture(expected);
        randomAccessFileMode.accept(fixture, raf -> assertArrayEquals(expected, read(raf)));
    }

    @ParameterizedTest
    @EnumSource(RandomAccessFileMode.class)
    void testCreateString(final RandomAccessFileMode randomAccessFileMode) throws IOException {
        final byte[] expected = BYTES_FIXTURE;
        final Path fixture = writeFixture(expected);
        try (RandomAccessFile randomAccessFile = randomAccessFileMode.create(fixture.toString())) {
            assertArrayEquals(expected, read(randomAccessFile));
        }
    }

    @ParameterizedTest
    @EnumSource(RandomAccessFileMode.class)
    void testIoString(final RandomAccessFileMode randomAccessFileMode) throws IOException {
        final byte[] expected = BYTES_FIXTURE;
        final Path fixture = writeFixture(expected);
        try (IORandomAccessFile randomAccessFile = randomAccessFileMode.io(fixture.toString())) {
            assertArrayEquals(expected, read(randomAccessFile));
        }
    }

    @ParameterizedTest
    @EnumSource(LinkOption.class)
    void testValueOf(final LinkOption option) {
        assertTrue(RandomAccessFileMode.valueOf(option).implies(RandomAccessFileMode.READ_ONLY));
    }

    @ParameterizedTest
    @EnumSource(StandardOpenOption.class)
    void testValueOf(final StandardOpenOption option) {
        assertTrue(RandomAccessFileMode.valueOf(option).implies(RandomAccessFileMode.READ_ONLY));
    }

    private Path writeFixture(final byte[] bytes) throws IOException {
        return Files.write(tempDir.resolve(FIXTURE), bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    /**
     * Tests the standard {@link Enum#toString()} behavior.
     */
    @Test
    void testToString() {
        assertEquals("READ_ONLY", RandomAccessFileMode.READ_ONLY.toString());
        assertEquals("READ_WRITE", RandomAccessFileMode.READ_WRITE.toString());
        assertEquals("READ_WRITE_SYNC_ALL", RandomAccessFileMode.READ_WRITE_SYNC_ALL.toString());
        assertEquals("READ_WRITE_SYNC_CONTENT", RandomAccessFileMode.READ_WRITE_SYNC_CONTENT.toString());
    }
}
