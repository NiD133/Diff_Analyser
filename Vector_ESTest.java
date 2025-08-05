package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.parser.Matrix;
import com.itextpdf.text.pdf.parser.Vector;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * A readable and maintainable test suite for the {@link Vector} class.
 */
public class VectorTest {

    /**
     * A small tolerance for floating-point comparisons.
     */
    private static final float DELTA = 0.001f;

    // --- Constructor and Getter Tests ---

    @Test
    public void constructor_shouldSetVectorComponentsCorrectly() {
        // Arrange
        float x = 1.0f, y = 2.0f, z = 3.0f;

        // Act
        Vector vector = new Vector(x, y, z);

        // Assert
        assertEquals("X component should be set correctly", x, vector.get(Vector.I1), DELTA);
        assertEquals("Y component should be set correctly", y, vector.get(Vector.I2), DELTA);
        assertEquals("Z component should be set correctly", z, vector.get(Vector.I3), DELTA);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void get_withInvalidIndex_shouldThrowException() {
        // Arrange
        Vector vector = new Vector(1, 2, 3);

        // Act
        vector.get(3); // Invalid index
    }

    // --- Mathematical Operation Tests ---

    @Test
    public void dot_withSelf_shouldReturnLengthSquared() {
        // Arrange
        Vector vector = new Vector(2.0f, 3.0f, 4.0f);
        float expectedLengthSquared = 2 * 2 + 3 * 3 + 4 * 4; // 4 + 9 + 16 = 29

        // Act
        float dotProduct = vector.dot(vector);

        // Assert
        assertEquals(expectedLengthSquared, dotProduct, DELTA);
        assertEquals(vector.lengthSquared(), dotProduct, DELTA);
    }

    @Test
    public void dot_withZeroVector_shouldReturnZero() {
        // Arrange
        Vector vector = new Vector(123.4f, 567.8f, 9.0f);
        Vector zeroVector = new Vector(0, 0, 0);

        // Act
        float dotProduct = vector.dot(zeroVector);

        // Assert
        assertEquals(0.0f, dotProduct, DELTA);
    }
    
    @Test
    public void dot_withGeneralVectors_shouldReturnCorrectValue() {
        // Arrange
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(4, -5, 6);
        // Dot product = (1*4) + (2*-5) + (3*6) = 4 - 10 + 18 = 12
        float expectedDotProduct = 12.0f;

        // Act
        float actualDotProduct = v1.dot(v2);

        // Assert
        assertEquals(expectedDotProduct, actualDotProduct, DELTA);
    }

    @Test(expected = NullPointerException.class)
    public void dot_withNull_shouldThrowException() {
        new Vector(1, 1, 1).dot(null);
    }

    @Test
    public void subtract_fromSelf_shouldReturnZeroVector() {
        // Arrange
        Vector vector = new Vector(10.5f, -20.0f, 30.1f);
        Vector zeroVector = new Vector(0, 0, 0);

        // Act
        Vector result = vector.subtract(vector);

        // Assert
        assertEquals(zeroVector, result);
    }

    @Test
    public void subtract_zeroVector_shouldReturnOriginalVector() {
        // Arrange
        Vector vector = new Vector(10.5f, -20.0f, 30.1f);
        Vector zeroVector = new Vector(0, 0, 0);

        // Act
        Vector result = vector.subtract(zeroVector);

        // Assert
        assertEquals(vector, result);
    }

    @Test(expected = NullPointerException.class)
    public void subtract_withNull_shouldThrowException() {
        new Vector(1, 1, 1).subtract(null);
    }

    @Test
    public void crossWithVector_withSelf_shouldReturnZeroVector() {
        // Arrange
        Vector vector = new Vector(15.0f, 25.0f, 35.0f);
        Vector zeroVector = new Vector(0, 0, 0);

        // Act
        Vector result = vector.cross(vector);

        // Assert
        assertEquals(zeroVector, result);
    }

    @Test
    public void crossWithVector_withStandardBasisVectors_shouldFollowRightHandRule() {
        // Arrange
        Vector i = new Vector(1, 0, 0);
        Vector j = new Vector(0, 1, 0);
        Vector k = new Vector(0, 0, 1);

        // Act & Assert
        assertEquals("i x j should equal k", k, i.cross(j));
        assertEquals("j x k should equal i", i, j.cross(k));
        assertEquals("k x i should equal j", j, k.cross(i));
    }

    @Test(expected = NullPointerException.class)
    public void crossWithVector_withNull_shouldThrowException() {
        new Vector(1, 1, 1).cross((Vector) null);
    }

    @Test
    public void crossWithMatrix_withIdentityMatrix_shouldReturnOriginalVector() {
        // Arrange
        Vector vector = new Vector(5, 10, 1);
        Matrix identityMatrix = new Matrix(); // Default constructor creates an identity matrix

        // Act
        Vector result = vector.cross(identityMatrix);

        // Assert
        assertEquals(vector, result);
    }

    @Test(expected = NullPointerException.class)
    public void crossWithMatrix_withNull_shouldThrowException() {
        new Vector(1, 1, 1).cross((Matrix) null);
    }

    @Test
    public void multiply_byScalar_shouldScaleVectorCorrectly() {
        // Arrange
        Vector vector = new Vector(2, -3, 4);
        float scalar = 5.0f;
        Vector expected = new Vector(10, -15, 20);

        // Act
        Vector result = vector.multiply(scalar);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void multiply_byZero_shouldReturnZeroVector() {
        // Arrange
        Vector vector = new Vector(123.4f, 567.8f, 9.0f);
        Vector zeroVector = new Vector(0, 0, 0);

        // Act
        Vector result = vector.multiply(0.0f);

        // Assert
        assertEquals(zeroVector, result);
    }

    // --- Magnitude and Normalization Tests ---

    @Test
    public void length_shouldReturnCorrectMagnitude() {
        // Arrange
        Vector vector = new Vector(3, 4, 0); // A simple Pythagorean triple
        float expectedLength = 5.0f;

        // Act
        float actualLength = vector.length();

        // Assert
        assertEquals(expectedLength, actualLength, DELTA);
    }

    @Test
    public void lengthSquared_shouldReturnCorrectSquareOfMagnitude() {
        // Arrange
        Vector vector = new Vector(3, 4, 0);
        float expectedLengthSquared = 25.0f; // 3*3 + 4*4 + 0*0

        // Act
        float actualLengthSquared = vector.lengthSquared();

        // Assert
        assertEquals(expectedLengthSquared, actualLengthSquared, DELTA);
    }

    @Test
    public void length_ofZeroVector_shouldBeZero() {
        // Arrange
        Vector zeroVector = new Vector(0, 0, 0);

        // Act & Assert
        assertEquals(0.0f, zeroVector.length(), DELTA);
        assertEquals(0.0f, zeroVector.lengthSquared(), DELTA);
    }

    @Test
    public void normalize_shouldProduceUnitVector() {
        // Arrange
        Vector vector = new Vector(3, -4, 0);
        Vector expected = new Vector(0.6f, -0.8f, 0.0f);

        // Act
        Vector normalized = vector.normalize();

        // Assert
        assertEquals(1.0f, normalized.length(), DELTA);
        assertEquals(expected.get(Vector.I1), normalized.get(Vector.I1), DELTA);
        assertEquals(expected.get(Vector.I2), normalized.get(Vector.I2), DELTA);
        assertEquals(expected.get(Vector.I3), normalized.get(Vector.I3), DELTA);
    }

    @Test
    public void normalize_zeroVector_shouldReturnZeroVector() {
        // Arrange
        Vector zeroVector = new Vector(0, 0, 0);

        // Act
        Vector result = zeroVector.normalize();

        // Assert
        assertEquals("Normalizing a zero vector should result in a zero vector", zeroVector, result);
    }

    // --- Object Method Tests (equals, hashCode, toString) ---

    @Test
    public void equals_withSameInstance_shouldReturnTrue() {
        Vector v1 = new Vector(1, 2, 3);
        assertTrue("A vector should be equal to itself", v1.equals(v1));
    }

    @Test
    public void equals_withEqualVectors_shouldReturnTrue() {
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(1, 2, 3);
        assertTrue("Vectors with the same component values should be equal", v1.equals(v2));
    }

    @Test
    public void equals_withDifferentVectors_shouldReturnFalse() {
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(3, 2, 1);
        assertFalse("Vectors with different component values should not be equal", v1.equals(v2));
    }

    @Test
    public void equals_withNull_shouldReturnFalse() {
        Vector v1 = new Vector(1, 2, 3);
        assertFalse("A vector should not be equal to null", v1.equals(null));
    }

    @Test
    public void equals_withDifferentObjectType_shouldReturnFalse() {
        Vector v1 = new Vector(1, 2, 3);
        Object other = new Object();
        assertFalse("A vector should not be equal to an object of a different type", v1.equals(other));
    }

    @Test
    public void hashCode_forEqualVectors_shouldBeEqual() {
        // Arrange
        Vector v1 = new Vector(1.2f, 3.4f, 5.6f);
        Vector v2 = new Vector(1.2f, 3.4f, 5.6f);

        // Assert
        assertEquals("Equal vectors must have equal hash codes", v1.hashCode(), v2.hashCode());
    }

    @Test
    public void toString_shouldReturnCorrectFormat() {
        // Arrange
        Vector vector = new Vector(1.5f, -2.0f, 3.123f);
        String expected = "1.5,-2.0,3.123";

        // Act
        String actual = vector.toString();

        // Assert
        assertEquals(expected, actual);
    }
}