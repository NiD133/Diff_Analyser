package org.apache.commons.compress.archivers.ar;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;
import org.evosuite.runtime.mock.java.io.MockPrintStream;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class ArArchiveOutputStreamTest extends ArArchiveOutputStream_ESTest_scaffolding {

    private static final String TEMP_FILE_PREFIX = "tempFile";
    private static final String TEMP_FILE_SUFFIX = "tmp";
    private static final int TIMEOUT = 4000;
    private static final int LONG_FILE_MODE = 1;
    private static final int INVALID_OFFSET = -90;
    private static final int INVALID_LENGTH = 315;

    @Test(timeout = TIMEOUT)
    public void testWriteEntryWithLongFileMode() throws Throwable {
        MockFileOutputStream mockOutputStream = new MockFileOutputStream("*m{4/p6", false);
        ArArchiveOutputStream arOutputStream = new ArArchiveOutputStream(mockOutputStream);
        arOutputStream.setLongFileMode(LONG_FILE_MODE);
        ArArchiveEntry entry = new ArArchiveEntry("entryName", 3101L);
        arOutputStream.putArchiveEntry(entry);
        assertEquals(68L, arOutputStream.getBytesWritten());
    }

    @Test(timeout = TIMEOUT)
    public void testCreateAndWriteEntry() throws Throwable {
        MockFileOutputStream mockOutputStream = new MockFileOutputStream("*m{4/p6", false);
        ArArchiveOutputStream arOutputStream = new ArArchiveOutputStream(mockOutputStream);
        MockFile mockFile = new MockFile("*m{4/p6");
        ArArchiveEntry entry = arOutputStream.createArchiveEntry((File) mockFile, "*m{4/p6");
        arOutputStream.setLongFileMode(-2628);
        arOutputStream.putArchiveEntry(entry);
        assertEquals(68L, mockFile.length());
        assertEquals(68L, arOutputStream.getBytesWritten());
    }

    @Test(timeout = TIMEOUT)
    public void testIOExceptionOnWriteBeyondArrayBounds() throws Throwable {
        MockPrintStream mockPrintStream = new MockPrintStream("HU)HAv");
        byte[] byteArray = new byte[7];
        File tempFile = MockFile.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX, null);
        ArArchiveOutputStream arOutputStream = new ArArchiveOutputStream(mockPrintStream);
        ArArchiveEntry entry = arOutputStream.createArchiveEntry(tempFile, "HU)HAv");
        arOutputStream.putArchiveEntry(entry);
        try {
            arOutputStream.write(byteArray, 127, 536);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.apache.commons.compress.archivers.ar.ArArchiveOutputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testWriteEntryWithInvalidOffset() throws Throwable {
        MockFileOutputStream mockOutputStream = new MockFileOutputStream("*m{1/p6");
        ArArchiveOutputStream arOutputStream = new ArArchiveOutputStream(mockOutputStream);
        byte[] byteArray = new byte[7];
        try {
            arOutputStream.write(byteArray, INVALID_OFFSET, INVALID_LENGTH);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = TIMEOUT)
    public void testNullPointerExceptionOnNullOutputStream() throws Throwable {
        ArArchiveOutputStream arOutputStream = new ArArchiveOutputStream((OutputStream) null);
        try {
            arOutputStream.close();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.io.FilterOutputStream", e);
        }
    }

    // Additional tests can be refactored similarly...

}