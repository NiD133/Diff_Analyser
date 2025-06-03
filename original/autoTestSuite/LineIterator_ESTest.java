package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;


@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true, useJEE = true)
public class LineIterator_ESTest {

    @Test(timeout = 4000)
    public void test00_nextLine_returnsLine() throws IOException {
        // Given: A StringReader with a single line of text.
        StringReader stringReader = new StringReader(")DuFnfWZ&V3D_i");
        LineIterator lineIterator = new LineIterator(stringReader);

        // When: nextLine() is called.
        String line = lineIterator.nextLine();

        // Then: The line of text should be returned.
        assertEquals(")DuFnfWZ&V3D_i", line);
    }

    @Test(timeout = 4000)
    public void test01_next_returnsEmptyStringForCR() throws IOException {
        // Given: A StringReader containing only a carriage return character.
        StringReader stringReader = new StringReader("\r");
        LineIterator lineIterator = new LineIterator(stringReader);

        // When: next() is called.
        String line = lineIterator.next();

        // Then: An empty string should be returned (because the carriage return is treated as a line separator).
        assertEquals("", line);
    }

    @Test(timeout = 4000)
    public void test02_isValidLine_returnsTrue() throws IOException {
        // Given: A StringReader and a LineIterator.
        StringReader stringReader = new StringReader("\n");
        BufferedReader bufferedReader = new BufferedReader(stringReader);
        LineIterator lineIterator = new LineIterator(bufferedReader);

        // When: isValidLine() is called with a non-null string.
        boolean isValid = lineIterator.isValidLine("I?");

        // Then: The result should be true (as any non-null string is considered valid).
        assertTrue(isValid);
    }

    @Test(timeout = 4000)
    public void test03_nextLine_throwsIllegalStateExceptionWhenReaderIsClosed() throws IOException {
        // Given: A BufferedReader that has been closed.
        StringReader stringReader = new StringReader("CRLF");
        BufferedReader bufferedReader = new BufferedReader(stringReader);
        bufferedReader.close();
        LineIterator lineIterator = new LineIterator(bufferedReader);

        // Then: Calling nextLine() should throw an IllegalStateException.
        assertThrows(IllegalStateException.class, () -> {
            lineIterator.nextLine();
        });
    }

    @Test(timeout = 4000)
    public void test04_next_throwsIllegalStateExceptionWhenReaderIsClosed() throws IOException {
        // Given: A BufferedReader that has been closed.
        StringReader stringReader = new StringReader("org.apache.commons.io.LineIterator");
        BufferedReader bufferedReader = new BufferedReader(stringReader);
        bufferedReader.close();
        LineIterator lineIterator = new LineIterator(bufferedReader);

        // Then: Calling next() should throw an IllegalStateException.
        assertThrows(IllegalStateException.class, () -> {
            lineIterator.next();
        });
    }

    @Test(timeout = 4000)
    public void test05_hasNext_throwsIllegalStateExceptionWhenReaderIsClosed() throws IOException {
        StringReader stringReader0 = new StringReader("DwHpo._Y&0~e=On_{");
        LineIterator lineIterator0 = new LineIterator(stringReader0);
        LineIterator lineIterator1 = new LineIterator(stringReader0);
        LineIterator.closeQuietly(lineIterator1);
        assertThrows(IllegalStateException.class, () -> {
            lineIterator0.hasNext();
        });
    }

    @Test(timeout = 4000)
    public void test06_constructor_throwsNullPointerExceptionWhenReaderIsNull() throws IOException {
        assertThrows(NullPointerException.class, () -> {
           new LineIterator((Reader) null);
        });
    }

    @Test(timeout = 4000)
    public void test07_nextLine_returnsEmptyStringForNewline() throws IOException {
        // Given: A StringReader containing only a newline character.
        StringReader stringReader = new StringReader("\n");
        BufferedReader bufferedReader = new BufferedReader(stringReader);
        LineIterator lineIterator = new LineIterator(bufferedReader);

        // When: nextLine() is called.
        String line = lineIterator.nextLine();

        // Then: An empty string should be returned (because the newline is treated as a line separator).
        assertEquals("", line);
    }

    @Test(timeout = 4000)
    public void test08_hasNext_returnsFalseAfterReadingAllLines() throws IOException {
        StringReader stringReader = new StringReader("[>J6V7G{V9/r,");
        LineIterator lineIterator = new LineIterator(stringReader);
        Consumer<Object> consumer0 = (Consumer<Object>) mock(Consumer.class, new ViolatedAssumptionAnswer());
        lineIterator.forEachRemaining(consumer0);
        boolean boolean0 = lineIterator.hasNext();
        assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test09_forEachRemaining_calledAfterHasNext_isCalledCorrectly() throws IOException {
        StringReader stringReader = new StringReader("[>J6V7G{V9/r,");
        LineIterator lineIterator = new LineIterator(stringReader);
        lineIterator.hasNext();
        Consumer<Object> consumer0 = (Consumer<Object>) mock(Consumer.class, new ViolatedAssumptionAnswer());
        lineIterator.forEachRemaining(consumer0);
    }

    @Test(timeout = 4000)
    public void test10_next_throwsNoSuchElementExceptionWhenNoMoreLines() throws IOException {
        // Given: A LineIterator that has reached the end of the reader and the reader is closed.
        StringReader stringReader = new StringReader("qA92@1;@bJJXiw\"");
        LineIterator lineIterator = new LineIterator(stringReader);
        lineIterator.close();

        // Then: Calling next() should throw a NoSuchElementException.
        assertThrows(NoSuchElementException.class, () -> {
            lineIterator.next();
        });
    }

    @Test(timeout = 4000)
    public void test11_nextLine_throwsNoSuchElementExceptionWhenNoMoreLines() throws IOException {
        // Given: A LineIterator that has reached the end of the reader.
        StringReader stringReader = new StringReader(")!PsaWEtKc");
        LineIterator lineIterator = new LineIterator(stringReader);
        Consumer<Object> consumer0 = (Consumer<Object>) mock(Consumer.class, new ViolatedAssumptionAnswer());
        lineIterator.forEachRemaining(consumer0);

        // Then: Calling nextLine() should throw a NoSuchElementException.
        assertThrows(NoSuchElementException.class, () -> {
            lineIterator.nextLine();
        });
    }

    @Test(timeout = 4000)
    public void test12_next_returnsNextLine() throws IOException {
        // Given: A StringReader with a line of text.
        StringReader stringReader = new StringReader("qA92@1;@bJJXiw\"");
        LineIterator lineIterator = new LineIterator(stringReader);

        // When: next() is called.
        String line = lineIterator.next();

        // Then: The line should be returned.
        assertNotNull(line);
    }

    @Test(timeout = 4000)
    public void test13_remove_throwsUnsupportedOperationException() throws IOException {
        // Given: A LineIterator.
        StringReader stringReader = new StringReader("!77U'||S(5");
        BufferedReader bufferedReader = new BufferedReader(stringReader);
        LineIterator lineIterator = new LineIterator(bufferedReader);

        // Then: Calling remove() should throw an UnsupportedOperationException.
        assertThrows(UnsupportedOperationException.class, () -> {
            lineIterator.remove();
        });
    }
}