package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

// Note: The class name and base class are preserved from the original auto-generated test.
// A more conventional name would be JsonLocationTest.
public class JsonLocation_ESTestTest4 extends JsonLocation_ESTest_scaffolding {

    /**
     * Tests that the equals() method correctly identifies two JsonLocation objects
     * as non-equal when their column numbers differ, while all other properties are the same.
     */
    @Test
    public void testEqualsReturnsFalseWhenColumnNumbersDiffer() {
        // Arrange: Create two JsonLocation instances that are identical except for the column number.
        ContentReference source = ContentReference.unknown();
        long byteOffset = 100L;
        long charOffset = 100L;
        int line = 10;

        JsonLocation locationBase = new JsonLocation(source, byteOffset, charOffset, line, 20);
        JsonLocation locationWithDifferentColumn = new JsonLocation(source, byteOffset, charOffset, line, 99);

        // Act & Assert: The two locations should not be considered equal because their
        // column numbers are different.
        assertNotEquals(locationBase, locationWithDifferentColumn);
    }
}