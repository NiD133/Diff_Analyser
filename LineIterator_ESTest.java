package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.NoSuchElementException;
import org.apache.commons.io.LineIterator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class LineIterator_ESTest extends LineIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testNextLineReturnsCorrectString() throws Throwable {
        StringReader reader = new StringReader("_MA");
        LineIterator iterator = new LineIterator(reader);
        String line = iterator.nextLine();
        assertEquals("_MA", line);
    }

    @Test(timeout = 4000)
    public void testNextReturnsCorrectString() throws Throwable {
        StringReader reader = new StringReader("p)");
        LineIterator iterator = new LineIterator(reader);
        String line = iterator.next();
        assertEquals("p)", line);
    }

    @Test(timeout = 4000)
    public void testIsValidLineReturnsTrueForValidLine() throws Throwable {
        StringReader reader = new StringReader("w*N4EtL4abL*9i`");
        LineIterator iterator = new LineIterator(reader);
        boolean isValid = iterator.isValidLine("w*N4EtL4abL*9i`");
        assertTrue(isValid);
    }

    @Test(timeout = 4000)
    public void testNextLineThrowsExceptionWhenStreamClosed() throws Throwable {
        StringReader reader = new StringReader("remove Kot supported");
        reader.close();
        LineIterator iterator = new LineIterator(reader);
        try {
            iterator.nextLine();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.apache.commons.io.LineIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testNextThrowsExceptionWhenNoMoreLines() throws Throwable {
        StringReader reader = new StringReader("p)");
        LineIterator iterator = new LineIterator(reader);
        iterator.hasNext();
        LineIterator anotherIterator = new LineIterator(reader);
        try {
            anotherIterator.next();
            fail("Expecting exception: NoSuchElementException");
        } catch (NoSuchElementException e) {
            verifyException("org.apache.commons.io.LineIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testNextThrowsExceptionWhenStreamClosed() throws Throwable {
        StringReader reader = new StringReader("remove Kot supported");
        reader.close();
        LineIterator iterator = new LineIterator(reader);
        try {
            iterator.next();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.apache.commons.io.LineIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testHasNextThrowsExceptionWhenStreamClosed() throws Throwable {
        StringReader reader = new StringReader("remove not supported");
        reader.close();
        LineIterator iterator = new LineIterator(reader);
        try {
            iterator.hasNext();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.apache.commons.io.LineIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructorThrowsExceptionForNullReader() throws Throwable {
        try {
            new LineIterator((Reader) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testHasNextReturnsFalseForEmptyReader() throws Throwable {
        StringReader reader = new StringReader("");
        BufferedReader bufferedReader = new BufferedReader(reader, 2302);
        LineIterator iterator = new LineIterator(bufferedReader);
        boolean hasNext = iterator.hasNext();
        assertFalse(hasNext);
    }

    @Test(timeout = 4000)
    public void testHasNextReturnsTrueForNonEmptyReader() throws Throwable {
        StringReader reader = new StringReader("w*N4EtL4abL*9i`");
        LineIterator iterator = new LineIterator(reader);
        iterator.hasNext();
        boolean hasNext = iterator.hasNext();
        assertTrue(hasNext);
    }

    @Test(timeout = 4000)
    public void testCloseMethodClosesReader() throws Throwable {
        StringReader reader = new StringReader("remove not supported");
        BufferedReader bufferedReader = new BufferedReader(reader, 2745);
        LineIterator iterator = new LineIterator(bufferedReader);
        iterator.close();
    }

    @Test(timeout = 4000)
    public void testNextLineThrowsExceptionForEmptyReader() throws Throwable {
        StringReader reader = new StringReader("");
        LineIterator iterator = new LineIterator(reader);
        try {
            iterator.nextLine();
            fail("Expecting exception: NoSuchElementException");
        } catch (NoSuchElementException e) {
            verifyException("org.apache.commons.io.LineIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testNextLineReturnsEmptyStringForSingleNewline() throws Throwable {
        StringReader reader = new StringReader("\n");
        BufferedReader bufferedReader = new BufferedReader(reader, 1048);
        LineIterator iterator = new LineIterator(bufferedReader);
        iterator.hasNext();
        String line = iterator.nextLine();
        assertEquals("", line);
    }

    @Test(timeout = 4000)
    public void testNextReturnsEmptyStringForSingleNewline() throws Throwable {
        StringReader reader = new StringReader("\n");
        LineIterator iterator = new LineIterator(reader);
        String line = iterator.next();
        assertEquals("", line);
    }

    @Test(timeout = 4000)
    public void testCloseQuietlyClosesIterator() throws Throwable {
        StringReader reader = new StringReader("w*N4EtL4abL*9i`");
        LineIterator iterator = new LineIterator(reader);
        LineIterator.closeQuietly(iterator);
        boolean hasNext = iterator.hasNext();
        assertFalse(hasNext);
    }

    @Test(timeout = 4000)
    public void testRemoveThrowsUnsupportedOperationException() throws Throwable {
        StringReader reader = new StringReader("");
        LineIterator iterator = new LineIterator(reader);
        try {
            iterator.remove();
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            verifyException("org.apache.commons.io.LineIterator", e);
        }
    }
}