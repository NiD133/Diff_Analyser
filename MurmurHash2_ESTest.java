package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.codec.digest.MurmurHash2;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * Test suite for MurmurHash2 class.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class MurmurHash2_ESTest extends MurmurHash2_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testHash64WithStringAndSeed() {
        long hash = MurmurHash2.hash64("bPH \"XdK'x'8?hr", 4, 0);
        assertEquals(-7207201254813729732L, hash);
    }

    @Test(timeout = 4000)
    public void testHash64WithString() {
        long hash = MurmurHash2.hash64("q%DCbQXCHT4'G\"^L");
        assertEquals(3105811143660689330L, hash);
    }

    @Test(timeout = 4000)
    public void testHash32WithNullStringThrowsNullPointerException() {
        try {
            MurmurHash2.hash32((String) null, 1396, 467);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.codec.digest.MurmurHash2", e);
        }
    }

    @Test(timeout = 4000)
    public void testHash64WithEmptyByteArray() {
        byte[] byteArray = new byte[2];
        long hash = MurmurHash2.hash64(byteArray, 0, 0);
        assertEquals(0L, hash);
    }

    @Test(timeout = 4000)
    public void testHash64WithByteArrayAndSeed() {
        byte[] byteArray = new byte[5];
        byteArray[0] = (byte) 24;
        long hash = MurmurHash2.hash64(byteArray, 1);
        assertEquals(24027485454243747L, hash);
    }

    @Test(timeout = 4000)
    public void testHash64WithNullByteArrayReturnsDefaultHash() {
        long hash = MurmurHash2.hash64((byte[]) null, 0);
        assertEquals(-7207201254813729732L, hash);
    }

    @Test(timeout = 4000)
    public void testHash32WithEmptyByteArray() {
        byte[] byteArray = new byte[0];
        int hash = MurmurHash2.hash32(byteArray, 0, 0);
        assertEquals(0, hash);
    }

    @Test(timeout = 4000)
    public void testHash32WithByteArrayAndSeed() {
        byte[] byteArray = new byte[6];
        byteArray[0] = (byte) 18;
        int hash = MurmurHash2.hash32(byteArray, 2, -3970);
        assertEquals(-1628438012, hash);
    }

    @Test(timeout = 4000)
    public void testHash32WithByteArrayAndNegativeSeed() {
        byte[] byteArray = new byte[6];
        int hash = MurmurHash2.hash32(byteArray, -1756908916);
        assertEquals(0, hash);
    }

    @Test(timeout = 4000)
    public void testHash32WithNullByteArrayReturnsDefaultHash() {
        int hash = MurmurHash2.hash32((byte[]) null, 0);
        assertEquals(275646681, hash);
    }

    @Test(timeout = 4000)
    public void testHash32WithStringAndDefaultSeed() {
        int hash = MurmurHash2.hash32("Dpn ='f", 0, 0);
        assertEquals(275646681, hash);
    }

    @Test(timeout = 4000)
    public void testHash32WithEmptyString() {
        int hash = MurmurHash2.hash32("");
        assertEquals(275646681, hash);
    }

    @Test(timeout = 4000)
    public void testHash64WithNullByteArrayThrowsNullPointerException() {
        try {
            MurmurHash2.hash64((byte[]) null, -420);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.codec.digest.MurmurHash2", e);
        }
    }

    @Test(timeout = 4000)
    public void testHash64WithStringAndNegativeIndicesThrowsStringIndexOutOfBoundsException() {
        try {
            MurmurHash2.hash64(",;9b|Qi Zv", -2432, -2432);
            fail("Expected StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testHash64WithNullStringThrowsNullPointerException() {
        try {
            MurmurHash2.hash64((String) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.codec.digest.MurmurHash2", e);
        }
    }

    @Test(timeout = 4000)
    public void testHash32WithNullByteArrayAndSeedThrowsNullPointerException() {
        try {
            MurmurHash2.hash32((byte[]) null, 32);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.codec.digest.MurmurHash2", e);
        }
    }

    @Test(timeout = 4000)
    public void testHash32WithEmptyByteArrayAndSeedThrowsArrayIndexOutOfBoundsException() {
        byte[] byteArray = new byte[0];
        try {
            MurmurHash2.hash32(byteArray, 32);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.apache.commons.codec.digest.MurmurHash2", e);
        }
    }

    @Test(timeout = 4000)
    public void testHash32WithStringAndNegativeIndicesThrowsStringIndexOutOfBoundsException() {
        try {
            MurmurHash2.hash32(": ", 12, 12);
            fail("Expected StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testHash32WithNullStringThrowsNullPointerException() {
        try {
            MurmurHash2.hash32((String) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.codec.digest.MurmurHash2", e);
        }
    }

    @Test(timeout = 4000)
    public void testHash64WithByteArrayAndSeed() {
        byte[] byteArray = new byte[6];
        long hash = MurmurHash2.hash64(byteArray, 5, 56);
        assertEquals(-3113210640657759650L, hash);
    }

    @Test(timeout = 4000)
    public void testHash64WithByteArrayAndNegativeIndexThrowsArrayIndexOutOfBoundsException() {
        byte[] byteArray = new byte[6];
        try {
            MurmurHash2.hash64(byteArray, -14, 110);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.apache.commons.codec.digest.MurmurHash2", e);
        }
    }

    @Test(timeout = 4000)
    public void testHash64WithByteArrayAndNegativeIndicesThrowsArrayIndexOutOfBoundsException() {
        byte[] byteArray = new byte[1];
        try {
            MurmurHash2.hash64(byteArray, -26, -26);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.apache.commons.codec.digest.MurmurHash2", e);
        }
    }

    @Test(timeout = 4000)
    public void testHash64WithByteArrayAndNegativeStartIndexThrowsArrayIndexOutOfBoundsException() {
        byte[] byteArray = new byte[1];
        try {
            MurmurHash2.hash64(byteArray, -941, 0);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.apache.commons.codec.digest.MurmurHash2", e);
        }
    }

    @Test(timeout = 4000)
    public void testHash64WithByteArrayAndIndices() {
        byte[] byteArray = new byte[5];
        long hash = MurmurHash2.hash64(byteArray, 1, 1);
        assertEquals(-5720937396023583481L, hash);
    }

    @Test(timeout = 4000)
    public void testHash64WithNullByteArrayAndNegativeIndicesThrowsNullPointerException() {
        try {
            MurmurHash2.hash64((byte[]) null, -420, -420);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.codec.digest.MurmurHash2", e);
        }
    }

    @Test(timeout = 4000)
    public void testHash64WithByteArrayAndNegativeIndicesThrowsArrayIndexOutOfBoundsException() {
        byte[] byteArray = new byte[2];
        try {
            MurmurHash2.hash64(byteArray, -1, -1);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.apache.commons.codec.digest.MurmurHash2", e);
        }
    }

    @Test(timeout = 4000)
    public void testHash64WithByteArrayAndLargeIndicesThrowsArrayIndexOutOfBoundsException() {
        byte[] byteArray = new byte[7];
        try {
            MurmurHash2.hash64(byteArray, 1677, 496);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.apache.commons.codec.digest.MurmurHash2", e);
        }
    }

    @Test(timeout = 4000)
    public void testHash32WithByteArrayAndNegativeStartIndex() {
        byte[] byteArray = new byte[2];
        int hash = MurmurHash2.hash32(byteArray, -1564, 0);
        assertEquals(1307949917, hash);
    }

    @Test(timeout = 4000)
    public void testHash32WithByteArrayAndNegativeIndicesThrowsArrayIndexOutOfBoundsException() {
        byte[] byteArray = new byte[6];
        try {
            MurmurHash2.hash32(byteArray, -1, -652);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.apache.commons.codec.digest.MurmurHash2", e);
        }
    }

    @Test(timeout = 4000)
    public void testHash32WithByteArrayAndIndices() {
        byte[] byteArray = new byte[6];
        int hash = MurmurHash2.hash32(byteArray, 1, 615);
        assertEquals(1161250932, hash);
    }

    @Test(timeout = 4000)
    public void testHash32WithEmptyByteArrayAndLargeIndicesThrowsArrayIndexOutOfBoundsException() {
        byte[] byteArray = new byte[0];
        try {
            MurmurHash2.hash32(byteArray, 1834, 1834);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.apache.commons.codec.digest.MurmurHash2", e);
        }
    }

    @Test(timeout = 4000)
    public void testHash64WithEmptyByteArrayAndLargeIndexThrowsArrayIndexOutOfBoundsException() {
        byte[] byteArray = new byte[0];
        try {
            MurmurHash2.hash64(byteArray, 2441);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.apache.commons.codec.digest.MurmurHash2", e);
        }
    }

    @Test(timeout = 4000)
    public void testHash64WithByteArrayAndNegativeSeed() {
        byte[] byteArray = new byte[6];
        long hash = MurmurHash2.hash64(byteArray, 0, -66);
        assertEquals(2692789288766115115L, hash);
    }

    @Test(timeout = 4000)
    public void testHash64WithString() {
        long hash = MurmurHash2.hash64("}oZe|_r,wwn+'.Z");
        assertEquals(-823493256237211900L, hash);
    }

    @Test(timeout = 4000)
    public void testHash64WithStringAndIndices() {
        long hash = MurmurHash2.hash64("ylLM~55", 1, 1);
        assertEquals(4591197677584300775L, hash);
    }

    @Test(timeout = 4000)
    public void testHash32WithEmptyByteArrayAndNegativeSeed() {
        byte[] byteArray = new byte[0];
        int hash = MurmurHash2.hash32(byteArray, -1819289676);
        assertEquals(-563837603, hash);
    }

    @Test(timeout = 4000)
    public void testHash32WithNullByteArrayAndNegativeIndicesThrowsNullPointerException() {
        try {
            MurmurHash2.hash32((byte[]) null, -1278, -1278);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.codec.digest.MurmurHash2", e);
        }
    }

    @Test(timeout = 4000)
    public void testHash32WithStringAndIndices() {
        int hash = MurmurHash2.hash32("9chG_Yo[`m", 1, 1);
        assertEquals(-1877468854, hash);
    }

    @Test(timeout = 4000)
    public void testHash64WithNullStringAndNegativeIndicesThrowsNullPointerException() {
        try {
            MurmurHash2.hash64((String) null, -3577, -3577);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.codec.digest.MurmurHash2", e);
        }
    }

    @Test(timeout = 4000)
    public void testHash32WithString() {
        int hash = MurmurHash2.hash32("org.apache.commons.codec.binary.StringUtils");
        assertEquals(-1819289676, hash);
    }
}