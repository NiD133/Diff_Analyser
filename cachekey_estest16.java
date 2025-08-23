package org.apache.ibatis.cache;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the CacheKey class.
 */
public class CacheKeyTest {

    /**
     * Verifies that the toString() method of a newly created CacheKey
     * returns a string representing its default initial state.
     * The expected format is "hashcode:checksum", which for a new key
     * defaults to "17:0".
     */
    @Test
    public void toStringShouldReturnInitialStateForNewCacheKey() {
        // Arrange: Create a new CacheKey without any updates.
        CacheKey emptyCacheKey = new CacheKey();
        String expectedRepresentation = "17:0";

        // Act: Get the string representation of the new key.
        String actualRepresentation = emptyCacheKey.toString();

        // Assert: Verify the string matches the expected initial state.
        assertEquals(expectedRepresentation, actualRepresentation);
    }
}