package com.google.common.io;

import static org.junit.Assert.assertThrows;
import java.io.IOException;
import java.nio.CharBuffer;
import junit.framework.TestCase;
import org.jspecify.annotations.NullUnmarked;

public class CharSequenceReaderTestTest5 extends TestCase {

    /**
     * Creates a CharSequenceReader wrapping the given CharSequence and tests that the reader produces
     * the same sequence when read using each type of read method it provides.
     */
    private static void assertReadsCorrectly(CharSequence charSequence) throws IOException {
        String expected = charSequence.toString();
        // read char by char
        CharSequenceReader reader = new CharSequenceReader(charSequence);
        for (int i = 0; i < expected.length(); i++) {
            assertEquals(expected.charAt(i), reader.read());
        }
        assertFullyRead(reader);
        // read all to one array
        reader = new CharSequenceReader(charSequence);
        char[] buf = new char[expected.length()];
        assertEquals(expected.length() == 0 ? -1 : expected.length(), reader.read(buf));
        assertEquals(expected, new String(buf));
        assertFullyRead(reader);
        // read in chunks to fixed array
        reader = new CharSequenceReader(charSequence);
        buf = new char[5];
        StringBuilder builder = new StringBuilder();
        int read;
        while ((read = reader.read(buf, 0, buf.length)) != -1) {
            builder.append(buf, 0, read);
        }
        assertEquals(expected, builder.toString());
        assertFullyRead(reader);
        // read all to one CharBuffer
        reader = new CharSequenceReader(charSequence);
        CharBuffer buf2 = CharBuffer.allocate(expected.length());
        assertEquals(expected.length() == 0 ? -1 : expected.length(), reader.read(buf2));
        Java8Compatibility.flip(buf2);
        assertEquals(expected, buf2.toString());
        assertFullyRead(reader);
        // read in chunks to fixed CharBuffer
        reader = new CharSequenceReader(charSequence);
        buf2 = CharBuffer.allocate(5);
        builder = new StringBuilder();
        while (reader.read(buf2) != -1) {
            Java8Compatibility.flip(buf2);
            builder.append(buf2);
            Java8Compatibility.clear(buf2);
        }
        assertEquals(expected, builder.toString());
        assertFullyRead(reader);
        // skip fully
        reader = new CharSequenceReader(charSequence);
        assertEquals(expected.length(), reader.skip(Long.MAX_VALUE));
        assertFullyRead(reader);
        // skip 5 and read the rest
        if (expected.length() > 5) {
            reader = new CharSequenceReader(charSequence);
            assertEquals(5, reader.skip(5));
            buf = new char[expected.length() - 5];
            assertEquals(buf.length, reader.read(buf, 0, buf.length));
            assertEquals(expected.substring(5), new String(buf));
            assertFullyRead(reader);
        }
    }

    private static void assertFullyRead(CharSequenceReader reader) throws IOException {
        assertEquals(-1, reader.read());
        assertEquals(-1, reader.read(new char[10], 0, 10));
        assertEquals(-1, reader.read(CharBuffer.allocate(10)));
        assertEquals(0, reader.skip(10));
    }

    private static String readFully(CharSequenceReader reader) throws IOException {
        StringBuilder builder = new StringBuilder();
        int read;
        while ((read = reader.read()) != -1) {
            builder.append((char) read);
        }
        return builder.toString();
    }

    public void testMethodsThrowWhenClosed() throws IOException {
        CharSequenceReader reader = new CharSequenceReader("");
        reader.close();
        assertThrows(IOException.class, () -> reader.read());
        assertThrows(IOException.class, () -> reader.read(new char[10]));
        assertThrows(IOException.class, () -> reader.read(new char[10], 0, 10));
        assertThrows(IOException.class, () -> reader.read(CharBuffer.allocate(10)));
        assertThrows(IOException.class, () -> reader.skip(10));
        assertThrows(IOException.class, () -> reader.ready());
        assertThrows(IOException.class, () -> reader.mark(10));
        assertThrows(IOException.class, () -> reader.reset());
    }
}
