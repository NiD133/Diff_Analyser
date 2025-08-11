package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.core.util.JsonRecyclerPools;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class ByteArrayBuilderTest extends ByteArrayBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testWriteWithNegativeLength() throws Throwable {
        byte[] byteArray = new byte[1];
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial(byteArray, (byte) 74);
        builder.write(byteArray, 74, -278);
        assertEquals(74, builder.size());
    }

    @Test(timeout = 4000)
    public void testFinishCurrentSegment() throws Throwable {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        ByteArrayBuilder builderFromInitial = ByteArrayBuilder.fromInitial(builder.NO_BYTES, 2000);
        byte[] byteArray = builderFromInitial.finishCurrentSegment();
        builder.write(byteArray);
        assertEquals(0, builderFromInitial.getCurrentSegmentLength());
        assertEquals(500, builder.getCurrentSegmentLength());
    }

    @Test(timeout = 4000)
    public void testAppendFourBytes() throws Throwable {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.appendFourBytes(-35);
        assertEquals(4, builder.size());
    }

    @Test(timeout = 4000)
    public void testAppendThreeBytes() throws Throwable {
        byte[] byteArray = new byte[0];
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial(byteArray, 1);
        builder.appendThreeBytes((byte) -48);
        assertEquals(3, builder.size());
    }

    @Test(timeout = 4000)
    public void testAppendThreeBytesWithLargeValue() throws Throwable {
        ByteArrayBuilder builder = new ByteArrayBuilder(2);
        builder.appendThreeBytes(2341);
        assertEquals(3, builder.size());
    }

    @Test(timeout = 4000)
    public void testAppendTwoBytes() throws Throwable {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.appendTwoBytes(-2767);
        assertEquals(2, builder.getCurrentSegmentLength());
    }

    @Test(timeout = 4000)
    public void testInitialSegmentLength() throws Throwable {
        ByteArrayBuilder builder = new ByteArrayBuilder(131072);
        assertEquals(0, builder.getCurrentSegmentLength());
    }

    @Test(timeout = 4000)
    public void testAppendSingleByte() throws Throwable {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.append(8000);
        assertEquals(1, builder.size());
        assertEquals(1, builder.getCurrentSegmentLength());
    }

    @Test(timeout = 4000)
    public void testResetAndGetFirstSegmentWithNullInitial() throws Throwable {
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial((byte[]) null, 2);
        byte[] byteArray = builder.resetAndGetFirstSegment();
        assertNull(byteArray);
    }

    @Test(timeout = 4000)
    public void testResetAndGetFirstSegment() throws Throwable {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        ByteArrayBuilder builderFromInitial = ByteArrayBuilder.fromInitial(builder.NO_BYTES, 4000);
        byte[] byteArray = builderFromInitial.resetAndGetFirstSegment();
        assertEquals(0, byteArray.length);
    }

    @Test(timeout = 4000)
    public void testSetCurrentSegmentLength() throws Throwable {
        JsonRecyclerPools.NonRecyclingPool pool = JsonRecyclerPools.NonRecyclingPool.GLOBAL;
        BufferRecycler recycler = pool.acquirePooled();
        ByteArrayBuilder builder = new ByteArrayBuilder(recycler);
        builder.setCurrentSegmentLength(2);
        assertEquals(2, builder.size());
        assertEquals(2, builder.getCurrentSegmentLength());
    }

    @Test(timeout = 4000)
    public void testSetNegativeCurrentSegmentLength() throws Throwable {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.setCurrentSegmentLength(-1);
        assertEquals(-1, builder.size());
        assertEquals(-1, builder.getCurrentSegmentLength());
    }

    @Test(timeout = 4000)
    public void testReleaseAndGetCurrentSegment() throws Throwable {
        JsonRecyclerPools.NonRecyclingPool pool = JsonRecyclerPools.NonRecyclingPool.GLOBAL;
        BufferRecycler recycler = pool.acquirePooled();
        ByteArrayBuilder builder = new ByteArrayBuilder(recycler, -1);
        builder.release();
        byte[] byteArray = builder.getCurrentSegment();
        assertNull(byteArray);
    }

    @Test(timeout = 4000)
    public void testGetCurrentSegment() throws Throwable {
        BufferRecycler recycler = new BufferRecycler();
        ByteArrayBuilder builder = new ByteArrayBuilder(recycler, -3320);
        ByteArrayBuilder builderFromInitial = ByteArrayBuilder.fromInitial(builder.NO_BYTES, 6);
        builderFromInitial.getCurrentSegment();
        assertEquals(6, builderFromInitial.size());
    }

    @Test(timeout = 4000)
    public void testGetClearAndRelease() throws Throwable {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.finishCurrentSegment();
        assertEquals(500, builder.size());

        byte[] byteArray = builder.getClearAndRelease();
        assertEquals(500, byteArray.length);
    }

    @Test(timeout = 4000)
    public void testCompleteAndCoalesce() throws Throwable {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        byte[] byteArray = builder.completeAndCoalesce(0);
        assertEquals(0, byteArray.length);
    }

    @Test(timeout = 4000)
    public void testBufferRecycler() throws Throwable {
        BufferRecycler recycler = new BufferRecycler();
        JsonRecyclerPools.ThreadLocalPool pool = JsonRecyclerPools.ThreadLocalPool.GLOBAL;
        BufferRecycler pooledRecycler = recycler.withPool(pool);
        ByteArrayBuilder builder = new ByteArrayBuilder(pooledRecycler, 1357);
        BufferRecycler bufferRecycler = builder.bufferRecycler();
        assertEquals(1, BufferRecycler.BYTE_WRITE_ENCODING_BUFFER);
    }

    @Test(timeout = 4000)
    public void testWriteNullByteArray() throws Throwable {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        try {
            builder.write((byte[]) null, 557, 557);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testWriteNullByteArrayWithoutOffset() throws Throwable {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        try {
            builder.write((byte[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
            verifyException("com.fasterxml.jackson.core.util.ByteArrayBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testWriteWithNegativeSegmentLength() throws Throwable {
        ByteArrayBuilder builder = new ByteArrayBuilder(130740);
        builder.setCurrentSegmentLength(-2165);
        byte[] byteArray = new byte[1];
        try {
            builder.write(byteArray);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testWriteWithNegativeInitialLength() throws Throwable {
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial((byte[]) null, 131050);
        try {
            builder.write(131050);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
            verifyException("com.fasterxml.jackson.core.util.ByteArrayBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testToByteArrayWithNegativeInitialLength() throws Throwable {
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial((byte[]) null, -538);
        try {
            builder.toByteArray();
            fail("Expecting exception: NegativeArraySizeException");
        } catch (NegativeArraySizeException e) {
            // Expected exception
            verifyException("com.fasterxml.jackson.core.util.ByteArrayBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testToByteArrayWithLargeInitialLength() throws Throwable {
        byte[] byteArray = new byte[0];
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial(byteArray, 294912);
        try {
            builder.toByteArray();
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetClearAndReleaseWithNullInitial() throws Throwable {
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial((byte[]) null, 131050);
        try {
            builder.getClearAndRelease();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetClearAndReleaseWithNegativeSegmentLength() throws Throwable {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.setCurrentSegmentLength(-1);
        try {
            builder.getClearAndRelease();
            fail("Expecting exception: NegativeArraySizeException");
        } catch (NegativeArraySizeException e) {
            // Expected exception
            verifyException("com.fasterxml.jackson.core.util.ByteArrayBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testFinishCurrentSegmentWithNullInitial() throws Throwable {
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial((byte[]) null, 2000);
        try {
            builder.finishCurrentSegment();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testCompleteAndCoalesceWithNullInitial() throws Throwable {
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial((byte[]) null, 2);
        try {
            builder.completeAndCoalesce(2);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testCompleteAndCoalesceWithNegativeLength() throws Throwable {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        try {
            builder.completeAndCoalesce(-270);
            fail("Expecting exception: NegativeArraySizeException");
        } catch (NegativeArraySizeException e) {
            // Expected exception
            verifyException("com.fasterxml.jackson.core.util.ByteArrayBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testCompleteAndCoalesceWithLargeLength() throws Throwable {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        try {
            builder.completeAndCoalesce(2014);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testAppendTwoBytesWithNullInitial() throws Throwable {
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial((byte[]) null, -1615);
        try {
            builder.appendTwoBytes(4000);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
            verifyException("com.fasterxml.jackson.core.util.ByteArrayBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testAppendTwoBytesWithNegativeInitialLength() throws Throwable {
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial((byte[]) null, -733);
        try {
            builder.appendTwoBytes(130739);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
            verifyException("com.fasterxml.jackson.core.util.ByteArrayBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testAppendThreeBytesWithNullInitial() throws Throwable {
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial((byte[]) null, 1495);
        try {
            builder.appendThreeBytes(4000);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
            verifyException("com.fasterxml.jackson.core.util.ByteArrayBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testAppendThreeBytesWithNegativeSegmentLength() throws Throwable {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.setCurrentSegmentLength(-870);
        try {
            builder.appendThreeBytes(-2056);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
            verifyException("com.fasterxml.jackson.core.util.ByteArrayBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testAppendFourBytesWithNullInitial() throws Throwable {
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial((byte[]) null, -1623);
        try {
            builder.appendFourBytes(-1623);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
            verifyException("com.fasterxml.jackson.core.util.ByteArrayBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testAppendFourBytesWithNegativeInitialLength() throws Throwable {
        byte[] byteArray = new byte[1];
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial(byteArray, (byte) -1);
        try {
            builder.appendFourBytes((byte) -1);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
            verifyException("com.fasterxml.jackson.core.util.ByteArrayBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testAppendWithNullInitial() throws Throwable {
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial((byte[]) null, 2078);
        try {
            builder.append(4000);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
            verifyException("com.fasterxml.jackson.core.util.ByteArrayBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testAppendWithNegativeSegmentLength() throws Throwable {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.setCurrentSegmentLength(-2612);
        try {
            builder.append(-2612);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
            verifyException("com.fasterxml.jackson.core.util.ByteArrayBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithNegativeSize() throws Throwable {
        try {
            new ByteArrayBuilder((BufferRecycler) null, -1216);
            fail("Expecting exception: NegativeArraySizeException");
        } catch (NegativeArraySizeException e) {
            // Expected exception
            verifyException("com.fasterxml.jackson.core.util.ByteArrayBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithZeroSize() throws Throwable {
        BufferRecycler recycler = new BufferRecycler(0, 0);
        try {
            new ByteArrayBuilder(recycler, 0);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
            verifyException("java.util.concurrent.atomic.AtomicReferenceArray", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithSmallRecycler() throws Throwable {
        BufferRecycler recycler = new BufferRecycler(1, 1);
        try {
            new ByteArrayBuilder(recycler);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
            verifyException("java.util.concurrent.atomic.AtomicReferenceArray", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithNegativeBlockSize() throws Throwable {
        try {
            new ByteArrayBuilder(-14);
            fail("Expecting exception: NegativeArraySizeException");
        } catch (NegativeArraySizeException e) {
            // Expected exception
            verifyException("com.fasterxml.jackson.core.util.ByteArrayBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testWriteAndToByteArray() throws Throwable {
        JsonRecyclerPools.ConcurrentDequePool pool = JsonRecyclerPools.ConcurrentDequePool.construct();
        BufferRecycler recycler = pool.acquirePooled();
        ByteArrayBuilder builder = new ByteArrayBuilder(recycler);
        byte[] byteArray = new byte[2];
        builder.write(byteArray, 1, 1);
        builder.toByteArray();
        assertEquals(1, builder.getCurrentSegmentLength());
    }

    @Test(timeout = 4000)
    public void testFinishCurrentSegmentAndToByteArray() throws Throwable {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.finishCurrentSegment();
        builder.toByteArray();
        assertEquals(0, builder.size());
    }

    @Test(timeout = 4000)
    public void testToByteArrayWithEmptyBuilder() throws Throwable {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        byte[] byteArray = builder.toByteArray();
        assertArrayEquals(new byte[]{}, byteArray);
    }

    @Test(timeout = 4000)
    public void testAppendAndAppendFourBytes() throws Throwable {
        ByteArrayBuilder builder = new ByteArrayBuilder(4);
        builder.append(4);
        builder.appendFourBytes(4);
        assertEquals(5, builder.size());
    }

    @Test(timeout = 4000)
    public void testRelease() throws Throwable {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.release();
        assertEquals(0, builder.size());
    }

    @Test(timeout = 4000)
    public void testReleaseTwice() throws Throwable {
        JsonRecyclerPools.NonRecyclingPool pool = JsonRecyclerPools.NonRecyclingPool.GLOBAL;
        BufferRecycler recycler = pool.acquirePooled();
        ByteArrayBuilder builder = new ByteArrayBuilder(recycler, 1);
        builder.release();
        builder.release();
        assertEquals(0, builder.getCurrentSegmentLength());
    }

    @Test(timeout = 4000)
    public void testReset() throws Throwable {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.reset();
        assertEquals(0, builder.size());
    }

    @Test(timeout = 4000)
    public void testResetAfterAppendTwoBytes() throws Throwable {
        byte[] byteArray = new byte[1];
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial(byteArray, (byte) 74);
        builder.appendTwoBytes(1981);
        builder.reset();
        assertEquals(0, builder.getCurrentSegmentLength());
    }

    @Test(timeout = 4000)
    public void testCompleteAndCoalesceWithWrite() throws Throwable {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        byte[] byteArray = builder.completeAndCoalesce(500);
        try {
            builder.write(byteArray, 500, 500);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testAppendFourBytesWithNegativeValue() throws Throwable {
        byte[] byteArray = new byte[0];
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial(byteArray, 1);
        builder.appendFourBytes((byte) -96);
        assertEquals(4, builder.getCurrentSegmentLength());
    }

    @Test(timeout = 4000)
    public void testAppendThreeBytesWithNegativeValue() throws Throwable {
        ByteArrayBuilder builder = new ByteArrayBuilder(131050);
        builder.appendThreeBytes(-2057);
        assertEquals(3, builder.getCurrentSegmentLength());
    }

    @Test(timeout = 4000)
    public void testGetClearAndReleaseTwice() throws Throwable {
        BufferRecycler recycler = new BufferRecycler();
        ByteArrayBuilder builder = new ByteArrayBuilder(recycler, 2);
        builder.getClearAndRelease();
        byte[] byteArray = builder.getClearAndRelease();
        assertEquals(0, byteArray.length);
    }

    @Test(timeout = 4000)
    public void testCompleteAndCoalesceWithAppendThreeBytes() throws Throwable {
        ByteArrayBuilder builder = new ByteArrayBuilder(1);
        builder.appendThreeBytes(1);
        assertEquals(2, builder.getCurrentSegmentLength());

        byte[] byteArray = builder.completeAndCoalesce(1);
        assertEquals(2, byteArray.length);
    }

    @Test(timeout = 4000)
    public void testInitialSize() throws Throwable {
        ByteArrayBuilder builder = new ByteArrayBuilder(131050);
        assertEquals(0, builder.size());
    }

    @Test(timeout = 4000)
    public void testGetCurrentSegmentWithNegativeSize() throws Throwable {
        BufferRecycler recycler = new BufferRecycler();
        ByteArrayBuilder builder = new ByteArrayBuilder(recycler, -3320);
        byte[] byteArray = builder.getCurrentSegment();
        assertEquals(2000, byteArray.length);
    }

    @Test(timeout = 4000)
    public void testSize() throws Throwable {
        ByteArrayBuilder builder = new ByteArrayBuilder(131050);
        int size = builder.size();
        assertEquals(0, size);
    }

    @Test(timeout = 4000)
    public void testWriteWithNegativeValue() throws Throwable {
        ByteArrayBuilder builder = new ByteArrayBuilder(131050);
        builder.write(-219);
        assertEquals(1, builder.getCurrentSegmentLength());
    }

    @Test(timeout = 4000)
    public void testBufferRecyclerIsNull() throws Throwable {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        BufferRecycler recycler = builder.bufferRecycler();
        assertNull(recycler);
    }

    @Test(timeout = 4000)
    public void testFlush() throws Throwable {
        byte[] byteArray = new byte[0];
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial(byteArray, 1);
        builder.flush();
        assertEquals(1, builder.size());
    }

    @Test(timeout = 4000)
    public void testResetAndGetFirstSegment() throws Throwable {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        byte[] byteArray = builder.resetAndGetFirstSegment();
        assertEquals(500, byteArray.length);
        assertEquals(0, builder.size());
    }

    @Test(timeout = 4000)
    public void testCompleteAndCoalesceWithAppendAndFinishSegments() throws Throwable {
        ByteArrayBuilder builder = new ByteArrayBuilder(131050);
        builder.completeAndCoalesce(131050);
        builder.append(131050);
        builder.finishCurrentSegment();
        builder.finishCurrentSegment();
        assertEquals(294862, builder.size());
    }

    @Test(timeout = 4000)
    public void testClose() throws Throwable {
        JsonRecyclerPools.BoundedPool pool = new JsonRecyclerPools.BoundedPool(0);
        BufferRecycler recycler = pool.createPooled();
        ByteArrayBuilder builder = new ByteArrayBuilder(recycler);
        builder.close();
        assertEquals(0, builder.getCurrentSegmentLength());
    }

    @Test(timeout = 4000)
    public void testGetCurrentSegmentLength() throws Throwable {
        JsonRecyclerPools.BoundedPool pool = new JsonRecyclerPools.BoundedPool(0);
        BufferRecycler recycler = pool.createPooled();
        ByteArrayBuilder builder = new ByteArrayBuilder(recycler);
        int length = builder.getCurrentSegmentLength();
        assertEquals(0, length);
    }

    @Test(timeout = 4000)
    public void testSizeWithNegativeInitialLength() throws Throwable {
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial((byte[]) null, -219);
        int size = builder.size();
        assertEquals(-219, size);
    }
}