package com.google.common.io;

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.io.ByteSource;
import com.google.common.io.MultiInputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.ListIterator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class MultiInputStream_ESTest extends MultiInputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testSkipZeroBytes() throws Throwable {
        ByteSource emptySource = ByteSource.empty();
        ArrayDeque<ByteSource> sources = new ArrayDeque<>();
        sources.add(emptySource);
        MultiInputStream multiInputStream = new MultiInputStream(sources.iterator());
        long skippedBytes = multiInputStream.skip(0L);
        assertEquals(0L, skippedBytes);
    }

    @Test(timeout = 4000)
    public void testReadFromEmptyStream() throws Throwable {
        LinkedList<ByteSource> sources = new LinkedList<>();
        MultiInputStream multiInputStream = new MultiInputStream(sources.descendingIterator());
        byte[] buffer = new byte[7];
        int bytesRead = multiInputStream.read(buffer, 0, 387);
        assertEquals(-1, bytesRead);
    }

    @Test(timeout = 4000)
    public void testReadSingleByte() throws Throwable {
        byte[] data = new byte[]{(byte) -98};
        ByteSource wrappedSource = ByteSource.wrap(data);
        ArrayDeque<ByteSource> sources = new ArrayDeque<>();
        sources.add(wrappedSource);
        MultiInputStream multiInputStream = new MultiInputStream(sources.iterator());
        int byteRead = multiInputStream.read();
        assertEquals(158, byteRead);
    }

    @Test(timeout = 4000)
    public void testConcurrentModificationExceptionOnSkip() throws Throwable {
        ByteSource emptySource = ByteSource.empty();
        ArrayDeque<ByteSource> sources = new ArrayDeque<>();
        sources.add(emptySource);
        sources.add(emptySource);
        Iterator<ByteSource> iterator = sources.iterator();
        MultiInputStream multiInputStream = new MultiInputStream(iterator);
        sources.add(emptySource); // Modify after iterator creation

        try {
            multiInputStream.skip(4134L);
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            verifyException("java.util.ArrayDeque$DeqIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullPointerExceptionOnNullBufferRead() throws Throwable {
        ArrayDeque<ByteSource> sources = new ArrayDeque<>();
        MultiInputStream multiInputStream = new MultiInputStream(sources.iterator());

        try {
            multiInputStream.read(null, -332, -332);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testIndexOutOfBoundsExceptionOnRead() throws Throwable {
        ByteSource emptySource = ByteSource.empty();
        LinkedList<ByteSource> sources = new LinkedList<>();
        sources.add(emptySource);
        MultiInputStream multiInputStream = new MultiInputStream(sources.descendingIterator());
        byte[] buffer = new byte[0];

        try {
            multiInputStream.read(buffer, 408, 408);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.io.ByteArrayInputStream", e);
        }
    }

    @Test(timeout = 4000)
    public void testConcurrentModificationExceptionOnRead() throws Throwable {
        ArrayList<ByteSource> sources = new ArrayList<>();
        ByteSource emptySource = ByteSource.empty();
        sources.add(emptySource);
        ListIterator<ByteSource> iterator = sources.listIterator();
        MultiInputStream multiInputStream = new MultiInputStream(iterator);
        LinkedHashSet<ByteSource> additionalSources = new LinkedHashSet<>();
        additionalSources.add(emptySource);
        sources.addAll(additionalSources); // Modify after iterator creation

        try {
            multiInputStream.read();
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            verifyException("java.util.ArrayList$Itr", e);
        }
    }

    @Test(timeout = 4000)
    public void testConcurrentModificationExceptionOnInitialization() throws Throwable {
        ByteSource emptySource = ByteSource.empty();
        ArrayDeque<ByteSource> sources = new ArrayDeque<>();
        sources.add(emptySource);
        Iterator<ByteSource> iterator = sources.iterator();
        sources.add(emptySource); // Modify after iterator creation

        try {
            new MultiInputStream(iterator);
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            verifyException("java.util.ArrayDeque$DeqIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullPointerExceptionOnNullSource() throws Throwable {
        ArrayList<ByteSource> sources = new ArrayList<>();
        sources.add(null);
        Iterator<ByteSource> iterator = sources.iterator();

        try {
            new MultiInputStream(iterator);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.io.MultiInputStream", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullPointerExceptionOnNullIterator() throws Throwable {
        try {
            new MultiInputStream(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadEmptyByteSource() throws Throwable {
        ByteSource emptySource = ByteSource.empty();
        ArrayDeque<ByteSource> sources = new ArrayDeque<>();
        sources.add(emptySource);
        ByteSource concatenatedSource = ByteSource.concat(sources.iterator());
        byte[] data = concatenatedSource.read();
        assertEquals(0, data.length);
    }

    @Test(timeout = 4000)
    public void testReadFromEmptyMultiInputStream() throws Throwable {
        ArrayDeque<ByteSource> sources = new ArrayDeque<>();
        MultiInputStream multiInputStream = new MultiInputStream(sources.iterator());
        int byteRead = multiInputStream.read();
        assertEquals(-1, byteRead);
    }

    @Test(timeout = 4000)
    public void testCloseMultiInputStream() throws Throwable {
        ByteSource emptySource = ByteSource.empty();
        ArrayDeque<ByteSource> sources = new ArrayDeque<>();
        sources.add(emptySource);
        MultiInputStream multiInputStream = new MultiInputStream(sources.iterator());
        multiInputStream.close();
        assertFalse(multiInputStream.markSupported());
    }

    @Test(timeout = 4000)
    public void testSkipBytesInConcatenatedSource() throws Throwable {
        ArrayDeque<ByteSource> sources = new ArrayDeque<>();
        ByteSource[] byteSources = new ByteSource[3];
        ByteSource emptySource = ByteSource.empty();
        byteSources[0] = emptySource;
        byteSources[1] = ByteSource.wrap(new byte[6]);
        byteSources[2] = emptySource;
        ByteSource concatenatedSource = ByteSource.concat(byteSources);
        sources.add(concatenatedSource);
        MultiInputStream multiInputStream = new MultiInputStream(sources.iterator());
        long skippedBytes = multiInputStream.skip(60);
        assertEquals(6L, skippedBytes);
    }

    @Test(timeout = 4000)
    public void testSkipNegativeBytes() throws Throwable {
        LinkedList<ByteSource> sources = new LinkedList<>();
        ByteSource wrappedSource = ByteSource.wrap(new byte[8]);
        sources.add(wrappedSource);
        MultiInputStream multiInputStream = new MultiInputStream(sources.descendingIterator());
        long skippedBytes = multiInputStream.skip(-896L);
        assertEquals(0L, skippedBytes);
    }

    @Test(timeout = 4000)
    public void testSkipNegativeBytesInEmptyStream() throws Throwable {
        ArrayList<ByteSource> sources = new ArrayList<>();
        MultiInputStream multiInputStream = new MultiInputStream(sources.iterator());
        long skippedBytes = multiInputStream.skip(-1293L);
        assertEquals(0L, skippedBytes);
    }

    @Test(timeout = 4000)
    public void testContentEqualsOnConcatenatedSource() throws Throwable {
        ArrayDeque<ByteSource> sources = new ArrayDeque<>();
        ByteSource wrappedSource = ByteSource.wrap(new byte[3]);
        sources.add(wrappedSource);
        ByteSource concatenatedSource = ByteSource.concat(sources.iterator());
        assertTrue(concatenatedSource.contentEquals(concatenatedSource));
    }

    @Test(timeout = 4000)
    public void testReadSingleByteFromWrappedSource() throws Throwable {
        LinkedList<ByteSource> sources = new LinkedList<>();
        ByteSource wrappedSource = ByteSource.wrap(new byte[8]);
        sources.add(wrappedSource);
        MultiInputStream multiInputStream = new MultiInputStream(sources.descendingIterator());
        int byteRead = multiInputStream.read();
        assertEquals(0, byteRead);
    }

    @Test(timeout = 4000)
    public void testAvailableBytesInWrappedSource() throws Throwable {
        LinkedList<ByteSource> sources = new LinkedList<>();
        ByteSource wrappedSource = ByteSource.wrap(new byte[8]);
        sources.add(wrappedSource);
        MultiInputStream multiInputStream = new MultiInputStream(sources.descendingIterator());
        int availableBytes = multiInputStream.available();
        assertEquals(8, availableBytes);
    }

    @Test(timeout = 4000)
    public void testSkipBytesInEmptySource() throws Throwable {
        ByteSource emptySource = ByteSource.empty();
        ArrayDeque<ByteSource> sources = new ArrayDeque<>();
        sources.add(emptySource);
        MultiInputStream multiInputStream = new MultiInputStream(sources.iterator());
        long skippedBytes = multiInputStream.skip(2173L);
        assertEquals(0L, skippedBytes);
    }

    @Test(timeout = 4000)
    public void testMarkNotSupported() throws Throwable {
        LinkedList<ByteSource> sources = new LinkedList<>();
        MultiInputStream multiInputStream = new MultiInputStream(sources.descendingIterator());
        assertFalse(multiInputStream.markSupported());
    }

    @Test(timeout = 4000)
    public void testAvailableBytesInEmptySource() throws Throwable {
        ArrayDeque<ByteSource> sources = new ArrayDeque<>();
        MultiInputStream multiInputStream = new MultiInputStream(sources.iterator());
        int availableBytes = multiInputStream.available();
        assertEquals(0, availableBytes);
    }
}