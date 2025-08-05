package org.apache.commons.io.output;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Writer;
import java.nio.CharBuffer;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import org.apache.commons.io.output.XmlStreamWriter;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.io.MockPrintStream;
import org.junit.runner.RunWith;

/**
 * Test suite for XmlStreamWriter functionality including:
 * - Constructor validation and error handling
 * - Character encoding detection and management
 * - Write operations and stream lifecycle
 * - Builder pattern usage
 */
@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true)
public class XmlStreamWriterTest extends XmlStreamWriter_ESTest_scaffolding {

    // ========== Constructor Tests ==========
    
    @Test(timeout = 4000)
    public void testConstructorWithNullFile_ThrowsNullPointerException() throws Throwable {
        try {
            new XmlStreamWriter((File) null, "UTF-8");
            fail("Expected NullPointerException when file is null");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithNonExistentFile_ThrowsFileNotFoundException() throws Throwable {
        MockFile nonExistentFile = new MockFile("", "");
        
        try {
            new XmlStreamWriter(nonExistentFile);
            fail("Expected FileNotFoundException for non-existent file");
        } catch (FileNotFoundException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithValidFile_SetsDefaultEncoding() throws Throwable {
        MockFile validFile = new MockFile("test-file");
        XmlStreamWriter writer = new XmlStreamWriter(validFile, null);
        
        assertEquals("UTF-8", writer.getDefaultEncoding());
    }

    @Test(timeout = 4000)
    public void testConstructorWithInvalidCharsetName_ThrowsIllegalCharsetNameException() throws Throwable {
        java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
        
        try {
            new XmlStreamWriter(outputStream, "<?!invalid-charset");
            fail("Expected IllegalCharsetNameException for invalid charset name");
        } catch (IllegalCharsetNameException e) {
            assertEquals("<?!invalid-charset", e.getCharsetName());
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithUnsupportedCharset_ThrowsUnsupportedCharsetException() throws Throwable {
        java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
        
        try {
            new XmlStreamWriter(outputStream, "unsupported-charset");
            fail("Expected UnsupportedCharsetException for unsupported charset");
        } catch (UnsupportedCharsetException e) {
            assertEquals("unsupported-charset", e.getCharsetName());
        }
    }

    // ========== Write Operation Tests ==========

    @Test(timeout = 4000)
    public void testWriteWithNullCharArray_ThrowsNullPointerException() throws Throwable {
        PipedInputStream inputStream = new PipedInputStream();
        PipedOutputStream outputStream = new PipedOutputStream(inputStream);
        XmlStreamWriter writer = new XmlStreamWriter(outputStream);
        
        try {
            writer.write((char[]) null, 0, 10);
            fail("Expected NullPointerException when writing null char array");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testWriteWithValidCharArray_WritesSuccessfully() throws Throwable {
        java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
        XmlStreamWriter writer = new XmlStreamWriter(outputStream, null);
        char[] testData = {'t', 'e', 's', 't', '\0'};
        
        writer.write(testData, 1, 1);
        
        assertEquals("UTF-8", writer.getDefaultEncoding());
    }

    @Test(timeout = 4000)
    public void testWriteWithInvalidArrayBounds_ThrowsIndexOutOfBoundsException() throws Throwable {
        XmlStreamWriter writer = new XmlStreamWriter((OutputStream) null);
        char[] emptyArray = new char[0];
        
        try {
            writer.write(emptyArray, 10, 10);
            fail("Expected IndexOutOfBoundsException for invalid array bounds");
        } catch (IndexOutOfBoundsException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testWriteAfterClose_ThrowsIOException() throws Throwable {
        PipedInputStream inputStream = new PipedInputStream();
        PipedOutputStream outputStream = new PipedOutputStream(inputStream);
        XmlStreamWriter writer = new XmlStreamWriter(outputStream);
        char[] testData = new char[9];
        
        writer.write(testData);
        writer.close();
        
        try {
            writer.write(testData, 0, testData.length);
            fail("Expected IOException when writing to closed stream");
        } catch (IOException e) {
            // Expected behavior
        }
    }

    // ========== Stream Lifecycle Tests ==========

    @Test(timeout = 4000)
    public void testFlushAfterClose_ThrowsIOException() throws Throwable {
        java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
        XmlStreamWriter writer = new XmlStreamWriter(outputStream);
        
        writer.close();
        
        try {
            writer.flush();
            fail("Expected IOException when flushing closed stream");
        } catch (IOException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testCloseWithNullOutputStream_ThrowsNullPointerException() throws Throwable {
        XmlStreamWriter writer = new XmlStreamWriter((OutputStream) null);
        
        try {
            writer.close();
            fail("Expected NullPointerException when closing with null output stream");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testCloseWithDisconnectedPipe_ThrowsIOException() throws Throwable {
        PipedOutputStream disconnectedPipe = new PipedOutputStream();
        XmlStreamWriter writer = new XmlStreamWriter(disconnectedPipe);
        char[] testData = new char[9];
        writer.write(testData);
        
        try {
            writer.close();
            fail("Expected IOException when closing disconnected pipe");
        } catch (IOException e) {
            assertTrue("Should mention pipe connection", e.getMessage().contains("Pipe not connected"));
        }
    }

    @Test(timeout = 4000)
    public void testFlushWithConnectedPipe_WritesDataSuccessfully() throws Throwable {
        PipedInputStream inputStream = new PipedInputStream();
        PipedOutputStream outputStream = new PipedOutputStream(inputStream);
        XmlStreamWriter writer = new XmlStreamWriter(outputStream);
        char[] testData = new char[9];
        
        writer.write(testData);
        writer.flush();
        
        assertEquals("Data should be available in input stream", 9, inputStream.available());
    }

    @Test(timeout = 4000)
    public void testFlushWithoutPriorWrite_CompletesSuccessfully() throws Throwable {
        java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
        XmlStreamWriter writer = new XmlStreamWriter(outputStream);
        
        writer.flush(); // Should not throw exception
        
        assertEquals("UTF-8", writer.getDefaultEncoding());
    }

    // ========== Encoding Detection Tests ==========

    @Test(timeout = 4000)
    public void testGetEncodingAfterWritingXmlDeclaration_ReturnsDetectedEncoding() throws Throwable {
        MockFile testFile = new MockFile("test.xml");
        MockPrintStream printStream = new MockPrintStream(testFile);
        XmlStreamWriter writer = new XmlStreamWriter(printStream);
        
        writer.close();
        
        assertEquals("UTF-8", writer.getEncoding());
    }

    @Test(timeout = 4000)
    public void testGetEncodingBeforeWriting_ThrowsNullPointerException() throws Throwable {
        java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
        XmlStreamWriter writer = new XmlStreamWriter(outputStream);
        
        try {
            writer.getEncoding();
            fail("Expected NullPointerException when getting encoding before writing");
        } catch (NullPointerException e) {
            // Expected behavior - encoding not yet determined
        }
    }

    @Test(timeout = 4000)
    public void testWriteXmlContent_DetectsEncodingCorrectly() throws Throwable {
        java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
        XmlStreamWriter writer = new XmlStreamWriter(outputStream);
        
        writer.append("<?xml");
        writer.write("?>");
        
        assertEquals("UTF-8", writer.getEncoding());
    }

    @Test(timeout = 4000)
    public void testWriteLargeContent_HandlesBufferCorrectly() throws Throwable {
        java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
        XmlStreamWriter writer = new XmlStreamWriter(outputStream);
        CharBuffer largeBuffer = CharBuffer.allocate(8192);
        
        writer.write("<?xml");
        writer.append(largeBuffer);
        
        assertEquals("Large content should be written", 8192, outputStream.size());
    }

    @Test(timeout = 4000)
    public void testWriteComplexXmlDocument_MaintainsEncoding() throws Throwable {
        MockFile testFile = new MockFile("complex-doc.xml");
        XmlStreamWriter writer = new XmlStreamWriter(testFile);
        
        writer.append("<?xml version='1.0'?>");
        writer.write("<root>content</root>");
        
        assertEquals("UTF-8", writer.getEncoding());
    }

    // ========== Default Encoding Tests ==========

    @Test(timeout = 4000)
    public void testGetDefaultEncoding_ReturnsUTF8() throws Throwable {
        PipedInputStream inputStream = new PipedInputStream();
        PipedOutputStream outputStream = new PipedOutputStream(inputStream);
        XmlStreamWriter writer = new XmlStreamWriter(outputStream);
        
        assertEquals("UTF-8", writer.getDefaultEncoding());
    }

    // ========== Builder Pattern Tests ==========

    @Test(timeout = 4000)
    public void testBuilderCreation_ReturnsValidBuilder() throws Throwable {
        XmlStreamWriter.Builder builder = new XmlStreamWriter.Builder();
        
        assertEquals("Builder should have default buffer size", 8192, builder.getBufferSizeDefault());
    }

    @Test(timeout = 4000)
    public void testBuilderWithoutOrigin_ThrowsIllegalStateException() throws Throwable {
        XmlStreamWriter.Builder builder = XmlStreamWriter.builder();
        
        try {
            builder.get();
            fail("Expected IllegalStateException when building without origin");
        } catch (IllegalStateException e) {
            assertTrue("Should mention null origin", e.getMessage().contains("origin == null"));
        }
    }

    @Test(timeout = 4000)
    public void testAppendOperationAfterWrite_HandlesNullSequence() throws Throwable {
        java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
        XmlStreamWriter writer = new XmlStreamWriter(outputStream);
        
        Writer appendResult = writer.append((CharSequence) null);
        char[] testData = new char[1];
        appendResult.write(testData);
        
        try {
            writer.write((char[]) null, 0, 10);
            fail("Expected NullPointerException when writing null after append");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }
}