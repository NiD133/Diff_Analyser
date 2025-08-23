package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

// The EvoSuite runner annotations are kept as this test may be part of a larger, generated suite.
import org.evosuite.runtime.EvoRunner;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) // This annotation is from the original test.
public class BBoxCalculator_ESTestTest15 extends BBoxCalculator_ESTest_scaffolding {

    /**
     * Verifies that calling expandRange() on a BBoxCalculator initialized with a null
     * SpatialContext throws a NullPointerException.
     *
     * This test ensures that the BBoxCalculator handles invalid initialization states gracefully
     * by failing fast.
     */
    @Test(timeout = 4000, expected = NullPointerException.class)
    public void expandRangeWithNullContextThrowsNullPointerException() {
        // Arrange: Create a BBoxCalculator with a null context. The expandRange method
        // relies on the context, so this is an invalid state.
        BBoxCalculator calculator = new BBoxCalculator((SpatialContext) null);

        // Act & Assert: Attempt to expand the bounding box. This action is expected to
        // trigger a NullPointerException because the internal context is null.
        // The specific coordinate values are arbitrary as the exception should occur first.
        calculator.expandRange(-610.206, -610.206, -610.206, -610.206);
    }
}