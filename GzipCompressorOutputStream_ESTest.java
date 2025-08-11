package org.apache.commons.compress.compressors.gzip;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Locale;
import org.apache.commons.compress.compressors.gzip.ExtraField;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipParameters;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;
import org.evosuite.runtime.mock.java.io.MockPrintStream;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class GzipCompressorOutputStreamTest extends GzipCompressorOutputStream_ESTest_scaffolding {

    private static final int TIMEOUT = 4000;
    private static final int GZIP_HEADER_SIZE = 10;
    private static final int GZIP_HEADER_WITH_CRC_SIZE = 12;
    private static final int GZIP_FINISH_SIZE = 20;

    @Test(timeout = TIMEOUT)
    public void testWriteBeyondArrayBounds() throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzipOutputStream = new GzipCompressorOutputStream(byteArrayOutputStream);
        byte[] byteArray = new byte[0];

        try {
            gzipOutputStream.write(byteArray, 2860, 2860);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("java.util.zip.Deflater", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testWriteAfterClose() throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzipOutputStream = new GzipCompressorOutputStream(byteArrayOutputStream);
        byte[] byteArray = new byte[0];
        gzipOutputStream.close();

        try {
            gzipOutputStream.write(byteArray, 2860, 2860);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.apache.commons.compress.CompressFilterOutputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testWriteNullByteArray() throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzipOutputStream = new GzipCompressorOutputStream(byteArrayOutputStream);

        try {
            gzipOutputStream.write((byte[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testFinishAfterClose() throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzipOutputStream = new GzipCompressorOutputStream(byteArrayOutputStream);
        gzipOutputStream.close();

        try {
            gzipOutputStream.finish();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.zip.Deflater", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testGzipOutputStreamWithNullOutputStream() throws Throwable {
        try {
            new GzipCompressorOutputStream((OutputStream) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testGzipOutputStreamWithNullParameters() throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            new GzipCompressorOutputStream(byteArrayOutputStream, (GzipParameters) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testGzipOutputStreamWithInvalidCompressionLevel() throws Throwable {
        GzipParameters gzipParameters = new GzipParameters();
        gzipParameters.setDeflateStrategy(14);

        try {
            new GzipCompressorOutputStream((OutputStream) null, gzipParameters);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("java.util.zip.Deflater", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testGzipOutputStreamWithPipedOutputStream() throws Throwable {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        GzipParameters gzipParameters = new GzipParameters();

        try {
            new GzipCompressorOutputStream(pipedOutputStream, gzipParameters);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedOutputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testGzipOutputStreamWithHeaderCRC() throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GzipParameters gzipParameters = new GzipParameters();
        gzipParameters.setHeaderCRC(true);
        new GzipCompressorOutputStream(byteArrayOutputStream, gzipParameters);

        assertEquals(GZIP_HEADER_WITH_CRC_SIZE, byteArrayOutputStream.size());
    }

    @Test(timeout = TIMEOUT)
    public void testFinish() throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzipOutputStream = new GzipCompressorOutputStream(byteArrayOutputStream);
        gzipOutputStream.finish();

        assertEquals(GZIP_FINISH_SIZE, byteArrayOutputStream.size());
    }

    @Test(timeout = TIMEOUT)
    public void testCloseTwice() throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzipOutputStream = new GzipCompressorOutputStream(byteArrayOutputStream);
        gzipOutputStream.close();
        gzipOutputStream.close();

        assertTrue(gzipOutputStream.isClosed());
    }

    // Additional tests can be added here following the same pattern
}