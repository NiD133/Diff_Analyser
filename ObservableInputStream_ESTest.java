package org.apache.commons.io.input;

import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;

import java.io.*;
import java.nio.CharBuffer;
import java.security.MessageDigest;
import java.util.*;

import org.apache.commons.io.input.MessageDigestInputStream;
import org.apache.commons.io.input.ObservableInputStream;
import org.apache.commons.io.input.TimestampedObserver;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.io.MockFileInputStream;
import org.evosuite.runtime.mock.java.io.MockIOException;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class ObservableInputStream_ESTest extends ObservableInputStream_ESTest_scaffolding {

    // Observer notification tests
    @Test(timeout = 4000)
    public void testNoteErrorWithNoObservers() throws Throwable {
        ObservableInputStream stream = new ObservableInputStream((InputStream) null);
        stream.noteError(new MockIOException());
    }

    @Test(timeout = 4000)
    public void testNoteDataBytesWithNoObservers() throws Throwable {
        ObservableInputStream stream = new ObservableInputStream((InputStream) null);
        stream.noteDataBytes(new byte[0], 10, 10);
    }

    @Test(timeout = 4000)
    public void testNoteDataByteWithNoObservers() throws Throwable {
        ObservableInputStream stream = new ObservableInputStream((InputStream) null);
        stream.noteDataByte(2158);
    }

    @Test(timeout = 4000)
    public void testNoteFinishedWithNoObservers() throws Throwable {
        ObservableInputStream stream = new ObservableInputStream((InputStream) null);
        stream.noteFinished();
    }

    @Test(timeout = 4000)
    public void testNoteClosedWithNoObservers() throws Throwable {
        ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        builder.setCharSequence(new StringBuffer());
        ObservableInputStream stream = new ObservableInputStream(builder);
        stream.noteClosed();
    }

    // Observer management tests
    @Test(timeout = 4000)
    public void testRemoveAllObserversWithNoObservers() throws Throwable {
        ObservableInputStream stream = new ObservableInputStream((InputStream) null);
        stream.removeAllObservers();
    }

    @Test(timeout = 4000)
    public void testRemoveNonExistentObserver() throws Throwable {
        ObservableInputStream stream = new ObservableInputStream((InputStream) null);
        stream.remove(new TimestampedObserver());
    }

    @Test(timeout = 4000)
    public void testAddNullObserver() throws Throwable {
        ObservableInputStream stream = new ObservableInputStream((InputStream) null);
        stream.add(null);
    }

    @Test(timeout = 4000)
    public void testGetObserversWhenNoneExist() throws Throwable {
        ObservableInputStream stream = new ObservableInputStream((InputStream) null);
        assertTrue(stream.getObservers().isEmpty());
    }

    // Read method tests
    @Test(timeout = 4000)
    public void testReadWithZeroLength() throws Throwable {
        ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        builder.setCharSequence(new StringBuffer());
        ObservableInputStream stream = new ObservableInputStream(builder);
        byte[] buffer = new byte[2];
        assertEquals(0, stream.read(buffer, 0, 0));
    }

    @Test(timeout = 4000)
    public void testReadEmptySequenceInputStream() throws Throwable {
        Enumeration<ObjectInputStream> emptyEnum = mock(Enumeration.class);
        when(emptyEnum.hasMoreElements()).thenReturn(false);
        SequenceInputStream seqStream = new SequenceInputStream(emptyEnum);
        ObservableInputStream stream = new ObservableInputStream(seqStream, new ObservableInputStream.Observer[0]);
        byte[] buffer = new byte[6];
        assertEquals(-1, stream.read(buffer, -69, 47));
    }

    @Test(timeout = 4000)
    public void testReadEmptyStream() throws Throwable {
        ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        builder.setCharSequence(new StringBuffer());
        ObservableInputStream stream = new ObservableInputStream(builder);
        byte[] buffer = new byte[2];
        assertEquals(-1, stream.read(buffer));
    }

    @Test(timeout = 4000)
    public void testReadSingleByte() throws Throwable {
        byte[] data = {77};
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ObservableInputStream stream = new ObservableInputStream(input);
        assertEquals(77, stream.read());
    }

    @Test(timeout = 4000)
    public void testReadEmptyByteArray() throws Throwable {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        ObservableInputStream stream = new ObservableInputStream(input);
        assertEquals(-1, stream.read());
    }

    // Error handling tests
    @Test(timeout = 4000)
    public void testObserverManagementWithUnmodifiableList() throws Throwable {
        FileDescriptor fd = new FileDescriptor();
        MockFileInputStream fileInput = new MockFileInputStream(fd);
        PushbackInputStream pushbackInput = new PushbackInputStream(fileInput);
        ObservableInputStream.Observer[] observers = new ObservableInputStream.Observer[4];
        ObservableInputStream stream = new ObservableInputStream(pushbackInput, observers);
        assertThrows(UnsupportedOperationException.class, stream::removeAllObservers);
    }

    @Test(timeout = 4000)
    public void testReadWithInvalidOffset() throws Throwable {
        PipedInputStream pipedInput = new PipedInputStream();
        ObservableInputStream stream = new ObservableInputStream(pipedInput);
        byte[] buffer = new byte[0];
        assertThrows(IndexOutOfBoundsException.class, () -> stream.read(buffer, 1665, 1665));
    }

    @Test(timeout = 4000)
    public void testReadWithNegativeLength() throws Throwable {
        ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        builder.setCharSequence(new StringBuffer());
        ObservableInputStream stream = builder.get();
        byte[] buffer = new byte[0];
        assertThrows(IndexOutOfBoundsException.class, () -> stream.read(buffer, 0, -1));
    }

    @Test(timeout = 4000)
    public void testReadWithInvalidBuffer() throws Throwable {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[8], -1, 6);
        ObservableInputStream stream = new ObservableInputStream(input);
        byte[] buffer = new byte[8];
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> stream.read(buffer, 1, 1));
    }

    @Test(timeout = 4000)
    public void testReadOnClosedStream() throws Throwable {
        PushbackInputStream pushbackInput = new PushbackInputStream(null);
        ObservableInputStream stream = new ObservableInputStream(pushbackInput, new ObservableInputStream.Observer[0]);
        byte[] buffer = new byte[2];
        assertThrows(IOException.class, () -> stream.read(buffer, 3, -2701));
    }

    @Test(timeout = 4000)
    public void testReadOnNullStream() throws Throwable {
        BufferedInputStream bufferedInput = new BufferedInputStream(null);
        ObservableInputStream stream = new ObservableInputStream(bufferedInput);
        byte[] buffer = new byte[1];
        assertThrows(IOException.class, () -> stream.read(buffer));
    }

    @Test(timeout = 4000)
    public void testReadOnUnconnectedPipedStream() throws Throwable {
        PipedInputStream pipedInput = new PipedInputStream(957);
        TimestampedObserver observer = new TimestampedObserver();
        ObservableInputStream stream = new ObservableInputStream(pipedInput, new ObservableInputStream.Observer[]{observer});
        assertThrows(IOException.class, stream::read);
    }

    @Test(timeout = 4000)
    public void testReadWithInvalidByteArray() throws Throwable {
        byte[] data = new byte[1];
        ByteArrayInputStream input = new ByteArrayInputStream(data, -792, 73);
        ObservableInputStream stream = new ObservableInputStream(input);
        assertThrows(ArrayIndexOutOfBoundsException.class, stream::read);
    }

    @Test(timeout = 4000)
    public void testReadOnUnconnectedPipedStreamSingleObserver() throws Throwable {
        PipedInputStream pipedInput = new PipedInputStream();
        ObservableInputStream stream = new ObservableInputStream(pipedInput);
        assertThrows(IOException.class, stream::read);
    }

    // Observer notification error tests
    @Test(timeout = 4000)
    public void testNoteErrorWithObserverThrowingException() throws Throwable {
        Enumeration<ObjectInputStream> emptyEnum = mock(Enumeration.class);
        when(emptyEnum.hasMoreElements()).thenReturn(false);
        SequenceInputStream seqStream = new SequenceInputStream(emptyEnum);
        TimestampedObserver observer = new TimestampedObserver();
        ObservableInputStream stream = new ObservableInputStream(seqStream, new ObservableInputStream.Observer[]{observer});
        assertThrows(IOException.class, () -> stream.noteError(new MockIOException("")));
    }

    @Test(timeout = 4000)
    public void testNoteDataBytesWithNullBuffer() throws Throwable {
        PipedInputStream pipedInput = new PipedInputStream();
        ObservableInputStream stream = new ObservableInputStream(pipedInput, new ObservableInputStream.Observer[1]);
        assertThrows(NullPointerException.class, () -> stream.noteDataBytes(null, 3677, -1));
    }

    @Test(timeout = 4000)
    public void testNoteDataByteWithNullObservers() throws Throwable {
        PipedInputStream pipedInput = new PipedInputStream(1136);
        ObservableInputStream stream = new ObservableInputStream(pipedInput, new ObservableInputStream.Observer[3]);
        assertThrows(NullPointerException.class, () -> stream.noteDataByte(1136));
    }

    @Test(timeout = 4000)
    public void testGetObserversWithNullList() throws Throwable {
        ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        builder.setCharSequence(new StringBuffer());
        ObservableInputStream stream = builder.get();
        assertThrows(NullPointerException.class, stream::getObservers);
    }

    // Stream consumption tests
    @Test(timeout = 4000)
    public void testConsumeNullStream() throws Throwable {
        ObservableInputStream stream = new ObservableInputStream((InputStream) null);
        assertThrows(NullPointerException.class, stream::consume);
    }

    @Test(timeout = 4000)
    public void testConsumeWithInvalidArray() throws Throwable {
        byte[] data = new byte[10];
        ByteArrayInputStream input = new ByteArrayInputStream(data, -1732, 1);
        ObservableInputStream stream = new ObservableInputStream(input);
        assertThrows(ArrayIndexOutOfBoundsException.class, stream::consume);
    }

    // Builder tests
    @Test(timeout = 4000)
    public void testBuilderWithNonexistentPath() throws Throwable {
        ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        builder.setPath("PZkNv|C6'u*&le;,");
        assertThrows(NoSuchFileException.class, () -> new ObservableInputStream(builder));
    }

    @Test(timeout = 4000)
    public void testBuilderWithUnsupportedOrigin() throws Throwable {
        ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        builder.setWriter(new StringWriter());
        assertThrows(UnsupportedOperationException.class, () -> new ObservableInputStream(builder));
    }

    @Test(timeout = 4000)
    public void testBuilderWithNullParameter() throws Throwable {
        assertThrows(NullPointerException.class, () -> new ObservableInputStream((ObservableInputStream.AbstractBuilder<?>) null));
    }

    @Test(timeout = 4000)
    public void testBuilderWithoutOrigin() throws Throwable {
        ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        assertThrows(IllegalStateException.class, () -> new ObservableInputStream(builder));
    }

    @Test(timeout = 4000)
    public void testConstructorWithNullObservers() throws Throwable {
        ObservableInputStream baseStream = new ObservableInputStream((InputStream) null);
        assertThrows(NullPointerException.class, () -> new ObservableInputStream(baseStream, (ObservableInputStream.Observer[]) null));
    }

    // Observer behavior tests
    @Test(timeout = 4000)
    public void testObserverErrorHandling() throws Throwable {
        TimestampedObserver observer = new TimestampedObserver();
        assertThrows(IOException.class, () -> observer.error(new MockIOException("org.apache.commons.io.input.ObservableInputStream$Builder")));
    }

    @Test(timeout = 4000)
    public void testReadWithNullObservers() throws Throwable {
        PipedInputStream pipedInput = new PipedInputStream(957);
        ObservableInputStream stream = new ObservableInputStream(pipedInput, new ObservableInputStream.Observer[1]);
        assertThrows(NullPointerException.class, stream::read);
    }

    @Test(timeout = 4000)
    public void testReadEmptyBuffer() throws Throwable {
        BufferedInputStream bufferedInput = new BufferedInputStream(null);
        ObservableInputStream stream = new ObservableInputStream(bufferedInput);
        assertEquals(0, stream.read(new byte[0]));
    }

    @Test(timeout = 4000)
    public void testConsumeCharSequence() throws Throwable {
        ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        builder.setCharSequence(CharBuffer.allocate(255));
        ObservableInputStream stream = new ObservableInputStream(builder);
        stream.consume();
    }

    @Test(timeout = 4000)
    public void testCloseDataInputStream() throws Throwable {
        DataInputStream dataInput = new DataInputStream(null);
        ObservableInputStream stream = new ObservableInputStream(dataInput);
        stream.close();
    }

    @Test(timeout = 4000)
    public void testObserverFinished() throws Throwable {
        TimestampedObserver observer = new TimestampedObserver();
        observer.finished();
        assertFalse(observer.isClosed());
    }

    @Test(timeout = 4000)
    public void testObserverDataInt() throws Throwable {
        TimestampedObserver observer = new TimestampedObserver();
        observer.data(1225);
        assertFalse(observer.isClosed());
    }

    @Test(timeout = 4000)
    public void testMessageDigestObserverClosed() throws Throwable {
        MessageDigest md = MessageDigest.getInstance("MD5");
        MessageDigestInputStream.MessageDigestMaintainingObserver observer = 
            new MessageDigestInputStream.MessageDigestMaintainingObserver(md);
        observer.closed();
    }

    @Test(timeout = 4000)
    public void testObserverDataByteArray() throws Throwable {
        TimestampedObserver observer = new TimestampedObserver();
        observer.data(null, -1, -1);
        assertFalse(observer.isClosed());
    }

    @Test(timeout = 4000)
    public void testReadOnUnconnectedPipedStreamWithObserver() throws Throwable {
        PipedInputStream pipedInput = new PipedInputStream(3);
        TimestampedObserver observer = new TimestampedObserver();
        ObservableInputStream stream = new ObservableInputStream(pipedInput, new ObservableInputStream.Observer[]{observer});
        assertThrows(IOException.class, () -> stream.read(new byte[1]));
    }

    @Test(timeout = 4000)
    public void testBuilderWithObserverList() throws Throwable {
        ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        builder.setObservers(new LinkedList<>());
        assertEquals(0, builder.observers.size());
    }

    @Test(timeout = 4000)
    public void testReadWithWrappedStreams() throws Throwable {
        PipedInputStream pipedInput = new PipedInputStream(3);
        ObservableInputStream.Observer[] observers = new ObservableInputStream.Observer[1];
        ObservableInputStream innerStream = new ObservableInputStream(pipedInput, observers);
        PushbackInputStream pushbackStream = new PushbackInputStream(innerStream);
        BufferedInputStream bufferedStream = new BufferedInputStream(pushbackStream);
        ObservableInputStream outerStream = new ObservableInputStream(bufferedStream);
        assertThrows(NullPointerException.class, () -> outerStream.read(new byte[1]));
    }

    @Test(timeout = 4000)
    public void testReadCharSequence() throws Throwable {
        ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        builder.setCharSequence(CharBuffer.allocate(1332));
        ObservableInputStream stream = builder.get();
        byte[] buffer = new byte[5];
        assertEquals(5, stream.read(buffer));
    }

    @Test(timeout = 4000)
    public void testAddNullObserverToBuilderInstance() throws Throwable {
        ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        builder.setCharSequence(new StringBuffer());
        ObservableInputStream stream = builder.get();
        assertThrows(NullPointerException.class, () -> stream.add(null));
    }

    @Test(timeout = 4000)
    public void testCloseCharSequenceStream() throws Throwable {
        ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        builder.setCharSequence(CharBuffer.allocate(255));
        ObservableInputStream stream = new ObservableInputStream(builder);
        stream.close();
    }

    @Test(timeout = 4000)
    public void testGetObserversWhenExist() throws Throwable {
        Enumeration<ObjectInputStream> emptyEnum = mock(Enumeration.class);
        when(emptyEnum.hasMoreElements()).thenReturn(false);
        SequenceInputStream seqStream = new SequenceInputStream(emptyEnum);
        ObservableInputStream stream = new ObservableInputStream(seqStream, new ObservableInputStream.Observer[1]);
        assertEquals(1, stream.getObservers().size());
    }

    @Test(timeout = 4000)
    public void testRemoveObserverFromBuilderInstance() throws Throwable {
        ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        builder.setCharSequence(new StringBuffer());
        ObservableInputStream stream = builder.get();
        assertThrows(NullPointerException.class, () -> stream.remove(new TimestampedObserver()));
    }

    @Test(timeout = 4000)
    public void testRemoveAllObserversFromBuilderInstance() throws Throwable {
        ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        builder.setCharSequence(new StringBuffer());
        ObservableInputStream stream = builder.get();
        assertThrows(NullPointerException.class, stream::removeAllObservers);
    }

    @Test(timeout = 4000)
    public void testConsumeOnClosedStream() throws Throwable {
        BufferedInputStream bufferedInput = new BufferedInputStream(null);
        ObservableInputStream stream = new ObservableInputStream(bufferedInput);
        assertThrows(IOException.class, stream::consume);
    }

    @Test(timeout = 4000)
    public void testReadSingleByteFromCharSequence() throws Throwable {
        ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        builder.setCharSequence(CharBuffer.allocate(1332));
        ObservableInputStream stream = builder.get();
        assertEquals(0, stream.read());
    }
}