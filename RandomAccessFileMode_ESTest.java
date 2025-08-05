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
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class RandomAccessFileMode_ESTest extends RandomAccessFileMode_ESTest_scaffolding {

    // ========== Enum Basic Operations Tests ==========
    
    @Test(timeout = 4000)
    public void testEnumValues_ShouldReturnFourModes() throws Throwable {
        RandomAccessFileMode[] modes = RandomAccessFileMode.values();
        assertEquals("Should have exactly 4 access modes", 4, modes.length);
    }

    @Test(timeout = 4000)
    public void testValueOf_WithValidEnumName_ShouldReturnCorrectMode() throws Throwable {
        RandomAccessFileMode mode = RandomAccessFileMode.valueOf("READ_WRITE");
        assertEquals(RandomAccessFileMode.READ_WRITE, mode);
    }

    // ========== Mode String Conversion Tests ==========
    
    @Test(timeout = 4000)
    public void testValueOfMode_WithReadOnlyString_ShouldReturnReadOnlyMode() throws Throwable {
        RandomAccessFileMode mode = RandomAccessFileMode.valueOfMode("r");
        assertEquals("Mode 'r' should map to READ_ONLY", RandomAccessFileMode.READ_ONLY, mode);
        assertEquals("Mode string should be 'r'", "r", mode.getMode());
    }

    @Test(timeout = 4000)
    public void testValueOfMode_WithReadWriteSyncAllString_ShouldReturnCorrectMode() throws Throwable {
        RandomAccessFileMode mode = RandomAccessFileMode.valueOfMode("rws");
        assertEquals("Mode 'rws' should map to READ_WRITE_SYNC_ALL", RandomAccessFileMode.READ_WRITE_SYNC_ALL, mode);
    }

    @Test(timeout = 4000)
    public void testValueOfMode_WithReadWriteSyncContentString_ShouldReturnCorrectMode() throws Throwable {
        RandomAccessFileMode mode = RandomAccessFileMode.valueOfMode("rwd");
        assertEquals("Mode 'rwd' should map to READ_WRITE_SYNC_CONTENT", RandomAccessFileMode.READ_WRITE_SYNC_CONTENT, mode);
    }

    @Test(timeout = 4000)
    public void testGetMode_ForReadWriteSyncAll_ShouldReturnRwsString() throws Throwable {
        RandomAccessFileMode mode = RandomAccessFileMode.READ_WRITE_SYNC_ALL;
        String modeString = mode.getMode();
        assertEquals("READ_WRITE_SYNC_ALL mode should return 'rws'", "rws", modeString);
    }

    // ========== OpenOption Conversion Tests ==========
    
    @Test(timeout = 4000)
    public void testValueOf_WithSyncOpenOption_ShouldReturnReadWriteSyncAll() throws Throwable {
        StandardOpenOption syncOption = StandardOpenOption.SYNC;
        OpenOption[] options = new OpenOption[5];
        options[1] = syncOption;
        options[2] = syncOption;
        
        RandomAccessFileMode mode = RandomAccessFileMode.valueOf(options);
        assertEquals("SYNC option should map to READ_WRITE_SYNC_ALL", 
                     RandomAccessFileMode.READ_WRITE_SYNC_ALL, mode);
    }

    @Test(timeout = 4000)
    public void testValueOf_WithWriteOpenOption_ShouldReturnReadWrite() throws Throwable {
        OpenOption[] options = new OpenOption[1];
        options[0] = StandardOpenOption.WRITE;
        
        RandomAccessFileMode mode = RandomAccessFileMode.valueOf(options);
        
        MockFile testFile = new MockFile("test-file");
        IORandomAccessFile randomAccessFile = (IORandomAccessFile) mode.create(testFile);
        assertEquals("WRITE option should result in 'rw' mode", "rw", randomAccessFile.getMode());
    }

    // ========== Implies Relationship Tests ==========
    
    @Test(timeout = 4000)
    public void testImplies_ReadWriteSyncContentImpliesReadOnly_ShouldReturnTrue() throws Throwable {
        RandomAccessFileMode higherMode = RandomAccessFileMode.READ_WRITE_SYNC_CONTENT;
        RandomAccessFileMode lowerMode = RandomAccessFileMode.READ_ONLY;
        
        boolean implies = higherMode.implies(lowerMode);
        assertTrue("READ_WRITE_SYNC_CONTENT should imply READ_ONLY", implies);
    }

    @Test(timeout = 4000)
    public void testImplies_SameModeImpliesItself_ShouldReturnTrue() throws Throwable {
        RandomAccessFileMode mode = RandomAccessFileMode.READ_WRITE_SYNC_CONTENT;
        
        boolean implies = mode.implies(mode);
        assertTrue("Mode should imply itself", implies);
    }

    @Test(timeout = 4000)
    public void testImplies_LowerModeDoesNotImplyHigherMode_ShouldReturnFalse() throws Throwable {
        RandomAccessFileMode lowerMode = RandomAccessFileMode.READ_WRITE_SYNC_CONTENT;
        RandomAccessFileMode higherMode = RandomAccessFileMode.READ_WRITE_SYNC_ALL;
        
        boolean implies = lowerMode.implies(higherMode);
        assertFalse("READ_WRITE_SYNC_CONTENT should not imply READ_WRITE_SYNC_ALL", implies);
    }

    // ========== File Creation Tests ==========
    
    @Test(timeout = 4000)
    public void testCreate_WithValidPath_ShouldCreateRandomAccessFile() throws Throwable {
        RandomAccessFileMode mode = RandomAccessFileMode.READ_WRITE_SYNC_CONTENT;
        MockFile mockFile = new MockFile(null, "test-file");
        Path path = mockFile.toPath();
        
        RandomAccessFile file = mode.create(path);
        assertEquals("File pointer should start at 0", 0L, file.getFilePointer());
    }

    @Test(timeout = 4000)
    public void testCreate_WithValidFile_ShouldCreateIORandomAccessFile() throws Throwable {
        // Setup test file
        EvoSuiteFile testFile = new EvoSuiteFile("test-file");
        FileSystemHandling.appendLineToFile(testFile, "test content");
        MockFile mockFile = new MockFile("test-file");
        
        RandomAccessFileMode mode = RandomAccessFileMode.READ_WRITE_SYNC_CONTENT;
        IORandomAccessFile file = (IORandomAccessFile) mode.create(mockFile);
        
        assertEquals("Should create file with 'rwd' mode", "rwd", file.getMode());
    }

    @Test(timeout = 4000)
    public void testCreate_WithValidString_ShouldCreateIORandomAccessFile() throws Throwable {
        RandomAccessFileMode mode = RandomAccessFileMode.READ_WRITE_SYNC_ALL;
        IORandomAccessFile file = (IORandomAccessFile) mode.create("test-file");
        
        assertEquals("Should create file with 'rws' mode", "rws", file.getMode());
    }

    @Test(timeout = 4000)
    public void testIo_WithValidFileName_ShouldCreateIORandomAccessFile() throws Throwable {
        RandomAccessFileMode mode = RandomAccessFileMode.READ_WRITE_SYNC_CONTENT;
        IORandomAccessFile file = mode.io("test-file");
        
        assertEquals("New file should have length 0", 0L, file.length());
    }

    @Test(timeout = 4000)
    public void testIo_WithExistingFile_ShouldOpenSuccessfully() throws Throwable {
        // Setup existing file
        EvoSuiteFile existingFile = new EvoSuiteFile("existing-file");
        FileSystemHandling.appendStringToFile(existingFile, "content");
        
        RandomAccessFileMode mode = RandomAccessFileMode.valueOfMode("r");
        mode.io("existing-file");
        
        assertEquals("Mode should be 'r'", "r", mode.getMode());
    }

    // ========== Functional Interface Tests ==========
    
    @Test(timeout = 4000)
    public void testAccept_WithValidConsumer_ShouldExecuteSuccessfully() throws Throwable {
        RandomAccessFileMode mode = RandomAccessFileMode.READ_WRITE_SYNC_ALL;
        MockFile mockFile = new MockFile("test-file");
        Path path = mockFile.toPath();
        IOConsumer<RandomAccessFile> noOpConsumer = IOConsumer.noop();
        
        mode.accept(path, noOpConsumer);
        assertEquals("Mode should be 'rws'", "rws", mode.getMode());
    }

    // ========== Error Handling Tests ==========
    
    @Test(timeout = 4000)
    public void testValueOfMode_WithNullInput_ShouldThrowNullPointerException() throws Throwable {
        try {
            RandomAccessFileMode.valueOfMode(null);
            fail("Should throw NullPointerException for null input");
        } catch(NullPointerException e) {
            verifyException("org.apache.commons.io.RandomAccessFileMode", e);
        }
    }

    @Test(timeout = 4000)
    public void testValueOfMode_WithInvalidMode_ShouldThrowIllegalArgumentException() throws Throwable {
        try {
            RandomAccessFileMode.valueOfMode("invalid-mode");
            fail("Should throw IllegalArgumentException for invalid mode");
        } catch(IllegalArgumentException e) {
            verifyException("org.apache.commons.io.RandomAccessFileMode", e);
        }
    }

    @Test(timeout = 4000)
    public void testValueOf_WithNullOpenOptions_ShouldThrowNullPointerException() throws Throwable {
        try {
            RandomAccessFileMode.valueOf((OpenOption[]) null);
            fail("Should throw NullPointerException for null OpenOption array");
        } catch(NullPointerException e) {
            verifyException("org.apache.commons.io.RandomAccessFileMode", e);
        }
    }

    @Test(timeout = 4000)
    public void testCreate_WithReadOnlyModeAndNonExistentFile_ShouldThrowFileNotFoundException() throws Throwable {
        RandomAccessFileMode readOnlyMode = RandomAccessFileMode.valueOfMode("r");
        MockFile nonExistentFile = new MockFile("non-existent-file");
        Path path = nonExistentFile.toPath();
        
        try {
            readOnlyMode.create(path);
            fail("Should throw FileNotFoundException for non-existent file in read-only mode");
        } catch(FileNotFoundException e) {
            verifyException("org.evosuite.runtime.mock.java.io.MockRandomAccessFile", e);
        }
    }

    @Test(timeout = 4000)
    public void testCreate_WithNullFile_ShouldThrowNullPointerException() throws Throwable {
        RandomAccessFileMode mode = RandomAccessFileMode.READ_ONLY;
        
        try {
            mode.create((File) null);
            fail("Should throw NullPointerException for null File");
        } catch(NullPointerException e) {
            verifyException("org.evosuite.runtime.mock.java.io.MockRandomAccessFile", e);
        }
    }

    @Test(timeout = 4000)
    public void testCreate_WithNullPath_ShouldThrowNullPointerException() throws Throwable {
        RandomAccessFileMode mode = RandomAccessFileMode.READ_WRITE_SYNC_ALL;
        
        try {
            mode.create((Path) null);
            fail("Should throw NullPointerException for null Path");
        } catch(NullPointerException e) {
            verifyException("org.apache.commons.io.RandomAccessFileMode", e);
        }
    }

    @Test(timeout = 4000)
    public void testCreate_WithNullString_ShouldThrowNullPointerException() throws Throwable {
        RandomAccessFileMode mode = RandomAccessFileMode.READ_ONLY;
        
        try {
            mode.create((String) null);
            fail("Should throw NullPointerException for null String");
        } catch(NullPointerException e) {
            verifyException("org.evosuite.runtime.mock.java.io.MockRandomAccessFile", e);
        }
    }

    @Test(timeout = 4000)
    public void testIo_WithNullFileName_ShouldThrowNullPointerException() throws Throwable {
        RandomAccessFileMode mode = RandomAccessFileMode.READ_WRITE_SYNC_ALL;
        
        try {
            mode.io(null);
            fail("Should throw NullPointerException for null file name");
        } catch(NullPointerException e) {
            verifyException("org.evosuite.runtime.mock.java.io.MockRandomAccessFile", e);
        }
    }

    @Test(timeout = 4000)
    public void testAccept_WithNullPath_ShouldThrowNullPointerException() throws Throwable {
        RandomAccessFileMode mode = RandomAccessFileMode.READ_WRITE_SYNC_ALL;
        IOConsumer<RandomAccessFile> consumer = IOConsumer.noop();
        
        try {
            mode.accept(null, consumer);
            fail("Should throw NullPointerException for null Path");
        } catch(NullPointerException e) {
            verifyException("org.apache.commons.io.RandomAccessFileMode", e);
        }
    }

    @Test(timeout = 4000)
    public void testAccept_WithNullConsumer_ShouldThrowNullPointerException() throws Throwable {
        MockFile mockFile = new MockFile("test-file");
        Path path = mockFile.toPath();
        RandomAccessFileMode mode = RandomAccessFileMode.READ_WRITE_SYNC_CONTENT;
        
        try {
            mode.accept(path, null);
            fail("Should throw NullPointerException for null consumer");
        } catch(NullPointerException e) {
            verifyException("org.apache.commons.io.RandomAccessFileMode", e);
        }
    }

    @Test(timeout = 4000)
    public void testApply_WithNullPath_ShouldThrowNullPointerException() throws Throwable {
        RandomAccessFileMode mode = RandomAccessFileMode.READ_WRITE;
        
        try {
            mode.apply(null, (IOFunction<RandomAccessFile, RandomAccessFileMode>) null);
            fail("Should throw NullPointerException for null Path");
        } catch(NullPointerException e) {
            verifyException("org.apache.commons.io.RandomAccessFileMode", e);
        }
    }

    @Test(timeout = 4000)
    public void testApply_WithNullFunction_ShouldThrowNullPointerException() throws Throwable {
        RandomAccessFileMode mode = RandomAccessFileMode.READ_WRITE_SYNC_CONTENT;
        MockFile mockFile = new MockFile("test-file");
        Path path = mockFile.toPath();
        
        try {
            mode.apply(path, null);
            fail("Should throw NullPointerException for null function");
        } catch(NullPointerException e) {
            verifyException("org.apache.commons.io.RandomAccessFileMode", e);
        }
    }

    // ========== IOException Handling Tests ==========
    
    @Test(timeout = 4000)
    public void testAccept_WithIOException_ShouldPropagateException() throws Throwable {
        MockFile mockFile = new MockFile("test-file");
        Path path = mockFile.toPath();
        FileSystemHandling.shouldAllThrowIOExceptions();
        IOConsumer<RandomAccessFile> consumer = IOConsumer.noop();
        RandomAccessFileMode mode = RandomAccessFileMode.READ_WRITE_SYNC_CONTENT;
        
        try {
            mode.accept(path, consumer);
            fail("Should propagate IOException");
        } catch(IOException e) {
            verifyException("org.evosuite.runtime.vfs.VirtualFileSystem", e);
        }
    }

    @Test(timeout = 4000)
    public void testAccept_WithReadOnlyModeAndNonExistentFile_ShouldThrowFileNotFoundException() throws Throwable {
        RandomAccessFileMode readOnlyMode = RandomAccessFileMode.READ_ONLY;
        MockFile nonExistentFile = new MockFile("non-existent-file");
        Path path = nonExistentFile.toPath();
        IOConsumer<RandomAccessFile> consumer = IOConsumer.noop();
        
        try {
            readOnlyMode.accept(path, consumer);
            fail("Should throw FileNotFoundException for non-existent file in read-only mode");
        } catch(FileNotFoundException e) {
            verifyException("org.evosuite.runtime.mock.java.io.MockRandomAccessFile", e);
        }
    }

    @Test(timeout = 4000)
    public void testApply_WithReadOnlyModeAndNonExistentFile_ShouldThrowFileNotFoundException() throws Throwable {
        MockFile nonExistentFile = new MockFile("non-existent-file");
        Path path = nonExistentFile.toPath();
        RandomAccessFileMode readOnlyMode = RandomAccessFileMode.READ_ONLY;
        
        try {
            readOnlyMode.apply(path, (IOFunction<RandomAccessFile, RandomAccessFileMode>) null);
            fail("Should throw FileNotFoundException for non-existent file in read-only mode");
        } catch(FileNotFoundException e) {
            verifyException("org.evosuite.runtime.mock.java.io.MockRandomAccessFile", e);
        }
    }
}