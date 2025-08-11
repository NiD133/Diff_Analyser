package com.itextpdf.text.pdf.parser;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for Vector cross(Matrix) behavior.
 *
 * Notes on Matrix(a, b, c, d, e, f):
 *   | a  c  e |
 *   | b  d  f |
 *   | 0  0  1 |
 *
 * The cross product here effectively applies the affine transform to the vector:
 *   x' = a*x + c*y + e*z
 *   y' = b*x + d*y + f*z
 *   z' = z  (z is preserved)
 */
public class VectorTest {

    private static final float EPS = 0.0001f;

    @Test
    public void cross_withAffineMatrix_multipliesVectorAndPreservesZ() {
        // Arrange (Given)
        final Vector vector = new Vector(2, 3, 4);
        final Matrix transform = new Matrix(5, 6, 7, 8, 9, 10);

        // Act (When)
        final Vector result = vector.cross(transform);

        // Assert (Then)
        // Expected:
        // x' = 5*2 + 7*3 + 9*4 = 10 + 21 + 36 = 67
        // y' = 6*2 + 8*3 + 10*4 = 12 + 24 + 40 = 76
        // z' = 4
        assertEquals(67f, result.get(Vector.I1), EPS);
        assertEquals(76f, result.get(Vector.I2), EPS);
        assertEquals(4f,  result.get(Vector.I3), EPS);
    }

    @Test
    public void cross_withIdentityMatrix_returnsSameVector() {
        // Arrange
        final Vector original = new Vector(12.5f, -3.75f, 1f);
        final Matrix identity = new Matrix(1, 0, 0, 1, 0, 0);

        // Act
        final Vector result = original.cross(identity);

        // Assert
        assertEquals(original.get(Vector.I1), result.get(Vector.I1), EPS);
        assertEquals(original.get(Vector.I2), result.get(Vector.I2), EPS);
        assertEquals(original.get(Vector.I3), result.get(Vector.I3), EPS);
    }

    @Test
    public void cross_withTranslation_whenZIsOne_appliesOffsetToXY() {
        // Arrange
        final Vector point = new Vector(10f, 20f, 1f);
        final float dx = 3f;
        final float dy = -7f;
        final Matrix translation = new Matrix(1, 0, 0, 1, dx, dy);

        // Act
        final Vector result = point.cross(translation);

        // Assert
        assertEquals(10f + dx, result.get(Vector.I1), EPS);
        assertEquals(20f + dy, result.get(Vector.I2), EPS);
        assertEquals(1f,        result.get(Vector.I3), EPS);
    }
}