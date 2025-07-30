package org.apache.commons.io.input;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.file.NoSuchFileException;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.input.MessageDigestInputStream;
import org.apache.commons.io.input.ObservableInputStream;
import org.apache.commons.io.input.TimestampedObserver;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.io.MockFileInputStream;
import org.evosuite.runtime.mock.java.io.MockIOException;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class ObservableInputStream_ESTest extends ObservableInputStream_ESTest_scaffolding {

    private static final int TIMEOUT = 4000;

    @Test(timeout = TIMEOUT)
    public void testNoteErrorWithMockIOException() throws Throwable {
        ObservableInputStream observableInputStream = new ObservableInputStream((InputStream) null);
        MockIOException mockIOException = new MockIOException();
        observableInputStream.noteError(mockIOException);
    }

    @Test(timeout = TIMEOUT)
    public void testNoteDataBytesWithEmptyArray() throws Throwable {
        ObservableInputStream observableInputStream = new ObservableInputStream((InputStream) null);
        byte[] byteArray = new byte[0];
        observableInputStream.noteDataBytes(byteArray, 10, 10);
        assertEquals(0, byteArray.length);
    }

    @Test(timeout = TIMEOUT)
    public void testNoteDataByte() throws Throwable {
        ObservableInputStream observableInputStream = new ObservableInputStream((InputStream) null);
        observableInputStream.noteDataByte(2158);
    }

    @Test(timeout = TIMEOUT)
    public void testNoteFinished() throws Throwable {
        ObservableInputStream observableInputStream = new ObservableInputStream((InputStream) null);
        observableInputStream.noteFinished();
    }

    @Test(timeout = TIMEOUT)
    public void testNoteClosedWithBuilder() throws Throwable {
        ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        StringWriter stringWriter = new StringWriter();
        StringBuffer stringBuffer = stringWriter.getBuffer();
        builder.setCharSequence(stringBuffer);
        ObservableInputStream observableInputStream = new ObservableInputStream(builder);
        observableInputStream.noteClosed();
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveAllObservers() throws Throwable {
        ObservableInputStream observableInputStream = new ObservableInputStream((InputStream) null);
        observableInputStream.removeAllObservers();
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveObserver() throws Throwable {
        ObservableInputStream observableInputStream = new ObservableInputStream((InputStream) null);
        TimestampedObserver observer = new TimestampedObserver();
        observableInputStream.remove(observer);
        assertFalse(observer.isClosed());
    }

    @Test(timeout = TIMEOUT)
    public void testAddNullObserver() throws Throwable {
        ObservableInputStream observableInputStream = new ObservableInputStream((InputStream) null);
        observableInputStream.add(null);
    }

    @Test(timeout = TIMEOUT)
    public void testReadWithEmptyBuffer() throws Throwable {
        StringWriter stringWriter = new StringWriter();
        StringBuffer stringBuffer = stringWriter.getBuffer();
        ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        builder.setCharSequence(stringBuffer);
        ObservableInputStream observableInputStream = new ObservableInputStream(builder);
        byte[] byteArray = new byte[2];
        int bytesRead = observableInputStream.read(byteArray, 0, 0);
        assertEquals(0, bytesRead);
    }

    @Test(timeout = TIMEOUT)
    public void testReadFromSequenceInputStream() throws Throwable {
        Enumeration<ObjectInputStream> enumeration = mock(Enumeration.class, new ViolatedAssumptionAnswer());
        doReturn(false).when(enumeration).hasMoreElements();
        SequenceInputStream sequenceInputStream = new SequenceInputStream(enumeration);
        ObservableInputStream.Observer[] observers = new ObservableInputStream.Observer[0];
        ObservableInputStream observableInputStream = new ObservableInputStream(sequenceInputStream, observers);
        byte[] byteArray = new byte[6];
        int bytesRead = observableInputStream.read(byteArray, -69, 47);
        assertEquals(-1, bytesRead);
    }

    @Test(timeout = TIMEOUT)
    public void testReadFromByteArrayInputStream() throws Throwable {
        byte[] byteArray = new byte[1];
        byteArray[0] = (byte) 77;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        ObservableInputStream observableInputStream = new ObservableInputStream(byteArrayInputStream);
        int byteRead = observableInputStream.read();
        assertEquals(77, byteRead);
    }

    @Test(timeout = TIMEOUT)
    public void testGetObservers() throws Throwable {
        ObservableInputStream observableInputStream = new ObservableInputStream((InputStream) null);
        List<ObservableInputStream.Observer> observers = observableInputStream.getObservers();
        assertTrue(observers.isEmpty());
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveAllObserversThrowsUnsupportedOperationException() throws Throwable {
        FileDescriptor fileDescriptor = new FileDescriptor();
        MockFileInputStream mockFileInputStream = new MockFileInputStream(fileDescriptor);
        PushbackInputStream pushbackInputStream = new PushbackInputStream(mockFileInputStream);
        ObservableInputStream.Observer[] observers = new ObservableInputStream.Observer[4];
        ObservableInputStream observableInputStream = new ObservableInputStream(pushbackInputStream, observers);
        try {
            observableInputStream.removeAllObservers();
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            verifyException("java.util.AbstractList", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testReadFromPipedInputStreamThrowsIndexOutOfBoundsException() throws Throwable {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream);
        ObservableInputStream observableInputStream = new ObservableInputStream(pipedInputStream);
        byte[] byteArray = new byte[0];
        try {
            observableInputStream.read(byteArray, 1665, 1665);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.io.PipedInputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testReadWithNegativeLengthThrowsIndexOutOfBoundsException() throws Throwable {
        ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        StringWriter stringWriter = new StringWriter();
        StringBuffer stringBuffer = stringWriter.getBuffer();
        builder.setCharSequence(stringBuffer);
        ObservableInputStream observableInputStream = builder.get();
        byte[] byteArray = new byte[0];
        try {
            observableInputStream.read(byteArray, 0, -1);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("org.apache.commons.io.input.CharSequenceInputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testReadWithInvalidOffsetThrowsArrayIndexOutOfBoundsException() throws Throwable {
        byte[] byteArray = new byte[8];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray, -1, 6);
        ObservableInputStream observableInputStream = new ObservableInputStream(byteArrayInputStream);
        try {
            observableInputStream.read(byteArray, 1, 1);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("java.io.ByteArrayInputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testReadFromClosedPushbackInputStreamThrowsIOException() throws Throwable {
        PushbackInputStream pushbackInputStream = new PushbackInputStream((InputStream) null);
        ObservableInputStream.Observer[] observers = new ObservableInputStream.Observer[0];
        ObservableInputStream observableInputStream = new ObservableInputStream(pushbackInputStream, observers);
        byte[] byteArray = new byte[2];
        try {
            observableInputStream.read(byteArray, 3, -2701);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.PushbackInputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testReadFromClosedBufferedInputStreamThrowsIOException() throws Throwable {
        BufferedInputStream bufferedInputStream = new BufferedInputStream((InputStream) null);
        ObservableInputStream observableInputStream = new ObservableInputStream(bufferedInputStream);
        byte[] byteArray = new byte[1];
        try {
            observableInputStream.read(byteArray);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.BufferedInputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testReadFromUnconnectedPipedInputStreamThrowsIOException() throws Throwable {
        PipedInputStream pipedInputStream = new PipedInputStream(957);
        ObservableInputStream.Observer[] observers = new ObservableInputStream.Observer[1];
        observers[0] = new TimestampedObserver();
        ObservableInputStream observableInputStream = new ObservableInputStream(pipedInputStream, observers);
        try {
            observableInputStream.read();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.apache.commons.io.IOExceptionList", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testNoteErrorWithMockIOExceptionThrowsIOException() throws Throwable {
        Enumeration<ObjectInputStream> enumeration = mock(Enumeration.class, new ViolatedAssumptionAnswer());
        doReturn(false).when(enumeration).hasMoreElements();
        SequenceInputStream sequenceInputStream = new SequenceInputStream(enumeration);
        ObservableInputStream.Observer[] observers = new ObservableInputStream.Observer[1];
        observers[0] = new TimestampedObserver();
        ObservableInputStream observableInputStream = new ObservableInputStream(sequenceInputStream, observers);
        MockIOException mockIOException = new MockIOException("");
        try {
            observableInputStream.noteError(mockIOException);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.apache.commons.io.IOExceptionList", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testNoteDataBytesWithNullArrayThrowsNullPointerException() throws Throwable {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream);
        ObservableInputStream.Observer[] observers = new ObservableInputStream.Observer[1];
        ObservableInputStream observableInputStream = new ObservableInputStream(pipedInputStream, observers);
        try {
            observableInputStream.noteDataBytes(null, 3677, -1);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.io.input.ObservableInputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testNoteDataByteWithNullObserversThrowsNullPointerException() throws Throwable {
        PipedInputStream pipedInputStream = new PipedInputStream(1136);
        ObservableInputStream.Observer[] observers = new ObservableInputStream.Observer[3];
        ObservableInputStream observableInputStream = new ObservableInputStream(pipedInputStream, observers);
        try {
            observableInputStream.noteDataByte(1136);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.io.input.ObservableInputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testGetObserversThrowsNullPointerException() throws Throwable {
        ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        StringWriter stringWriter = new StringWriter();
        StringBuffer stringBuffer = stringWriter.getBuffer();
        builder.setCharSequence(stringBuffer);
        ObservableInputStream observableInputStream = builder.get();
        try {
            observableInputStream.getObservers();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.ArrayList", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testConsumeThrowsNullPointerException() throws Throwable {
        ObservableInputStream observableInputStream = new ObservableInputStream((InputStream) null);
        try {
            observableInputStream.consume();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.io.input.ProxyInputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testConsumeThrowsArrayIndexOutOfBoundsException() throws Throwable {
        byte[] byteArray = new byte[10];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray, -1732, 1);
        ObservableInputStream observableInputStream = new ObservableInputStream(byteArrayInputStream);
        try {
            observableInputStream.consume();
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("java.io.ByteArrayInputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testBuilderSetPathThrowsNoSuchFileException() throws Throwable {
        ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        builder.setPath("PZkNv|C6'u*&le;,");
        try {
            new ObservableInputStream(builder);
            fail("Expecting exception: NoSuchFileException");
        } catch (NoSuchFileException e) {
            // Expected exception
        }
    }

    @Test(timeout = TIMEOUT)
    public void testBuilderSetWriterThrowsUnsupportedOperationException() throws Throwable {
        ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        StringWriter stringWriter = new StringWriter();
        builder.setWriter(stringWriter);
        try {
            new ObservableInputStream(builder);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            verifyException("org.apache.commons.io.build.AbstractOrigin", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testConstructorWithNullBuilderThrowsNullPointerException() throws Throwable {
        try {
            new ObservableInputStream((ObservableInputStream.AbstractBuilder<?>) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.io.input.ProxyInputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testBuilderGetThrowsIllegalStateException() throws Throwable {
        ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        try {
            new ObservableInputStream(builder);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.apache.commons.io.build.AbstractOriginSupplier", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testConstructorWithNullObserversThrowsNullPointerException() throws Throwable {
        ObservableInputStream observableInputStream = new ObservableInputStream((InputStream) null);
        try {
            new ObservableInputStream(observableInputStream, (ObservableInputStream.Observer[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testReadWithNegativeOffsetAndLengthThrowsNullPointerException() throws Throwable {
        ObservableInputStream observableInputStream = new ObservableInputStream((InputStream) null);
        byte[] byteArray = new byte[0];
        try {
            observableInputStream.read(byteArray, -1, -1);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.io.input.ProxyInputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testTimestampedObserverErrorThrowsIOException() throws Throwable {
        TimestampedObserver observer = new TimestampedObserver();
        MockIOException mockIOException = new MockIOException("org.apache.commons.io.input.ObservableInputStream$Builder");
        try {
            observer.error(mockIOException);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            // Expected exception
        }
    }

    @Test(timeout = TIMEOUT)
    public void testReadFromUnconnectedPipedInputStreamThrowsNullPointerException() throws Throwable {
        PipedInputStream pipedInputStream = new PipedInputStream(957);
        ObservableInputStream.Observer[] observers = new ObservableInputStream.Observer[1];
        ObservableInputStream observableInputStream = new ObservableInputStream(pipedInputStream, observers);
        try {
            observableInputStream.read();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.io.input.ObservableInputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testReadFromClosedBufferedInputStreamReturnsZero() throws Throwable {
        BufferedInputStream bufferedInputStream = new BufferedInputStream((InputStream) null);
        ObservableInputStream observableInputStream = new ObservableInputStream(bufferedInputStream);
        byte[] byteArray = new byte[0];
        int bytesRead = observableInputStream.read(byteArray);
        assertEquals(0, bytesRead);
    }

    @Test(timeout = TIMEOUT)
    public void testConsumeWithCharBuffer() throws Throwable {
        ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        CharBuffer charBuffer = CharBuffer.allocate(255);
        builder.setCharSequence(charBuffer);
        ObservableInputStream observableInputStream = new ObservableInputStream(builder);
        observableInputStream.consume();
    }

    @Test(timeout = TIMEOUT)
    public void testCloseDataInputStream() throws Throwable {
        DataInputStream dataInputStream = new DataInputStream((InputStream) null);
        ObservableInputStream observableInputStream = new ObservableInputStream(dataInputStream);
        observableInputStream.close();
    }

    @Test(timeout = TIMEOUT)
    public void testTimestampedObserverFinished() throws Throwable {
        TimestampedObserver observer = new TimestampedObserver();
        observer.finished();
        assertFalse(observer.isClosed());
    }

    @Test(timeout = TIMEOUT)
    public void testTimestampedObserverData() throws Throwable {
        TimestampedObserver observer = new TimestampedObserver();
        observer.data(1225);
        assertFalse(observer.isClosed());
    }

    @Test(timeout = TIMEOUT)
    public void testMessageDigestMaintainingObserverClosed() throws Throwable {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        MessageDigestInputStream.MessageDigestMaintainingObserver observer = new MessageDigestInputStream.MessageDigestMaintainingObserver(messageDigest);
        observer.closed();
    }

    @Test(timeout = TIMEOUT)
    public void testTimestampedObserverDataWithNullArray() throws Throwable {
        TimestampedObserver observer = new TimestampedObserver();
        observer.data(null, -1, -1);
        assertFalse(observer.isClosed());
    }

    @Test(timeout = TIMEOUT)
    public void testReadFromUnconnectedPipedInputStreamThrowsIOException() throws Throwable {
        PipedInputStream pipedInputStream = new PipedInputStream(3);
        ObservableInputStream.Observer[] observers = new ObservableInputStream.Observer[1];
        observers[0] = new TimestampedObserver();
        ObservableInputStream observableInputStream = new ObservableInputStream(pipedInputStream, observers);
        byte[] byteArray = new byte[1];
        try {
            observableInputStream.read(byteArray);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.apache.commons.io.IOExceptionList", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testBuilderSetObservers() throws Throwable {
        ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        LinkedList<ObservableInputStream.Observer> observers = new LinkedList<>();
        builder.setObservers(observers);
        assertEquals(0, observers.size());
    }

    @Test(timeout = TIMEOUT)
    public void testReadFromBufferedInputStreamThrowsNullPointerException() throws Throwable {
        PipedInputStream pipedInputStream = new PipedInputStream(3);
        ObservableInputStream.Observer[] observers = new ObservableInputStream.Observer[1];
        ObservableInputStream observableInputStream = new ObservableInputStream(pipedInputStream, observers);
        PushbackInputStream pushbackInputStream = new PushbackInputStream(observableInputStream);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(pushbackInputStream);
        ObservableInputStream observableInputStream1 = new ObservableInputStream(bufferedInputStream);
        byte[] byteArray = new byte[1];
        try {
            observableInputStream1.read(byteArray);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.io.input.ObservableInputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testReadFromCharBuffer() throws Throwable {
        ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        CharBuffer charBuffer = CharBuffer.allocate(1332);
        builder.setCharSequence(charBuffer);
        ObservableInputStream observableInputStream = builder.get();
        byte[] byteArray = new byte[5];
        int bytesRead = observableInputStream.read(byteArray);
        assertEquals(5, bytesRead);
    }

    @Test(timeout = TIMEOUT)
    public void testAddNullObserverThrowsNullPointerException() throws Throwable {
        ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        StringWriter stringWriter = new StringWriter();
        StringBuffer stringBuffer = stringWriter.getBuffer();
        builder.setCharSequence(stringBuffer);
        ObservableInputStream observableInputStream = builder.get();
        try {
            observableInputStream.add(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.io.input.ObservableInputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testCloseObservableInputStream() throws Throwable {
        ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        CharBuffer charBuffer = CharBuffer.allocate(255);
        builder.setCharSequence(charBuffer);
        ObservableInputStream observableInputStream = new ObservableInputStream(builder);
        observableInputStream.close();
    }

    @Test(timeout = TIMEOUT)
    public void testGetObserversFromSequenceInputStream() throws Throwable {
        Enumeration<ObjectInputStream> enumeration = mock(Enumeration.class, new ViolatedAssumptionAnswer());
        doReturn(false).when(enumeration).hasMoreElements();
        SequenceInputStream sequenceInputStream = new SequenceInputStream(enumeration);
        ObservableInputStream.Observer[] observers = new ObservableInputStream.Observer[1];
        ObservableInputStream observableInputStream = new ObservableInputStream(sequenceInputStream, observers);
        List<ObservableInputStream.Observer> observerList = observableInputStream.getObservers();
        assertEquals(1, observerList.size());
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveObserverThrowsNullPointerException() throws Throwable {
        ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        StringWriter stringWriter = new StringWriter();
        StringBuffer stringBuffer = stringWriter.getBuffer();
        builder.setCharSequence(stringBuffer);
        ObservableInputStream observableInputStream = builder.get();
        TimestampedObserver observer = new TimestampedObserver();
        try {
            observableInputStream.remove(observer);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.io.input.ObservableInputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testReadFromEmptyByteArrayInputStream() throws Throwable {
        byte[] byteArray = new byte[0];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        ObservableInputStream observableInputStream = new ObservableInputStream(byteArrayInputStream);
        int byteRead = observableInputStream.read();
        assertEquals(-1, byteRead);
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveAllObserversThrowsNullPointerException() throws Throwable {
        ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        StringWriter stringWriter = new StringWriter();
        StringBuffer stringBuffer = stringWriter.getBuffer();
        builder.setCharSequence(stringBuffer);
        ObservableInputStream observableInputStream = builder.get();
        try {
            observableInputStream.removeAllObservers();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.io.input.ObservableInputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testConsumeFromClosedBufferedInputStreamThrowsIOException() throws Throwable {
        BufferedInputStream bufferedInputStream = new BufferedInputStream((InputStream) null);
        ObservableInputStream observableInputStream = new ObservableInputStream(bufferedInputStream);
        try {
            observableInputStream.consume();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.BufferedInputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testReadFromCharBufferReturnsZero() throws Throwable {
        ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        CharBuffer charBuffer = CharBuffer.allocate(1332);
        builder.setCharSequence(charBuffer);
        ObservableInputStream observableInputStream = builder.get();
        int byteRead = observableInputStream.read();
        assertEquals(0, byteRead);
    }
}