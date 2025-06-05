package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import org.apache.commons.io.LineIterator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true,
    useJEE = true
)
public class LineIterator_ESTest extends LineIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testNextLineReturnsCorrectString() throws Throwable {
        // Test that nextLine() returns the correct string from the reader
        StringReader stringReader = new StringReader(")DuFnfWZ&V3D_i");
        LineIterator lineIterator = new LineIterator(stringReader);
        String result = lineIterator.nextLine();
        assertEquals(")DuFnfWZ&V3D_i", result);
    }

    @Test(timeout = 4000)
    public void testNextReturnsEmptyStringForCarriageReturn() throws Throwable {
        // Test that next() returns an empty string for a carriage return
        StringReader stringReader = new StringReader("\r");
        LineIterator lineIterator = new LineIterator(stringReader);
        String result = lineIterator.next();
        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void testIsValidLineReturnsTrue() throws Throwable {
        // Test that isValidLine() returns true for a given string
        StringReader stringReader = new StringReader("\n");
        BufferedReader bufferedReader = new BufferedReader(stringReader);
        LineIterator lineIterator = new LineIterator(bufferedReader);
        boolean isValid = lineIterator.isValidLine("I?");
        assertTrue(isValid);
    }

    @Test(timeout = 4000)
    public void testNextLineThrowsExceptionWhenReaderClosed() throws Throwable {
        // Test that nextLine() throws IllegalStateException when the reader is closed
        StringReader stringReader = new StringReader("CRLF");
        BufferedReader bufferedReader = new BufferedReader(stringReader);
        bufferedReader.close();
        LineIterator lineIterator = new LineIterator(bufferedReader);
        try {
            lineIterator.nextLine();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.apache.commons.io.LineIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testNextThrowsExceptionWhenReaderClosed() throws Throwable {
        // Test that next() throws IllegalStateException when the reader is closed
        StringReader stringReader = new StringReader("org.apache.commons.io.LineIterator");
        BufferedReader bufferedReader = new BufferedReader(stringReader);
        bufferedReader.close();
        LineIterator lineIterator = new LineIterator(bufferedReader);
        try {
            lineIterator.next();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.apache.commons.io.LineIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testHasNextThrowsExceptionAfterCloseQuietly() throws Throwable {
        // Test that hasNext() throws IllegalStateException after closeQuietly() is called
        StringReader stringReader = new StringReader("DwHpo._Y&0~e=On_{");
        LineIterator lineIterator = new LineIterator(stringReader);
        LineIterator.closeQuietly(lineIterator);
        try {
            lineIterator.hasNext();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.apache.commons.io.LineIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructorThrowsExceptionForNullReader() throws Throwable {
        // Test that the constructor throws NullPointerException for a null reader
        try {
            new LineIterator((Reader) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testNextLineReturnsEmptyStringForNewLine() throws Throwable {
        // Test that nextLine() returns an empty string for a new line
        StringReader stringReader = new StringReader("\n");
        BufferedReader bufferedReader = new BufferedReader(stringReader);
        LineIterator lineIterator = new LineIterator(bufferedReader);
        String result = lineIterator.nextLine();
        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void testForEachRemainingConsumesAllLines() throws Throwable {
        // Test that forEachRemaining() consumes all lines
        StringReader stringReader = new StringReader("[>J6V7G{V9/r,");
        LineIterator lineIterator = new LineIterator(stringReader);
        Consumer<Object> consumer = (Consumer<Object>) mock(Consumer.class, new ViolatedAssumptionAnswer());
        lineIterator.forEachRemaining(consumer);
        boolean hasNext = lineIterator.hasNext();
        assertFalse(hasNext);
    }

    @Test(timeout = 4000)
    public void testForEachRemainingAfterHasNext() throws Throwable {
        // Test that forEachRemaining() works correctly after calling hasNext()
        StringReader stringReader = new StringReader("[>J6V7G{V9/r,");
        LineIterator lineIterator = new LineIterator(stringReader);
        lineIterator.hasNext();
        Consumer<Object> consumer = (Consumer<Object>) mock(Consumer.class, new ViolatedAssumptionAnswer());
        lineIterator.forEachRemaining(consumer);
    }

    @Test(timeout = 4000)
    public void testNextThrowsExceptionAfterClose() throws Throwable {
        // Test that next() throws NoSuchElementException after close() is called
        StringReader stringReader = new StringReader("qA92@1;@bJJXiw\"");
        LineIterator lineIterator = new LineIterator(stringReader);
        lineIterator.close();
        try {
            lineIterator.next();
            fail("Expecting exception: NoSuchElementException");
        } catch (NoSuchElementException e) {
            verifyException("org.apache.commons.io.LineIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testNextLineThrowsExceptionAfterForEachRemaining() throws Throwable {
        // Test that nextLine() throws NoSuchElementException after forEachRemaining() is called
        StringReader stringReader = new StringReader(")!PsaWEtKc");
        LineIterator lineIterator = new LineIterator(stringReader);
        Consumer<Object> consumer = (Consumer<Object>) mock(Consumer.class, new ViolatedAssumptionAnswer());
        lineIterator.forEachRemaining(consumer);
        try {
            lineIterator.nextLine();
            fail("Expecting exception: NoSuchElementException");
        } catch (NoSuchElementException e) {
            verifyException("org.apache.commons.io.LineIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testNextReturnsNonNullString() throws Throwable {
        // Test that next() returns a non-null string
        StringReader stringReader = new StringReader("qA92@1;@bJJXiw\"");
        LineIterator lineIterator = new LineIterator(stringReader);
        String result = lineIterator.next();
        assertNotNull(result);
    }

    @Test(timeout = 4000)
    public void testRemoveThrowsUnsupportedOperationException() throws Throwable {
        // Test that remove() throws UnsupportedOperationException
        StringReader stringReader = new StringReader("!77U'||S(5");
        BufferedReader bufferedReader = new BufferedReader(stringReader);
        LineIterator lineIterator = new LineIterator(bufferedReader);
        try {
            lineIterator.remove();
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            verifyException("org.apache.commons.io.LineIterator", e);
        }
    }
}