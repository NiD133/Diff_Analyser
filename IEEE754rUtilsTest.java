package org.apache.commons.lang3.math;

import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.apache.commons.lang3.LangAssertions.assertNullPointerException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.math.IEEE754rUtils}.
 */
class IEEE754rUtilsTest extends AbstractLangTest {

    private static final double DELTA = 0.01;
    private static final double[] DOUBLE_ARRAY_WITH_NAN = { 1.2, Double.NaN, 3.7, 27.0, 42.0, Double.NaN };
    private static final double[] DOUBLE_ARRAY_WITH_NAN_START = { Double.NaN, 1.2, Double.NaN, 3.7, 27.0, 42.0, Double.NaN };
    private static final float[] FLOAT_ARRAY_WITH_NAN = { 1.2f, Float.NaN, 3.7f, 27.0f, 42.0f, Float.NaN };
    private static final float[] FLOAT_ARRAY_WITH_NAN_START = { Float.NaN, 1.2f, Float.NaN, 3.7f, 27.0f, 42.0f, Float.NaN };

    @Test
    void testConstructorExists() {
        // Verify that the constructor exists and can be called
        new IEEE754rUtils();
    }

    @Test
    void testNullAndEmptyArrayExceptions() {
        // Verify that NullPointerException is thrown for null inputs
        assertNullPointerException(() -> IEEE754rUtils.min((float[]) null), "NullPointerException expected for null input");
        assertNullPointerException(() -> IEEE754rUtils.max((float[]) null), "NullPointerException expected for null input");
        assertNullPointerException(() -> IEEE754rUtils.min((double[]) null), "NullPointerException expected for null input");
        assertNullPointerException(() -> IEEE754rUtils.max((double[]) null), "NullPointerException expected for null input");

        // Verify that IllegalArgumentException is thrown for empty inputs
        assertIllegalArgumentException(IEEE754rUtils::min, "IllegalArgumentException expected for empty input");
        assertIllegalArgumentException(IEEE754rUtils::max, "IllegalArgumentException expected for empty input");
    }

    @Test
    void testMinMaxWithNaNValues() {
        // Verify min and max functions with NaN values for double
        assertEquals(1.2, IEEE754rUtils.min(1.2, 2.5, Double.NaN), DELTA);
        assertEquals(2.5, IEEE754rUtils.max(1.2, 2.5, Double.NaN), DELTA);
        assertTrue(Double.isNaN(IEEE754rUtils.max(Double.NaN, Double.NaN, Double.NaN)));

        // Verify min and max functions with NaN values for float
        assertEquals(1.2f, IEEE754rUtils.min(1.2f, 2.5f, Float.NaN), DELTA);
        assertEquals(2.5f, IEEE754rUtils.max(1.2f, 2.5f, Float.NaN), DELTA);
        assertTrue(Float.isNaN(IEEE754rUtils.max(Float.NaN, Float.NaN, Float.NaN)));
    }

    @Test
    void testMinMaxWithArraysContainingNaN() {
        // Verify max and min functions with arrays containing NaN for double
        assertEquals(42.0, IEEE754rUtils.max(DOUBLE_ARRAY_WITH_NAN), DELTA);
        assertEquals(1.2, IEEE754rUtils.min(DOUBLE_ARRAY_WITH_NAN), DELTA);

        assertEquals(42.0, IEEE754rUtils.max(DOUBLE_ARRAY_WITH_NAN_START), DELTA);
        assertEquals(1.2, IEEE754rUtils.min(DOUBLE_ARRAY_WITH_NAN_START), DELTA);

        // Verify max and min functions with arrays containing NaN for float
        assertEquals(42.0f, IEEE754rUtils.max(FLOAT_ARRAY_WITH_NAN), DELTA);
        assertEquals(1.2f, IEEE754rUtils.min(FLOAT_ARRAY_WITH_NAN), DELTA);

        assertEquals(42.0f, IEEE754rUtils.max(FLOAT_ARRAY_WITH_NAN_START), DELTA);
        assertEquals(1.2f, IEEE754rUtils.min(FLOAT_ARRAY_WITH_NAN_START), DELTA);
    }
}