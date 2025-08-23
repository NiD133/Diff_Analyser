package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class RandomAccessFileModeTest {

    @TempDir
    Path tempDir;

    // ---------- Enum basics ----------

    @Test
    void values_hasFourConstants() {
        assertEquals(4, RandomAccessFileMode.values().length);
    }

    @Test
    void enumValueOf_matchesConstant() {
        assertEquals(RandomAccessFileMode.READ_WRITE,
                Enum.valueOf(RandomAccessFileMode.class, "READ_WRITE"));
    }

    @Test
    void getMode_returnsExpectedStrings() {
        assertAll(
            () -> assertEquals("r",   RandomAccessFileMode.READ_ONLY.getMode()),
            () -> assertEquals("rw",  RandomAccessFileMode.READ_WRITE.getMode()),
            () -> assertEquals("rwd", RandomAccessFileMode.READ_WRITE_SYNC_CONTENT.getMode()),
            () -> assertEquals("rws", RandomAccessFileMode.READ_WRITE_SYNC_ALL.getMode())
        );
    }

    @Test
    void valueOfMode_validInputs() {
        assertEquals(RandomAccessFileMode.READ_ONLY,               RandomAccessFileMode.valueOfMode("r"));
        assertEquals(RandomAccessFileMode.READ_WRITE,              RandomAccessFileMode.valueOfMode("rw"));
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT, RandomAccessFileMode.valueOfMode("rwd"));
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_ALL,     RandomAccessFileMode.valueOfMode("rws"));
    }

    @Test
    void valueOfMode_invalidInput_throwsIAE() {
        assertThrows(IllegalArgumentException.class, () -> RandomAccessFileMode.valueOfMode("not-a-mode"));
    }

    @Test
    void valueOfMode_null_throwsNPE() {
        assertThrows(NullPointerException.class, () -> RandomAccessFileMode.valueOfMode(null));
    }

    // ---------- OpenOption mapping ----------

    @Test
    void valueOfOpenOptions_defaultsToReadOnly() {
        assertEquals(RandomAccessFileMode.READ_ONLY, RandomAccessFileMode.valueOf(new OpenOption[0]));
    }

    @Test
    void valueOfOpenOptions_writeGivesReadWrite() {
        assertEquals(RandomAccessFileMode.READ_WRITE, RandomAccessFileMode.valueOf(StandardOpenOption.WRITE));
    }

    @Test
    void valueOfOpenOptions_dsyncGivesSyncContent() {
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT,
                RandomAccessFileMode.valueOf(StandardOpenOption.WRITE, StandardOpenOption.DSYNC));
    }

    @Test
    void valueOfOpenOptions_syncGivesSyncAll() {
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_ALL,
                RandomAccessFileMode.valueOf(StandardOpenOption.WRITE, StandardOpenOption.SYNC));
    }

    @Test
    void valueOfOpenOptions_nullArray_throwsNPE() {
        assertThrows(NullPointerException.class, () -> RandomAccessFileMode.valueOf((OpenOption[]) null));
    }

    // ---------- implies() semantics ----------

    @Test
    void implies_isReflexiveAndOrdered() {
        // Reflexive
        assertTrue(RandomAccessFileMode.READ_ONLY.implies(RandomAccessFileMode.READ_ONLY));
        assertTrue(RandomAccessFileMode.READ_WRITE.implies(RandomAccessFileMode.READ_WRITE));
        assertTrue(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT.implies(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT));
        assertTrue(RandomAccessFileMode.READ_WRITE_SYNC_ALL.implies(RandomAccessFileMode.READ_WRITE_SYNC_ALL));

        // Order: rws > rwd > rw > r
        assertTrue(RandomAccessFileMode.READ_WRITE.implies(RandomAccessFileMode.READ_ONLY));
        assertTrue(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT.implies(RandomAccessFileMode.READ_WRITE));
        assertTrue(RandomAccessFileMode.READ_WRITE_SYNC_ALL.implies(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT));

        // Negative sample
        assertFalse(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT.implies(RandomAccessFileMode.READ_WRITE_SYNC_ALL));
    }

    // ---------- create(...) and io(...) ----------

    @Test
    void io_onWriteMode_createsNewFileWithZeroLength() throws Exception {
        Path file = tempDir.resolve("created-by-io.bin");
        assertFalse(Files.exists(file));

        try (IORandomAccessFile raf = RandomAccessFileMode.READ_WRITE_SYNC_CONTENT.io(file.toString())) {
            assertEquals(0L, raf.length());
            assertEquals("rwd", raf.getMode());
        }
        assertTrue(Files.exists(file));
    }

    @Test
    void create_onWriteMode_returnsRafAtStart() throws Exception {
        Path file = tempDir.resolve("created-by-create.bin");

        try (RandomAccessFile raf = RandomAccessFileMode.READ_WRITE.create(file)) {
            assertEquals(0L, raf.getFilePointer());
            assertTrue(Files.exists(file));
        }
    }

    @Test
    void create_onReadOnly_missingFile_throwsFNF() {
        Path missing = tempDir.resolve("missing-read-only.bin");
        assertFalse(Files.exists(missing));

        assertThrows(FileNotFoundException.class,
                () -> RandomAccessFileMode.READ_ONLY.create(missing));
    }

    @Test
    void create_withNullArguments_throwsNPE() {
        assertAll(
            () -> assertThrows(NullPointerException.class,
                    () -> RandomAccessFileMode.READ_ONLY.create((Path) null)),
            () -> assertThrows(NullPointerException.class,
                    () -> RandomAccessFileMode.READ_ONLY.create((File) null)),
            () -> assertThrows(NullPointerException.class,
                    () -> RandomAccessFileMode.READ_ONLY.create((String) null))
        );
    }

    // ---------- accept(...) and apply(...) ----------

    @Test
    void accept_onWriteMode_createsFileAndInvokesConsumer() throws Exception {
        Path file = tempDir.resolve("accept-write.bin");
        assertFalse(Files.exists(file));

        RandomAccessFileMode.READ_WRITE_SYNC_ALL.accept(file, raf -> {
            raf.writeInt(123);
        });

        assertTrue(Files.exists(file));
        assertTrue(Files.size(file) >= Integer.BYTES);
    }

    @Test
    void accept_nullArguments_throwNPE() {
        Path file = tempDir.resolve("accept-npe.bin");

        assertAll(
            () -> assertThrows(NullPointerException.class,
                    () -> RandomAccessFileMode.READ_WRITE.accept(null, raf -> {})),
            () -> assertThrows(NullPointerException.class,
                    () -> RandomAccessFileMode.READ_WRITE.accept(file, null))
        );
    }

    @Test
    void apply_onWriteMode_returnsFunctionResult() throws Exception {
        Path file = tempDir.resolve("apply-write.bin");

        long length = RandomAccessFileMode.READ_WRITE.apply(file, raf -> {
            raf.writeUTF("hello");
            return raf.length();
        });

        assertTrue(length > 0L);
        assertTrue(Files.exists(file));
        assertEquals(length, Files.size(file));
    }

    @Test
    void apply_onReadOnly_missingFile_throwsFNF() {
        Path missing = tempDir.resolve("apply-missing.bin");
        assertFalse(Files.exists(missing));

        assertThrows(FileNotFoundException.class,
                () -> RandomAccessFileMode.READ_ONLY.apply(missing, raf -> 0));
    }

    @Test
    void apply_nullArguments_throwNPE() {
        Path file = tempDir.resolve("apply-npe.bin");

        assertAll(
            () -> assertThrows(NullPointerException.class,
                    () -> RandomAccessFileMode.READ_WRITE.apply(null, raf -> 1)),
            () -> assertThrows(NullPointerException.class,
                    () -> RandomAccessFileMode.READ_WRITE.apply(file, null))
        );
    }
}