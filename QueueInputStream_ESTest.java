package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;

import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * Unit tests for {@link QueueInputStream}.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class QueueInputStream_ESTest extends QueueInputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testBuilderSetTimeout() throws Throwable {
        QueueInputStream.Builder builder = QueueInputStream.builder();
        Duration oneHourDuration = ChronoUnit.HOURS.getDuration();
        QueueInputStream.Builder updatedBuilder = builder.setTimeout(oneHourDuration);
        assertSame(builder, updatedBuilder);
    }

    @Test(timeout = 4000)
    public void testBuilderSetBlockingQueue() throws Throwable {
        QueueInputStream.Builder builder = new QueueInputStream.Builder();
        LinkedBlockingDeque<Integer> queue = new LinkedBlockingDeque<>();
        QueueInputStream.Builder updatedBuilder = builder.setBlockingQueue(queue);
        assertSame(builder, updatedBuilder);
    }

    @Test(timeout = 4000)
    public void testReadFromQueueWithData() throws Throwable {
        PriorityBlockingQueue<Integer> queue = new PriorityBlockingQueue<>();
        queue.add(-728);
        QueueInputStream inputStream = new QueueInputStream(queue);
        byte[] buffer = new byte[8];
        int bytesRead = inputStream.read(buffer, 1, 1);
        assertEquals(1, bytesRead);
    }

    @Test(timeout = 4000)
    public void testReadFromEmptyQueue() throws Throwable {
        QueueInputStream inputStream = new QueueInputStream();
        byte[] buffer = new byte[4];
        int bytesRead = inputStream.read(buffer, 1, 1);
        assertEquals(-1, bytesRead);
    }

    @Test(timeout = 4000)
    public void testReadSingleByteFromQueueWithData() throws Throwable {
        PriorityBlockingQueue<Integer> queue = new PriorityBlockingQueue<>();
        queue.add(0);
        QueueInputStream inputStream = new QueueInputStream(queue);
        int byteRead = inputStream.read();
        assertEquals(0, byteRead);
    }

    @Test(timeout = 4000)
    public void testReadSingleByteFromEmptyQueue() throws Throwable {
        PriorityBlockingQueue<Integer> queue = new PriorityBlockingQueue<>();
        QueueInputStream inputStream = new QueueInputStream(queue);
        int byteRead = inputStream.read();
        assertEquals(-1, byteRead);
    }

    @Test(timeout = 4000)
    public void testSkipBytesInQueue() throws Throwable {
        PriorityBlockingQueue<Integer> queue = new PriorityBlockingQueue<>();
        queue.add(-728);
        QueueInputStream inputStream = new QueueInputStream(queue);
        long bytesSkipped = inputStream.skip(4014L);
        assertEquals(1L, bytesSkipped);
    }

    @Test(timeout = 4000)
    public void testReadZeroBytes() throws Throwable {
        PriorityBlockingQueue<Integer> queue = new PriorityBlockingQueue<>();
        QueueInputStream inputStream = new QueueInputStream(queue);
        byte[] buffer = new byte[8];
        int bytesRead = inputStream.read(buffer, 0, 0);
        assertEquals(0, bytesRead);
    }

    @Test(timeout = 4000)
    public void testReadWithInvalidOffsetAndLength() throws Throwable {
        QueueInputStream inputStream = new QueueInputStream();
        byte[] buffer = new byte[0];
        try {
            inputStream.read(buffer, 2005, 2005);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("org.apache.commons.io.input.QueueInputStream", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadWithNegativeLength() throws Throwable {
        QueueInputStream inputStream = new QueueInputStream();
        byte[] buffer = new byte[19];
        try {
            inputStream.read(buffer, 1557, -1357);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("org.apache.commons.io.input.QueueInputStream", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadWithNegativeOffsetAndLength() throws Throwable {
        QueueInputStream inputStream = new QueueInputStream();
        byte[] buffer = new byte[0];
        try {
            inputStream.read(buffer, -3123, -3123);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("org.apache.commons.io.input.QueueInputStream", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadWithNullBuffer() throws Throwable {
        QueueInputStream inputStream = new QueueInputStream();
        try {
            inputStream.read(null, 16, 16);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.io.input.QueueInputStream", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadSingleByteFromQueueWithNegativeData() throws Throwable {
        PriorityBlockingQueue<Integer> queue = new PriorityBlockingQueue<>();
        queue.add(-728);
        QueueInputStream inputStream = new QueueInputStream(queue);
        int byteRead = inputStream.read();
        assertEquals(40, byteRead);
    }

    @Test(timeout = 4000)
    public void testBuilderSetNegativeTimeout() throws Throwable {
        QueueInputStream.Builder builder = QueueInputStream.builder();
        Duration negativeDuration = Duration.ofHours(-2277L);
        try {
            builder.setTimeout(negativeDuration);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.io.input.QueueInputStream$Builder", e);
        }
    }

    @Test(timeout = 4000)
    public void testBuilderSetNullTimeout() throws Throwable {
        QueueInputStream.Builder builder = new QueueInputStream.Builder();
        QueueInputStream.Builder updatedBuilder = builder.setTimeout(null);
        assertSame(builder, updatedBuilder);
    }

    @Test(timeout = 4000)
    public void testBuilderGetInputStream() throws Throwable {
        QueueInputStream.Builder builder = QueueInputStream.builder();
        QueueInputStream inputStream = builder.get();
        assertNotNull(inputStream);
    }

    @Test(timeout = 4000)
    public void testGetBlockingQueueFromNullQueueInputStream() throws Throwable {
        QueueInputStream inputStream = new QueueInputStream((BlockingQueue<Integer>) null);
        BlockingQueue<Integer> queue = inputStream.getBlockingQueue();
        assertNotNull(queue);
    }

    @Test(timeout = 4000)
    public void testNewQueueOutputStream() throws Throwable {
        QueueInputStream inputStream = new QueueInputStream();
        QueueOutputStream outputStream = inputStream.newQueueOutputStream();
        assertNotNull(outputStream);
    }

    @Test(timeout = 4000)
    public void testBuilderSetTimeoutFromInputStream() throws Throwable {
        PriorityBlockingQueue<Integer> queue = new PriorityBlockingQueue<>();
        QueueInputStream inputStream = new QueueInputStream(queue);
        Duration timeout = inputStream.getTimeout();
        QueueInputStream.Builder builder = QueueInputStream.builder();
        QueueInputStream.Builder updatedBuilder = builder.setTimeout(timeout);
        assertSame(builder, updatedBuilder);
    }
}