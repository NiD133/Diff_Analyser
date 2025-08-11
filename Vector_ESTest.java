package com.itextpdf.text.pdf.parser;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Human-friendly tests for Vector.
 *
 * Goals:
 * - Use descriptive test names
 * - Keep numbers simple and intent-focused
 * - Cover key behaviors and edge cases
 * - Avoid EvoSuite/EvoRunner scaffolding and magic values
 */
public class VectorTest {

    private static final float EPS = 1.0e-4f;

    private static void assertVectorEquals(Vector v, float x, float y, float z) {
        assertEquals(x, v.get(Vector.I1), EPS);
        assertEquals(y, v.get(Vector.I2), EPS);
        assertEquals(z, v.get(Vector.I3), EPS);
    }

    @Test
    public void dot_withSelf_equalsLengthSquared() {
        Vector v = new Vector(3.0f, 4.0f, 0.0f);

        float dot = v.dot(v);
        float len2 = v.lengthSquared();
        float len = v.length();

        assertEquals(len2, dot, EPS);
        assertEquals(25.0f, len2, EPS);
        assertEquals(5.0f, len, EPS);
    }

    @Test
    public void dot_ofOrthogonalVectors_isZero() {
        Vector x = new Vector(1.0f, 0.0f, 0.0f);
        Vector y = new Vector(0.0f, 1.0f, 0.0f);

        assertEquals(0.0f, x.dot(y), EPS);
    }

    @Test
    public void multiply_byZero_yieldsZeroVector() {
        Vector v = new Vector(0.0f, 0.0f, 742.77f);

        Vector scaled = v.multiply(0.0f);

        assertEquals(0.0f, scaled.length(), EPS);
        assertEquals(742.77f, v.length(), EPS); // original is unchanged
    }

    @Test
    public void subtract_self_yieldsZero_and_originMinusV_hasNegatedCoordinates() {
        Vector v = new Vector(2.0f, -5.0f, 1.0f);
        Vector origin = new Vector(0.0f, 0.0f, 0.0f);

        Vector zero = v.subtract(v);
        Vector negated = origin.subtract(v);

        assertVectorEquals(zero, 0.0f, 0.0f, 0.0f);
        assertVectorEquals(negated, -2.0f, 5.0f, -1.0f);
    }

    @Test
    public void cross_withSelf_returnsZeroVector() {
        Vector v = new Vector(-2.0f, 3.0f, 1.0f);

        Vector cross = v.cross(v);

        assertVectorEquals(cross, 0.0f, 0.0f, 0.0f);
    }

    @Test
    public void normalize_zeroVector_returnsZeroVector() {
        Vector zero = new Vector(0.0f, 0.0f, 0.0f);

        Vector normalized = zero.normalize();

        assertVectorEquals(normalized, 0.0f, 0.0f, 0.0f);
        assertEquals(0.0f, normalized.length(), EPS);
    }

    @Test
    public void equals_hashCode_contract_and_basicCases() {
        Vector a = new Vector(1.0f, 2.0f, 3.0f);
        Vector b = new Vector(1.0f, 2.0f, 3.0f);
        Vector c = new Vector(1.0f, 2.0f, 4.0f);

        // reflexive
        assertTrue(a.equals(a));

        // symmetric and consistent with hashCode
        assertTrue(a.equals(b));
        assertTrue(b.equals(a));
        assertEquals(a.hashCode(), b.hashCode());

        // different value -> not equal
        assertFalse(a.equals(c));

        // null and different type
        assertFalse(a.equals(null));
        assertFalse(a.equals("not a vector"));
    }

    @Test
    public void get_returnsComponents_and_outOfBoundsThrows() {
        Vector v = new Vector(10.0f, 20.0f, 30.0f);

        assertEquals(10.0f, v.get(Vector.I1), EPS);
        assertEquals(20.0f, v.get(Vector.I2), EPS);
        assertEquals(30.0f, v.get(Vector.I3), EPS);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void get_withInvalidIndex_throws() {
        Vector v = new Vector(0.0f, 0.0f, 0.0f);
        v.get(360);
    }

    @Test
    public void toString_printsCommaSeparatedXYZ() {
        Vector v = new Vector(1.0f, 2.0f, 3.0f);
        assertEquals("1.0,2.0,3.0", v.toString());
    }

    @Test
    public void cross_withMatrix_keepsZeroVectorUnchanged() {
        Vector zero = new Vector(0.0f, 0.0f, 0.0f);
        Matrix any = new Matrix(); // default matrix

        Vector crossed = zero.cross(any);

        assertVectorEquals(crossed, 0.0f, 0.0f, 0.0f);
    }

    @Test(expected = NullPointerException.class)
    public void subtract_withNull_throws() {
        Vector v = new Vector(1.0f, 1.0f, 1.0f);
        v.subtract(null);
    }

    @Test(expected = NullPointerException.class)
    public void dot_withNull_throws() {
        Vector v = new Vector(1.0f, 0.0f, 0.0f);
        v.dot(null);
    }

    @Test(expected = NullPointerException.class)
    public void cross_withNullVector_throws() {
        Vector v = new Vector(1.0f, 0.0f, 0.0f);
        v.cross((Vector) null);
    }

    @Test(expected = NullPointerException.class)
    public void cross_withNullMatrix_throws() {
        Vector v = new Vector(1.0f, 0.0f, 0.0f);
        v.cross((Matrix) null);
    }

    @Test
    public void lengthSquared_matches_length_times_length() {
        Vector v = new Vector(-6.0f, 8.0f, 0.0f);

        float len = v.length();
        float len2 = v.lengthSquared();

        assertEquals(len * len, len2, EPS);
        assertEquals(100.0f, len2, EPS);
    }
}