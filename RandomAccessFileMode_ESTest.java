package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import org.apache.commons.io.IORandomAccessFile;
import org.apache.commons.io.RandomAccessFileMode;
import org.apache.commons.io.function.IOConsumer;
import org.apache.commons.io.function.IOFunction;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.testdata.EvoSuiteFile;
import org.evosuite.runtime.testdata.FileSystemHandling;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true, 
    useVFS = true, 
    useVNET = true, 
    resetStaticState = true, 
    separateClassLoader = true
)
public class RandomAccessFileModeTest extends RandomAccessFileMode_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testReadWriteSyncContentImpliesReadOnly() throws Throwable {
        RandomAccessFileMode readWriteSyncContent = RandomAccessFileMode.READ_WRITE_SYNC_CONTENT;
        RandomAccessFileMode readOnly = RandomAccessFileMode.READ_ONLY;
        assertTrue(readWriteSyncContent.implies(readOnly));
    }

    @Test(timeout = 4000)
    public void testRandomAccessFileModeValues() throws Throwable {
        RandomAccessFileMode[] modes = RandomAccessFileMode.values();
        assertEquals(4, modes.length);
    }

    @Test(timeout = 4000)
    public void testValueOfReadWrite() throws Throwable {
        RandomAccessFileMode mode = RandomAccessFileMode.valueOf("READ_WRITE");
        assertEquals(RandomAccessFileMode.READ_WRITE, mode);
    }

    @Test(timeout = 4000)
    public void testIoWithReadWriteSyncContent() throws Throwable {
        RandomAccessFileMode mode = RandomAccessFileMode.READ_WRITE_SYNC_CONTENT;
        IORandomAccessFile file = mode.io(".4");
        assertEquals(0L, file.length());
    }

    @Test(timeout = 4000)
    public void testValueOfModeReadOnly() throws Throwable {
        RandomAccessFileMode mode = RandomAccessFileMode.valueOfMode("r");
        assertEquals(RandomAccessFileMode.READ_ONLY, mode);

        EvoSuiteFile evoFile = new EvoSuiteFile("r");
        FileSystemHandling.appendStringToFile(evoFile, "r");
        mode.io("r");
        assertEquals("r", mode.getMode());
    }

    @Test(timeout = 4000)
    public void testReadWriteSyncContentImpliesItself() throws Throwable {
        RandomAccessFileMode mode = RandomAccessFileMode.READ_WRITE_SYNC_CONTENT;
        assertTrue(mode.implies(mode));
    }

    @Test(timeout = 4000)
    public void testReadWriteSyncContentDoesNotImplyReadWriteSyncAll() throws Throwable {
        RandomAccessFileMode syncAll = RandomAccessFileMode.READ_WRITE_SYNC_ALL;
        RandomAccessFileMode syncContent = RandomAccessFileMode.READ_WRITE_SYNC_CONTENT;
        assertFalse(syncContent.implies(syncAll));
    }

    @Test(timeout = 4000)
    public void testCreateWithPath() throws Throwable {
        RandomAccessFileMode mode = RandomAccessFileMode.valueOfMode("r");
        assertEquals(RandomAccessFileMode.READ_ONLY, mode);

        EvoSuiteFile evoFile = new EvoSuiteFile("r");
        FileSystemHandling.appendStringToFile(evoFile, "r");
        MockFile mockFile = new MockFile("r");
        Path path = mockFile.toPath();
        mode.create(path);
        assertEquals("r", mode.getMode());
    }

    @Test(timeout = 4000)
    public void testCreateWithNullPathThrowsException() throws Throwable {
        RandomAccessFileMode mode = RandomAccessFileMode.READ_WRITE_SYNC_ALL;
        try {
            mode.create((Path) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.io.RandomAccessFileMode", e);
        }
    }

    @Test(timeout = 4000)
    public void testCreateWithNonExistentFileThrowsException() throws Throwable {
        RandomAccessFileMode mode = RandomAccessFileMode.valueOfMode("r");
        MockFile mockFile = new MockFile("r");
        try {
            mode.create((File) mockFile);
            fail("Expecting exception: FileNotFoundException");
        } catch (FileNotFoundException e) {
            verifyException("org.evosuite.runtime.mock.java.io.MockRandomAccessFile", e);
        }
    }

    @Test(timeout = 4000)
    public void testValueOfModeWithInvalidStringThrowsException() throws Throwable {
        try {
            RandomAccessFileMode.valueOfMode("listener");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.io.RandomAccessFileMode", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetModeForReadWriteSyncAll() throws Throwable {
        RandomAccessFileMode mode = RandomAccessFileMode.READ_WRITE_SYNC_ALL;
        assertEquals("rws", mode.getMode());
    }

    // Additional tests for exception handling and other scenarios can be added here
}