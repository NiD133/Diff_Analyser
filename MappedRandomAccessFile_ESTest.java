package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.itextpdf.text.pdf.MappedRandomAccessFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.testdata.EvoSuiteFile;
import org.evosuite.runtime.testdata.FileSystemHandling;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true) 
public class MappedRandomAccessFile_ESTest extends MappedRandomAccessFile_ESTest_scaffolding {

    // Constants for test readability
    private static final String READ_WRITE_MODE = "rw";
    private static final long LARGE_POSITION = 1073741824L;
    private static final int NEGATIVE_POSITION = -1934028355;
    private static final int EOF_RESULT = -1;
    private static final long INITIAL_FILE_POINTER = 0L;

    // ========== Constructor Tests ==========
    
    @Test(timeout = 4000)
    public void testConstructor_WithNullFilename_ThrowsNullPointerException() throws Throwable {
        try {
            new MappedRandomAccessFile(null, READ_WRITE_MODE);
            fail("Expected NullPointerException when filename is null");
        } catch(NullPointerException e) {
            verifyException("org.evosuite.runtime.mock.java.io.MockRandomAccessFile", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructor_WithInvalidMode_ThrowsFileNotFoundException() throws Throwable {
        try {
            new MappedRandomAccessFile(READ_WRITE_MODE, "PmF8^@U[W<vM7");
            fail("Expected FileNotFoundException for invalid file mode");
        } catch(Throwable e) {
            verifyException("org.evosuite.runtime.mock.java.io.MockFileInputStream", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructor_WithExistingFile_ThrowsIOException() throws Throwable {
        // Setup: Create a file with content
        EvoSuiteFile testFile = new EvoSuiteFile("qw");
        FileSystemHandling.appendStringToFile(testFile, "qw");
        
        try {
            new MappedRandomAccessFile("qw", "qw");
            fail("Expected IOException due to MappedByteBuffer mock limitation");
        } catch(Throwable e) {
            verifyException("org.evosuite.runtime.mock.java.io.EvoFileChannel", e);
        }
    }

    // ========== File Pointer and Seek Tests ==========

    @Test(timeout = 4000)
    public void testGetFilePointer_InitialPosition_ReturnsZero() throws Throwable {
        MappedRandomAccessFile file = new MappedRandomAccessFile(READ_WRITE_MODE, READ_WRITE_MODE);
        
        long position = file.getFilePointer();
        
        assertEquals("Initial file pointer should be 0", INITIAL_FILE_POINTER, position);
    }

    @Test(timeout = 4000)
    public void testSeek_PositivePosition_UpdatesFilePointer() throws Throwable {
        MappedRandomAccessFile file = new MappedRandomAccessFile(READ_WRITE_MODE, READ_WRITE_MODE);
        long expectedPosition = 686L;
        
        file.seek(expectedPosition);
        
        assertEquals("File pointer should be updated to seek position", 
                     expectedPosition, file.getFilePointer());
    }

    @Test(timeout = 4000)
    public void testSeek_NegativePosition_UpdatesFilePointer() throws Throwable {
        MappedRandomAccessFile file = new MappedRandomAccessFile(READ_WRITE_MODE, READ_WRITE_MODE);
        long negativePosition = -2756L;
        
        file.seek(negativePosition);
        
        assertEquals("File pointer should accept negative positions", 
                     negativePosition, file.getFilePointer());
    }

    // ========== Read Operation Tests ==========

    @Test(timeout = 4000)
    public void testRead_EmptyFile_ReturnsEOF() throws Throwable {
        MappedRandomAccessFile file = new MappedRandomAccessFile(READ_WRITE_MODE, READ_WRITE_MODE);
        
        int result = file.read();
        
        assertEquals("Reading from empty file should return EOF", EOF_RESULT, result);
        assertEquals("File pointer should remain at start", INITIAL_FILE_POINTER, file.getFilePointer());
    }

    @Test(timeout = 4000)
    public void testRead_AtLargePosition_ReturnsEOF() throws Throwable {
        MappedRandomAccessFile file = new MappedRandomAccessFile(READ_WRITE_MODE, READ_WRITE_MODE);
        
        file.seek(LARGE_POSITION);
        int result = file.read();
        
        assertEquals("Reading beyond file size should return EOF", EOF_RESULT, result);
    }

    @Test(timeout = 4000)
    public void testRead_AtNegativePosition_ThrowsArrayIndexOutOfBoundsException() throws Throwable {
        MappedRandomAccessFile file = new MappedRandomAccessFile(READ_WRITE_MODE, READ_WRITE_MODE);
        
        file.seek(NEGATIVE_POSITION);
        
        try {
            file.read();
            fail("Expected ArrayIndexOutOfBoundsException for negative position");
        } catch(ArrayIndexOutOfBoundsException e) {
            verifyException("com.itextpdf.text.pdf.MappedRandomAccessFile", e);
        }
    }

    // ========== Read Array Tests ==========

    @Test(timeout = 4000)
    public void testReadArray_ValidParameters_ReturnsEOF() throws Throwable {
        MappedRandomAccessFile file = new MappedRandomAccessFile(READ_WRITE_MODE, READ_WRITE_MODE);
        byte[] buffer = new byte[9];
        
        int bytesRead = file.read(buffer, 0, 105);
        
        assertEquals("Reading from empty file should return EOF", EOF_RESULT, bytesRead);
        assertEquals("File pointer should remain unchanged", INITIAL_FILE_POINTER, file.getFilePointer());
    }

    @Test(timeout = 4000)
    public void testReadArray_ZeroLength_ReturnsEOF() throws Throwable {
        MappedRandomAccessFile file = new MappedRandomAccessFile(READ_WRITE_MODE, READ_WRITE_MODE);
        byte[] buffer = new byte[9];
        
        int bytesRead = file.read(buffer, -56, 0);
        
        assertEquals("Reading zero bytes should return EOF", EOF_RESULT, bytesRead);
        assertEquals("File pointer should remain unchanged", INITIAL_FILE_POINTER, file.getFilePointer());
    }

    @Test(timeout = 4000)
    public void testReadArray_AtLargePosition_ReturnsEOF() throws Throwable {
        MappedRandomAccessFile file = new MappedRandomAccessFile(READ_WRITE_MODE, READ_WRITE_MODE);
        byte[] buffer = new byte[9];
        
        file.seek(LARGE_POSITION);
        int bytesRead = file.read(buffer, -1458, 642);
        
        assertEquals("Reading beyond file size should return EOF", EOF_RESULT, bytesRead);
    }

    @Test(timeout = 4000)
    public void testReadArray_WithNullBuffer_ReturnsEOF() throws Throwable {
        MappedRandomAccessFile file = new MappedRandomAccessFile(READ_WRITE_MODE, READ_WRITE_MODE);
        
        file.seek(NEGATIVE_POSITION);
        int bytesRead = file.read(null, NEGATIVE_POSITION, -1458);
        
        assertEquals("Reading with null buffer should return EOF", EOF_RESULT, bytesRead);
    }

    @Test(timeout = 4000)
    public void testReadArray_WithInvalidOffset_ThrowsArrayIndexOutOfBoundsException() throws Throwable {
        MappedRandomAccessFile file = new MappedRandomAccessFile(READ_WRITE_MODE, READ_WRITE_MODE);
        byte[] emptyBuffer = new byte[0];
        
        file.seek(-1934028347);
        
        try {
            file.read(emptyBuffer, -1934028347, 841);
            fail("Expected ArrayIndexOutOfBoundsException for invalid offset");
        } catch(ArrayIndexOutOfBoundsException e) {
            verifyException("com.itextpdf.text.pdf.MappedRandomAccessFile", e);
        }
    }

    // ========== File Operations Tests ==========

    @Test(timeout = 4000)
    public void testLength_EmptyFile_ReturnsLength() throws Throwable {
        MappedRandomAccessFile file = new MappedRandomAccessFile(READ_WRITE_MODE, READ_WRITE_MODE);
        
        file.length(); // Just verify it doesn't throw
        
        assertEquals("File pointer should remain unchanged after length call", 
                     INITIAL_FILE_POINTER, file.getFilePointer());
    }

    @Test(timeout = 4000)
    public void testGetChannel_NormalOperation_ReturnsChannel() throws Throwable {
        MappedRandomAccessFile file = new MappedRandomAccessFile(READ_WRITE_MODE, READ_WRITE_MODE);
        
        file.getChannel(); // Just verify it doesn't throw
        
        assertEquals("File pointer should remain unchanged after getChannel call", 
                     INITIAL_FILE_POINTER, file.getFilePointer());
    }

    @Test(timeout = 4000)
    public void testGetChannel_AfterClose_ReturnsChannel() throws Throwable {
        MappedRandomAccessFile file = new MappedRandomAccessFile(READ_WRITE_MODE, READ_WRITE_MODE);
        
        file.close();
        file.getChannel(); // Should not throw after close
        
        assertEquals("File pointer should remain unchanged", INITIAL_FILE_POINTER, file.getFilePointer());
    }

    // ========== Close and Cleanup Tests ==========

    @Test(timeout = 4000)
    public void testClose_MultipleCloses_DoesNotThrow() throws Throwable {
        MappedRandomAccessFile file = new MappedRandomAccessFile(READ_WRITE_MODE, READ_WRITE_MODE);
        
        file.close();
        file.close(); // Second close should not throw
        
        assertEquals("File pointer should remain unchanged after close", 
                     INITIAL_FILE_POINTER, file.getFilePointer());
    }

    @Test(timeout = 4000)
    public void testClose_WithIOExceptionSimulation_ThrowsIOException() throws Throwable {
        FileSystemHandling.shouldAllThrowIOExceptions();
        MappedRandomAccessFile file = new MappedRandomAccessFile(READ_WRITE_MODE, READ_WRITE_MODE);
        
        try {
            file.close();
            fail("Expected IOException when file system throws exceptions");
        } catch(IOException e) {
            assertEquals("Simulated IOException", e.getMessage());
            verifyException("org.evosuite.runtime.vfs.VirtualFileSystem", e);
        }
    }

    @Test(timeout = 4000)
    public void testFinalize_NormalOperation_DoesNotThrow() throws Throwable {
        MappedRandomAccessFile file = new MappedRandomAccessFile(READ_WRITE_MODE, READ_WRITE_MODE);
        
        file.finalize(); // Should not throw under normal conditions
        
        assertEquals("File pointer should remain unchanged after finalize", 
                     INITIAL_FILE_POINTER, file.getFilePointer());
    }

    @Test(timeout = 4000)
    public void testFinalize_WithIOExceptionSimulation_ThrowsIOException() throws Throwable {
        FileSystemHandling.shouldAllThrowIOExceptions();
        MappedRandomAccessFile file = new MappedRandomAccessFile(READ_WRITE_MODE, READ_WRITE_MODE);
        
        try {
            file.finalize();
            fail("Expected IOException when file system throws exceptions");
        } catch(IOException e) {
            assertEquals("Simulated IOException", e.getMessage());
            verifyException("org.evosuite.runtime.vfs.VirtualFileSystem", e);
        }
    }

    // ========== Static Utility Method Tests ==========

    @Test(timeout = 4000)
    public void testClean_WithNullBuffer_ReturnsFalse() throws Throwable {
        boolean result = MappedRandomAccessFile.clean(null);
        
        assertFalse("Cleaning null buffer should return false", result);
    }

    @Test(timeout = 4000)
    public void testClean_WithRegularByteBuffer_ReturnsFalse() throws Throwable {
        ByteBuffer buffer = ByteBuffer.allocate(2089);
        
        boolean result = MappedRandomAccessFile.clean(buffer);
        
        assertFalse("Cleaning regular ByteBuffer should return false", result);
    }

    @Test(timeout = 4000)
    public void testClean_WithDirectByteBuffer_ReturnsTrue() throws Throwable {
        ByteBuffer directBuffer = ByteBuffer.allocateDirect(0);
        
        boolean result = MappedRandomAccessFile.clean(directBuffer);
        
        assertTrue("Cleaning direct ByteBuffer should return true", result);
    }
}