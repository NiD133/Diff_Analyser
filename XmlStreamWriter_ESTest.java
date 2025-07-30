package org.apache.commons.io.output;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.*;
import java.nio.CharBuffer;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import org.apache.commons.io.output.XmlStreamWriter;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.io.MockPrintStream;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class XmlStreamWriter_ESTest extends XmlStreamWriter_ESTest_scaffolding {

    // Helper method to create XmlStreamWriter with Piped Streams
    private XmlStreamWriter createXmlStreamWriterWithPipedStreams() throws IOException {
        PipedInputStream pipedInputStream = new PipedInputStream();
        PipedOutputStream pipedOutputStream = new PipedOutputStream(pipedInputStream);
        return new XmlStreamWriter(pipedOutputStream);
    }

    @Test(timeout = 4000)
    public void testWriteNullCharArrayThrowsNullPointerException() throws Throwable {
        XmlStreamWriter xmlStreamWriter = createXmlStreamWriterWithPipedStreams();
        try {
            xmlStreamWriter.write((char[]) null, 2049, 8192);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testWriteWithDefaultEncoding() throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        XmlStreamWriter xmlStreamWriter = new XmlStreamWriter(byteArrayOutputStream, (String) null);
        char[] charArray = new char[5];
        xmlStreamWriter.write(charArray, 1, 1);
        assertEquals("UTF-8", xmlStreamWriter.getDefaultEncoding());
    }

    @Test(timeout = 4000)
    public void testDefaultEncodingForMockFile() throws Throwable {
        MockFile mockFile = new MockFile("U-");
        XmlStreamWriter xmlStreamWriter = new XmlStreamWriter(mockFile, (String) null);
        assertEquals("UTF-8", xmlStreamWriter.getDefaultEncoding());
    }

    @Test(timeout = 4000)
    public void testCloseAndGetEncoding() throws Throwable {
        MockFile mockFile = new MockFile("<?xml");
        MockPrintStream mockPrintStream = new MockPrintStream(mockFile);
        XmlStreamWriter xmlStreamWriter = new XmlStreamWriter(mockPrintStream);
        xmlStreamWriter.close();
        assertEquals("UTF-8", xmlStreamWriter.getEncoding());
    }

    @Test(timeout = 4000)
    public void testWriteWithInvalidOffsetThrowsIndexOutOfBoundsException() throws Throwable {
        XmlStreamWriter xmlStreamWriter = new XmlStreamWriter((OutputStream) null);
        char[] charArray = new char[0];
        try {
            xmlStreamWriter.write(charArray, 6343, 6343);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.io.StringWriter", e);
        }
    }

    @Test(timeout = 4000)
    public void testWriteAfterCloseThrowsIOException() throws Throwable {
        XmlStreamWriter xmlStreamWriter = createXmlStreamWriterWithPipedStreams();
        char[] charArray = new char[9];
        xmlStreamWriter.write(charArray);
        xmlStreamWriter.close();
        try {
            xmlStreamWriter.write(charArray, 2049, 2049);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testFlushAfterCloseThrowsIOException() throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        XmlStreamWriter xmlStreamWriter = new XmlStreamWriter(byteArrayOutputStream);
        xmlStreamWriter.close();
        try {
            xmlStreamWriter.flush();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testCloseNullOutputStreamThrowsNullPointerException() throws Throwable {
        XmlStreamWriter xmlStreamWriter = new XmlStreamWriter((OutputStream) null);
        try {
            xmlStreamWriter.close();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.io.Writer", e);
        }
    }

    @Test(timeout = 4000)
    public void testCloseUnconnectedPipeThrowsIOException() throws Throwable {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        XmlStreamWriter xmlStreamWriter = new XmlStreamWriter(pipedOutputStream);
        char[] charArray = new char[9];
        xmlStreamWriter.write(charArray);
        try {
            xmlStreamWriter.close();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedOutputStream", e);
        }
    }

    @Test(timeout = 4000)
    public void testUnsupportedCharsetThrowsException() throws Throwable {
        try {
            new XmlStreamWriter((OutputStream) null, "wm");
            fail("Expecting exception: UnsupportedCharsetException");
        } catch (UnsupportedCharsetException e) {
            verifyException("java.nio.charset.Charset", e);
        }
    }

    @Test(timeout = 4000)
    public void testIllegalCharsetNameThrowsException() throws Throwable {
        try {
            new XmlStreamWriter((OutputStream) null, "<?!wml");
            fail("Expecting exception: IllegalCharsetNameException");
        } catch (IllegalCharsetNameException e) {
            verifyException("java.nio.charset.Charset", e);
        }
    }

    @Test(timeout = 4000)
    public void testUnsupportedCharsetForMockFileThrowsException() throws Throwable {
        MockFile mockFile = new MockFile("z");
        try {
            new XmlStreamWriter(mockFile, "z");
            fail("Expecting exception: UnsupportedCharsetException");
        } catch (UnsupportedCharsetException e) {
            verifyException("java.nio.charset.Charset", e);
        }
    }

    @Test(timeout = 4000)
    public void testIllegalCharsetNameForFileThrowsException() throws Throwable {
        File file = MockFile.createTempFile("O`7", "O`7");
        try {
            new XmlStreamWriter(file, "O`7");
            fail("Expecting exception: IllegalCharsetNameException");
        } catch (IllegalCharsetNameException e) {
            verifyException("java.nio.charset.Charset", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullFileThrowsNullPointerException() throws Throwable {
        try {
            new XmlStreamWriter((File) null, "org.apache.commons.io.output.XmlStreamWriter$Builder");
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.io.File", e);
        }
    }

    @Test(timeout = 4000)
    public void testFileNotFoundForMockFileThrowsException() throws Throwable {
        MockFile mockFile = new MockFile("", "");
        try {
            new XmlStreamWriter(mockFile, "");
            fail("Expecting exception: FileNotFoundException");
        } catch (Throwable e) {
            verifyException("org.evosuite.runtime.mock.java.io.MockFileOutputStream", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullFileThrowsNullPointerExceptionWithoutEncoding() throws Throwable {
        try {
            new XmlStreamWriter((File) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.io.File", e);
        }
    }

    @Test(timeout = 4000)
    public void testFileNotFoundForMockFileWithoutEncodingThrowsException() throws Throwable {
        MockFile mockFile = new MockFile("", "");
        try {
            new XmlStreamWriter(mockFile);
            fail("Expecting exception: FileNotFoundException");
        } catch (Throwable e) {
            verifyException("org.evosuite.runtime.mock.java.io.MockFileOutputStream", e);
        }
    }

    @Test(timeout = 4000)
    public void testDefaultBufferSizeInBuilder() throws Throwable {
        XmlStreamWriter.Builder builder = new XmlStreamWriter.Builder();
        assertEquals(8192, builder.getBufferSizeDefault());
    }

    @Test(timeout = 4000)
    public void testAppendNullCharSequenceThrowsNullPointerException() throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        XmlStreamWriter xmlStreamWriter = new XmlStreamWriter(byteArrayOutputStream);
        Writer writer = xmlStreamWriter.append((CharSequence) null);
        char[] charArray = new char[1];
        writer.write(charArray);
        try {
            xmlStreamWriter.write((char[]) null, 2049, 2049);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testFlushAfterWrite() throws Throwable {
        XmlStreamWriter xmlStreamWriter = createXmlStreamWriterWithPipedStreams();
        char[] charArray = new char[9];
        xmlStreamWriter.write(charArray);
        xmlStreamWriter.flush();
        assertEquals(9, xmlStreamWriter.getOutputStream().available());
    }

    @Test(timeout = 4000)
    public void testFlushWithDefaultEncoding() throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        XmlStreamWriter xmlStreamWriter = new XmlStreamWriter(byteArrayOutputStream);
        xmlStreamWriter.flush();
        assertEquals("UTF-8", xmlStreamWriter.getDefaultEncoding());
    }

    @Test(timeout = 4000)
    public void testAppendAndWriteWithXmlDeclaration() throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        XmlStreamWriter xmlStreamWriter = new XmlStreamWriter(byteArrayOutputStream);
        xmlStreamWriter.append((CharSequence) "<?xml");
        xmlStreamWriter.write("?>");
        assertEquals("UTF-8", xmlStreamWriter.getEncoding());
    }

    @Test(timeout = 4000)
    public void testAppendCharBuffer() throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        XmlStreamWriter xmlStreamWriter = new XmlStreamWriter(byteArrayOutputStream);
        xmlStreamWriter.write("<?xml");
        CharBuffer charBuffer = CharBuffer.allocate(8192);
        xmlStreamWriter.append((CharSequence) charBuffer);
        assertEquals(8192, byteArrayOutputStream.size());
    }

    @Test(timeout = 4000)
    public void testGetDefaultEncoding() throws Throwable {
        XmlStreamWriter xmlStreamWriter = createXmlStreamWriterWithPipedStreams();
        assertEquals("UTF-8", xmlStreamWriter.getDefaultEncoding());
    }

    @Test(timeout = 4000)
    public void testGetEncodingThrowsNullPointerException() throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        XmlStreamWriter xmlStreamWriter = new XmlStreamWriter(byteArrayOutputStream);
        try {
            xmlStreamWriter.getEncoding();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.io.output.XmlStreamWriter", e);
        }
    }

    @Test(timeout = 4000)
    public void testAppendAndWriteWithMockFile() throws Throwable {
        MockFile mockFile = new MockFile("org.apache.commons.io.output.XmlStreamWriter$1");
        XmlStreamWriter xmlStreamWriter = new XmlStreamWriter(mockFile);
        xmlStreamWriter.append((CharSequence) "org.apache.commons.io.output.XmlStreamWriter$1");
        xmlStreamWriter.write("org.apache.commons.io.output.XmlStreamWriter$1");
        assertEquals("UTF-8", xmlStreamWriter.getEncoding());
    }

    @Test(timeout = 4000)
    public void testBuilderGetThrowsIllegalStateException() throws Throwable {
        XmlStreamWriter.Builder builder = XmlStreamWriter.builder();
        try {
            builder.get();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.apache.commons.io.build.AbstractOriginSupplier", e);
        }
    }
}