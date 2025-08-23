package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

/**
 * Test suite for {@link BBoxCalculator}.
 */
public class BBoxCalculatorTest {

    /**
     * Verifies that calling doesXWorldWrap() throws a NullPointerException
     * if the BBoxCalculator was constructed with a null SpatialContext.
     * The context is required to determine world-wrapping behavior.
     */
    @Test(expected = NullPointerException.class)
    public void doesXWorldWrap_shouldThrowNPE_whenContextIsNull() {
        // Given a BBoxCalculator initialized with a null context
        BBoxCalculator bboxCalculator = new BBoxCalculator((SpatialContext) null);

        // When doesXWorldWrap is called, it should fail because the context is needed
        bboxCalculator.doesXWorldWrap();

        // Then a NullPointerException is expected (verified by the @Test annotation)
    }
}