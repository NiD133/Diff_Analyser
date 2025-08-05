/*
 * Test suite for IEEE754rUtils - IEEE-754r compliant min/max operations
 * Tests cover all method overloads for both float and double types
 */

package org.apache.commons.lang3.math;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.lang3.math.IEEE754rUtils;

public class IEEE754rUtilsTest {

    // ========== MIN OPERATION TESTS ==========

    @Test
    public void testMin_ThreeFloats_ReturnsSmallest() {
        float result = IEEE754rUtils.min(0.0F, 629.0559F, -161.0F);
        assertEquals(-161.0F, result, 0.01F);
    }

    @Test
    public void testMin_FloatArray_ReturnsSmallestValue() {
        float[] values = {1.0F, 1.0F, 825.0F, 1.0F, 1.0F, 825.0F};
        
        float result = IEEE754rUtils.min(values);
        
        assertEquals(1.0F, result, 0.01F);
    }

    @Test
    public void testMin_SingleElementFloatArray_ReturnsThatElement() {
        float[] values = {-1.0F};
        
        float result = IEEE754rUtils.min(values);
        
        assertEquals(-1.0F, result, 0.01F);
    }

    @Test
    public void testMin_SingleElementDoubleArray_ReturnsThatElement() {
        double[] values = {1.0};
        
        double result = IEEE754rUtils.min(values);
        
        assertEquals(1.0, result, 0.01);
    }

    @Test
    public void testMin_DoubleArrayWithNegativeValue_ReturnsNegativeValue() {
        double[] values = {0.0, -567.84087, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        
        double result = IEEE754rUtils.min(values);
        
        assertEquals(-567.84087, result, 0.01);
    }

    @Test
    public void testMin_ThreeIdenticalFloats_ReturnsTheValue() {
        float result = IEEE754rUtils.min(4616.2134F, 4616.2134F, 4616.2134F);
        assertEquals(4616.2134F, result, 0.01F);
    }

    @Test
    public void testMin_TwoZeroFloats_ReturnsZero() {
        float result = IEEE754rUtils.min(0.0F, 0.0F);
        assertEquals(0.0F, result, 0.01F);
    }

    @Test
    public void testMin_TwoIdenticalNegativeFloats_ReturnsNegativeValue() {
        float result = IEEE754rUtils.min(-513.9F, -513.9F);
        assertEquals(-513.9F, result, 0.01F);
    }

    @Test
    public void testMin_ThreeIdenticalDoubles_ReturnsTheValue() {
        double result = IEEE754rUtils.min(1.0, 1.0, 1.0);
        assertEquals(1.0, result, 0.01);
    }

    @Test
    public void testMin_ThreeDoubles_ReturnsSmallest() {
        double result = IEEE754rUtils.min(1660.66, -567.84087, 4173.8887585);
        assertEquals(-567.84087, result, 0.01);
    }

    @Test
    public void testMin_TwoIdenticalDoubles_ReturnsTheValue() {
        double result = IEEE754rUtils.min(2855.35762973, 2855.35762973);
        assertEquals(2855.35762973, result, 0.01);
    }

    @Test
    public void testMin_NegativeAndZeroDouble_ReturnsNegative() {
        double result = IEEE754rUtils.min(-855.02919, 0.0);
        assertEquals(-855.02919, result, 0.01);
    }

    // ========== MAX OPERATION TESTS ==========

    @Test
    public void testMax_FloatArrayWithPositiveValue_ReturnsPositiveValue() {
        float[] values = {0.0F, 0.0F, 981.74023F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F};
        
        float result = IEEE754rUtils.max(values);
        
        assertEquals(981.74023F, result, 0.01F);
    }

    @Test
    public void testMax_FloatArrayWithAllNegativeValues_ReturnsLeastNegative() {
        float[] values = {-835.94F, -1742.84F, -1.0F};
        
        float result = IEEE754rUtils.max(values);
        
        assertEquals(-1.0F, result, 0.01F);
    }

    @Test
    public void testMax_DoubleArrayWithPositiveValue_ReturnsPositiveValue() {
        double[] values = {0.0, 1769.7924036104557, 0.0, 0.0};
        
        double result = IEEE754rUtils.max(values);
        
        assertEquals(1769.7924036104557, result, 0.01);
    }

    @Test
    public void testMax_DoubleArrayWithAllNegativeValues_ReturnsLeastNegative() {
        double[] values = {-1742.84, -163.0, -835.94, -1.0, -835.94, -1742.84, -1.0};
        
        double result = IEEE754rUtils.max(values);
        
        assertEquals(-1.0, result, 0.01);
    }

    @Test
    public void testMax_ThreeFloatsWithPositiveValue_ReturnsPositive() {
        float result = IEEE754rUtils.max(-1.0F, 0.0F, -2806.0F);
        assertEquals(0.0F, result, 0.01F);
    }

    @Test
    public void testMax_ThreeIdenticalNegativeFloats_ReturnsTheValue() {
        float result = IEEE754rUtils.max(-659.8F, -659.8F, -659.8F);
        assertEquals(-659.8F, result, 0.01F);
    }

    @Test
    public void testMax_TwoFloats_ReturnsLarger() {
        float result = IEEE754rUtils.max(1.0F, 1190.8F);
        assertEquals(1190.8F, result, 0.01F);
    }

    @Test
    public void testMax_TwoIdenticalNegativeFloats_ReturnsTheValue() {
        float result = IEEE754rUtils.max(-2157.9656F, -2157.9656F);
        assertEquals(-2157.9656F, result, 0.01F);
    }

    @Test
    public void testMax_ThreeDoublesWithZeroAndNegative_ReturnsZero() {
        double result = IEEE754rUtils.max(0.0, -1416.7961547236, 0.0);
        assertEquals(0.0, result, 0.01);
    }

    @Test
    public void testMax_ThreeIdenticalDoubles_ReturnsTheValue() {
        double result = IEEE754rUtils.max(1.0, 1.0, 1.0);
        assertEquals(1.0, result, 0.01);
    }

    @Test
    public void testMax_TwoDoubles_ReturnsLarger() {
        double result = IEEE754rUtils.max(-4398.39854599338, 564.128287262);
        assertEquals(564.128287262, result, 0.01);
    }

    @Test
    public void testMax_TwoNegativeDoubles_ReturnsLessNegative() {
        double result = IEEE754rUtils.max(-3055.536570341673, -1.0);
        assertEquals(-1.0, result, 0.01);
    }

    // ========== ERROR CONDITION TESTS ==========

    @Test(expected = NullPointerException.class)
    public void testMin_NullFloatArray_ThrowsNullPointerException() {
        IEEE754rUtils.min((float[]) null);
    }

    @Test(expected = NullPointerException.class)
    public void testMin_NullDoubleArray_ThrowsNullPointerException() {
        IEEE754rUtils.min((double[]) null);
    }

    @Test(expected = NullPointerException.class)
    public void testMax_NullFloatArray_ThrowsNullPointerException() {
        IEEE754rUtils.max((float[]) null);
    }

    @Test(expected = NullPointerException.class)
    public void testMax_NullDoubleArray_ThrowsNullPointerException() {
        IEEE754rUtils.max((double[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMin_EmptyFloatArray_ThrowsIllegalArgumentException() {
        float[] emptyArray = new float[0];
        IEEE754rUtils.min(emptyArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMin_EmptyDoubleArray_ThrowsIllegalArgumentException() {
        double[] emptyArray = new double[0];
        IEEE754rUtils.min(emptyArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMax_EmptyFloatArray_ThrowsIllegalArgumentException() {
        float[] emptyArray = new float[0];
        IEEE754rUtils.max(emptyArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMax_EmptyDoubleArray_ThrowsIllegalArgumentException() {
        double[] emptyArray = new double[0];
        IEEE754rUtils.max(emptyArray);
    }

    // ========== EDGE CASE TESTS ==========

    @Test
    public void testMin_ArrayWithAllZeros_ReturnsZero() {
        float[] zeros = new float[2]; // defaults to 0.0F
        float result = IEEE754rUtils.min(zeros);
        assertEquals(0.0F, result, 0.01F);
    }

    @Test
    public void testMax_ArrayWithAllZeros_ReturnsZero() {
        float[] zeros = new float[3]; // defaults to 0.0F
        float result = IEEE754rUtils.max(zeros);
        assertEquals(0.0F, result, 0.01F);
    }

    @Test
    public void testConstructor_CanBeInstantiated() {
        // Test deprecated constructor for completeness
        IEEE754rUtils utils = new IEEE754rUtils();
        assertNotNull(utils);
    }
}