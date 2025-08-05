package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.PipedReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Vector;
import org.apache.commons.io.input.SequenceReader;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * Test suite for the SequenceReader class.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class SequenceReader_ESTest extends SequenceReader_ESTest_scaffolding {

    /**
     * Tests skipping characters in a SequenceReader.
     */
    @Test(timeout = 4000)
    public void testSkipCharacters() throws Throwable {
        ArrayDeque<StringReader> readers = new ArrayDeque<>();
        readers.add(new StringReader("!DROK>c"));
        readers.add(new StringReader("org.apache.commons.io.filefilter.CanExecuteFileFilter"));
        SequenceReader sequenceReader = new SequenceReader(readers);
        
        long skippedCharacters = sequenceReader.skip(204L);
        assertEquals(60L, skippedCharacters);
    }

    /**
     * Tests reading from an empty SequenceReader.
     */
    @Test(timeout = 4000)
    public void testReadFromEmptySequenceReader() throws Throwable {
        ArrayDeque<StringReader> readers = new ArrayDeque<>();
        SequenceReader sequenceReader = new SequenceReader(readers);
        
        char[] buffer = new char[7];
        int readCharacters = sequenceReader.read(buffer, 1, 0);
        assertEquals(-1, readCharacters);
    }

    /**
     * Tests reading with a null buffer, expecting a NullPointerException.
     */
    @Test(timeout = 4000)
    public void testReadWithNullBuffer() throws Throwable {
        ArrayDeque<StringReader> readers = new ArrayDeque<>();
        SequenceReader sequenceReader = new SequenceReader(readers);
        
        try {
            sequenceReader.read(null, -1, -1);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    /**
     * Tests reading from a closed SequenceReader, expecting an IOException.
     */
    @Test(timeout = 4000)
    public void testReadFromClosedReader() throws Throwable {
        StringReader emptyReader = new StringReader("");
        Reader[] readerArray = {emptyReader, emptyReader};
        SequenceReader sequenceReader = new SequenceReader(readerArray);
        
        char[] buffer = new char[3];
        try {
            sequenceReader.read(buffer, 1, 1);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.StringReader", e);
        }
    }

    /**
     * Tests concurrent modification of the reader collection, expecting a ConcurrentModificationException.
     */
    @Test(timeout = 4000)
    public void testConcurrentModification() throws Throwable {
        ArrayDeque<StringReader> readers = new ArrayDeque<>();
        StringReader emptyReader = new StringReader("");
        readers.add(emptyReader);
        readers.add(emptyReader);
        SequenceReader sequenceReader = new SequenceReader(readers);
        
        readers.add(emptyReader);
        try {
            sequenceReader.read();
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            verifyException("java.util.ArrayDeque$DeqIterator", e);
        }
    }

    /**
     * Tests reading from a PipedReader that is not connected, expecting an IOException.
     */
    @Test(timeout = 4000)
    public void testReadFromUnconnectedPipedReader() throws Throwable {
        Reader[] readerArray = {new PipedReader()};
        SequenceReader sequenceReader = new SequenceReader(readerArray);
        
        try {
            sequenceReader.read();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedReader", e);
        }
    }

    /**
     * Tests closing a SequenceReader with concurrent modification, expecting a ConcurrentModificationException.
     */
    @Test(timeout = 4000)
    public void testCloseWithConcurrentModification() throws Throwable {
        Vector<StringReader> readers = new Vector<>();
        SequenceReader sequenceReader = new SequenceReader(readers);
        
        readers.add(new StringReader("_cA{/)2>I@4NJ("));
        try {
            sequenceReader.close();
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            verifyException("java.util.Vector$Itr", e);
        }
    }

    /**
     * Tests creating a SequenceReader with null readers, expecting a NullPointerException.
     */
    @Test(timeout = 4000)
    public void testCreateWithNullReaders() throws Throwable {
        try {
            new SequenceReader((Reader[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    /**
     * Tests creating a SequenceReader with a sublist of readers, expecting a ConcurrentModificationException.
     */
    @Test(timeout = 4000)
    public void testCreateWithSubList() throws Throwable {
        ArrayList<StringReader> readers = new ArrayList<>();
        List<StringReader> subList = readers.subList(0, 0);
        readers.add(new StringReader("Array Size="));
        
        try {
            new SequenceReader(subList);
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            verifyException("java.util.ArrayList$SubList", e);
        }
    }

    /**
     * Tests creating a SequenceReader with a null iterable, expecting a NullPointerException.
     */
    @Test(timeout = 4000)
    public void testCreateWithNullIterable() throws Throwable {
        try {
            new SequenceReader((Iterable<? extends Reader>) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    /**
     * Tests reading a single character from a SequenceReader.
     */
    @Test(timeout = 4000)
    public void testReadSingleCharacter() throws Throwable {
        ArrayDeque<StringReader> readers = new ArrayDeque<>();
        readers.add(new StringReader("directoryFilter"));
        SequenceReader sequenceReader = new SequenceReader(readers);
        
        char[] buffer = new char[3];
        int readCharacters = sequenceReader.read(buffer, 1, 1);
        assertArrayEquals(new char[] {'\u0000', 'd', '\u0000'}, buffer);
        assertEquals(1, readCharacters);
    }

    /**
     * Tests reading with an out-of-bounds offset and length, expecting an IndexOutOfBoundsException.
     */
    @Test(timeout = 4000)
    public void testReadWithOutOfBounds() throws Throwable {
        ArrayDeque<StringReader> readers = new ArrayDeque<>();
        SequenceReader sequenceReader = new SequenceReader(readers);
        
        char[] buffer = new char[0];
        try {
            sequenceReader.read(buffer, 136209934, 136209934);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("org.apache.commons.io.input.SequenceReader", e);
        }
    }

    /**
     * Tests reading with a negative offset, expecting an IndexOutOfBoundsException.
     */
    @Test(timeout = 4000)
    public void testReadWithNegativeOffset() throws Throwable {
        ArrayDeque<StringReader> readers = new ArrayDeque<>();
        SequenceReader sequenceReader = new SequenceReader(readers);
        
        char[] buffer = new char[0];
        try {
            sequenceReader.read(buffer, -1, 825);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("org.apache.commons.io.input.SequenceReader", e);
        }
    }

    /**
     * Tests reading with a negative offset and length, expecting an IndexOutOfBoundsException.
     */
    @Test(timeout = 4000)
    public void testReadWithNegativeOffsetAndLength() throws Throwable {
        ArrayDeque<StringReader> readers = new ArrayDeque<>();
        SequenceReader sequenceReader = new SequenceReader(readers);
        
        char[] buffer = new char[7];
        try {
            sequenceReader.read(buffer, -1, -1);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("org.apache.commons.io.input.SequenceReader", e);
        }
    }

    /**
     * Tests reading from an empty StringReader.
     */
    @Test(timeout = 4000)
    public void testReadFromEmptyStringReader() throws Throwable {
        ArrayDeque<StringReader> readers = new ArrayDeque<>();
        readers.add(new StringReader(""));
        SequenceReader sequenceReader = new SequenceReader(readers);
        
        int readCharacter = sequenceReader.read();
        assertEquals(-1, readCharacter);
    }

    /**
     * Tests reading the first character from a StringReader.
     */
    @Test(timeout = 4000)
    public void testReadFirstCharacter() throws Throwable {
        ArrayDeque<StringReader> readers = new ArrayDeque<>();
        readers.add(new StringReader("directoryFilter"));
        SequenceReader sequenceReader = new SequenceReader(readers);
        
        int readCharacter = sequenceReader.read();
        assertEquals('d', readCharacter);
    }

    /**
     * Tests closing a SequenceReader.
     */
    @Test(timeout = 4000)
    public void testCloseSequenceReader() throws Throwable {
        ArrayDeque<StringReader> readers = new ArrayDeque<>();
        StringReader stringReader = new StringReader("T37");
        readers.add(stringReader);
        readers.add(stringReader);
        SequenceReader sequenceReader = new SequenceReader(readers);
        
        sequenceReader.close();
    }
}