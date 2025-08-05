package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.codec.digest.XXHash32;

/**
 * Test suite for XXHash32 hash algorithm implementation.
 * Tests cover basic functionality, edge cases, and error conditions.
 */
public class XXHash32Test {

    // Expected hash values for common test cases
    private static final long DEFAULT_SEED_EMPTY_HASH = 46947589L;
    private static final long CUSTOM_SEED_97_EMPTY_HASH = 3659767818L;
    private static final long SINGLE_INT_2026_HASH = 968812856L;
    private static final long MULTIPLE_INTS_SEQUENCE_HASH = 1429036944L;

    @Test
    public void testDefaultConstructor_EmptyHash() {
        // Given: A new XXHash32 instance with default seed (0)
        XXHash32 hasher = new XXHash32();
        
        // When: Getting hash value without any updates
        long actualHash = hasher.getValue();
        
        // Then: Should return the expected empty hash for default seed
        assertEquals("Empty hash with default seed should match expected value", 
                     DEFAULT_SEED_EMPTY_HASH, actualHash);
    }

    @Test
    public void testCustomSeedConstructor() {
        // Given: A new XXHash32 instance with custom seed
        int customSeed = 97;
        XXHash32 hasher = new XXHash32(customSeed);
        
        // When: Getting hash value without any updates
        long actualHash = hasher.getValue();
        
        // Then: Should return the expected empty hash for custom seed
        assertEquals("Empty hash with custom seed should match expected value", 
                     CUSTOM_SEED_97_EMPTY_HASH, actualHash);
    }

    @Test
    public void testUpdateSingleInteger() {
        // Given: A new XXHash32 instance
        XXHash32 hasher = new XXHash32();
        
        // When: Updating with a single integer value
        int testValue = 2026;
        hasher.update(testValue);
        
        // Then: Hash should match expected value
        assertEquals("Hash of single integer should match expected value", 
                     SINGLE_INT_2026_HASH, hasher.getValue());
    }

    @Test
    public void testUpdateMultipleIntegers() {
        // Given: A new XXHash32 instance
        XXHash32 hasher = new XXHash32();
        
        // When: Updating with multiple integer values in sequence
        hasher.update(2);
        hasher.update(2);
        hasher.update(0);
        hasher.update(8);
        
        // Then: Hash should match expected value for the sequence
        assertEquals("Hash of integer sequence should match expected value", 
                     MULTIPLE_INTS_SEQUENCE_HASH, hasher.getValue());
    }

    @Test
    public void testUpdateByteArray_BasicCase() {
        // Given: A new XXHash32 instance and a byte array with some data
        XXHash32 hasher = new XXHash32();
        byte[] data = new byte[25];
        data[3] = 16; // Add some non-zero data
        
        // When: Updating with byte array
        hasher.update(data, 0, 24);
        
        // Then: Hash should be computed correctly
        assertEquals("Hash of byte array should match expected value", 
                     281612550L, hasher.getValue());
    }

    @Test
    public void testUpdateByteArray_EmptyLength() {
        // Given: A new XXHash32 instance and a byte array
        XXHash32 hasher = new XXHash32();
        byte[] data = new byte[8];
        
        // When: Updating with zero length
        hasher.update(data, 0, 0);
        
        // Then: Hash should remain as empty hash
        assertEquals("Hash should remain unchanged when updating with zero length", 
                     DEFAULT_SEED_EMPTY_HASH, hasher.getValue());
    }

    @Test
    public void testUpdateByteArray_MultipleUpdates() {
        // Given: A new XXHash32 instance and a byte array with data
        XXHash32 hasher = new XXHash32();
        byte[] data = new byte[25];
        data[1] = 16;
        
        // When: Performing multiple updates on the same data
        hasher.update(data, 16, 4);  // Update from offset 16, length 4
        hasher.update(data, 1, 4);   // Update from offset 1, length 4  
        hasher.update(data, 0, 16);  // Update from offset 0, length 16
        
        // Then: Hash should reflect all updates
        assertEquals("Hash should reflect cumulative updates", 
                     1465785993L, hasher.getValue());
    }

    @Test
    public void testUpdateByteArray_LargerData() {
        // Given: A new XXHash32 instance and a larger byte array
        XXHash32 hasher = new XXHash32();
        byte[] data = new byte[62];
        
        // When: Updating with a portion of the larger array
        hasher.update(data, 21, 21);
        
        // Then: Hash should be computed correctly
        assertEquals("Hash of larger byte array portion should match expected value", 
                     86206869L, hasher.getValue());
    }

    @Test
    public void testReset() {
        // Given: A XXHash32 instance that has been updated
        XXHash32 hasher = new XXHash32();
        hasher.update(12345); // Add some data
        
        // When: Resetting the hasher
        hasher.reset();
        
        // Then: Hash should return to initial empty state
        assertEquals("Hash should return to initial state after reset", 
                     DEFAULT_SEED_EMPTY_HASH, hasher.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateByteArray_NullArray() {
        // Given: A new XXHash32 instance
        XXHash32 hasher = new XXHash32();
        
        // When: Attempting to update with null byte array
        // Then: Should throw NullPointerException
        hasher.update(null, 0, 10);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testUpdateByteArray_InvalidOffset() {
        // Given: A new XXHash32 instance and a small byte array
        XXHash32 hasher = new XXHash32();
        byte[] data = new byte[25];
        
        // When: Attempting to update with offset beyond array bounds
        // Then: Should throw ArrayIndexOutOfBoundsException
        hasher.update(data, 374761393, 16);
    }

    @Test
    public void testUpdateByteArray_EdgeCaseValues() {
        // Given: A new XXHash32 instance
        XXHash32 hasher = new XXHash32();
        byte[] data = new byte[25];
        
        // When: Updating with very large offset and length values that cause overflow
        // Note: This tests the internal handling of large values
        hasher.update(data, 1336530510, 1336530510);
        
        // Then: Should handle the edge case and produce expected hash
        assertEquals("Should handle edge case values correctly", 
                     2363252416L, hasher.getValue());
    }

    @Test
    public void testUpdateByteArray_NegativeLength() {
        // Given: A new XXHash32 instance and a byte array
        XXHash32 hasher = new XXHash32();
        byte[] data = new byte[8];
        
        // When: Updating with negative length (edge case)
        hasher.update(data, 1989, -24);
        
        // Then: Should handle negative length appropriately
        assertEquals("Should handle negative length appropriately", 
                     46947589L, hasher.getValue());
    }
}