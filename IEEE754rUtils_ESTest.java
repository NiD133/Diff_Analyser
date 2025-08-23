package org.apache.commons.lang3.math;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Readable, behavior-focused tests for IEEE754rUtils.
 *
 * Test organization:
 * - min/max for float[] and double[] (varargs)
 * - min/max for 2 and 3 arguments
 * - Exceptional cases (null/empty arrays)
 * - Constructor availability
 */
public class IEEE754rUtilsTest {

    // ------------------------
    // min(...) - float varargs
    // ------------------------

    @Test
    public void minFloatArray_returnsSmallestValue() {
        float[] values = {1.0F, 1.0F, 825.0F, 1.0F, 1.0F, 825.0F};
        assertEquals(1.0F, IEEE754rUtils.min(values), 1e-6F);
    }

    @Test
    public void minFloatArray_singleElement_returnsThatElement() {
        float[] values = {-1.0F};
        assertEquals(-1.0F, IEEE754rUtils.min(values), 1e-6F);
    }

    @Test
    public void minFloatArray_withNegatives_returnsMostNegative() {
        float[] values = {-835.94F, -1742.84F, -1.0F};
        assertEquals(-1742.84F, IEEE754rUtils.min(values), 1e-6F);
    }

    // -------------------------
    // min(...) - double varargs
    // -------------------------

    @Test
    public void minDoubleArray_returnsSmallestValue() {
        double[] values = new double[8]; // all zeros
        values[1] = -567.84087;
        assertEquals(-567.84087, IEEE754rUtils.min(values), 1e-12);
    }

    @Test
    public void minDoubleArray_singleElement_returnsThatElement() {
        double[] values = {1.0};
        assertEquals(1.0, IEEE754rUtils.min(values), 1e-12);
    }

    // ------------------------
    // max(...) - float varargs
    // ------------------------

    @Test
    public void maxFloatArray_returnsLargestValue() {
        float[] values = new float[9]; // all zeros
        values[2] = 981.74023F;
        assertEquals(981.74023F, IEEE754rUtils.max(values), 1e-6F);
    }

    @Test
    public void maxFloatArray_withOnlyNegatives_returnsLeastNegative() {
        float[] values = {-835.94F, -1742.84F, -1.0F};
        assertEquals(-1.0F, IEEE754rUtils.max(values), 1e-6F);
    }

    @Test
    public void maxFloatArray_allZeros_returnsZero() {
        float[] values = new float[3];
        assertEquals(0.0F, IEEE754rUtils.max(values), 1e-6F);
    }

    // -------------------------
    // max(...) - double varargs
    // -------------------------

    @Test
    public void maxDoubleArray_returnsLargestValue() {
        double[] values = new double[4]; // all zeros
        values[1] = 1769.7924036104557;
        assertEquals(1769.7924036104557, IEEE754rUtils.max(values), 1e-12);
    }

    @Test
    public void maxDoubleArray_withOnlyNegatives_returnsLeastNegative() {
        double[] values = {
            -1742.84, -163.0, -835.94, -1.0, -835.94, -1742.84, -1.0
        };
        assertEquals(-1.0, IEEE754rUtils.max(values), 1e-12);
    }

    @Test
    public void maxDoubleArray_allZeros_returnsZero() {
        double[] values = new double[4];
        assertEquals(0.0, IEEE754rUtils.max(values), 1e-12);
    }

    // ------------------------
    // min(...) - 2 arguments
    // ------------------------

    @Test
    public void minFloat_twoEqualValues_returnsThatValue() {
        assertEquals(4616.2134F, IEEE754rUtils.min(4616.2134F, 4616.2134F), 1e-6F);
    }

    @Test
    public void minFloat_twoZeros_returnsZero() {
        assertEquals(0.0F, IEEE754rUtils.min(0.0F, 0.0F), 1e-6F);
    }

    @Test
    public void minFloat_twoEqualNegatives_returnsThatValue() {
        assertEquals(-513.9F, IEEE754rUtils.min(-513.9F, -513.9F), 1e-6F);
    }

    @Test
    public void minDouble_mixedPositiveAndZero_returnsZero() {
        assertEquals(0.0, IEEE754rUtils.min(0.0, 1460.933541), 1e-12);
    }

    @Test
    public void minDouble_negativeAndZero_returnsNegative() {
        assertEquals(-855.02919, IEEE754rUtils.min(-855.02919, 0.0), 1e-12);
    }

    // ------------------------
    // min(...) - 3 arguments
    // ------------------------

    @Test
    public void minFloat_threeValues_returnsSmallest() {
        assertEquals(-161.0F, IEEE754rUtils.min(0.0F, 629.0559F, -161.0F), 1e-6F);
    }

    @Test
    public void minFloat_threeEqualValues_returnsThatValue() {
        assertEquals(4616.2134F, IEEE754rUtils.min(4616.2134F, 4616.2134F, 4616.2134F), 1e-6F);
    }

    @Test
    public void minFloat_threeZeros_returnsZero() {
        assertEquals(0.0F, IEEE754rUtils.min(0.0F, 0.0F, 0.0F), 1e-6F);
    }

    @Test
    public void minDouble_threeValues_returnsSmallest() {
        assertEquals(-567.84087, IEEE754rUtils.min(1660.66, -567.84087, 4173.8887585), 1e-12);
    }

    @Test
    public void minDouble_threeEqualValues_returnsThatValue() {
        assertEquals(1.0, IEEE754rUtils.min(1.0, 1.0, 1.0), 1e-12);
    }

    @Test
    public void minDouble_withZeros_returnsZero() {
        assertEquals(0.0, IEEE754rUtils.min(976.5, 0.0, 0.0), 1e-12);
    }

    // ------------------------
    // max(...) - 2 arguments
    // ------------------------

    @Test
    public void maxFloat_twoValues_returnsLargest() {
        assertEquals(1190.8F, IEEE754rUtils.max(1.0F, 1190.8F), 1e-6F);
    }

    @Test
    public void maxFloat_twoEqualNegatives_returnsThatValue() {
        assertEquals(-2157.9656F, IEEE754rUtils.max(-2157.9656F, -2157.9656F), 1e-6F);
    }

    @Test
    public void maxFloat_twoZeros_returnsZero() {
        assertEquals(0.0F, IEEE754rUtils.max(0.0F, 0.0F), 1e-6F);
    }

    @Test
    public void maxDouble_twoValues_returnsLargest() {
        assertEquals(564.128287262, IEEE754rUtils.max(-4398.39854599338, 564.128287262), 1e-12);
    }

    @Test
    public void maxDouble_negativeVsLessNegative_returnsLessNegative() {
        assertEquals(-1.0, IEEE754rUtils.max(-3055.536570341673, -1.0), 1e-12);
    }

    @Test
    public void maxDouble_twoZeros_returnsZero() {
        assertEquals(0.0, IEEE754rUtils.max(0.0, 0.0), 1e-12);
    }

    // ------------------------
    // max(...) - 3 arguments
    // ------------------------

    @Test
    public void maxFloat_threeValues_returnsLargest() {
        assertEquals(2780.809F, IEEE754rUtils.max(2162.2F, 0.0F, 2780.809F), 1e-6F);
    }

    @Test
    public void maxFloat_threeEqualValues_returnsThatValue() {
        assertEquals(-659.8F, IEEE754rUtils.max(-659.8F, -659.8F, -659.8F), 1e-6F);
    }

    @Test
    public void maxFloat_includingZero_returnsZeroIfLargest() {
        assertEquals(0.0F, IEEE754rUtils.max(-1.0F, 0.0F, -2806.0F), 1e-6F);
    }

    @Test
    public void maxDouble_threeValues_returnsLargest() {
        assertEquals(0.0, IEEE754rUtils.max(0.0, -1416.7961547236, 0.0), 1e-12);
    }

    @Test
    public void maxDouble_threeEqualValues_returnsThatValue() {
        assertEquals(1.0, IEEE754rUtils.max(1.0, 1.0, 1.0), 1e-12);
    }

    @Test
    public void maxDouble_allNegative_returnsLeastNegative() {
        assertEquals(-1.0, IEEE754rUtils.max(-1.0, -1.0, -4660.4), 1e-12);
    }

    // ------------------------
    // Exceptional cases
    // ------------------------

    @Test
    public void minFloatArray_null_throwsNPE() {
        try {
            IEEE754rUtils.min((float[]) null);
            fail("Expected NullPointerException for null float array");
        } catch (NullPointerException expected) {
            // expected
        }
    }

    @Test
    public void minFloatArray_empty_throwsIAE() {
        try {
            IEEE754rUtils.min(new float[0]);
            fail("Expected IllegalArgumentException for empty float array");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    @Test
    public void minDoubleArray_null_throwsNPE() {
        try {
            IEEE754rUtils.min((double[]) null);
            fail("Expected NullPointerException for null double array");
        } catch (NullPointerException expected) {
            // expected
        }
    }

    @Test
    public void minDoubleArray_empty_throwsIAE() {
        try {
            IEEE754rUtils.min(new double[0]);
            fail("Expected IllegalArgumentException for empty double array");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    @Test
    public void maxFloatArray_null_throwsNPE() {
        try {
            IEEE754rUtils.max((float[]) null);
            fail("Expected NullPointerException for null float array");
        } catch (NullPointerException expected) {
            // expected
        }
    }

    @Test
    public void maxFloatArray_empty_throwsIAE() {
        try {
            IEEE754rUtils.max(new float[0]);
            fail("Expected IllegalArgumentException for empty float array");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    @Test
    public void maxDoubleArray_null_throwsNPE() {
        try {
            IEEE754rUtils.max((double[]) null);
            fail("Expected NullPointerException for null double array");
        } catch (NullPointerException expected) {
            // expected
        }
    }

    @Test
    public void maxDoubleArray_empty_throwsIAE() {
        try {
            IEEE754rUtils.max(new double[0]);
            fail("Expected IllegalArgumentException for empty double array");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    // ------------------------
    // Constructor
    // ------------------------

    @Test
    public void canInstantiateDeprecatedConstructor() {
        assertNotNull(new IEEE754rUtils());
    }
}