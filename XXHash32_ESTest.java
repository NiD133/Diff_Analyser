/*
 * Refactored test suite for XXHash32 for better understandability and maintainability.
 * Tests cover core functionality, edge cases, and exception scenarios.
 */
package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.codec.digest.XXHash32;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class XXHash32_ESTest extends XXHash32_ESTest_scaffolding {

    // =========================================
    // Constructor & Reset Tests
    // =========================================

    @Test(timeout = 4000)
    public void testConstructorWithSeed() {
        // Create instance with custom seed
        XXHash32 hash = new XXHash32(97);
        // Verify initial state matches seed
        assertEquals(3659767818L, hash.getValue());
    }

    @Test(timeout = 4000)
    public void testReset() {
        XXHash32 hash = new XXHash32();
        hash.reset();
        // After reset, value should revert to initial state
        assertEquals(46947589L, hash.getValue());
    }

    // =========================================
    // Single & Multiple Byte Update Tests
    // =========================================

    @Test(timeout = 4000)
    public void testUpdateSingleByte() {
        XXHash32 hash = new XXHash32();
        hash.update(2026); // Add single byte
        assertEquals(968812856L, hash.getValue());
    }

    @Test(timeout = 4000)
    public void testUpdateFourBytes() {
        XXHash32 hash = new XXHash32();
        // Update with four distinct bytes
        hash.update(2);
        hash.update(2);
        hash.update(0);
        hash.update(8);
        assertEquals(1429036944L, hash.getValue());
    }

    @Test(timeout = 4000)
    public void testUpdate21Zeros() {
        byte[] data = new byte[62]; // All zeros
        XXHash32 hash = new XXHash32();
        // Update with 21 zero bytes
        hash.update(data, 21, 21);
        assertEquals(86206869L, hash.getValue());
    }

    // =========================================
    // Non-Zero Data Update Tests
    // =========================================

    @Test(timeout = 4000)
    public void testUpdateWithNonZeroDataInTheMiddle() {
        byte[] data = new byte[25];
        data[21] = (byte) 16; // Set non-zero at position 21
        XXHash32 hash = new XXHash32();
        // Process 16 bytes starting at offset 7 (includes non-zero)
        hash.update(data, 7, 16);
        assertEquals(1866244335L, hash.getValue());
    }

    @Test(timeout = 4000)
    public void testUpdate24BytesWithNonZeroAtOffset3() {
        byte[] data = new byte[25];
        data[3] = (byte) 16; // Set non-zero at position 3
        XXHash32 hash = new XXHash32();
        // Process 24 bytes starting at offset 0
        hash.update(data, 0, 24);
        assertEquals(281612550L, hash.getValue());
    }

    @Test(timeout = 4000)
    public void testMultipleUpdatesWithNonZeroData() {
        byte[] data = new byte[25];
        data[1] = (byte) 16; // Set non-zero at position 1
        XXHash32 hash = new XXHash32();
        
        // Series of updates with overlapping positions
        hash.update(data, 16, 4); // Offset 16 (zeros)
        hash.update(data, 1, 4);  // Includes non-zero
        hash.update(data, 0, 16); // Includes non-zero at position 1
        
        assertEquals(1465785993L, hash.getValue());
    }

    // =========================================
    // Edge Case & Exception Tests
    // =========================================

    @Test(timeout = 4000)
    public void testUpdateWithOffsetAndLengthCausingIntegerOverflow() {
        XXHash32 hash = new XXHash32();
        byte[] data = new byte[25];
        // Extreme offset/length causing integer overflow
        hash.update(data, 1336530510, 1336530510);
        // Verify hash handles overflow without crashing
        assertEquals(2363252416L, hash.getValue());
    }

    @Test(timeout = 4000)
    public void testUpdateWithNegativeLengthShouldBeNoOp() {
        XXHash32 hash = new XXHash32();
        byte[] data = new byte[8];
        // Negative length should be ignored
        hash.update(data, 1989, -24);
        assertEquals(46947589L, hash.getValue()); // Initial state
    }

    @Test(timeout = 4000)
    public void testUpdateWithZeroLength() {
        XXHash32 hash = new XXHash32();
        byte[] data = new byte[8];
        // Zero-length update should not change state
        hash.update(data, 0, 0);
        assertEquals(46947589L, hash.getValue());
    }

    @Test(timeout = 4000)
    public void testUpdateWithNullArrayThrowsNullPointerException() {
        XXHash32 hash = new XXHash32();
        try {
            hash.update(null, 2128, 2128);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testUpdateWithOffsetBeyondArrayLengthThrowsArrayIndexOutOfBoundsException() {
        XXHash32 hash = new XXHash32();
        byte[] data = new byte[25];
        try {
            hash.update(data, 374761393, 16);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected behavior
        }
    }
}