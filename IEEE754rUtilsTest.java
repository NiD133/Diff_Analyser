package org.apache.commons.lang3.math;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link IEEE754rUtils}.
 *
 * These tests focus on two aspects:
 * - Input validation: null and empty arrays must throw the documented exceptions.
 * - IEEE-754r behavior with NaN: NaN is returned only if all inputs are NaN; otherwise, NaN values are ignored.
 */
class IEEE754rUtilsTest extends AbstractLangTest {

    private static final double DELTA_D = 0.01;
    private static final float DELTA_F = 0.01f;

    @Test
    @DisplayName("Public constructor exists for binary compatibility")
    void constructorExistsForBinaryCompatibility() {
        new IEEE754rUtils();
    }

    @Test
    @DisplayName("Null and empty inputs throw the documented exceptions (double)")
    void nullAndEmptyInputsThrow_double() {
        // min
        assertThrows(NullPointerException.class, () -> IEEE754rUtils.min((double[]) null));
        assertThrows(IllegalArgumentException.class, () -> IEEE754rUtils.min(new double[0]));

        // max
        assertThrows(NullPointerException.class, () -> IEEE754rUtils.max((double[]) null));
        assertThrows(IllegalArgumentException.class, () -> IEEE754rUtils.max(new double[0]));
    }

    @Test
    @DisplayName("Null and empty inputs throw the documented exceptions (float)")
    void nullAndEmptyInputsThrow_float() {
        // min
        assertThrows(NullPointerException.class, () -> IEEE754rUtils.min((float[]) null));
        assertThrows(IllegalArgumentException.class, () -> IEEE754rUtils.min(new float[0]));

        // max
        assertThrows(NullPointerException.class, () -> IEEE754rUtils.max((float[]) null));
        assertThrows(IllegalArgumentException.class, () -> IEEE754rUtils.max(new float[0]));
    }

    @Test
    @DisplayName("IEEE-754r: NaN is ignored unless all inputs are NaN (3-arg overloads) [LANG-381]")
    void threeArgOverloads_ignoreNaNsUnlessAllNaN_LANG381() {
        // double
        assertEquals(1.2, IEEE754rUtils.min(1.2, 2.5, Double.NaN), DELTA_D);
        assertEquals(2.5, IEEE754rUtils.max(1.2, 2.5, Double.NaN), DELTA_D);
        assertTrue(Double.isNaN(IEEE754rUtils.max(Double.NaN, Double.NaN, Double.NaN)));

        // float
        assertEquals(1.2f, IEEE754rUtils.min(1.2f, 2.5f, Float.NaN), DELTA_F);
        assertEquals(2.5f, IEEE754rUtils.max(1.2f, 2.5f, Float.NaN), DELTA_F);
        assertTrue(Float.isNaN(IEEE754rUtils.max(Float.NaN, Float.NaN, Float.NaN)));
    }

    @Test
    @DisplayName("IEEE-754r: NaN is ignored unless all inputs are NaN (2-arg overloads)")
    void twoArgOverloads_ignoreNaNsUnlessAllNaN() {
        // double
        assertEquals(1.0, IEEE754rUtils.min(Double.NaN, 1.0), DELTA_D);
        assertEquals(1.0, IEEE754rUtils.min(1.0, Double.NaN), DELTA_D);
        assertEquals(2.0, IEEE754rUtils.max(Double.NaN, 2.0), DELTA_D);
        assertEquals(2.0, IEEE754rUtils.max(2.0, Double.NaN), DELTA_D);
        assertTrue(Double.isNaN(IEEE754rUtils.min(Double.NaN, Double.NaN)));
        assertTrue(Double.isNaN(IEEE754rUtils.max(Double.NaN, Double.NaN)));

        // float
        assertEquals(1.0f, IEEE754rUtils.min(Float.NaN, 1.0f), DELTA_F);
        assertEquals(1.0f, IEEE754rUtils.min(1.0f, Float.NaN), DELTA_F);
        assertEquals(2.0f, IEEE754rUtils.max(Float.NaN, 2.0f), DELTA_F);
        assertEquals(2.0f, IEEE754rUtils.max(2.0f, Float.NaN), DELTA_F);
        assertTrue(Float.isNaN(IEEE754rUtils.min(Float.NaN, Float.NaN)));
        assertTrue(Float.isNaN(IEEE754rUtils.max(Float.NaN, Float.NaN)));
    }

    @Test
    @DisplayName("Array overloads: ignore NaNs unless all values are NaN [LANG-381]")
    void arrayOverloads_ignoreNaNsUnlessAllNaN_LANG381() {
        // double arrays
        final double[] doublesWithMiddleNaNs = { 1.2, Double.NaN, 3.7, 27.0, 42.0, Double.NaN };
        assertEquals(42.0, IEEE754rUtils.max(doublesWithMiddleNaNs), DELTA_D);
        assertEquals(1.2, IEEE754rUtils.min(doublesWithMiddleNaNs), DELTA_D);

        final double[] doublesWithEdgeNaNs = { Double.NaN, 1.2, Double.NaN, 3.7, 27.0, 42.0, Double.NaN };
        assertEquals(42.0, IEEE754rUtils.max(doublesWithEdgeNaNs), DELTA_D);
        assertEquals(1.2, IEEE754rUtils.min(doublesWithEdgeNaNs), DELTA_D);

        // float arrays
        final float[] floatsWithMiddleNaNs = { 1.2f, Float.NaN, 3.7f, 27.0f, 42.0f, Float.NaN };
        assertEquals(1.2f, IEEE754rUtils.min(floatsWithMiddleNaNs), DELTA_F);
        assertEquals(42.0f, IEEE754rUtils.max(floatsWithMiddleNaNs), DELTA_F);

        final float[] floatsWithEdgeNaNs = { Float.NaN, 1.2f, Float.NaN, 3.7f, 27.0f, 42.0f, Float.NaN };
        assertEquals(1.2f, IEEE754rUtils.min(floatsWithEdgeNaNs), DELTA_F);
        assertEquals(42.0f, IEEE754rUtils.max(floatsWithEdgeNaNs), DELTA_F);
    }
}