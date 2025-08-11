package org.apache.commons.compress.utils;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.file.StandardOpenOption.READ;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.NonWritableChannelException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Files;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Focused and readable tests for MultiReadOnlySeekableByteChannel.
 * 
 * These tests use small on-disk files to validate:
 * - concatenation of multiple channels/files
 * - positioning behavior (absolute and by segment index)
 * - read-only semantics (write/truncate not allowed)
 * - closed-channel behavior
 * - factory method contracts (null and missing inputs)
 */
public class MultiReadOnlySeekableByteChannelTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    @Test
    public void concatenatesFiles_sizeAndReadAcrossSegments() throws Exception {
        File f1 = tmp.newFile("part1.bin");
        File f2 = tmp.newFile("part2.bin");
        Files.write(f1.toPath(), "ABC".getBytes(US_ASCII));
        Files.write(f2.toPath(), "DE".getBytes(US_ASCII));

        try (SeekableByteChannel ch = MultiReadOnlySeekableByteChannel.forFiles(f1, f2)) {
            assertEquals("Total size must be the sum of all parts", 5L, ch.size());
            byte[] all = readAll(ch);
            assertArrayEquals("Merged content must preserve order",
                    "ABCDE".getBytes(US_ASCII), all);
        }
    }

    @Test
    public void positionWithinAndAcrossSegments() throws Exception {
        File f1 = tmp.newFile("part1.bin");
        File f2 = tmp.newFile("part2.bin");
        Files.write(f1.toPath(), "ABC".getBytes(US_ASCII));
        Files.write(f2.toPath(), "DE".getBytes(US_ASCII));

        try (MultiReadOnlySeekableByteChannel ch =
                     (MultiReadOnlySeekableByteChannel) MultiReadOnlySeekableByteChannel.forFiles(f1, f2)) {

            // Absolute position near the end
            ch.position(4L);
            assertEquals(4L, ch.position());
            assertEquals('E', (char) readOne(ch));

            // Segment-based position: channel #1 (second file), offset 0 -> global pos 3
            ch.position(1L, 0L);
            assertEquals(3L, ch.position());
            assertEquals('D', (char) readOne(ch));

            // Segment-based position: channel #1, offset 1 -> global pos 4
            ch.position(1L, 1L);
            assertEquals(4L, ch.position());
            assertEquals('E', (char) readOne(ch));
        }
    }

    @Test
    public void readOnlySemantics_writeAndTruncateThrow() {
        MultiReadOnlySeekableByteChannel ch =
                new MultiReadOnlySeekableByteChannel(java.util.Collections.emptyList());

        try {
            ch.write(ByteBuffer.allocate(1));
            fail("write must throw NonWritableChannelException");
        } catch (NonWritableChannelException expected) {
        }

        try {
            ch.truncate(0);
            fail("truncate must throw NonWritableChannelException");
        } catch (NonWritableChannelException expected) {
        }
    }

    @Test
    public void closedChannel_behavior() throws Exception {
        File f1 = tmp.newFile("p1.bin");
        File f2 = tmp.newFile("p2.bin");
        Files.write(f1.toPath(), "ABC".getBytes(US_ASCII));
        Files.write(f2.toPath(), "DE".getBytes(US_ASCII));

        MultiReadOnlySeekableByteChannel ch =
                (MultiReadOnlySeekableByteChannel) MultiReadOnlySeekableByteChannel.forFiles(f1, f2);

        ch.position(3L); // remember last known position
        ch.close();

        assertFalse("Channel should be closed", ch.isOpen());
        assertEquals("Querying position() on a closed channel returns last known position",
                3L, ch.position());

        try {
            ch.size();
            fail("size on closed channel must throw ClosedChannelException");
        } catch (ClosedChannelException expected) {
        }

        try {
            ch.position(0L);
            fail("position(long) on closed channel must throw ClosedChannelException");
        } catch (ClosedChannelException expected) {
        }

        try {
            ch.read(ByteBuffer.allocate(1));
            fail("read on closed channel must throw ClosedChannelException");
        } catch (ClosedChannelException expected) {
        }
    }

    @Test
    public void invalidPositionsThrow() throws Exception {
        MultiReadOnlySeekableByteChannel ch =
                new MultiReadOnlySeekableByteChannel(java.util.Collections.emptyList());

        try {
            ch.position(-1L);
            fail("Negative absolute position must throw IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }

        try {
            ch.position(0L, -1L);
            fail("Negative relative offset must throw IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    public void factory_forFiles_nullArrayThrows() throws Exception {
        try {
            MultiReadOnlySeekableByteChannel.forFiles((File[]) null);
            fail("Null file array must throw NullPointerException");
        } catch (NullPointerException expected) {
        }
    }

    @Test
    public void factory_forFiles_missingFileThrows() throws Exception {
        File missing = new File(tmp.getRoot(), "does-not-exist.bin");
        try {
            MultiReadOnlySeekableByteChannel.forFiles(missing);
            fail("Missing file must cause NoSuchFileException");
        } catch (NoSuchFileException expected) {
        }
    }

    @Test
    public void factory_forPaths_nullArrayThrows() throws Exception {
        try {
            MultiReadOnlySeekableByteChannel.forPaths((Path[]) null);
            fail("Null path array must throw NullPointerException");
        } catch (NullPointerException expected) {
        }
    }

    @Test
    public void factory_forPaths_missingPathThrows() throws Exception {
        Path missing = new File(tmp.getRoot(), "nope.bin").toPath();
        try {
            MultiReadOnlySeekableByteChannel.forPaths(missing);
            fail("Missing path must cause NoSuchFileException");
        } catch (NoSuchFileException expected) {
        }
    }

    @Test
    public void factory_forSeekableByteChannels_nullArrayThrows() {
        try {
            MultiReadOnlySeekableByteChannel.forSeekableByteChannels((SeekableByteChannel[]) null);
            fail("Null channels array must throw NullPointerException");
        } catch (NullPointerException expected) {
        }
    }

    @Test
    public void emptyChannel_behavesAsEmptyStream() throws Exception {
        MultiReadOnlySeekableByteChannel ch =
                new MultiReadOnlySeekableByteChannel(java.util.Collections.emptyList());

        assertTrue(ch.isOpen());
        assertEquals(0L, ch.size());

        ByteBuffer buf = ByteBuffer.allocate(8);
        assertEquals("Reading from empty channel returns -1", -1, ch.read(buf));
    }

    @Test
    public void readWithEmptyBufferReturnsZero() throws Exception {
        MultiReadOnlySeekableByteChannel ch =
                new MultiReadOnlySeekableByteChannel(java.util.Collections.emptyList());

        assertEquals(0, ch.read(ByteBuffer.allocate(0)));
    }

    @Test
    public void positionReturnsSameInstanceForChaining() throws Exception {
        MultiReadOnlySeekableByteChannel ch =
                new MultiReadOnlySeekableByteChannel(java.util.Collections.emptyList());

        assertSame("position(long) should be chainable (return this)", ch, ch.position(0L));
    }

    @Test
    public void forSeekableByteChannels_concatenatesProvidedChannels() throws Exception {
        File f1 = tmp.newFile("s1.bin");
        File f2 = tmp.newFile("s2.bin");
        Files.write(f1.toPath(), "Hi".getBytes(US_ASCII));
        Files.write(f2.toPath(), "!".getBytes(US_ASCII));

        try (SeekableByteChannel c1 = Files.newByteChannel(f1.toPath(), READ);
             SeekableByteChannel c2 = Files.newByteChannel(f2.toPath(), READ);
             SeekableByteChannel merged = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(c1, c2)) {

            assertEquals(3L, merged.size());
            byte[] all = readAll(merged);
            assertArrayEquals("Hi!".getBytes(US_ASCII), all);
        }
    }

    // Helpers

    private static byte[] readAll(SeekableByteChannel ch) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteBuffer buf = ByteBuffer.allocate(16);
        int n;
        while ((n = ch.read(buf)) != -1) {
            buf.flip();
            byte[] chunk = new byte[n];
            buf.get(chunk);
            out.write(chunk, 0, n);
            buf.clear();
        }
        return out.toByteArray();
    }

    private static int readOne(SeekableByteChannel ch) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(1);
        int n = ch.read(buf);
        assertEquals("Expected to read exactly one byte", 1, n);
        buf.flip();
        return buf.get() & 0xFF;
    }
}