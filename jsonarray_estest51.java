package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonArrayTest {

    @Test
    public void containsShouldReturnFalseWhenCheckingForADeepCopyOfItself() {
        // Arrange
        // Create a parent JsonArray that contains an empty child JsonArray: [[]]
        JsonArray childArray = new JsonArray();
        JsonArray parentArray = new JsonArray();
        parentArray.add(childArray);

        // Act
        // Create a deep copy, which will be structurally identical to the parent array.
        JsonArray deepCopyOfParent = parentArray.deepCopy();

        // Assert
        // First, confirm the deep copy is equal to the original, as expected.
        assertEquals("A deep copy should be equal to the original array.", parentArray, deepCopyOfParent);
        
        // Also confirm the parent array is not equal to its own child element.
        assertNotEquals("The parent array should not be equal to its child element.", parentArray, childArray);

        // The main assertion: The 'contains' method checks for an *element* that equals
        // the argument. The parent array's only element is the childArray (`[]`), which is
        // not equal to the deep copy (`[[]]`). Therefore, contains should return false.
        assertFalse(
            "An array should not report containing a deep copy of itself.",
            parentArray.contains(deepCopyOfParent)
        );
    }
}