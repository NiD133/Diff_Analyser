package com.itextpdf.text.pdf;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.channels.FileChannel;

import static org.junit.Assert.*;

public class MappedRandomAccessFileTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    @Test
    public void constructor_requiresNonNullFilename() {
        assertThrows(NullPointerException.class, () -> new MappedRandomAccessFile(null, "rw"));
    }

    @Test
    public void emptyFile_readReturnsMinusOne_andPointerStaysAtZero() throws Exception {
        File f = tmp.newFile("empty.dat");

        MappedRandomAccessFile raf = new MappedRandomAccessFile(f.getAbsolutePath(), "rw");
        try {
            assertEquals(0L, raf.getFilePointer());
            assertEquals(-1, raf.read());

            byte[] buf = new byte[8];
            assertEquals(-1, raf.read(buf, 0, buf.length));
            assertEquals(0L, raf.getFilePointer()); // no movement when nothing is read
        } finally {
            raf.close();
        }
    }

    @Test
    public void seek_updatesFilePointer_and_readPastEofReturnsMinusOne() throws Exception {
        File f = tmp.newFile("data1.dat");
        Files.write(f.toPath(), "abc".getBytes(StandardCharsets.US_ASCII));

        MappedRandomAccessFile raf = new MappedRandomAccessFile(f.getAbsolutePath(), "r");
        try {
            assertEquals(0L, raf.getFilePointer());

            long eof = raf.length();
            assertEquals(3L, eof);

            raf.seek(eof);
            assertEquals(eof, raf.getFilePointer());
            assertEquals(-1, raf.read());

            raf.seek(eof + 10);
            assertEquals(eof + 10, raf.getFilePointer());
            assertEquals(-1, raf.read());
        } finally {
            raf.close();
        }
    }

    @Test
    public void boundedRead_readsBytes_andAdvancesPointer() throws Exception {
        File f = tmp.newFile("data2.dat");
        Files.write(f.toPath(), "abcdef".getBytes(StandardCharsets.US_ASCII));

        MappedRandomAccessFile raf = new MappedRandomAccessFile(f.getAbsolutePath(), "r");
        try {
            byte[] buf = new byte[5];
            int n = raf.read(buf, 1, 3); // fill buf[1..3] with 'a','b','c'
            assertEquals(3, n);
            assertEquals(3L, raf.getFilePointer());

            assertEquals(0, buf[0]);
            assertEquals('a', buf[1]);
            assertEquals('b', buf[2]);
            assertEquals('c', buf[3]);
            assertEquals(0, buf[4]);
        } finally {
            raf.close();
        }
    }

    @Test
    public void read_withInvalidArrayBounds_throwsArrayIndexOutOfBounds() throws Exception {
        File f = tmp.newFile("data3.dat");
        Files.write(f.toPath(), "hi".getBytes(StandardCharsets.US_ASCII));

        MappedRandomAccessFile raf = new MappedRandomAccessFile(f.getAbsolutePath(), "r");
        try {
            byte[] buf = new byte[2];

            assertThrows(ArrayIndexOutOfBoundsException.class, () -> raf.read(buf, -1, 1));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> raf.read(buf, 0, 3));   // off + len > buf.length
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> raf.read(buf, 2, 1));   // off at end, len > 0
        } finally {
            raf.close();
        }
    }

    @Test
    public void length_returnsFileSize() throws Exception {
        File f = tmp.newFile("data4.dat");
        Files.write(f.toPath(), "12345".getBytes(StandardCharsets.US_ASCII));

        MappedRandomAccessFile raf = new MappedRandomAccessFile(f.getAbsolutePath(), "r");
        try {
            assertEquals(5L, raf.length());
            assertEquals(0L, raf.getFilePointer()); // length() should not move the pointer
        } finally {
            raf.close();
        }
    }

    @Test
    public void getChannel_isNonNull_andReflectsOpenCloseState() throws Exception {
        File f = tmp.newFile("data5.dat");
        Files.write(f.toPath(), "x".getBytes(StandardCharsets.US_ASCII));

        MappedRandomAccessFile raf = new MappedRandomAccessFile(f.getAbsolutePath(), "r");
        try {
            FileChannel ch = raf.getChannel();
            assertNotNull(ch);
            assertTrue(ch.isOpen());

            raf.close();
            assertFalse(ch.isOpen());
        } finally {
            // close again to ensure idempotency does not throw
            try { raf.close(); } catch (IOException ignore) {}
        }
    }

    @Test
    public void close_isIdempotent() throws Exception {
        File f = tmp.newFile("data6.dat");
        MappedRandomAccessFile raf = new MappedRandomAccessFile(f.getAbsolutePath(), "rw");
        raf.close();
        // should not throw when closing again
        raf.close();
    }

    @Test
    public void clean_onHeapOrNullBuffer_returnsFalse() {
        assertFalse(MappedRandomAccessFile.clean(null));
        assertFalse(MappedRandomAccessFile.clean(java.nio.ByteBuffer.allocate(16)));
    }
}