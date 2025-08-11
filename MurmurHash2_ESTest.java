package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.codec.digest.MurmurHash2;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class MurmurHash2_ESTest extends MurmurHash2_ESTest_scaffolding {

    // ================================================
    // Tests for hash64 methods
    // ================================================
    
    @Test(timeout = 4000)
    public void testHash64StringWithOffsetAndSeed() {
        long result = MurmurHash2.hash64("bPH \"XdK'x'8?hr", 4, 0);
        assertEquals(-7207201254813729732L, result);
    }

    @Test(timeout = 4000)
    public void testHash64String() {
        long result = MurmurHash2.hash64("q%DCbQXCHT4'G\"^L");
        assertEquals(3105811143660689330L, result);
    }

    @Test(timeout = 4000)
    public void testHash64ByteArrayZeroLength() {
        byte[] input = new byte[2];
        long result = MurmurHash2.hash64(input, 0, 0);
        assertEquals(0L, result);
    }

    @Test(timeout = 4000)
    public void testHash64ByteArrayWithOffset() {
        byte[] input = {24, 0, 0, 0, 0};
        long result = MurmurHash2.hash64(input, 1);
        assertEquals(24027485454243747L, result);
    }

    @Test(timeout = 4000)
    public void testHash64ByteArrayNullInput() {
        long result = MurmurHash2.hash64((byte[]) null, 0);
        assertEquals(-7207201254813729732L, result);
    }

    @Test(timeout = 4000)
    public void testHash64ByteArrayWithNegativeSeed() {
        byte[] input = new byte[6];
        long result = MurmurHash2.hash64(input, 5, 56);
        assertEquals(-3113210640657759650L, result);
    }

    @Test(timeout = 4000)
    public void testHash64ByteArraySingleByte() {
        byte[] input = {0};
        long result = MurmurHash2.hash64(input, 0, 1);
        assertEquals(-5720937396023583481L, result);
    }

    @Test(timeout = 4000)
    public void testHash64StringFullLength() {
        long result = MurmurHash2.hash64("}oZe|_r,wwn+'.Z");
        assertEquals(-823493256237211900L, result);
    }

    @Test(timeout = 4000)
    public void testHash64StringWithLength1() {
        long result = MurmurHash2.hash64("ylLM~55", 1, 1);
        assertEquals(4591197677584300775L, result);
    }

    // ================================================
    // Tests for hash32 methods
    // ================================================
    
    @Test(timeout = 4000)
    public void testHash32ByteArrayEmpty() {
        byte[] input = new byte[0];
        int result = MurmurHash2.hash32(input, 0, 0);
        assertEquals(0, result);
    }

    @Test(timeout = 4000)
    public void testHash32ByteArrayWithNegativeSeed() {
        byte[] input = {18, 0, 0, 0, 0, 0};
        int result = MurmurHash2.hash32(input, 2, -3970);
        assertEquals(-1628438012, result);
    }

    @Test(timeout = 4000)
    public void testHash32ByteArrayDefaultSeed() {
        byte[] input = new byte[6];
        int result = MurmurHash2.hash32(input, -1756908916);
        assertEquals(0, result);
    }

    @Test(timeout = 4000)
    public void testHash32ByteArrayNullInput() {
        int result = MurmurHash2.hash32((byte[]) null, 0);
        assertEquals(275646681, result);
    }

    @Test(timeout = 4000)
    public void testHash32StringZeroLength() {
        int result = MurmurHash2.hash32("Dpn ='f", 0, 0);
        assertEquals(275646681, result);
    }

    @Test(timeout = 4000)
    public void testHash32EmptyString() {
        int result = MurmurHash2.hash32("");
        assertEquals(275646681, result);
    }

    @Test(timeout = 4000)
    public void testHash32StringWithLength1() {
        int result = MurmurHash2.hash32("9chG_Yo[`m", 1, 1);
        assertEquals(-1877468854, result);
    }

    @Test(timeout = 4000)
    public void testHash32String() {
        int result = MurmurHash2.hash32("org.apache.commons.codec.binary.StringUtils");
        assertEquals(-1819289676, result);
    }

    // ================================================
    // Exception tests for hash32
    // ================================================
    
    @Test(timeout = 4000)
    public void testHash32StringNullInputThrowsNPE() {
        try {
            MurmurHash2.hash32((String) null, 1396, 467);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void testHash32ByteArrayInvalidOffsetThrowsException() {
        byte[] input = new byte[0];
        try {
            MurmurHash2.hash32(input, 32);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void testHash32ByteArrayNullInputThrowsNPE() {
        try {
            MurmurHash2.hash32((byte[]) null, 32);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void testHash32StringInvalidRangeThrowsException() {
        try {
            MurmurHash2.hash32(": ", 12, 12);
            fail("Expected StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void testHash32StringNullInputThrowsNPE_NoArgs() {
        try {
            MurmurHash2.hash32((String) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void testHash32ByteArrayNegativeOffsetThrowsException() {
        byte[] input = new byte[6];
        try {
            MurmurHash2.hash32(input, -1, -652);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void testHash32ByteArrayInvalidLengthThrowsException() {
        byte[] input = new byte[0];
        try {
            MurmurHash2.hash32(input, 1834, 1834);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void testHash32ByteArrayNullInputThrowsNPE_WithLength() {
        try {
            MurmurHash2.hash32((byte[]) null, -1278, -1278);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    // ================================================
    // Exception tests for hash64
    // ================================================
    
    @Test(timeout = 4000)
    public void testHash64ByteArrayNullInputThrowsNPE() {
        try {
            MurmurHash2.hash64((byte[]) null, -420);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void testHash64StringNegativeOffsetThrowsException() {
        try {
            MurmurHash2.hash64(",;9b|Qi Zv", -2432, -2432);
            fail("Expected StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void testHash64StringNullInputThrowsNPE() {
        try {
            MurmurHash2.hash64((String) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void testHash64ByteArrayNullInputThrowsNPE_WithLength() {
        try {
            MurmurHash2.hash64((byte[]) null, -420, -420);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void testHash64ByteArrayInvalidOffsetThrowsException() {
        byte[] input = new byte[1];
        try {
            MurmurHash2.hash64(input, -941, 0);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void testHash64ByteArrayNegativeOffsetThrowsException() {
        byte[] input = new byte[2];
        try {
            MurmurHash2.hash64(input, -1, -1);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void testHash64ByteArrayOutOfBoundsThrowsException() {
        byte[] input = new byte[7];
        try {
            MurmurHash2.hash64(input, 1677, 496);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void testHash64ByteArrayEmptyThrowsException() {
        byte[] input = new byte[0];
        try {
            MurmurHash2.hash64(input, 2441);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void testHash64StringNullInputThrowsNPE_WithOffset() {
        try {
            MurmurHash2.hash64((String) null, -3577, -3577);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    // ================================================
    // Edge case tests
    // ================================================
    
    @Test(timeout = 4000)
    public void testHash64ByteArrayNegativeLength() {
        byte[] input = new byte[6];
        long result = MurmurHash2.hash64(input, 0, -66);
        assertEquals(2692789288766115115L, result);
    }

    @Test(timeout = 4000)
    public void testHash32ByteArrayNegativeSeed() {
        byte[] input = new byte[2];
        int result = MurmurHash2.hash32(input, -1564, 0);
        assertEquals(1307949917, result);
    }

    @Test(timeout = 4000)
    public void testHash32ByteArrayWithOffsetAndLength() {
        byte[] input = new byte[6];
        int result = MurmurHash2.hash32(input, 1, 615);
        assertEquals(1161250932, result);
    }

    @Test(timeout = 4000)
    public void testHash32EmptyByteArrayWithNegativeSeed() {
        byte[] input = new byte[0];
        int result = MurmurHash2.hash32(input, -1819289676);
        assertEquals(-563837603, result);
    }
}