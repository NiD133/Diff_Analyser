package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.shape.Point;

/**
 * Unit tests for the {@link BufferedLine} class.
 */
public class BufferedLineTest {

    /**
     * Verifies that expandBufForLongitudeSkew throws a NullPointerException
     * when invoked with null Point arguments. The method is expected to fail fast
     * as it cannot operate on null inputs.
     */
    @Test(expected = NullPointerException.class)
    public void expandBufForLongitudeSkew_withNullPoints_throwsNullPointerException() {
        // Define an arbitrary buffer value; it's irrelevant as the method should
        // throw the exception before using this value.
        double irrelevantBuffer = -2061.36;

        // This call should fail because both point arguments are null.
        BufferedLine.expandBufForLongitudeSkew(null, null, irrelevantBuffer);
    }
}