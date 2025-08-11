package org.apache.commons.io.output;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

/**
 * Readable, behavior-focused tests for XmlStreamWriter.
 *
 * These tests avoid EvoSuite-specific scaffolding and focus on:
 * - Default and detected encodings.
 * - Behavior with and without an XML prolog.
 * - API behavior around close() and flush().
 * - The builder API.
 */
public class XmlStreamWriterTest {

    @Test
    public void defaultEncodingIsUtf8ByDefault() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XmlStreamWriter writer = new XmlStreamWriter(out);

        assertEquals("UTF-8", writer.getDefaultEncoding());
    }

    @Test
    public void encodingIsNullBeforeAnyWrite() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XmlStreamWriter writer = new XmlStreamWriter(out);

        assertNull("No encoding should be detected before any content is written", writer.getEncoding());
    }

    @Test
    public void usesDefaultEncodingWhenNoProlog() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XmlStreamWriter writer = new XmlStreamWriter(out);

        writer.write("<root/>");
        writer.flush();

        // No prolog => fall back to default encoding.
        assertEquals("UTF-8", writer.getEncoding());
    }

    @Test
    public void detectsEncodingDeclaredInXmlProlog() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XmlStreamWriter writer = new XmlStreamWriter(out);

        writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><root/>");
        writer.flush();

        assertEquals("ISO-8859-1", writer.getEncoding());
    }

    @Test
    public void appendAndWriteDoNotBreakDetection() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XmlStreamWriter writer = new XmlStreamWriter(out);

        // Prolog without an encoding attribute -> default (UTF-8).
        writer.append("<?xml version=\"1.0\"?>");
        writer.write("<root/>");
        writer.flush();

        assertEquals("UTF-8", writer.getEncoding());
    }

    @Test
    public void writeAfterCloseThrowsIOException() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XmlStreamWriter writer = new XmlStreamWriter(out);

        writer.write("<root/>");
        writer.close();

        try {
            writer.write("<again/>");
            fail("Expected IOException when writing after close()");
        } catch (IOException expected) {
            // ok
        }
    }

    @Test
    public void flushAfterCloseThrowsIOException() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XmlStreamWriter writer = new XmlStreamWriter(out);

        writer.write("<root/>");
        writer.close();

        try {
            writer.flush();
            fail("Expected IOException when flushing after close()");
        } catch (IOException expected) {
            // ok
        }
    }

    @Test
    public void deprecatedConstructorWithDefaultEncodingStringIsHonored() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XmlStreamWriter writer = new XmlStreamWriter(out, "US-ASCII");

        assertEquals("US-ASCII", writer.getDefaultEncoding());

        writer.write("<root/>");
        writer.flush();

        assertEquals("US-ASCII", writer.getEncoding());
    }

    @Test
    public void builderRequiresAnOriginOutputStream() {
        try {
            XmlStreamWriter.builder().get();
            fail("Expected IllegalStateException when no origin OutputStream is set");
        } catch (IllegalStateException expected) {
            // ok
        } catch (IOException e) {
            fail("Did not expect IOException here");
        }
    }

    @Test
    public void builderHonorsProvidedCharset() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        XmlStreamWriter writer = XmlStreamWriter.builder()
                .setOutputStream(out)
                .setCharset(StandardCharsets.US_ASCII)
                .get();

        assertEquals("US-ASCII", writer.getDefaultEncoding());

        writer.write("<root/>");
        writer.flush();

        assertEquals("US-ASCII", writer.getEncoding());
    }
}