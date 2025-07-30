package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class VectorTest {

    @Test(timeout = 4000)
    public void testDotProductWithSelf() {
        Vector vector = new Vector(-528.75F, -528.75F, 1.0F);
        float dotProduct = vector.dot(vector);
        assertEquals("Dot product of a vector with itself should be equal to the square of its length.", 559154.1F, dotProduct, 0.01F);
    }

    @Test(timeout = 4000)
    public void testDotProductWithZeroVector() {
        Vector zeroVector = new Vector(0.0F, 0.0F, 0.0F);
        Vector vector = new Vector(1103.0195F, 0.0F, 8.0F);
        float dotProduct = zeroVector.dot(vector);
        assertEquals("Dot product with a zero vector should be zero.", 0.0F, dotProduct, 0.01F);
        assertEquals("Length of the vector should be calculated correctly.", 1103.0486F, vector.length(), 0.01F);
    }

    @Test(timeout = 4000)
    public void testVectorSubtractionAndCrossProduct() {
        Vector zeroVector = new Vector(0.0F, 0.0F, 0.0F);
        Vector vector = new Vector(-1465.0F, -1465.0F, 1);
        Vector resultVector = zeroVector.subtract(vector);
        Matrix matrix = new Matrix(490.69257F, -667.658F, -1.0F, 0.0F, -1465.0F, 0.0F);
        Vector crossProduct = resultVector.cross(matrix);
        Vector scaledVector = crossProduct.multiply(8);
        assertEquals("Length of the scaled vector should be calculated correctly.", 9710969.0F, scaledVector.length(), 0.01F);
    }

    @Test(timeout = 4000)
    public void testCrossProductAndSubtraction() {
        Vector vector1 = new Vector(-2905.637F, -2905.637F, -1.0F);
        Vector vector2 = new Vector(0, -444.7289F, -3839.2217F);
        Vector crossProduct = vector1.cross(vector2);
        Vector resultVector = vector1.subtract(crossProduct);
        assertEquals("Length of the resulting vector should be calculated correctly.", 1.5828617E7F, resultVector.length(), 0.01F);
    }

    @Test(timeout = 4000)
    public void testNormalizationAndCrossProductWithMatrix() {
        Vector zeroVector = new Vector(0.0F, 0.0F, 0.0F);
        Vector normalizedVector = zeroVector.normalize();
        Matrix matrix = new Matrix(135.0858F, 0.0F, 2, 0, 0.0F, -1.0F);
        Vector crossProduct = normalizedVector.cross(matrix);
        assertTrue("Cross product of a zero vector with any matrix should be a zero vector.", crossProduct.equals(normalizedVector));
        assertEquals("Length of a zero vector should be zero.", 0.0F, zeroVector.length(), 0.01F);
    }

    @Test(timeout = 4000)
    public void testCrossProductWithMatrix() {
        Vector zeroVector = new Vector(0.0F, 0.0F, 0.0F);
        Matrix matrix = new Matrix(135.0858F, 0.0F, 2, 0, 0.0F, -1.0F);
        Vector crossProduct = zeroVector.cross(matrix);
        assertTrue("Cross product of a zero vector with any matrix should be a zero vector.", crossProduct.equals(zeroVector));
    }

    @Test(timeout = 4000)
    public void testScalarMultiplication() {
        Vector vector = new Vector(0.0F, 0.0F, 742.77F);
        Vector scaledVector = vector.multiply(0.0F);
        assertEquals("Length of a vector scaled by zero should be zero.", 0.0F, scaledVector.length(), 0.01F);
        assertEquals("Original vector length should remain unchanged.", 742.77F, vector.length(), 0.01F);
    }

    @Test(timeout = 4000)
    public void testLengthSquared() {
        Vector vector = new Vector(-1465.0F, -1465.0F, 1);
        float lengthSquared = vector.lengthSquared();
        assertEquals("Length squared should be calculated correctly.", 4292451.0F, lengthSquared, 0.01F);
    }

    @Test(timeout = 4000)
    public void testLength() {
        Vector vector = new Vector(-1465.0F, -1465.0F, 1);
        float length = vector.length();
        assertEquals("Length should be calculated correctly.", 2071.823F, length, 0.01F);
    }

    @Test(timeout = 4000)
    public void testVectorSubtraction() {
        Vector zeroVector = new Vector(0.0F, 0.0F, 0.0F);
        Vector vector = new Vector(-1465.0F, -1465.0F, 1);
        Vector resultVector = zeroVector.subtract(vector);
        float xCoordinate = resultVector.get(0);
        assertEquals("Length of the resulting vector should be calculated correctly.", 2071.823F, resultVector.length(), 0.01F);
        assertEquals("X coordinate should be equal to the negated value of the original vector.", 1465.0F, xCoordinate, 0.01F);
    }

    @Test(timeout = 4000)
    public void testGetCoordinate() {
        Vector vector = new Vector(1864.694F, -105.0F, -1351.098F);
        float yCoordinate = vector.get(1);
        assertEquals("Length squared should be calculated correctly.", 5313574.5F, vector.lengthSquared(), 0.01F);
        assertEquals("Y coordinate should be retrieved correctly.", -105.0F, yCoordinate, 0.01F);
    }

    @Test(timeout = 4000)
    public void testDotProductWithSelfForZAxisVector() {
        Vector vector = new Vector(0.0F, 0.0F, 742.77F);
        float dotProduct = vector.dot(vector);
        assertEquals("Dot product of a vector with itself should be equal to the square of its length.", 551707.3F, dotProduct, 0.01F);
    }

    @Test(timeout = 4000)
    public void testDotProductWithAnotherVector() {
        Vector vector1 = new Vector(0.0F, 0.0F, -1.0F);
        Vector vector2 = new Vector(0.0F, -1.0F, 2);
        float dotProduct = vector1.dot(vector2);
        assertEquals("Dot product should be calculated correctly.", -2.0F, dotProduct, 0.01F);
        assertEquals("Length squared of the second vector should be calculated correctly.", 5.0F, vector2.lengthSquared(), 0.01F);
    }

    @Test(timeout = 4000)
    public void testComplexCrossProductNormalization() {
        Vector vector1 = new Vector(-2905.637F, -2905.637F, -1.0F);
        Vector vector2 = new Vector(0, -444.7289F, -3839.2217F);
        Vector crossProduct1 = vector1.cross(vector2);
        Vector crossProduct2 = crossProduct1.cross(vector2);
        Vector crossProduct3 = crossProduct2.cross(vector1);
        Vector crossProduct4 = crossProduct3.cross(crossProduct2);
        Vector normalizedVector = crossProduct4.normalize();
        assertEquals("Normalized vector length should be zero.", 0.0F, normalizedVector.length(), 0.01F);
        assertEquals("Length squared of the third cross product should be calculated correctly.", 4.2085848E26F, crossProduct3.lengthSquared(), 0.01F);
    }

    @Test(timeout = 4000)
    public void testCrossProductWithDefaultMatrix() {
        Vector zeroVector = new Vector(0.0F, 0.0F, 0.0F);
        Matrix defaultMatrix = new Matrix();
        Vector crossProduct = zeroVector.cross(defaultMatrix);
        assertTrue("Cross product of a zero vector with a default matrix should be a zero vector.", crossProduct.equals(zeroVector));
    }

    @Test(timeout = 4000)
    public void testSubtractWithNullVector() {
        Vector vector = new Vector(-2350.2F, -2350.2F, -2350.2F);
        try {
            vector.subtract(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetWithInvalidIndex() {
        Vector vector = new Vector(0.0F, 0.0F, 0.0F);
        try {
            vector.get(360);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testDotProductWithNullVector() {
        Vector vector = new Vector(4, 3, 8);
        try {
            vector.dot(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testCrossProductWithNullVector() {
        Vector vector = new Vector(-557.00323F, -557.00323F, -557.00323F);
        try {
            vector.cross((Vector) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testCrossProductWithNullMatrix() {
        Vector vector = new Vector(-557.00323F, -557.00323F, -557.00323F);
        try {
            vector.cross((Matrix) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testZeroVectorLength() {
        Vector zeroVector = new Vector(0.0F, 0.0F, 0.0F);
        float length = zeroVector.length();
        assertEquals("Length of a zero vector should be zero.", 0.0F, length, 0.01F);
    }

    @Test(timeout = 4000)
    public void testZeroVectorLengthSquared() {
        Vector zeroVector = new Vector(0.0F, 0.0F, 0.0F);
        float lengthSquared = zeroVector.lengthSquared();
        assertEquals("Length squared of a zero vector should be zero.", 0.0F, lengthSquared, 0.01F);
    }

    @Test(timeout = 4000)
    public void testCrossProductWithMatrixAndEquality() {
        Vector vector = new Vector(0.0F, 0.0F, 3.3516045F);
        Matrix matrix = new Matrix(0.0F, 0);
        Vector crossProduct = vector.cross(matrix);
        assertTrue("Cross product of a vector with a matrix should result in a vector equal to the original vector.", vector.equals(crossProduct));
        assertEquals("Length squared should be calculated correctly.", 11.233253F, vector.lengthSquared(), 0.01F);
    }

    @Test(timeout = 4000)
    public void testVectorEqualityWithDifferentObject() {
        Vector vector = new Vector(0.0F, 0.0F, 0.0F);
        Object differentObject = new Object();
        assertFalse("Vector should not be equal to an object of a different type.", vector.equals(differentObject));
    }

    @Test(timeout = 4000)
    public void testVectorEqualityWithNull() {
        Vector vector = new Vector(-80.165F, -80.165F, -80.165F);
        assertFalse("Vector should not be equal to null.", vector.equals(null));
        assertEquals("Length squared should be calculated correctly.", 19279.281F, vector.lengthSquared(), 0.01F);
    }

    @Test(timeout = 4000)
    public void testVectorSelfEquality() {
        Vector vector = new Vector(0.0F, 0.0F, 0.0F);
        assertTrue("Vector should be equal to itself.", vector.equals(vector));
    }

    @Test(timeout = 4000)
    public void testVectorEqualityWithDifferentValues() {
        Vector vector1 = new Vector(0.0F, 0.0F, 3.3516045F);
        Vector vector2 = new Vector(0.0F, 0.0F, 1);
        assertFalse("Vectors with different values should not be equal.", vector1.equals(vector2));
        assertEquals("Length of the second vector should be calculated correctly.", 1.0F, vector2.length(), 0.01F);
    }

    @Test(timeout = 4000)
    public void testCrossProductWithSelf() {
        Vector vector = new Vector(-2905.637F, -2905.637F, -1.0F);
        Vector crossProduct = vector.cross(vector);
        assertEquals("Length of the original vector should be calculated correctly.", 4109.191F, vector.length(), 0.01F);
        assertEquals("Cross product of a vector with itself should be a zero vector.", 0.0F, crossProduct.length(), 0.01F);
    }

    @Test(timeout = 4000)
    public void testGetFirstCoordinate() {
        Vector vector = new Vector(0.0F, 0.0F, 0.0F);
        float xCoordinate = vector.get(0);
        assertEquals("X coordinate of a zero vector should be zero.", 0.0F, xCoordinate, 0.01F);
    }

    @Test(timeout = 4000)
    public void testSubtractWithSelf() {
        Vector vector = new Vector(-2905.637F, -2905.637F, -1.0F);
        Vector resultVector = vector.subtract(vector);
        assertEquals("Length squared of the original vector should be calculated correctly.", 1.6885452E7F, vector.lengthSquared(), 0.01F);
        assertEquals("Subtracting a vector from itself should result in a zero vector.", 0.0F, resultVector.length(), 0.01F);
    }

    @Test(timeout = 4000)
    public void testHashCode() {
        Vector vector = new Vector(-2905.637F, -2905.637F, -1.0F);
        vector.hashCode();
        assertEquals("Length of the vector should be calculated correctly.", 4109.191F, vector.length(), 0.01F);
    }

    @Test(timeout = 4000)
    public void testToString() {
        Vector vector = new Vector(-2905.637F, -2905.637F, -1.0F);
        String vectorString = vector.toString();
        assertEquals("String representation of the vector should be formatted correctly.", "-2905.637,-2905.637,-1.0", vectorString);
    }
}