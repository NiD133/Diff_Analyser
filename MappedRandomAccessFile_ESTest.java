package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.itextpdf.text.pdf.MappedRandomAccessFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
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
public class MappedRandomAccessFile_ESTest extends MappedRandomAccessFile_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testFilePointerInitiallyZero() throws Throwable {
        MappedRandomAccessFile file = new MappedRandomAccessFile("rw", "rw");
        assertEquals(0L, file.getFilePointer());
    }

    @Test(timeout = 4000)
    public void testSeekAndReadBeyondEOF() throws Throwable {
        MappedRandomAccessFile file = new MappedRandomAccessFile("rw", "rw");
        file.seek(1073741824L);
        byte[] buffer = new byte[9];
        int bytesRead = file.read(buffer, -1458, 642);
        assertEquals(-1, bytesRead);
    }

    @Test(timeout = 4000)
    public void testSeekAndReadSingleByteBeyondEOF() throws Throwable {
        MappedRandomAccessFile file = new MappedRandomAccessFile("rw", "rw");
        file.seek(1073741824L);
        assertEquals(1073741824L, file.getFilePointer());
        int byteRead = file.read();
        assertEquals(-1, byteRead);
    }

    @Test(timeout = 4000)
    public void testNullFilenameThrowsException() throws Throwable {
        try {
            new MappedRandomAccessFile(null, "rw");
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.evosuite.runtime.mock.java.io.MockRandomAccessFile", e);
        }
    }

    @Test(timeout = 4000)
    public void testSeekToSpecificPosition() throws Throwable {
        MappedRandomAccessFile file = new MappedRandomAccessFile("rw", "rw");
        file.seek(686);
        assertEquals(686L, file.getFilePointer());
    }

    @Test(timeout = 4000)
    public void testSeekToNegativePosition() throws Throwable {
        MappedRandomAccessFile file = new MappedRandomAccessFile("rw", "rw");
        file.seek(-2756L);
        assertEquals(-2756L, file.getFilePointer());
    }

    @Test(timeout = 4000)
    public void testCloseAndGetChannel() throws Throwable {
        MappedRandomAccessFile file = new MappedRandomAccessFile("rw", "rw");
        file.close();
        file.getChannel();
        assertEquals(0L, file.getFilePointer());
    }

    @Test(timeout = 4000)
    public void testFinalizeThrowsIOException() throws Throwable {
        FileSystemHandling.shouldAllThrowIOExceptions();
        MappedRandomAccessFile file = new MappedRandomAccessFile("rw", "rw");
        try {
            file.finalize();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.evosuite.runtime.vfs.VirtualFileSystem", e);
        }
    }

    @Test(timeout = 4000)
    public void testCloseThrowsIOException() throws Throwable {
        FileSystemHandling.shouldAllThrowIOExceptions();
        MappedRandomAccessFile file = new MappedRandomAccessFile("rw", "rw");
        try {
            file.close();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.evosuite.runtime.vfs.VirtualFileSystem", e);
        }
    }

    @Test(timeout = 4000)
    public void testCleanByteBuffer() throws Throwable {
        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate(2089);
        boolean result = MappedRandomAccessFile.clean(buffer);
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void testCleanNullByteBuffer() throws Throwable {
        boolean result = MappedRandomAccessFile.clean(null);
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void testMultipleCloseCalls() throws Throwable {
        MappedRandomAccessFile file = new MappedRandomAccessFile("rw", "rw");
        file.close();
        file.close();
        assertEquals(0L, file.getFilePointer());
    }

    @Test(timeout = 4000)
    public void testReadWithInvalidOffsetThrowsException() throws Throwable {
        MappedRandomAccessFile file = new MappedRandomAccessFile("rw", "rw");
        file.seek(-1934028347);
        byte[] buffer = new byte[0];
        try {
            file.read(buffer, -1934028347, 841);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("com.itextpdf.text.pdf.MappedRandomAccessFile", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadWithZeroLength() throws Throwable {
        MappedRandomAccessFile file = new MappedRandomAccessFile("rw", "rw");
        byte[] buffer = new byte[9];
        int bytesRead = file.read(buffer, 0, 105);
        assertEquals(-1, bytesRead);
        assertEquals(0L, file.getFilePointer());
    }

    @Test(timeout = 4000)
    public void testReadWithNegativeOffset() throws Throwable {
        byte[] buffer = new byte[9];
        MappedRandomAccessFile file = new MappedRandomAccessFile("rw", "rw");
        int bytesRead = file.read(buffer, -56, 0);
        assertEquals(0L, file.getFilePointer());
        assertEquals(-1, bytesRead);
    }

    @Test(timeout = 4000)
    public void testReadSingleByteAtStart() throws Throwable {
        MappedRandomAccessFile file = new MappedRandomAccessFile("rw", "rw");
        int byteRead = file.read();
        assertEquals(0L, file.getFilePointer());
        assertEquals(-1, byteRead);
    }

    @Test(timeout = 4000)
    public void testReadSingleByteWithNegativePositionThrowsException() throws Throwable {
        MappedRandomAccessFile file = new MappedRandomAccessFile("rw", "rw");
        file.seek(-1934028355);
        try {
            file.read();
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("com.itextpdf.text.pdf.MappedRandomAccessFile", e);
        }
    }

    @Test(timeout = 4000)
    public void testMappedByteBufferNotSupported() throws Throwable {
        EvoSuiteFile evoSuiteFile = new EvoSuiteFile("qw");
        FileSystemHandling.appendStringToFile(evoSuiteFile, "qw");
        try {
            new MappedRandomAccessFile("qw", "qw");
            fail("Expecting exception: IOException");
        } catch (Throwable e) {
            verifyException("org.evosuite.runtime.mock.java.io.EvoFileChannel", e);
        }
    }

    @Test(timeout = 4000)
    public void testFileNotFoundException() throws Throwable {
        try {
            new MappedRandomAccessFile("rw", "PmF8^@U[W<vM7");
            fail("Expecting exception: FileNotFoundException");
        } catch (Throwable e) {
            verifyException("org.evosuite.runtime.mock.java.io.MockFileInputStream", e);
        }
    }

    @Test(timeout = 4000)
    public void testCleanDirectByteBuffer() throws Throwable {
        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocateDirect(0);
        boolean result = MappedRandomAccessFile.clean(buffer);
        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void testGetChannel() throws Throwable {
        MappedRandomAccessFile file = new MappedRandomAccessFile("rw", "rw");
        file.getChannel();
        assertEquals(0L, file.getFilePointer());
    }

    @Test(timeout = 4000)
    public void testGetFilePointer() throws Throwable {
        MappedRandomAccessFile file = new MappedRandomAccessFile("rw", "rw");
        long position = file.getFilePointer();
        assertEquals(0L, position);
    }

    @Test(timeout = 4000)
    public void testFileLength() throws Throwable {
        MappedRandomAccessFile file = new MappedRandomAccessFile("rw", "rw");
        file.length();
        assertEquals(0L, file.getFilePointer());
    }

    @Test(timeout = 4000)
    public void testFinalize() throws Throwable {
        MappedRandomAccessFile file = new MappedRandomAccessFile("rw", "rw");
        file.finalize();
        assertEquals(0L, file.getFilePointer());
    }
}