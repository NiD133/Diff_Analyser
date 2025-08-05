package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.codec.digest.MurmurHash2;

/**
 * Test suite for MurmurHash2 hash function implementation.
 * Tests both 32-bit and 64-bit hash variants with various input scenarios.
 */
public class MurmurHash2Test {

    // Expected hash values for validation
    private static final long EXPECTED_HASH_64_COMPLEX_STRING = -7207201254813729732L;
    private static final long EXPECTED_HASH_64_SIMPLE_STRING = 3105811143660689330L;
    private static final int EXPECTED_HASH_32_EMPTY_INPUT = 275646681;
    private static final int EXPECTED_HASH_32_LONG_STRING = -1819289676;

    // Test data
    private static final String COMPLEX_TEST_STRING = "bPH \"XdK'x'8?hr";
    private static final String SIMPLE_TEST_STRING = "q%DCbQXCHT4'G\"^L";
    private static final String LONG_TEST_STRING = "org.apache.commons.codec.binary.StringUtils";

    // ========== 64-bit Hash Tests ==========

    @Test
    public void testHash64_withStringAndCustomSeedAndLength_returnsExpectedHash() {
        long actualHash = MurmurHash2.hash64(COMPLEX_TEST_STRING, 4, 0);
        assertEquals("Hash64 with custom seed and length should match expected value", 
                     EXPECTED_HASH_64_COMPLEX_STRING, actualHash);
    }

    @Test
    public void testHash64_withStringDefaultSeed_returnsExpectedHash() {
        long actualHash = MurmurHash2.hash64(SIMPLE_TEST_STRING);
        assertEquals("Hash64 with default seed should match expected value", 
                     EXPECTED_HASH_64_SIMPLE_STRING, actualHash);
    }

    @Test
    public void testHash64_withEmptyByteArray_returnsZero() {
        byte[] emptyArray = new byte[2];
        long actualHash = MurmurHash2.hash64(emptyArray, 0, 0);
        assertEquals("Hash64 of empty byte array should return 0", 0L, actualHash);
    }

    @Test
    public void testHash64_withSingleByteArray_returnsExpectedHash() {
        byte[] singleByteArray = new byte[5];
        singleByteArray[0] = (byte) 24;
        long actualHash = MurmurHash2.hash64(singleByteArray, 1);
        assertEquals("Hash64 of single byte should return expected value", 
                     24027485454243747L, actualHash);
    }

    @Test
    public void testHash64_withNullByteArray_returnsDefaultHash() {
        long actualHash = MurmurHash2.hash64((byte[]) null, 0);
        assertEquals("Hash64 of null byte array should return default hash", 
                     EXPECTED_HASH_64_COMPLEX_STRING, actualHash);
    }

    @Test
    public void testHash64_withByteArrayAndCustomParameters_returnsExpectedHash() {
        byte[] testArray = new byte[6];
        long actualHash = MurmurHash2.hash64(testArray, 5, 56);
        assertEquals("Hash64 with custom parameters should return expected value", 
                     -3113210640657759650L, actualHash);
    }

    @Test
    public void testHash64_withSubstring_returnsExpectedHash() {
        long actualHash = MurmurHash2.hash64("ylLM~55", 1, 1);
        assertEquals("Hash64 of substring should return expected value", 
                     4591197677584300775L, actualHash);
    }

    @Test
    public void testHash64_withLongString_returnsExpectedHash() {
        long actualHash = MurmurHash2.hash64("}oZe|_r,wwn+'.Z");
        assertEquals("Hash64 of long string should return expected value", 
                     -823493256237211900L, actualHash);
    }

    // ========== 32-bit Hash Tests ==========

    @Test
    public void testHash32_withEmptyByteArray_returnsZero() {
        byte[] emptyArray = new byte[0];
        int actualHash = MurmurHash2.hash32(emptyArray, 0, 0);
        assertEquals("Hash32 of empty byte array should return 0", 0, actualHash);
    }

    @Test
    public void testHash32_withByteArrayAndCustomSeed_returnsExpectedHash() {
        byte[] testArray = new byte[6];
        testArray[0] = (byte) 18;
        int actualHash = MurmurHash2.hash32(testArray, 2, -3970);
        assertEquals("Hash32 with custom seed should return expected value", 
                     -1628438012, actualHash);
    }

    @Test
    public void testHash32_withNullByteArray_returnsDefaultHash() {
        int actualHash = MurmurHash2.hash32((byte[]) null, 0);
        assertEquals("Hash32 of null byte array should return default hash", 
                     EXPECTED_HASH_32_EMPTY_INPUT, actualHash);
    }

    @Test
    public void testHash32_withEmptyString_returnsDefaultHash() {
        int actualHash = MurmurHash2.hash32("");
        assertEquals("Hash32 of empty string should return default hash", 
                     EXPECTED_HASH_32_EMPTY_INPUT, actualHash);
    }

    @Test
    public void testHash32_withStringAndZeroParameters_returnsDefaultHash() {
        int actualHash = MurmurHash2.hash32("Dpn ='f", 0, 0);
        assertEquals("Hash32 with zero parameters should return default hash", 
                     EXPECTED_HASH_32_EMPTY_INPUT, actualHash);
    }

    @Test
    public void testHash32_withSubstring_returnsExpectedHash() {
        int actualHash = MurmurHash2.hash32("9chG_Yo[`m", 1, 1);
        assertEquals("Hash32 of substring should return expected value", 
                     -1877468854, actualHash);
    }

    @Test
    public void testHash32_withLongString_returnsExpectedHash() {
        int actualHash = MurmurHash2.hash32(LONG_TEST_STRING);
        assertEquals("Hash32 of long string should return expected value", 
                     EXPECTED_HASH_32_LONG_STRING, actualHash);
    }

    @Test
    public void testHash32_withByteArrayAndVariousParameters_returnsExpectedHashes() {
        byte[] testArray = new byte[6];
        
        // Test with negative seed
        int hash1 = MurmurHash2.hash32(testArray, -1756908916);
        assertEquals("Hash32 with negative seed should return 0", 0, hash1);
        
        // Test with positive parameters
        int hash2 = MurmurHash2.hash32(testArray, 1, 615);
        assertEquals("Hash32 with positive parameters should return expected value", 
                     1161250932, hash2);
        
        // Test with negative length parameter
        byte[] smallArray = new byte[2];
        int hash3 = MurmurHash2.hash32(smallArray, -1564, 0);
        assertEquals("Hash32 with negative length should return expected value", 
                     1307949917, hash3);
    }

    @Test
    public void testHash64_withVariousParameters_returnsExpectedHashes() {
        byte[] testArray = new byte[5];
        
        // Test with specific offset and length
        long hash1 = MurmurHash2.hash64(testArray, 1, 1);
        assertEquals("Hash64 with offset and length should return expected value", 
                     -5720937396023583481L, hash1);
        
        // Test with negative length
        byte[] largerArray = new byte[6];
        long hash2 = MurmurHash2.hash64(largerArray, 0, -66);
        assertEquals("Hash64 with negative length should return expected value", 
                     2692789288766115115L, hash2);
        
        // Test with empty array and negative seed
        byte[] emptyArray = new byte[0];
        long hash3 = MurmurHash2.hash64(emptyArray, -1819289676);
        assertEquals("Hash64 with empty array and negative seed should return expected value", 
                     -563837603, hash3);
    }

    // ========== Exception Tests ==========

    @Test(expected = NullPointerException.class)
    public void testHash32_withNullString_throwsNullPointerException() {
        MurmurHash2.hash32((String) null, 1396, 467);
    }

    @Test(expected = NullPointerException.class)
    public void testHash64_withNullByteArrayAndNegativeLength_throwsNullPointerException() {
        MurmurHash2.hash64((byte[]) null, -420);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testHash64_withInvalidStringIndices_throwsStringIndexOutOfBoundsException() {
        MurmurHash2.hash64(",;9b|Qi Zv", -2432, -2432);
    }

    @Test(expected = NullPointerException.class)
    public void testHash64_withNullString_throwsNullPointerException() {
        MurmurHash2.hash64((String) null);
    }

    @Test(expected = NullPointerException.class)
    public void testHash32_withNullByteArrayAndPositiveLength_throwsNullPointerException() {
        MurmurHash2.hash32((byte[]) null, 32);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testHash32_withEmptyArrayAndPositiveLength_throwsArrayIndexOutOfBoundsException() {
        byte[] emptyArray = new byte[0];
        MurmurHash2.hash32(emptyArray, 32);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testHash32_withInvalidStringIndices_throwsStringIndexOutOfBoundsException() {
        MurmurHash2.hash32(": ", 12, 12);
    }

    @Test(expected = NullPointerException.class)
    public void testHash32_withNullString_throwsNullPointerException() {
        MurmurHash2.hash32((String) null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testHash64_withNegativeOffset_throwsArrayIndexOutOfBoundsException() {
        byte[] testArray = new byte[6];
        MurmurHash2.hash64(testArray, -14, 110);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testHash64_withInvalidArrayAccess_throwsArrayIndexOutOfBoundsException() {
        byte[] testArray = new byte[7];
        MurmurHash2.hash64(testArray, 1677, 496);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testHash32_withNegativeOffset_throwsArrayIndexOutOfBoundsException() {
        byte[] testArray = new byte[6];
        MurmurHash2.hash32(testArray, -1, -652);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testHash64_withEmptyArrayAndPositiveLength_throwsArrayIndexOutOfBoundsException() {
        byte[] emptyArray = new byte[0];
        MurmurHash2.hash64(emptyArray, 2441);
    }
}