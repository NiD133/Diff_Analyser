package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.codec.digest.XXHash32;
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
public class XXHash32_ESTest extends XXHash32_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testUpdateWithLargeOffsets() throws Throwable {
        XXHash32 hash = new XXHash32();
        byte[] data = new byte[25];
        hash.update(data, 1336530510, 1336530510);
        assertEquals(2363252416L, hash.getValue());
    }

    @Test(timeout = 4000)
    public void testUpdateWithNegativeLength() throws Throwable {
        XXHash32 hash = new XXHash32();
        byte[] data = new byte[8];
        hash.update(data, 1989, -24);
        assertEquals(46947589L, hash.getValue());
    }

    @Test(timeout = 4000)
    public void testUpdateWithSpecificByte() throws Throwable {
        XXHash32 hash = new XXHash32();
        byte[] data = new byte[25];
        data[21] = (byte) 16;
        hash.update(data, 7, 16);
        assertEquals(1866244335L, hash.getValue());
    }

    @Test(timeout = 4000)
    public void testUpdateWithSingleInt() throws Throwable {
        XXHash32 hash = new XXHash32();
        hash.update(2026);
        assertEquals(968812856L, hash.getValue());
    }

    @Test(timeout = 4000)
    public void testUpdateWithMultipleInts() throws Throwable {
        XXHash32 hash = new XXHash32();
        hash.update(2);
        hash.update(2);
        hash.update(0);
        hash.update(8);
        assertEquals(1429036944L, hash.getValue());
    }

    @Test(timeout = 4000)
    public void testMultipleUpdatesWithByteArray() throws Throwable {
        XXHash32 hash = new XXHash32();
        byte[] data = new byte[25];
        data[1] = (byte) 16;
        hash.update(data, 16, 4);
        hash.update(data, 1, 4);
        hash.update(data, 0, 16);
        assertEquals(1465785993L, hash.getValue());
    }

    @Test(timeout = 4000)
    public void testUpdateWithFullByteArray() throws Throwable {
        XXHash32 hash = new XXHash32();
        byte[] data = new byte[25];
        data[3] = (byte) 16;
        hash.update(data, 0, 24);
        assertEquals(281612550L, hash.getValue());
    }

    @Test(timeout = 4000)
    public void testInitialValueWithSeed() throws Throwable {
        XXHash32 hash = new XXHash32(97);
        assertEquals(3659767818L, hash.getValue());
    }

    @Test(timeout = 4000)
    public void testUpdateWithNullByteArray() throws Throwable {
        XXHash32 hash = new XXHash32();
        try {
            hash.update(null, 2128, 2128);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.codec.digest.XXHash32", e);
        }
    }

    @Test(timeout = 4000)
    public void testUpdateWithExcessiveOffset() throws Throwable {
        XXHash32 hash = new XXHash32();
        byte[] data = new byte[25];
        try {
            hash.update(data, 374761393, 16);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.apache.commons.codec.digest.XXHash32", e);
        }
    }

    @Test(timeout = 4000)
    public void testUpdateWithZeroLength() throws Throwable {
        XXHash32 hash = new XXHash32();
        byte[] data = new byte[8];
        hash.update(data, 0, 0);
        assertEquals(46947589L, hash.getValue());
    }

    @Test(timeout = 4000)
    public void testUpdateWithPartialByteArray() throws Throwable {
        byte[] data = new byte[62];
        XXHash32 hash = new XXHash32();
        hash.update(data, 21, 21);
        assertEquals(86206869L, hash.getValue());
    }

    @Test(timeout = 4000)
    public void testResetFunctionality() throws Throwable {
        XXHash32 hash = new XXHash32();
        hash.reset();
        assertEquals(46947589L, hash.getValue());
    }
}