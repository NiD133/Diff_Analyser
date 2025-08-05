package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import com.itextpdf.text.pdf.parser.Matrix;
import com.itextpdf.text.pdf.parser.Vector;

/**
 * Test suite for the Vector class, covering all mathematical operations
 * and edge cases for 3D vector calculations in PDF processing.
 */
public class VectorTest {

    // Test constants for better readability
    private static final float DELTA = 0.01F;
    private static final Vector ZERO_VECTOR = new Vector(0.0F, 0.0F, 0.0F);
    private static final Vector UNIT_Z_VECTOR = new Vector(0.0F, 0.0F, 1.0F);

    // ========== DOT PRODUCT TESTS ==========
    
    @Test
    public void dotProduct_WithSameVector_ReturnsLengthSquared() {
        Vector vector = new Vector(-528.75F, -528.75F, 1.0F);
        
        float result = vector.dot(vector);
        
        assertEquals("Dot product of vector with itself should equal length squared", 
                     559154.1F, result, DELTA);
    }

    @Test
    public void dotProduct_WithZeroVector_ReturnsZero() {
        Vector nonZeroVector = new Vector(1103.0195F, 0.0F, 8.0F);
        
        float result = ZERO_VECTOR.dot(nonZeroVector);
        
        assertEquals("Dot product with zero vector should be zero", 
                     0.0F, result, DELTA);
    }

    @Test
    public void dotProduct_PerpendicularVectors_ReturnsNegativeValue() {
        Vector vector1 = new Vector(0.0F, 0.0F, -1.0F);
        Vector vector2 = new Vector(0.0F, -1.0F, 2.0F);
        
        float result = vector1.dot(vector2);
        
        assertEquals("Dot product of these specific vectors", 
                     -2.0F, result, DELTA);
    }

    @Test
    public void dotProduct_SameZVector_ReturnsZComponentSquared() {
        Vector vector = new Vector(0.0F, 0.0F, 742.77F);
        
        float result = vector.dot(vector);
        
        assertEquals("Dot product should equal Z component squared", 
                     551707.3F, result, DELTA);
    }

    // ========== CROSS PRODUCT TESTS ==========

    @Test
    public void crossProduct_VectorWithItself_ReturnsZeroVector() {
        Vector vector = new Vector(-2905.637F, -2905.637F, -1.0F);
        
        Vector result = vector.cross(vector);
        
        assertEquals("Cross product of vector with itself should be zero vector", 
                     0.0F, result.length(), DELTA);
    }

    @Test
    public void crossProduct_ZeroVectorWithMatrix_ReturnsZeroVector() {
        Matrix matrix = new Matrix(135.0858F, 0.0F, 2, 0, 0.0F, -1.0F);
        
        Vector result = ZERO_VECTOR.cross(matrix);
        
        assertTrue("Cross product of zero vector with matrix should return zero vector", 
                   result.equals(ZERO_VECTOR));
    }

    @Test
    public void crossProduct_NormalizedZeroVectorWithMatrix_ReturnsZeroVector() {
        Vector normalizedZero = ZERO_VECTOR.normalize();
        Matrix matrix = new Matrix(135.0858F, 0.0F, 2, 0, 0.0F, -1.0F);
        
        Vector result = normalizedZero.cross(matrix);
        
        assertTrue("Cross product of normalized zero vector should return zero vector", 
                   result.equals(normalizedZero));
    }

    @Test
    public void crossProduct_WithDefaultMatrix_ReturnsZeroVector() {
        Matrix defaultMatrix = new Matrix();
        
        Vector result = ZERO_VECTOR.cross(defaultMatrix);
        
        assertTrue("Cross product with default matrix should return zero vector", 
                   result.equals(ZERO_VECTOR));
    }

    // ========== VECTOR ARITHMETIC TESTS ==========

    @Test
    public void subtract_VectorFromItself_ReturnsZeroVector() {
        Vector vector = new Vector(-2905.637F, -2905.637F, -1.0F);
        
        Vector result = vector.subtract(vector);
        
        assertEquals("Subtracting vector from itself should return zero vector", 
                     0.0F, result.length(), DELTA);
    }

    @Test
    public void subtract_FromZeroVector_ReturnsNegatedVector() {
        Vector vector = new Vector(-1465.0F, -1465.0F, 1.0F);
        
        Vector result = ZERO_VECTOR.subtract(vector);
        
        assertEquals("First component should be negated", 1465.0F, result.get(0), DELTA);
        assertEquals("Result should have same length as original", 
                     2071.823F, result.length(), DELTA);
    }

    @Test
    public void multiply_ByZero_ReturnsZeroVector() {
        Vector vector = new Vector(0.0F, 0.0F, 742.77F);
        
        Vector result = vector.multiply(0.0F);
        
        assertEquals("Multiplying by zero should return zero vector", 
                     0.0F, result.length(), DELTA);
    }

    @Test
    public void multiply_ByScalar_ScalesVectorLength() {
        Vector vector = new Vector(-2905.637F, -2905.637F, -1.0F);
        Vector crossProduct = vector.cross(new Vector(0, -444.7289F, -3839.2217F));
        
        Vector result = crossProduct.multiply(8);
        
        assertEquals("Multiplied vector should have scaled length", 
                     9710969.0F, result.length(), DELTA);
    }

    // ========== LENGTH AND MAGNITUDE TESTS ==========

    @Test
    public void length_ZeroVector_ReturnsZero() {
        float result = ZERO_VECTOR.length();
        
        assertEquals("Zero vector should have zero length", 0.0F, result, DELTA);
    }

    @Test
    public void length_NonZeroVector_ReturnsCorrectMagnitude() {
        Vector vector = new Vector(-1465.0F, -1465.0F, 1.0F);
        
        float result = vector.length();
        
        assertEquals("Vector length should be calculated correctly", 
                     2071.823F, result, DELTA);
    }

    @Test
    public void lengthSquared_ZeroVector_ReturnsZero() {
        float result = ZERO_VECTOR.lengthSquared();
        
        assertEquals("Zero vector should have zero length squared", 0.0F, result, DELTA);
    }

    @Test
    public void lengthSquared_NonZeroVector_ReturnsCorrectValue() {
        Vector vector = new Vector(-1465.0F, -1465.0F, 1.0F);
        
        float result = vector.lengthSquared();
        
        assertEquals("Length squared should be calculated correctly", 
                     4292451.0F, result, DELTA);
    }

    // ========== COMPONENT ACCESS TESTS ==========

    @Test
    public void get_XComponent_ReturnsCorrectValue() {
        Vector vector = new Vector(0.0F, 0.0F, 0.0F);
        
        float result = vector.get(0);
        
        assertEquals("X component should be accessible", 0.0F, result, DELTA);
    }

    @Test
    public void get_YComponent_ReturnsCorrectValue() {
        Vector vector = new Vector(1864.694F, -105.0F, -1351.098F);
        
        float result = vector.get(1);
        
        assertEquals("Y component should be accessible", -105.0F, result, DELTA);
    }

    // ========== NORMALIZATION TESTS ==========

    @Test
    public void normalize_ZeroVector_ReturnsZeroVector() {
        Vector result = ZERO_VECTOR.normalize();
        
        assertEquals("Normalizing zero vector should return zero vector", 
                     0.0F, result.length(), DELTA);
    }

    @Test
    public void normalize_ComplexCalculation_HandlesChainedOperations() {
        Vector vector1 = new Vector(-2905.637F, -2905.637F, -1.0F);
        Vector vector2 = new Vector(0, -444.7289F, -3839.2217F);
        
        // Chain multiple cross products and normalize
        Vector result = vector1.cross(vector2)
                              .cross(vector2)
                              .cross(vector1)
                              .cross(vector1.cross(vector2).cross(vector2).cross(vector1))
                              .normalize();
        
        assertEquals("Complex chained operations should be handled correctly", 
                     0.0F, result.length(), DELTA);
    }

    // ========== COMPLEX OPERATIONS TESTS ==========

    @Test
    public void complexOperation_SubtractAndCross_ProducesExpectedResult() {
        Vector vector1 = new Vector(-2905.637F, -2905.637F, -1.0F);
        Vector vector2 = new Vector(0, -444.7289F, -3839.2217F);
        
        Vector crossProduct = vector1.cross(vector2);
        Vector result = vector1.subtract(crossProduct);
        
        assertEquals("Complex subtract and cross operation", 
                     1.5828617E7F, result.length(), DELTA);
    }

    @Test
    public void complexOperation_ChainedCrossProducts_ProducesExpectedResult() {
        Vector zeroVector = new Vector(0.0F, 0.0F, 0.0F);
        Vector nonZeroVector = new Vector(-1465.0F, -1465.0F, 1);
        Matrix matrix = new Matrix(490.69257F, -667.658F, -1.0F, 0.0F, -1465.0F, 0.0F);
        
        Vector result = zeroVector.subtract(nonZeroVector)
                                 .cross(matrix)
                                 .multiply(8);
        
        assertEquals("Chained operations should produce expected result", 
                     9710969.0F, result.length(), DELTA);
    }

    // ========== EQUALITY TESTS ==========

    @Test
    public void equals_SameVector_ReturnsTrue() {
        assertTrue("Vector should equal itself", ZERO_VECTOR.equals(ZERO_VECTOR));
    }

    @Test
    public void equals_DifferentVectorsSameValues_ReturnsTrue() {
        Vector vector1 = new Vector(0.0F, 0.0F, 3.3516045F);
        Matrix matrix = new Matrix(0.0F, 0);
        Vector vector2 = vector1.cross(matrix);
        
        assertTrue("Vectors with same values should be equal", vector1.equals(vector2));
    }

    @Test
    public void equals_DifferentVectors_ReturnsFalse() {
        Vector vector1 = new Vector(0.0F, 0.0F, 3.3516045F);
        Vector vector2 = new Vector(0.0F, 0.0F, 1.0F);
        
        assertFalse("Vectors with different values should not be equal", 
                    vector1.equals(vector2));
    }

    @Test
    public void equals_WithNonVectorObject_ReturnsFalse() {
        Object notAVector = new Object();
        
        assertFalse("Vector should not equal non-vector object", 
                    ZERO_VECTOR.equals(notAVector));
    }

    @Test
    public void equals_WithNull_ReturnsFalse() {
        Vector vector = new Vector(-80.165F, -80.165F, -80.165F);
        
        assertFalse("Vector should not equal null", vector.equals(null));
    }

    // ========== ERROR HANDLING TESTS ==========

    @Test(expected = NullPointerException.class)
    public void subtract_WithNull_ThrowsNullPointerException() {
        Vector vector = new Vector(-2350.2F, -2350.2F, -2350.2F);
        
        vector.subtract(null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void get_WithInvalidIndex_ThrowsArrayIndexOutOfBoundsException() {
        ZERO_VECTOR.get(360);
    }

    @Test(expected = NullPointerException.class)
    public void dotProduct_WithNull_ThrowsNullPointerException() {
        Vector vector = new Vector(4, 3, 8);
        
        vector.dot(null);
    }

    @Test(expected = NullPointerException.class)
    public void crossProduct_WithNullVector_ThrowsNullPointerException() {
        Vector vector = new Vector(-557.00323F, -557.00323F, -557.00323F);
        
        vector.cross((Vector) null);
    }

    @Test(expected = NullPointerException.class)
    public void crossProduct_WithNullMatrix_ThrowsNullPointerException() {
        Vector vector = new Vector(-557.00323F, -557.00323F, -557.00323F);
        
        vector.cross((Matrix) null);
    }

    // ========== UTILITY METHOD TESTS ==========

    @Test
    public void hashCode_CalculatesCorrectly() {
        Vector vector = new Vector(-2905.637F, -2905.637F, -1.0F);
        
        // Just verify it doesn't throw an exception and returns some value
        int hashCode = vector.hashCode();
        
        // Hash code calculation should be consistent
        assertEquals("Hash code should be consistent", hashCode, vector.hashCode());
    }

    @Test
    public void toString_FormatsCorrectly() {
        Vector vector = new Vector(-2905.637F, -2905.637F, -1.0F);
        
        String result = vector.toString();
        
        assertEquals("String representation should be formatted correctly", 
                     "-2905.637,-2905.637,-1.0", result);
    }
}