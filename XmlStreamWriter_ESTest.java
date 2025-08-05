/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.io.output;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for {@link XmlStreamWriter}.
 */
class XmlStreamWriterTest {

    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

    // --- Constructor and Initialization Tests ---

    @Test
    void constructorWithFile_shouldUseUtf8AsDefaultEncoding(@TempDir Path tempDir) throws IOException {
        File testFile = tempDir.resolve("test.xml").toFile();
        try (XmlStreamWriter writer = new XmlStreamWriter(testFile)) {
            assertEquals("UTF-8", writer.getDefaultEncoding());
        }
    }

    @Test
    void constructorWithOutputStream_shouldUseUtf8AsDefaultEncoding() {
        try (XmlStreamWriter writer = new XmlStreamWriter(baos)) {
            assertEquals("UTF-8", writer.getDefaultEncoding());
        }
    }

    @Test
    void constructorWithOutputStreamAndNullEncoding_shouldUseUtf8AsDefault() {
        // The constructor with a String encoding is deprecated, but we test its behavior.
        // Passing null for the encoding should result in the default (UTF-8).
        try (XmlStreamWriter writer = new XmlStreamWriter(baos, (String) null)) {
            assertEquals("UTF-8", writer.getDefaultEncoding());
        }
    }

    @Test
    void constructor_shouldThrowExceptionForUnsupportedCharset() {
        UnsupportedCharsetException ex = assertThrows(UnsupportedCharsetException.class, () -> {
            new XmlStreamWriter(baos, "INVALID-CHARSET-NAME");
        });
        assertEquals("INVALID-CHARSET-NAME", ex.getCharsetName());
    }

    @Test
    void constructor_shouldThrowExceptionForNullFile() {
        assertThrows(NullPointerException.class, () -> new XmlStreamWriter((File) null));
    }

    @Test
    void constructor_shouldThrowExceptionForDirectoryAsFile(@TempDir Path tempDir) {
        // A directory cannot be opened as a file for writing.
        File directory = tempDir.toFile();
        assertThrows(IOException.class, () -> new XmlStreamWriter(directory));
    }

    // --- Encoding Detection and Retrieval Tests ---

    @Test
    void getEncoding_shouldReturnNullBeforeFirstWrite() {
        try (XmlStreamWriter writer = new XmlStreamWriter(baos)) {
            assertNull(writer.getEncoding(), "Encoding should be null before any data is written.");
        }
    }

    @Test
    void getEncoding_shouldReturnDefaultEncodingWhenPrologHasNoEncoding() throws IOException {
        try (XmlStreamWriter writer = new XmlStreamWriter(baos, "UTF-16BE")) {
            writer.write("<?xml version=\"1.0\"?>");
            writer.flush(); // Flushing triggers encoding detection
            assertEquals("UTF-16BE", writer.getEncoding(), "Encoding should fall back to the default.");
        }
    }

    @Test
    void write_shouldDetectEncodingFromXmlPrologWithSingleQuotes() throws IOException {
        try (XmlStreamWriter writer = new XmlStreamWriter(baos)) {
            String xmlProlog = "<?xml version='1.0' encoding='UTF-16'?>";
            writer.write(xmlProlog);
            writer.flush();

            assertEquals("UTF-16", writer.getEncoding());
            String writtenProlog = new String(baos.toByteArray(), StandardCharsets.UTF_16);
            assertEquals(xmlProlog, writtenProlog);
        }
    }

    @Test
    void write_shouldDetectEncodingFromXmlPrologWithDoubleQuotes() throws IOException {
        try (XmlStreamWriter writer = new XmlStreamWriter(baos)) {
            String xmlProlog = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";
            writer.write(xmlProlog);
            writer.write("<root/>");
            writer.close(); // Closing also triggers detection and flushes

            assertEquals("ISO-8859-1", writer.getEncoding());
            String writtenXml = new String(baos.toByteArray(), StandardCharsets.ISO_8859_1);
            assertEquals(xmlProlog + "<root/>", writtenXml);
        }
    }
    
    @Test
    void getEncoding_shouldReturnDetectedEncodingAfterClose() throws IOException {
        XmlStreamWriter writer = new XmlStreamWriter(baos);
        writer.write("<?xml encoding='UTF-16LE'?>");
        writer.close();
        assertEquals("UTF-16LE", writer.getEncoding());
    }

    // --- Writer Behavior Tests ---

    @Test
    void write_shouldSwitchToUnderlyingStreamAfterPrologBufferIsFull() throws IOException {
        try (XmlStreamWriter writer = new XmlStreamWriter(baos)) {
            // The internal buffer is 4096. Writing more than that forces a flush
            // and a switch from the internal prolog buffer to the real writer.
            String longString = "a".repeat(5000);
            writer.write(longString);
            
            assertEquals("UTF-8", writer.getEncoding(), "Encoding should be default as no prolog was written.");
            assertEquals(5000, baos.size(), "All data should be written to the underlying stream.");
        }
    }

    @Test
    void flush_shouldWriteBufferedContentToStream() throws IOException {
        try (XmlStreamWriter writer = new XmlStreamWriter(baos)) {
            writer.write("test");
            assertEquals(0, baos.size(), "Content should be buffered before flush.");
            
            writer.flush();
            assertEquals(4, baos.size(), "Content should be written after flush.");
        }
    }

    @Test
    void close_shouldCloseUnderlyingStream() throws IOException {
        OutputStream mockOutputStream = mock(OutputStream.class);
        XmlStreamWriter writer = new XmlStreamWriter(mockOutputStream);
        writer.close();
        verify(mockOutputStream).close();
    }

    @Test
    void close_shouldPropagateExceptionFromUnderlyingStream() throws IOException {
        OutputStream mockOutputStream = mock(OutputStream.class);
        doThrow(new IOException("Test Exception")).when(mockOutputStream).close();
        
        XmlStreamWriter writer = new XmlStreamWriter(mockOutputStream);
        
        IOException e = assertThrows(IOException.class, writer::close);
        assertEquals("Test Exception", e.getMessage());
    }

    @Test
    void operations_shouldThrowIOExceptionAfterWriterIsClosed() throws IOException {
        XmlStreamWriter writer = new XmlStreamWriter(baos);
        writer.write("data");
        writer.close();

        assertThrows(IOException.class, () -> writer.write("more data"));
        assertThrows(IOException.class, () -> writer.write(new char[]{'a'}));
        assertThrows(IOException.class, writer::flush);
    }

    @Test
    void operations_shouldFailWhenConstructedWithNullStream() {
        // The deprecated constructor accepts a null stream without throwing.
        XmlStreamWriter writer = new XmlStreamWriter((OutputStream) null);

        // However, any operation that uses the stream should fail.
        assertThrows(NullPointerException.class, () -> writer.write("test"));
        assertThrows(NullPointerException.class, writer::flush);
        assertThrows(NullPointerException.class, writer::close);
    }

    // --- Input Validation Tests ---

    @Test
    void write_shouldThrowExceptionForNullBuffer() {
        try (XmlStreamWriter writer = new XmlStreamWriter(baos)) {
            assertThrows(NullPointerException.class, () -> writer.write(null, 0, 0));
        }
    }

    @Test
    void write_shouldThrowExceptionForInvalidOffsetAndLength() {
        try (XmlStreamWriter writer = new XmlStreamWriter(baos)) {
            char[] buffer = new char[10];
            // Negative offset
            assertThrows(IndexOutOfBoundsException.class, () -> writer.write(buffer, -1, 5));
            // Negative length
            assertThrows(IndexOutOfBoundsException.class, () -> writer.write(buffer, 0, -5));
            // Offset + length > buffer.length
            assertThrows(IndexOutOfBoundsException.class, () -> writer.write(buffer, 6, 5));
        }
    }

    // --- Builder Tests ---

    @Test
    void builder_shouldThrowExceptionWhenOutputNotSet() {
        XmlStreamWriter.Builder builder = XmlStreamWriter.builder();
        IllegalStateException e = assertThrows(IllegalStateException.class, builder::get);
        assertEquals("origin == null", e.getMessage());
    }

    @Test
    void builder_shouldCreateWriterWithOutputStream() throws IOException {
        try (XmlStreamWriter writer = XmlStreamWriter.builder()
                .setOutputStream(baos)
                .get()) {
            writer.write("test");
        }
        assertEquals("test", baos.toString(StandardCharsets.UTF_8));
    }

    @Test
    void builder_shouldCreateWriterWithFile(@TempDir Path tempDir) throws IOException {
        File testFile = tempDir.resolve("builder-test.xml").toFile();
        String content = "<?xml encoding='UTF-16'?>";

        try (XmlStreamWriter writer = XmlStreamWriter.builder()
                .setFile(testFile)
                .setCharset(StandardCharsets.UTF_8) // Default charset
                .get()) {
            writer.write(content);
        }

        // The writer should detect UTF-16 from the prolog and use it.
        byte[] fileBytes = Files.readAllBytes(testFile.toPath());
        String fileContent = new String(fileBytes, StandardCharsets.UTF_16);
        assertEquals(content, fileContent);
    }

    @Test
    void builder_shouldCreateWriterWithCustomDefaultEncoding() throws IOException {
        try (XmlStreamWriter writer = XmlStreamWriter.builder()
                .setOutputStream(baos)
                .setCharset(StandardCharsets.US_ASCII)
                .get()) {
            
            assertEquals("US-ASCII", writer.getDefaultEncoding());
            
            writer.write("<root/>");
        }
        
        // The output should be in the specified default encoding.
        assertEquals("<root/>", baos.toString(StandardCharsets.US_ASCII));
    }
}