package com.google.common.io;

import static org.junit.Assert.*;

import java.io.EOFException;
import java.io.IOException;
import java.io.PipedReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Readable, focused, and maintainable tests for CharStreams.
 *
 * These tests cover:
 * - Basic copy operations and returned counts
 * - Reading to String and to lines
 * - LineProcessor behavior (early stop and full consumption)
 * - Exhausting a Readable
 * - Skipping fully (success and EOF)
 * - nullWriter and asWriter utilities
 * - Error and null-handling behavior
 */
public class CharStreamsTest {

  // copy(Readable, Appendable)

  @Test
  public void copy_readableToAppendable_copiesAllAndReturnsCount() throws IOException {
    StringReader from = new StringReader("hello world");
    StringBuilder to = new StringBuilder();

    long copied = CharStreams.copy(from, to);

    assertEquals(11L, copied);
    assertEquals("hello world", to.toString());
  }

  // copyReaderToBuilder

  @Test
  public void copyReaderToBuilder_copiesAllAndReturnsCount() throws IOException {
    StringReader from = new StringReader("abc123");
    StringBuilder to = new StringBuilder();

    long copied = CharStreams.copyReaderToBuilder(from, to);

    assertEquals(6L, copied);
    assertEquals("abc123", to.toString());
  }

  // copyReaderToWriter

  @Test
  public void copyReaderToWriter_copiesAllAndSecondCopyIsZero() throws IOException {
    StringReader from = new StringReader("Funnels.unencodedCharsFunnel()");
    Writer to = CharStreams.nullWriter();

    long first = CharStreams.copyReaderToWriter(from, to);
    long second = CharStreams.copyReaderToWriter(from, to);

    assertEquals(30L, first);
    assertEquals(0L, second); // reader already consumed
  }

  // toString(Readable)

  @Test
  public void toString_emptyReadable_returnsEmptyString() throws IOException {
    StringReader reader = new StringReader("");

    String s = CharStreams.toString(reader);

    assertEquals("", s);
  }

  @Test
  public void toString_readsAllContent() throws IOException {
    StringReader reader = new StringReader("guava");

    String s = CharStreams.toString(reader);

    assertEquals("guava", s);
  }

  // readLines(Readable)

  @Test
  public void readLines_returnsLinesWithoutTerminators() throws IOException {
    StringReader reader = new StringReader("a\nb\r\nc"); // mix of LF and CRLF

    List<String> lines = CharStreams.readLines(reader);

    assertEquals(3, lines.size());
    assertEquals("a", lines.get(0));
    assertEquals("b", lines.get(1));
    assertEquals("c", lines.get(2));
  }

  @Test
  public void readLines_emptyReadable_returnsEmptyList() throws IOException {
    StringReader reader = new StringReader("");

    List<String> lines = CharStreams.readLines(reader);

    assertTrue(lines.isEmpty());
  }

  // readLines(Readable, LineProcessor)

  @Test
  public void readLines_withProcessor_stopsEarlyAndReturnsResult() throws IOException {
    StringReader reader = new StringReader("first\nsecond\nthird");

    LineProcessor<String> firstLineProcessor = new LineProcessor<String>() {
      String first;
      @Override public boolean processLine(String line) {
        first = line;
        return false; // stop after first line
      }
      @Override public String getResult() {
        return first;
      }
    };

    String result = CharStreams.readLines(reader, firstLineProcessor);

    assertEquals("first", result);
  }

  @Test
  public void readLines_withProcessor_consumesAllAndReturnsAggregation() throws IOException {
    StringReader reader = new StringReader("x\ny\nz");

    LineProcessor<List<String>> collector = new LineProcessor<List<String>>() {
      final List<String> all = new ArrayList<>();
      @Override public boolean processLine(String line) {
        all.add(line);
        return true; // keep going
      }
      @Override public List<String> getResult() {
        return all;
      }
    };

    List<String> result = CharStreams.readLines(reader, collector);

    assertEquals(3, result.size());
    assertEquals("x", result.get(0));
    assertEquals("y", result.get(1));
    assertEquals("z", result.get(2));
  }

  // exhaust(Readable)

  @Test
  public void exhaust_withStringReader_returnsCount() throws IOException {
    StringReader reader = new StringReader("12345");

    long read = CharStreams.exhaust(reader);

    assertEquals(5L, read);
  }

  @Test
  public void exhaust_withCharBuffer_advancesPositionAndReturnsRemaining() throws IOException {
    CharBuffer buf = CharStreams.createBuffer(); // 2048 capacity, position=0

    long read = CharStreams.exhaust(buf);

    assertEquals(2048L, read);
    assertEquals(2048, buf.position());
    assertEquals(buf.limit(), buf.position());
  }

  // skipFully(Reader, long)

  @Test
  public void skipFully_skipsRequestedCharacters() throws IOException {
    StringReader reader = new StringReader("abcdef");

    CharStreams.skipFully(reader, 3L); // skip "abc"
    String remaining = CharStreams.toString(reader);

    assertEquals("def", remaining);
  }

  @Test
  public void skipFully_throwsEOFWhenNotEnoughCharacters() throws IOException {
    StringReader reader = new StringReader("abc");

    try {
      CharStreams.skipFully(reader, 10L);
      fail("Expected EOFException");
    } catch (EOFException expected) {
      // expected
    }
  }

  // nullWriter()

  @Test
  public void nullWriter_isSingletonAndSwallowsWrites() throws IOException {
    Writer w = CharStreams.nullWriter();

    // Should be fluent and no-ops
    assertSame(w, w.append('a'));
    assertSame(w, w.append("bc"));
    w.write("def");
    w.write(new char[] {'x', 'y', 'z'});
    w.flush();
    w.close();

    assertEquals("NullWriter", w.toString());
  }

  // asWriter(Appendable)

  @Test
  public void asWriter_withWriter_returnsSameInstance() {
    Writer base = CharStreams.nullWriter();

    Writer result = CharStreams.asWriter(base);

    assertSame(base, result);
  }

  @Test
  public void asWriter_wrapsAppendableAndForwardsWrites() throws IOException {
    StringBuilder target = new StringBuilder();

    Writer w = CharStreams.asWriter(target);
    w.write("abc");
    w.append('x');
    w.write("YZ", 0, 1); // 'Y'
    w.flush(); // StringBuilder is not Flushable; should be a no-op
    w.close(); // StringBuilder is not Closeable; should be a no-op

    assertEquals("abcxY", target.toString());
  }

  @Test
  public void asWriter_nullTarget_throwsNPE() {
    try {
      CharStreams.asWriter(null);
      fail("Expected NullPointerException");
    } catch (NullPointerException expected) {
      // expected
    }
  }

  // Error propagation

  @Test
  public void toString_propagatesIOExceptionFromReader() {
    PipedReader unconnected = new PipedReader(); // not connected -> read() throws IOException
    try {
      CharStreams.toString(unconnected);
      fail("Expected IOException from unconnected PipedReader");
    } catch (IOException expected) {
      // expected
    }
  }

  // Null handling

  @Test
  public void nullHandling_preconditions() {
    // toString
    try {
      CharStreams.toString((Reader) null);
      fail("Expected NullPointerException");
    } catch (NullPointerException expected) {
      // expected
    }

    // readLines (Readable)
    try {
      CharStreams.readLines((Reader) null);
      fail("Expected NullPointerException");
    } catch (NullPointerException expected) {
      // expected
    }

    // readLines with processor
    try {
      CharStreams.readLines(new StringReader("x"), null);
      fail("Expected NullPointerException");
    } catch (NullPointerException expected) {
      // expected
    }

    // exhaust
    try {
      CharStreams.exhaust((Reader) null);
      fail("Expected NullPointerException");
    } catch (NullPointerException expected) {
      // expected
    }

    // skipFully
    try {
      CharStreams.skipFully(null, 1L);
      fail("Expected NullPointerException");
    } catch (NullPointerException expected) {
      // expected
    }

    // copyReaderToBuilder
    try {
      CharStreams.copyReaderToBuilder(null, new StringBuilder());
      fail("Expected NullPointerException");
    } catch (NullPointerException expected) {
      // expected
    }

    // copyReaderToWriter
    try {
      CharStreams.copyReaderToWriter(null, CharStreams.nullWriter());
      fail("Expected NullPointerException");
    } catch (NullPointerException expected) {
      // expected
    }
  }
}