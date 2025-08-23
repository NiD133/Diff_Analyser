package org.apache.commons.lang3.math;

import org.junit.Test;

/**
 * Unit tests for {@link IEEE754rUtils}.
 */
public class IEEE754rUtilsTest {

    /**
     * Tests that min(float...) throws a NullPointerException when the input array is null,
     * as specified by the method's contract.
     */
    @Test(expected = NullPointerException.class)
    public void minFloatArrayShouldThrowNullPointerExceptionForNullInput() {
        // The cast to (float[]) is necessary to resolve ambiguity and select the
        // correct varargs method overload, min(float...), when passing null.
        IEEE754rUtils.min((float[]) null);
    }
}