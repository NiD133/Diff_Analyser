package org.apache.commons.lang3.math;

import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.math.IEEE754rUtils}.
 */
public class IEEE754rUtilsTest {

    /**
     * Tests that calling max() with an empty double array throws an IllegalArgumentException,
     * as the input array is not allowed to be empty.
     */
    @Test(expected = IllegalArgumentException.class)
    public void maxOfEmptyDoubleArrayShouldThrowIllegalArgumentException() {
        // Arrange: An empty array is an invalid argument for the max method.
        final double[] emptyArray = new double[0];

        // Act: Calling the method with the empty array.
        // Assert: The @Test annotation expects an IllegalArgumentException to be thrown.
        IEEE754rUtils.max(emptyArray);
    }
}