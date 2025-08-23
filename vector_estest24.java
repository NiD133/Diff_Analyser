package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Contains unit tests for the {@link Vector} class.
 */
public class VectorTest {

    /**
     * Verifies that the equals() method returns false when a Vector is
     * compared with an object of an incompatible type. This is a standard
     * requirement of the equals() contract.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparedWithIncompatibleType() {
        // Arrange
        Vector vector = new Vector(1.0f, 2.0f, 3.0f);
        Object incompatibleObject = new Object();

        // Act
        boolean areEqual = vector.equals(incompatibleObject);

        // Assert
        assertFalse("Vector.equals() must return false when compared to an object of a different type.", areEqual);
    }
}