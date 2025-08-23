package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Vector} class.
 */
public class VectorTest {

    /**
     * Tests that a Vector instance is equal to itself.
     * <p>
     * This test case verifies the reflexive property of the {@code equals()} method,
     * which states that for any non-null reference value x, {@code x.equals(x)}
     * must return true.
     */
    @Test
    public void equals_shouldReturnTrue_whenComparingVectorToItself() {
        // Arrange: Create a vector with arbitrary coordinates.
        Vector vector = new Vector(1.23f, 4.56f, 7.89f);

        // Act & Assert: An object must be equal to itself.
        // assertEquals uses the .equals() method for comparison and is the
        // standard way to assert object equality in JUnit.
        assertEquals("A vector instance should be equal to itself.", vector, vector);
    }
}