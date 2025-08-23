package org.apache.commons.lang3.math;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for input validation on array methods in {@link IEEE754rUtils}.
 */
@DisplayName("IEEE754rUtils Array Input Validation")
public class IEEE754rUtilsTestTest2 extends AbstractLangTest {

    // --- min(float...) tests ---

    @Test
    void minFloatArrayShouldThrowExceptionForNullInput() {
        assertThrows(NullPointerException.class, () -> {
            // The cast is necessary to resolve method ambiguity between float[] and double[]
            IEEE754rUtils.min((float[]) null);
        });
    }

    @Test
    void minFloatArrayShouldThrowExceptionForEmptyInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            IEEE754rUtils.min(new float[0]);
        });
    }

    // --- max(float...) tests ---

    @Test
    void maxFloatArrayShouldThrowExceptionForNullInput() {
        assertThrows(NullPointerException.class, () -> {
            IEEE754rUtils.max((float[]) null);
        });
    }

    @Test
    void maxFloatArrayShouldThrowExceptionForEmptyInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            IEEE754rUtils.max(new float[0]);
        });
    }

    // --- min(double...) tests ---

    @Test
    void minDoubleArrayShouldThrowExceptionForNullInput() {
        assertThrows(NullPointerException.class, () -> {
            // The cast is necessary to resolve method ambiguity between float[] and double[]
            IEEE754rUtils.min((double[]) null);
        });
    }

    @Test
    void minDoubleArrayShouldThrowExceptionForEmptyInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            IEEE754rUtils.min(new double[0]);
        });
    }

    // --- max(double...) tests ---

    @Test
    void maxDoubleArrayShouldThrowExceptionForNullInput() {
        assertThrows(NullPointerException.class, () -> {
            IEEE754rUtils.max((double[]) null);
        });
    }

    @Test
    void maxDoubleArrayShouldThrowExceptionForEmptyInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            IEEE754rUtils.max(new double[0]);
        });
    }
}