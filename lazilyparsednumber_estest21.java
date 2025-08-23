package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the LazilyParsedNumber class.
 */
public class LazilyParsedNumberTest {

    /**
     * Tests the reflexive property of the equals() method.
     * According to the Java contract for Object.equals(), an object must be equal to itself.
     */
    @Test
    public void equals_shouldReturnTrue_whenComparingObjectToItself() {
        // Arrange: Create an instance of the class under test.
        // The principle of reflexivity should hold for any value, including this unusual one.
        LazilyParsedNumber number = new LazilyParsedNumber("...");

        // Assert: Verify that the object is equal to itself.
        // This is a more direct and idiomatic way to test for reflexivity (x.equals(x) == true).
        assertEquals(number, number);
    }
}