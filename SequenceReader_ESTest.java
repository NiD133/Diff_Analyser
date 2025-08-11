package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
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

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class SequenceReader_ESTest extends SequenceReader_ESTest_scaffolding {

    // ===== Constructor Tests =====
    @Test(timeout = 4000)
    public void testConstructorWithNullReadersArray() {
        try {
            new SequenceReader((Reader[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected when readers array is null
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithConcurrentModification() {
        ArrayList<StringReader> readersList = new ArrayList<>();
        readersList.add(new StringReader("Array Size="));
        List<StringReader> subList = readersList.subList(0, 0);
        
        try {
            new SequenceReader(subList);
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            // Expected when underlying collection is modified
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithNullReadersIterable() {
        try {
            new SequenceReader((Iterable<? extends Reader>) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected when readers iterable is null
        }
    }

    // ===== Read Operations Tests =====
    @Test(timeout = 4000)
    public void testReadWithEmptyReaders() throws IOException {
        ArrayDeque<StringReader> emptyDeque = new ArrayDeque<>();
        try (SequenceReader sequenceReader = new SequenceReader(emptyDeque)) {
            char[] buffer = new char[7];
            int readCount = sequenceReader.read(buffer, 1, 0);
            assertEquals("Should return EOF for empty readers", EOF, readCount);
        }
    }

    @Test(timeout = 4000)
    public void testReadAfterReaderClosed() {
        StringReader closedReader = new StringReader("");
        closedReader.close();
        
        Reader[] readers = new Reader[]{closedReader, closedReader};
        try (SequenceReader sequenceReader = new SequenceReader(readers)) {
            char[] buffer = new char[3];
            sequenceReader.read(buffer, 1, 1);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Stream closed"));
        }
    }

    @Test(timeout = 4000)
    public void testConcurrentModificationOnRead() {
        ArrayDeque<StringReader> readersDeque = new ArrayDeque<>();
        StringReader reader = new StringReader("");
        readersDeque.add(reader);
        readersDeque.add(reader);
        
        try (SequenceReader sequenceReader = new SequenceReader(readersDeque)) {
            readersDeque.add(reader); // Modify during iteration
            sequenceReader.read();
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            // Expected when collection modified during read
        } catch (IOException e) {
            fail("Unexpected IOException: " + e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testReadFromUnconnectedPipedReader() {
        Reader[] readers = new Reader[]{new PipedReader()};
        try (SequenceReader sequenceReader = new SequenceReader(readers)) {
            sequenceReader.read();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Pipe not connected"));
        }
    }

    @Test(timeout = 4000)
    public void testReadFromEmptyReader() throws IOException {
        ArrayDeque<StringReader> readersDeque = new ArrayDeque<>();
        readersDeque.add(new StringReader(""));
        
        try (SequenceReader sequenceReader = new SequenceReader(readersDeque)) {
            int result = sequenceReader.read();
            assertEquals("Should return EOF for empty reader", EOF, result);
        }
    }

    @Test(timeout = 4000)
    public void testReadFirstChar() throws IOException {
        ArrayDeque<StringReader> readersDeque = new ArrayDeque<>();
        readersDeque.add(new StringReader("directoryFilter"));
        
        try (SequenceReader sequenceReader = new SequenceReader(readersDeque)) {
            int result = sequenceReader.read();
            assertEquals("Should read first character", 'd', result);
        }
    }

    @Test(timeout = 4000)
    public void testReadSingleChar() throws IOException {
        ArrayDeque<StringReader> readersDeque = new ArrayDeque<>();
        readersDeque.add(new StringReader("directoryFilter"));
        
        try (SequenceReader sequenceReader = new SequenceReader(readersDeque)) {
            char[] buffer = new char[3];
            int readCount = sequenceReader.read(buffer, 1, 1);
            assertEquals("Should read one character", 1, readCount);
            assertArrayEquals("Buffer should contain 'd' at position 1", 
                new char[]{'\u0000', 'd', '\u0000'}, buffer);
        }
    }

    // ===== Read with Buffer Boundary Tests =====
    @Test(timeout = 4000)
    public void testReadWithNullBuffer() {
        ArrayDeque<StringReader> emptyDeque = new ArrayDeque<>();
        try (SequenceReader sequenceReader = new SequenceReader(emptyDeque)) {
            sequenceReader.read(null, -1, -1);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected when buffer is null
        } catch (IOException e) {
            fail("Unexpected IOException: " + e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testReadWithInvalidOffsetAndLength() {
        ArrayDeque<StringReader> emptyDeque = new ArrayDeque<>();
        try (SequenceReader sequenceReader = new SequenceReader(emptyDeque)) {
            char[] buffer = new char[0];
            sequenceReader.read(buffer, 136209934, 136209934);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            assertTrue(e.getMessage().contains("Array Size=0, offset=136209934, length=136209934"));
        } catch (IOException e) {
            fail("Unexpected IOException: " + e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testReadWithNegativeOffset() {
        ArrayDeque<StringReader> emptyDeque = new ArrayDeque<>();
        try (SequenceReader sequenceReader = new SequenceReader(emptyDeque)) {
            char[] buffer = new char[0];
            sequenceReader.read(buffer, -1, 825);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            assertTrue(e.getMessage().contains("Array Size=0, offset=-1, length=825"));
        } catch (IOException e) {
            fail("Unexpected IOException: " + e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testReadWithNegativeOffsetAndLength() {
        ArrayDeque<StringReader> emptyDeque = new ArrayDeque<>();
        try (SequenceReader sequenceReader = new SequenceReader(emptyDeque)) {
            char[] buffer = new char[7];
            sequenceReader.read(buffer, -1, -1);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            assertTrue(e.getMessage().contains("Array Size=7, offset=-1, length=-1"));
        } catch (IOException e) {
            fail("Unexpected IOException: " + e.getMessage());
        }
    }

    // ===== Skip Operation Tests =====
    @Test(timeout = 4000)
    public void testSkipOverMultipleReaders() throws IOException {
        ArrayDeque<StringReader> readersDeque = new ArrayDeque<>();
        readersDeque.add(new StringReader("!DROK>c")); // 7 characters
        readersDeque.add(new StringReader("org.apache.commons.io.filefilter.CanExecuteFileFilter")); // 49 characters
        
        try (SequenceReader sequenceReader = new SequenceReader(readersDeque)) {
            long skipped = sequenceReader.skip(204L);
            assertEquals("Should skip all available characters", 56L, skipped);
        }
    }

    // ===== Close Operation Tests =====
    @Test(timeout = 4000)
    public void testConcurrentModificationOnClose() {
        Vector<StringReader> readersVector = new Vector<>();
        readersVector.add(new StringReader("_cA{/)2>I@4NJ("));
        
        SequenceReader sequenceReader = new SequenceReader(readersVector);
        readersVector.add(new StringReader("additional")); // Modify during close
        
        try {
            sequenceReader.close();
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            // Expected when collection modified during close
        } catch (IOException e) {
            fail("Unexpected IOException: " + e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testClose() throws IOException {
        ArrayDeque<StringReader> readersDeque = new ArrayDeque<>();
        readersDeque.add(new StringReader("T37"));
        readersDeque.add(new StringReader("T37"));
        
        SequenceReader sequenceReader = new SequenceReader(readersDeque);
        sequenceReader.close(); // Should not throw
    }
}